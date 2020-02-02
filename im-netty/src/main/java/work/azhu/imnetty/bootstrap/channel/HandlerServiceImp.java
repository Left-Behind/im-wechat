package work.azhu.imnetty.bootstrap.channel;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.model.bean.netty.SendInChat;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import work.azhu.imcommon.service.DubboUserService;
import work.azhu.imnetty.bootstrap.backmsg.ImwechatBackMsgService;
import work.azhu.imnetty.bootstrap.backmsg.InChatBackMapService;
import work.azhu.imnetty.bootstrap.channel.cache.WsCacheMap;
import work.azhu.imnetty.bootstrap.channel.http.HttpChannelService;
import work.azhu.imnetty.bootstrap.channel.ws.WsChannelService;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.ChatConstant;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Value("${netty.port}")
    Long nettyPort;

    @Override
    public void getList(Channel channel) {

    }

    @Override
    public Integer getSize(Channel channel) {
        return WsCacheMap.getSize();
    }

    @Override
    public void sendFromServer(Channel channel, SendServerVO sendServerVO) {
        httpChannelService.sendFromServer(channel,sendServerVO);
    }

    @Override
    public void sendInChat(Channel channel, FullHttpMessage msg) {
        Map<String,Object> maps = (Map) JSON.parse(msg.content().toString(CharsetUtil.UTF_8));
        if(maps.get("type").equals(ChatConstant.NOTIFY)){
            sendNotify(maps);
        }else {
            sendToText(channel,maps);
        }
        close(channel);
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
            //本地缓存保存,用户userId和channel对应
            wsChannelService.loginWsSuccess(channel,user.getId().toString(),token);
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(imwechatBackMsgService.loginSuccess())));
            log.info(user.getUserName()+"从IP:"+ip+"成功登录----> Netty服务集群-"+nettyPort);
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

        String content =(String) maps.get(ChatConstant.CONTENT);
        String toUserId = (String) maps.get(ChatConstant.TOUSERID);

        //返回给自己 这里可以移除，看前端怎么设置
        /*channel.writeAndFlush(new TextWebSocketFrame(
                JSONObject.toJSONString(inChatBackMapService.sendBack(toUserId,content))));*/
        //发送给对方--在线
        if (wsChannelService.hasOther(toUserId)){
            Channel otherChannel = wsChannelService.getChannel(toUserId);
            if (otherChannel == null){
                log.info("本地无toUserId对应的连接实列Channel----------->转分布式(http)");
               httpChannelService.sendInChat(toUserId,maps);
            }else{
                otherChannel.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(imwechatBackMsgService.getMsg(maps))));
            }
        }
        try {

            //dataAsynchronousTask.writeData(maps);  这里还有待补充
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void sendGroupText(Channel channel, Map<String, Object> maps) {
        
        List<User> usersList = dubboUserService.queryUserDetailList();
        usersList.stream().forEach(item->{
                String toUserId=item.getId().toString();
                /*channel.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(inChatBackMapService.sendBack(toUserId,content))));*/
                //发送给对方--在线
                if (wsChannelService.hasOther(toUserId)){
                    //获得连接实列
                    Channel otherChannel = wsChannelService.getChannel(toUserId);
                    if (otherChannel == null){
                        log.info("本地无toUserId对应的连接实列Channel----------->转分布式(http)");
                        httpChannelService.sendInChat(toUserId,maps);
                        log.info("分布式通讯成功");
                    }else{
                        otherChannel.writeAndFlush(new TextWebSocketFrame(
                                JSONObject.toJSONString(imwechatBackMsgService.getMsg(maps))));
                    }
                }
                try {

                    //dataAsynchronousTask.writeData(maps);  这里还有待补充
                } catch (Exception e) {
                    return;
                }
            });

    }

    @Override
    public void verify(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendPhotoToMe(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void loginNotify(Channel channel) {
        List<User> usersList = dubboUserService.queryUserDetailList();
        usersList.stream().forEach(item->{
            String toUserId=item.getId().toString();
            //发送给对方--在线
            if (wsChannelService.hasOther(toUserId)){
                //获得连接实列
                Channel otherChannel = wsChannelService.getChannel(toUserId);
                Map<String,Object> maps = new HashMap<>();
                maps.put("type",ChatConstant.NOTIFY);
                maps.put("status","success");
                if (otherChannel == null){
                    maps.put("toUserId",toUserId);
                    log.info("本地无toUserId对应的连接实列Channel----------->转分布式(http),有用户登录广播");
                    httpChannelService.sendInChat(toUserId,maps);
                }else{
                    otherChannel.writeAndFlush(new TextWebSocketFrame(
                            JSONObject.toJSONString(imwechatBackMsgService.loginNotify())));
                }
            }
         
        });
    }

    @Override
    public void sendNotify(Map<String, Object> maps) {
        String toUserId = (String) maps.get(ChatConstant.TOUSERID);
        if (wsChannelService.hasOther(toUserId)){
            Channel otherChannel = wsChannelService.getChannel(toUserId);
            //广播通知
            otherChannel.writeAndFlush(new TextWebSocketFrame(
                    JSONObject.toJSONString(imwechatBackMsgService.loginNotify())));
        }
    }

    @Override
    public void close(Channel channel) {
        wsChannelService.close(channel);
    }
}
