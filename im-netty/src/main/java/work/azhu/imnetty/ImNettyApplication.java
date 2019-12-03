package work.azhu.imnetty;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.DependsOn;

@EnableDubbo
@EnableHystrix
@SpringBootApplication
public class ImNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImNettyApplication.class, args);
    }

}
