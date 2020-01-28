package work.azhu.imdatabase.service.dubbo;


import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.service.DubboMessageService;
import work.azhu.imdatabase.mapper.MessageMapper;

import java.util.List;

@Slf4j
@Service(version = "${demo.service.version}")
public class DubboMessageServiceImpl implements DubboMessageService {

    @Autowired
    public MessageMapper messageMapper;


    @Override
    public Long insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public List<Message> queryMessageByUserId(Long userId) {
        return messageMapper.queryMessageByUserId(userId);
    }

    @Override
    public List<Message> queryMessageByGroupId(Long groupId) {
        return messageMapper.queryMessageByGroupId(groupId);
    }
}
