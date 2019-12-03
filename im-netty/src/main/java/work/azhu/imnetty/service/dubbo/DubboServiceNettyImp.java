package work.azhu.imnetty.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import work.azhu.imcommon.service.DubboServiceNetty;

/**
 * @Author Azhu
 * @Date 2019/12/3 19:45
 * @Description
 */
@Slf4j
@Service(version = "${demo.service.version}")
public class DubboServiceNettyImp implements DubboServiceNetty {
    @Override
    public String testDubbo() {
        log.info("im-Netty模块被调用->");
        return "Dubbo服务: im-Netty";
    }

    @Override
    public String testDubbo(String s) {
        log.info("im-Netty模块被调用->");
        return "Dubbo服务: im-Netty"+s;
    }
}
