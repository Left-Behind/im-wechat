package work.azhu.imweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Azhu
 * @Date 2020/1/2 16:31
 * @Description
 */
@Controller
public class IndexController {


    @RequestMapping(value = {"index",""})
    public String index(){

        return "templates/login.html";
    }

    @GetMapping("login")

    public String login(){

        return "views/login.html";
    }
}
