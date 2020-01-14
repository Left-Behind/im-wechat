package work.azhu.imdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.util.SnowflakeIdWorker;
import work.azhu.imdatabase.service.MessageService;

import java.util.Random;
import java.util.UUID;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:36
 * @Description: 使用Jmeter请求,测试读写分离性能
 */
@Controller
public class JmeterController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/testWrite",method = RequestMethod.GET)
    @ResponseBody
    public String testWrite(){

        Message message = new Message();
        String uuid = UUID.randomUUID().toString();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        long id= idWorker.nextId();
        message.setId(id);
        message.setContent(uuid);
        message.setToUserId(1);
        message.setToGroupId(1);
        message.setFromUserId(1);
        messageService.insertMessage(message);
        return  "ok";

    }
}
