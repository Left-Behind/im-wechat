package work.azhu.imnetty.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import work.azhu.imcommon.service.DubboNettyService;
import work.azhu.imnetty.bootstrap.channel.cache.WsCacheMap;

import java.util.Set;

@Slf4j
@Service(version = "${demo.service.version}")
public class DubboNettyServiceImp implements DubboNettyService {

    @Override
    public Integer getChannelSize() {
        return WsCacheMap.getSize();
    }

    @Override
    public Boolean queryUserIdIfOnline(String userId) {
        return WsCacheMap.getByToken(userId)==null?false:true;
    }

    @Override
    public Set<String> queryUserIdSet(){
        return WsCacheMap.getTokenSet();
    }


}
