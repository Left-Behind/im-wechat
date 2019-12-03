package work.azhu.imweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.azhu.imcommon.service.DubboServiceDatabase;
import work.azhu.imcommon.service.DubboServiceNetty;


/**
 * @Author Azhu
 * @Date 2019/11/1 17:03
 * @Description
 */
@RestController
public class DubboController {

    @Reference(version = "${demo.service.version}")
    private DubboServiceDatabase dubboServiceDatabase;

    @Reference(version = "${demo.service.version}")
    private DubboServiceNetty dubboServiceNetty;


    @RequestMapping("/database/t/{name}")
    public String testDubbo(@PathVariable("name") String name) {
        return dubboServiceDatabase.testDubbo(name);
    }
    @RequestMapping("/database/test")
    public String testDubbo() {
        return dubboServiceDatabase.testDubbo();
    }

    @RequestMapping("/netty/t/{name}")
    public String testDubboNetty(@PathVariable("name") String name) {
        return dubboServiceNetty.testDubbo(name);
    }
    @RequestMapping("/netty/test")
    public String testDubboNetty() {
        return dubboServiceNetty.testDubbo();
    }
}
