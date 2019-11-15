package work.azhu.imnetty.common.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import work.azhu.imnetty.common.constant.LogConstant;

/**
 * @Author Azhu
 * @Date 2019/11/14 17:26
 * @Description Netty IO操作处理初始层
 */
@Slf4j
public abstract class Handler extends SimpleChannelInboundHandler<Object> {

    HandlerApi handlerApi;

    public Handler(HandlerApi handlerApi){
        this.handlerApi = handlerApi;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof TextWebSocketFrame){
            log.info("TextWebSocketFrame--->"+msg);
            textdoMessage(ctx,(TextWebSocketFrame)msg);
        }else if (msg instanceof WebSocketFrame){
            log.info("WebSocketFrame--->"+msg);
            webdoMessage(ctx,(WebSocketFrame)msg);
        }else if (msg instanceof FullHttpRequest){
            log.info("FullHttpRequest--->"+msg);
            httpdoMessage(ctx,(FullHttpRequest)msg);
        }else {
            log.info("msg.type: "+msg.getClass());
            log.info("channelRead0--->没有找到适合的消息类型");
        }

    }

    protected abstract void webdoMessage(ChannelHandlerContext ctx, WebSocketFrame msg);

    protected abstract void textdoMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg);

    protected abstract void httpdoMessage(ChannelHandlerContext ctx, FullHttpRequest msg);

    /**
     * 断开连接时会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(LogConstant.CHANNELINACTIVE+ctx.channel().localAddress().toString()+LogConstant.CLOSE_SUCCESS);
        try {
            handlerApi.close(ctx.channel());
        }catch (Exception e){
            log.error(/*LogConstant.NOTFINDLOGINCHANNLEXCEPTION*/"关闭异常");
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if(evt instanceof IdleStateEvent){
//            webSocketHandlerApi.doTimeOut(ctx.channel(),(IdleStateEvent)evt);
//        }
        super.userEventTriggered(ctx, evt);
    }
}
