package work.azhu.imnetty.bootstrap.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.azhu.imnetty.common.base.Handler;
import work.azhu.imnetty.common.base.HandlerApi;
import work.azhu.imnetty.common.base.HandlerService;
import work.azhu.imnetty.common.constant.LogConstant;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/14 17:54
 * @Description
 */
@Slf4j
@ChannelHandler.Sharable
public class DefaultHandler extends Handler {

    private final HandlerApi handlerApi;

    public DefaultHandler(HandlerApi handlerApi) {
        super(handlerApi);
        this.handlerApi = handlerApi;
    }

    @Override
    protected void webdoMessage(ChannelHandlerContext ctx, WebSocketFrame msg) {
        Channel channel = ctx.channel();
        HandlerService httpHandlerService;
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
