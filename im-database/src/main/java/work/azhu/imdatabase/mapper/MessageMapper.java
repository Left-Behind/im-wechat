package work.azhu.imdatabase.mapper;

import org.apache.ibatis.annotations.Mapper;
import work.azhu.imcommon.model.bean.common.Message;

/**
 * @Author Azhu
 * @Date 2020/1/14 15:42
 * @Description
 */
@Mapper
public interface MessageMapper {

    Long insertMessage(Message message);
}
