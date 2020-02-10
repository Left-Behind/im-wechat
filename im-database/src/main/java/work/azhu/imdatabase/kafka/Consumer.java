package work.azhu.imdatabase.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.util.SnowflakeIdWorker;
import work.azhu.imdatabase.mapper.MessageMapper;

import java.util.Optional;

/**
 * @Author Azhu
 * @Date 2019/12/3 17:18
 * @Description: 测试kafka消费
 */
@Component
@Slf4j
public class Consumer {


    @Autowired
    private MessageMapper messageMapper;

    @KafkaListener(topics = {"web"})
    public void listenWeb(ConsumerRecord<?, ?> record){

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();
            log.info("消费到了web模块的消息");
            System.out.println("---->"+record);
            System.out.println("---->"+message);

        }

    }

    @KafkaListener(topics = {"database"})
    public void listenDatabase(ConsumerRecord<?, ?> record){

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

        if (kafkaMessage.isPresent()) {
            Object object = kafkaMessage.get();
            JSONObject userJson = JSONObject.parseObject(object.toString());
            Message message = JSON.toJavaObject(userJson,Message.class);
            message.setId(idWorker.nextId());

            log.info("消费到了netty服务的消息");
            log.info("插入数据库信息: "+object.toString());
            messageMapper.insertMessage(message);
        }

    }
}
