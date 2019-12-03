package work.azhu.imweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.util.SnowflakeIdWorker;

import java.util.Date;
import java.util.UUID;

/**
 * @Author Azhu
 * @Date 2019/12/3 17:27
 * @Description
 */
@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    //发送消息方法
    @RequestMapping("/kafka/producer")
    public String send() {
        Message message = new Message();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        message.setId(idWorker.nextId());
        message.setContent("一条来自Web模块的kafka消息");
        message.setCreateTime(new Date());
        kafkaTemplate.send("web", message.toString());
        return "发送成功";
    }
}
