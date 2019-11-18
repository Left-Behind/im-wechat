package work.azhu.imnetty.bootstrap.channel.http;

import io.netty.channel.Channel;
import work.azhu.imcommon.model.bean.netty.SendInChat;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/18 9:52
 * @Description
 */
public interface HttpChannelService {

    void getSize(Channel channel);

    void sendFromServer(Channel channel, SendServerVO serverVO);

    void notFindUri(Channel channel);

    void close(Channel channel);

    void getList(Channel channel);

    void sendInChat(String token, Map msg);

    void sendByInChat(Channel channel, SendInChat sendInChat);
}
