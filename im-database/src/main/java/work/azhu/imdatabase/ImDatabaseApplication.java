package work.azhu.imdatabase;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableDubbo
@EnableHystrix
@SpringBootApplication
public class ImDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImDatabaseApplication.class, args);
    }

}
