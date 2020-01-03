package work.azhu.imweb.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import work.azhu.imweb.util.CookieUtil;
import work.azhu.imweb.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2020/1/2 16:31
 * @Description
 */
@Controller
public class IndexController {

    @Value("${jwt.key}")
    private String key;

    @RequestMapping(value = {"index",""})
    public String index(){
        return "templates/login.html";
    }

    @RequestMapping("chatroom")
    public String chatroom(HttpServletRequest request, HttpServletResponse response){
        String token=CookieUtil.getCookieValue(request,"token",true);
        System.out.println("token: "+token);
        return "templates/chatroom.html";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(@RequestBody JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response){
        String userName=jsonObject.getString("userName");
        String password=jsonObject.getString("password");
        System.out.println("userName: "+userName);
        System.out.println("password: "+password);
        Map<String,Object> map = new HashMap();
        map.put("userName",userName);
        map.put("password",password);
        map.put("timestamp",System.currentTimeMillis());
        String ip =getIp(request);
        System.out.println("ip: "+ip);
        String jwtToken= JwtUtil.encode(key,map,ip);
        CookieUtil.setCookie(request,response,"token",jwtToken,60*60*2,true);
        return resSuccessJson(jwtToken);
    }

    public String getIp(HttpServletRequest request){

        String ip= request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }
        return ip;
    }

    public String resSuccessJson(Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("validate", true);
        map.put("message","成功");
        map.put("result", object);
        map.put("time", new Date().getTime() / 1000);
        return JSONObject.toJSONString(map);
    }

}
