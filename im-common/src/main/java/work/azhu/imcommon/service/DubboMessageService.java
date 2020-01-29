package work.azhu.imcommon.service;

import work.azhu.imcommon.model.bean.common.Message;

import java.util.List;

public interface DubboMessageService {

    /**
     * 插入聊天消息数据
     * @param message
     * @return
     */
    Long insertMessage(Message message);

    /**
     * 查询和userId相关的所有消息记录
     * @param userId
     * @return
     */
    List<Message> queryMessageByUserId(Long userId);

    /**
     * 返回某个群组的所有消息
     * @param groupId
     * @return
     */
    List<Message> queryMessageByGroupId(Long groupId);
}
