package work.azhu.imnetty.bootstrap.handler;

import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import work.azhu.imnetty.bootstrap.channel.HandlerServiceImp;
import work.azhu.imnetty.common.base.Handler;
import work.azhu.imnetty.common.base.HandlerApi;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.ChatConstant;
import work.azhu.imnetty.common.constant.HttpConstant;
import work.azhu.imnetty.common.constant.LogConstant;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import work.azhu.imnetty.common.utils.HttpUtil;

/**
 * @Author Azhu
 * @Date 2019/11/14 17:54
 * @Description
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class DefaultHandler extends Handler {


    @Autowired
    private HandlerServiceImp handlerApi;


    @Override
    protected void webdoMessage(ChannelHandlerContext ctx, WebSocketFrame msg) {
        Channel channel = ctx.channel();
        HandlerService httpHandlerService;
        log.info("handlerApi == "+handlerApi);
        if (handlerApi instanceof HandlerService){
            httpHandlerService = (HandlerService)handlerApi;
        }else {
            log.error("Server Handler 不匹配");
        }
        if (msg instanceof BinaryWebSocketFrame){
            //TODO 实现图片处理
        }
    }

    @Override
    protected void httpdoMessage(ChannelHandlerContext ctx, FullHttpRequest msg) {
        switch (HttpUtil.checkType(msg)){
            case HttpConstant.GETSIZE:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_GETSIZE);
                handlerApi.getSize(ctx.channel());
                break;
            case HttpConstant.SENDFROMSERVER:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_SENDFROMSERVER);
                SendServerVO serverVO = null;
                try {
                    serverVO = HttpUtil.getToken(msg);
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                handlerApi.sendFromServer(ctx.channel(),serverVO);
                break;
            case HttpConstant.GETLIST:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_GETLIST);
                handlerApi.getList(ctx.channel());
                break;
            case HttpConstant.SENDINCHAT:
                //分布式消息发送
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_SENDINCHAT);
                handlerApi.sendInChat(ctx.channel(),msg);
                break;
            case HttpConstant.NOTFINDURI:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_NOTFINDURI);
                handlerApi.notFindUri(ctx.channel());
                break;
            default:
                System.out.println("未匹配"+msg);
                break;
        }
    }

    @Override
    protected void textdoMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        //将json格式存入Map对象
        Map<String,Object> maps = (Map) JSON.parse(msg.text());
        log.info("[textdoMessage] 打印Websocket请求的信息");
        log.info(JSON.toJSONString(maps, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));
        switch((String)maps.get(ChatConstant.TYPE)){
            //登录到Netty服务器
            case ChatConstant.LOGIN:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_LOGIN);
                handlerApi.login(ctx.channel(),maps);
                handlerApi.loginNotify(ctx.channel());
                break;
            //发送消息给某人
            case ChatConstant.SINGLE_SENDING:
                log.info(LogConstant.DefaultWebSocketHandler_SENDTO);
                handlerApi.sendToText(ctx.channel(),maps);
                break;
            //发送消息给某人(文件)
            case ChatConstant.FILE_MSG_SINGLE_SENDING:
                log.info(LogConstant.DefaultWebSocketHandler_SENDTO);
                handlerApi.sendToText(ctx.channel(),maps);
                break;
            //发送消息到群组
            case ChatConstant.GROUP_SENDING:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_SENDGROUP);
                handlerApi.sendGroupText(ctx.channel(),maps);
                break;
            //发送消息到群组(文件)
            case ChatConstant.FILE_MSG_GROUP_SENDING:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_SENDGROUP);
                handlerApi.sendGroupText(ctx.channel(),maps);
                break;
             //退出Netty服务器
            case ChatConstant.LOGOUT:
                handlerApi.close(ctx.channel());
                break;
            default:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("远程ip: "+ctx.channel().remoteAddress());
        log.info(LogConstant.CHANNELACTIVE+ctx.channel().remoteAddress().toString()+LogConstant.CHANNEL_SUCCESS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
//        log.error("exception",cause);
        log.info(LogConstant.EXCEPTIONCAUGHT+ctx.channel().remoteAddress().toString()+LogConstant.DISCONNECT);
        ctx.close();
    }
}
