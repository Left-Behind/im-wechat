package work.azhu.imnetty.bootstrap.channel;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.model.bean.netty.SendInChat;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import work.azhu.imcommon.service.DubboUserService;
import work.azhu.imnetty.bootstrap.backmsg.ImwechatBackMsgService;
import work.azhu.imnetty.bootstrap.backmsg.InChatBackMapService;
import work.azhu.imnetty.bootstrap.channel.http.HttpChannelService;
import work.azhu.imnetty.bootstrap.channel.ws.WsChannelService;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.ChatConstant;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/15 10:22
 * @Description
 */
@Service
@Slf4j
public class HandlerServiceImp extends HandlerService {

    @Resource
    private InChatBackMapService inChatBackMapService;

    @Resource
    private ImwechatBackMsgService imwechatBackMsgService;

    @Resource
    private WsChannelService wsChannelService;

    @Resource
    private HttpChannelService httpChannelService;

    @Reference(version = "${demo.service.version}")
    private DubboUserService dubboUserService;

    @Override
    public void getList(Channel channel) {

    }

    @Override
    public void getSize(Channel channel) {

    }

    @Override
    public void sendFromServer(Channel channel, SendServerVO sendServerVO) {
        httpChannelService.sendFromServer(channel,sendServerVO);
    }

    @Override
    public void sendInChat(Channel channel, FullHttpMessage msg) {
        System.out.println(msg);
        String content = msg.content().toString(CharsetUtil.UTF_8);
        Gson gson = new Gson();
        SendInChat sendInChat = gson.fromJson(content, SendInChat.class);
        httpChannelService.sendByInChat(channel,sendInChat);
    }

    @Override
    public void notFindUri(Channel channel) {


    }

    @Override
    public boolean login(Channel channel, Map<String, Object> map) {

        String token=(String)map.get(ChatConstant.TOKEN);
        String ip=(String)map.get(ChatConstant.IP);
        //远程服务调用,获得该token对应的用户
        User user=dubboUserService.verifyToken(token,ip);
        if (user!=null){
            log.info(user.getUserName()+"登录成功");
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(imwechatBackMsgService.loginSuccess())));
            //本地缓存保存,用户userId和channel对应
            wsChannelService.loginWsSuccess(channel,user.getId().toString());
            return true;
        }
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(imwechatBackMsgService.loginError())));
        close(channel);
        return false;
    }

    @Override
    public void sendMeText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendToText(Channel channel, Map<String, Object> maps) {


        String fromUserId = (String) maps.get(ChatConstant.FROMUSERID);
        String content =(String) maps.get(ChatConstant.CONTENT);
        String toUserId = (String) maps.get(ChatConstant.TOUSERID);

//        String otherOne = (String) maps.get(ChatConstant.ONE);
//        String value = (String) maps.get(ChatConstant.VALUE);
//        String token = (String) maps.get(ChatConstant.TOKEN);
        //返回给自己 这里可以移除，看前端怎么设置
        channel.writeAndFlush(new TextWebSocketFrame(
                JSONObject.toJSONString(inChatBackMapService.sendBack(toUserId,content))));
        if (wsChannelService.hasOther(toUserId)){
            //发送给对方--在线
            Channel otherChannel = wsChannelService.getChannel(toUserId);
            if (otherChannel == null){
                log.info(" ----------->转http分布式");
               httpChannelService.sendInChat(toUserId,maps);
            }else{
                otherChannel.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(imwechatBackMsgService.getMsg(maps))));
            }
        }else {
            maps.put(ChatConstant.ON_ONLINE,toUserId);
            channel.writeAndFlush(new TextWebSocketFrame(
                    JSONObject.toJSONString(imwechatBackMsgService.sendOffLine())));
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
        wsChannelService.close(channel);
    }
}
