package work.azhu.imnetty.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Azhu
 * @Date 2019/11/7 16:45
 * @Description
 */

@Controller
public class IndexController {



    @Value("${netty.port}")
    Long nettyPort;

    @GetMapping("/index")
    public String index(){
        System.out.println("访问 ---> ws://www.azhu.work:"+nettyPort+"/ws");
        return "chat"+nettyPort;
    }

    @RequestMapping("/getNettyPort")
    @ResponseBody
    public String getSession() {
        return "当前Netty服务器监听端口：" + nettyPort;
    }


    @RequestMapping("/chat")
    public String chatromm() {
        return "chat"+nettyPort ;
    }

    @RequestMapping("/getIp")
    @ResponseBody
    public String getIp(HttpServletRequest request){

        System.out.println(request.getHeader("x-forwarded-for"));

        System.out.println(request.getRemoteAddr());
        return "sss";
    }
    @RequestMapping("/8070")
    public String html8070(){
        return "chat8070";
    }
    @RequestMapping("/8090")
    public String html8090(){
        return "chat8090";
    }

    @RequestMapping("/Azhu")
    public String htmlAzhu(){
        return "Azhu";
    }

    @RequestMapping("/Left")
    public String htmlLeft(){
        return "Left";
    }

    @RequestMapping("/Behind")
    public String htmlBehind(){
        return "Behind";
    }

}
