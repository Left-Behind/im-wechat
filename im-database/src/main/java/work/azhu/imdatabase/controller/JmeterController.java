package work.azhu.imdatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:36
 * @Description: 使用Jmeter请求,测试读写分离性能
 */
@Controller
public class JmeterController {

    @RequestMapping(value = "/testWrite",method = RequestMethod.GET)
    public void testWrite(){

    }
}
