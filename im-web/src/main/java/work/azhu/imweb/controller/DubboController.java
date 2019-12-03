package work.azhu.imweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import work.azhu.imcommon.DubboService;


/**
 * @Author Azhu
 * @Date 2019/11/1 17:03
 * @Description
 */
@RestController
public class DubboController {

    @Reference(version = "${demo.service.version}")
    private DubboService dubboService;


    @RequestMapping("/sayHello/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return dubboService.sayHello(name);
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return dubboService.welcome();
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Azhu,hello";
    }
}
