package work.azhu.imdatabase.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import work.azhu.imcommon.service.DubboServiceDatabase;

/**
 * @Author Azhu
 * @Date 2019/12/3 19:37
 * @Description
 */
@Slf4j
@Service(version = "${demo.service.version}")
public class DubboServiceDatabaseImp implements DubboServiceDatabase {


    @Override
    public String  testDubbo() {
        log.info("DataBase模块被调用->");
        return "Dubbo服务: im-database";
    }

    @Override
    public String testDubbo(String s) {
        log.info("DataBase模块被调用");
        return "Dubbo服务: im-database"+s;
    }
}
