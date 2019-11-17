package work.azhu.imnetty.bootstrap.handler;

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
import work.azhu.imnetty.bootstrap.channel.HandlerServiceImp;
import work.azhu.imnetty.common.base.Handler;
import work.azhu.imnetty.common.base.HandlerApi;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.ChatConstant;
import work.azhu.imnetty.common.constant.LogConstant;

import java.util.Date;
import java.util.Map;
import com.alibaba.fastjson.JSON;
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

    }

    @Override
    protected void textdoMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        //将json格式小时存入Map对象
        Map<String,Object> maps = (Map) JSON.parse(msg.text());
        //添加消息时间戳
        maps.put(ChatConstant.TIME, new Date());
        switch((String)maps.get(ChatConstant.TYPE)){
            //登录到Netty服务器
            case ChatConstant.LOGIN:
                log.info(LogConstant.DEFAULTWEBSOCKETHANDLER_LOGIN);
                handlerApi.login(ctx.channel(),maps);
                break;
            //发送消息给某人
            case ChatConstant.SENDTO:
                log.info(LogConstant.DefaultWebSocketHandler_SENDTO);
                handlerApi.sendToText(ctx.channel(),maps);
                break;
            //发送消息到群组
            case ChatConstant.SENDGROUP:
                break;
             //退出Netty服务器
            case ChatConstant.LOGOUT:
                break;
            default:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(LogConstant.CHANNELACTIVE+ctx.channel().remoteAddress().toString()+LogConstant.CHANNEL_SUCCESS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
//        log.error("exception",cause);
        log.info(LogConstant.EXCEPTIONCAUGHT+ctx.channel().remoteAddress().toString()+LogConstant.DISCONNECT);
        ctx.close();
    }
}
