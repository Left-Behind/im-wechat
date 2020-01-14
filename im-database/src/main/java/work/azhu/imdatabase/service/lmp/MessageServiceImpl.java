package work.azhu.imdatabase.service.lmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imdatabase.mapper.MessageMapper;
import work.azhu.imdatabase.service.MessageService;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:41
 * @Description
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Long insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }
}
