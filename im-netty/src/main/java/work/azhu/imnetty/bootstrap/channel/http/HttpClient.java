package work.azhu.imnetty.bootstrap.channel.http;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.azhu.imcommon.model.bean.netty.SendInChat;
import work.azhu.imnetty.common.constant.ChatConstant;
import work.azhu.imnetty.common.constant.HttpConstant;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/18 10:06
 * @Description
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private static HttpClient instance = new HttpClient();

    public static Bootstrap bootstrap;

    private HttpClient(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建Netty客户端
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                ch.pipeline().addLast(new HttpResponseDecoder());
                // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                ch.pipeline().addLast(new HttpRequestEncoder());
                //这里先不能使用SSL加密,否者会导致http请求格式错误，导致传递消息失败
           /* try {
                SSLContext context = SslUtil.createSSLContext("JKS","inchat.jks","123456");
                SSLEngine engine = context.createSSLEngine();
                engine.setUseClientMode(true);
//                engine.setNeedClientAuth(false);
                ch.pipeline().addLast("ssl",new SslHandler(engine));
            }catch (Exception e){
                e.printStackTrace();
            }*/
            }
        });
        this.bootstrap = b;
    }

    public static HttpClient getInstance(){
        return instance;
    }

    public void send(String host, int port, String token, Map value) throws Exception {
        // Start the client.
        ChannelFuture f = this.bootstrap.connect(host, port).sync();

        URI uri = new URI(HttpConstant.URI_SENDINCHAT);

        Gson gson = new Gson();
        String content = gson.toJson(new SendInChat(token,value));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
//      request.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);

        // 发送http请求
        f.channel().write(request);
        f.channel().flush();
        f.channel().closeFuture().sync();
    }

    //    public static void main(String[] args) throws Exception {
//        HttpClient client = new HttpClient();
//        client.connect("192.168.1.121",8090);
//    }
    public static void main(String[] args) throws Exception{
        String host="192.168.10.122";
        int port=8070;
        HttpClient client = new HttpClient();
        client.connect(host,port);
        /*String token="2222";
        Map value=new HashMap();
        value.put("from","1111");
        value.put("type","sendTo");
        value.put("value","水水水水");
        ChannelFuture f = HttpClient.bootstrap.connect(host, port).sync();

        URI uri = new URI(HttpConstant.URI_SENDINCHAT);

        Gson gson = new Gson();
        String content = gson.toJson(new SendInChat(token,value));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(),Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);

        // 发送http请求
        f.channel().write(request);
        f.channel().flush();
        f.channel().closeFuture().sync();*/
    }

    /**
     * 测试用例
     * @param host
     * @param port
     * @throws Exception
     */
    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            URI uri = new URI("/send_inchat");
//            String msg = "Are you ok?";
            String token = "2222";
            Map<String,String> backMap = new HashMap<String,String>();
            backMap.put(ChatConstant.TYPE,ChatConstant.SENDTO);
            backMap.put(ChatConstant.VALUE,"你好");
            backMap.put(ChatConstant.ONE,"2222");
            Gson gson = new Gson();
            String content = gson.toJson(new SendInChat(token,backMap));
//            ByteBuf buf = Unpooled.wrappedBuffer(gson.toJson(new SendInChat(token,msg)).getBytes("UTF-8"));
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(),Unpooled.wrappedBuffer(content.getBytes("UTF-8")));

            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
//            request.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);

            // 发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}
