package work.azhu.imnetty.bootstrap.channel;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imnetty.bootstrap.backmsg.InChatBackMapService;
import work.azhu.imnetty.bootstrap.channel.ws.WsChannelService;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.ChatConstant;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/15 10:22
 * @Description
 */
@Service
public class HandlerServiceImp extends HandlerService {

    @Resource
    private InChatBackMapService inChatBackMapService;

    @Resource
    private WsChannelService wsChannelService;
    @Override
    public void getList(Channel channel) {

    }

    @Override
    public void getSize(Channel channel) {

    }

    @Override
    public void sendInChat(Channel channel, FullHttpMessage msg) {

    }

    @Override
    public void notFindUri(Channel channel) {

    }

    @Override
    public boolean login(Channel channel, Map<String, Object> map) {

        String token=(String)map.get(ChatConstant.TOKEN);
        if (/*inChatVerifyService.verifyToken(token)*/true){
            System.out.println("登录成功");
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(inChatBackMapService.loginSuccess())));
            wsChannelService.loginWsSuccess(channel,token);
            return true;
        }
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(inChatBackMapService.loginError())));
        close(channel);
        return false;
    }

    @Override
    public void sendMeText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendToText(Channel channel, Map<String, Object> maps) {
        String otherOne = (String) maps.get(ChatConstant.ONE);
        String value = (String) maps.get(ChatConstant.VALUE);
        String token = (String) maps.get(ChatConstant.TOKEN);
        //返回给自己
        channel.writeAndFlush(new TextWebSocketFrame(
                JSONObject.toJSONString(inChatBackMapService.sendBack(otherOne,value))));
        if (wsChannelService.hasOther(otherOne)){
            //发送给对方--在线
            Channel other = wsChannelService.getChannel(otherOne);
            if (other == null){
                //转http分布式
               // httpChannelService.sendInChat(otherOne,inChatBackMapService.getMsg(token,value));
            }else{
                other.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(inChatBackMapService.getMsg(token,value))));
            }
        }else {
            maps.put(ChatConstant.ON_ONLINE,otherOne);
        }
        try {
            //dataAsynchronousTask.writeData(maps);  这里还有待补充
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void sendGroupText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void verify(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendPhotoToMe(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void close(Channel channel) {

    }
}
