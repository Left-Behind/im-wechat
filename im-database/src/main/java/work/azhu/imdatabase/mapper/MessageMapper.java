package work.azhu.imdatabase.mapper;

import work.azhu.imcommon.model.bean.common.Message;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:42
 * @Description
 */
public interface MessageMapper {

    Long insertMessage(Message message);
}
