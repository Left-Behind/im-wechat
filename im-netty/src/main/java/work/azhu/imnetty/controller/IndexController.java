package work.azhu.imnetty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        System.out.println("访问 ---> ws://www.azhu.work:8070/ws");
        return "chat70";
    }

    @RequestMapping("/getNettyPort")
    @ResponseBody
    public String getSession() {
        return "当前Netty服务器监听端口：" + nettyPort;
    }

}
