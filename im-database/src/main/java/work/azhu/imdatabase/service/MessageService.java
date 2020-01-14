package work.azhu.imdatabase.service;

import work.azhu.imcommon.model.bean.common.Message;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:40
 * @Description
 */
public interface MessageService {

    Long insertMessage(Message message);
}
