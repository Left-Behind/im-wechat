package work.azhu.imweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDubbo
@EnableHystrix
@EnableSwagger2
@SpringBootApplication
public class ImWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImWebApplication.class, args);
    }

}
