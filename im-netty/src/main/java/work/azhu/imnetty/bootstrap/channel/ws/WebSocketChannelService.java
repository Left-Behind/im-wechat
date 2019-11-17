package work.azhu.imnetty.bootstrap.channel.ws;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;
import work.azhu.imnetty.bootstrap.channel.cache.WsCacheMap;

import java.util.Map;

/**
 * @Author Azhu
 * @Description 聊天业务消息处理 Service层
 * @Date 2019/11/17 13:20
 **/
@Service
public class WebSocketChannelService implements WsChannelService{
    @Override
    public void loginWsSuccess(Channel channel, String token) {
        WsCacheMap.saveWs(token,channel);
        WsCacheMap.saveAd(channel.remoteAddress().toString(),token);
    }

    @Override
    public boolean hasOther(String otherOne) {
        return WsCacheMap.hasToken(otherOne);
    }

    @Override
    public Channel getChannel(String otherOne) {
        return WsCacheMap.getByToken(otherOne);
    }

    @Override
    public void close(Channel channel) {
        String token = WsCacheMap.getByAddress(channel.remoteAddress().toString());
        WsCacheMap.deleteAd(channel.remoteAddress().toString());
        WsCacheMap.deleteWs(token);
        channel.close();
    }

    @Override
    public boolean sendFromServer(Channel channel, Map<String, String> map) {
        try {
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(map)));
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
