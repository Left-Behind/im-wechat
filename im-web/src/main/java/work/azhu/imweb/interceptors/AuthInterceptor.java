package work.azhu.imweb.interceptors;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import work.azhu.imcommon.util.http.HttpclientUtil;
import work.azhu.imweb.annotations.LoginRequired;
import work.azhu.imweb.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/12/9 10:12
 * @Description
 */
@Component
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public AuthInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截代码
        // 判断被拦截的请求的访问的方法的注解(是否时需要拦截的)
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        StringBuffer url = request.getRequestURL();

        // 是否拦截
        if (methodAnnotation == null) {
            return true;
        }

        String token = "";

        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }

        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }

        // 是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();// 获得该请求是否必登录成功

        // 调用认证中心进行验证
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            String successJson  = HttpclientUtil.doGet("http://passport.gmall.com:8085/verify?token=" + token+"&currentIp="+ip);

            successMap = JSON.parseObject(successJson,Map.class);

            success = successMap.get("status");

        }

        if (loginSuccess) {
            // 必须登录成功才能使用
            if (!success.equals("success")) {
                //重定向会passport登录
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://passport.gmall.com:8085/index?ReturnUrl="+requestURL);
                return false;
            }

            // 需要将token携带的用户信息写入
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickname", successMap.get("nickname"));
            //验证通过，覆盖cookie中的token
            if(StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
            }

        } else {
            // 没有登录也能用，但是必须验证
            if (success.equals("success")) {
                // 需要将token携带的用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));

                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }

            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
