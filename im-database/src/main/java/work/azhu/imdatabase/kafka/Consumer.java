package work.azhu.imdatabase.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author Azhu
 * @Date 2019/12/3 17:18
 * @Description: 测试kafka消费
 */
@Component
@Slf4j
public class Consumer {

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

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();
            log.info("消费到了database模块的消息");
            System.out.println("---->"+record);
            System.out.println("---->"+message);

        }

    }
}
