package work.azhu.imnetty.bootstrap.channel.ws;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

/**
 * @Author Azhu
 * @Description //TODO
 * @Date 2019/11/17 13:20
 **/
public interface WsChannelService {

    /**
     * 登录成功存储到本地缓存
     * @param channel {@link Channel} 链接实例
     * @param userId {@link String} 用户标识
     */
    void loginWsSuccess(Channel channel, String userId);

    /**
     * 判断是否存在当前在线用户
     * @param otherOne {@link String} 某人的用户标识
     * @return {@link Boolean} 是否存在
     */
    boolean hasOther(String otherOne);

    /**
     * 获取某人的链接实例
     * @param otherOne {@link String} 用户唯一标识
     * @return {@link Channel} 链接实例
     */
    Channel getChannel(String otherOne);

    /**
     * 删除链接与本地存储信息
     * @param channel {@link Channel} 链接实例
     */
    void close(Channel channel);

    /**
     * 以服务端API调用向链接发送信息
     * @param channel {@link }
     * @param map
     * @return
     */
    boolean sendFromServer(Channel channel, Map<String,String> map);
}
