package work.azhu.imweb.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import work.azhu.imcommon.common.BaseController;
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
@Api("IndexController")
@Controller
public class IndexController extends BaseController {

    @Value("${jwt.key}")
    private String key;

    @RequestMapping(value = {"index",""})
    public String index(){
        return "templates/login.html";
    }

    @RequestMapping("chatroom")
    public String chatroom(){
        String token=CookieUtil.getCookieValue(request,"token",true);
        System.out.println("token: "+token);
        return "templates/chatroom.html";
    }
    @RequestMapping("chatroom1")
    public String chatroom1(){
        String token1 = (String) request.getAttribute("token");
        //CookieUtil.setCookie(request,response,"token",token,60*60*2,true);
        String token=CookieUtil.getCookieValue(request,"token",true);
        System.out.println("token1: "+token1);
        System.out.println("token: "+token);
        return "redirect:/chatroom";
    }

    @ApiOperation(value="用户登陆", notes="用户登陆")
    @RequestMapping(value = "login",method = RequestMethod.POST)
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
        String jwt=getJwt(key,map);
        return resSuccessJson(jwt);
    }




    @RequestMapping("test")
    @ResponseBody
    public String test(){
        String ip=getIp();
        System.out.println(ip);
        return ip;
    }



}
