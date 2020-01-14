package work.azhu.imdatabase.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import work.azhu.imcommon.DubboService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Azhu
 * @Date 2019/11/1 16:57
 * @Description dubbo和springcloud中的feign不同，dubbo暴露的是service层接口
 */
@Service(version = "${demo.service.version}")
public class DubboSeriveceImpl implements DubboService {

    @Value("${server.port}")
    Integer port;
    public String sayHello(String name) {
        return "微服务端口号："+port+"Hello, " + name + " (from Spring Boot)";
    }

    @HystrixCommand
    public String welcome() {
        return "这是一个通过zookeeper注册的服务";
    }

}
