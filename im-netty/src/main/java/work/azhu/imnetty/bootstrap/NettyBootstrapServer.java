package work.azhu.imnetty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import work.azhu.imnetty.bootstrap.handler.DefaultHandler;
import work.azhu.imnetty.common.constant.BootstrapConstant;
import work.azhu.imnetty.common.utils.IpUtils;
import work.azhu.imnetty.config.ConfigFactory;
import work.azhu.imnetty.config.RedisConfig;

/**
 * @Author Azhu
 * @Date 2019/11/14 17:09
 * @Description Netty服务器启动类
 *              v使用独立的线程启动
 */
@Slf4j
@Component
public class NettyBootstrapServer implements Runnable{

    /**
     * EventLoopGroup 实现于Executor顶级的线程池接口
     * 因此bossGroup 和 workerGroup 是 Bootstrap 构造
     * 方法中传入的两个对象，这两个 group 均是线程池：
     */
    //bossGroup 线程池则只是在 Bind 某个端口后，获得其中一个线程作为 MainReactor，
    // 专门处理端口的 Accept 事件，每个端口对应一个 Boss 线程、(接待线程池)
    private EventLoopGroup bossGroup;
    //workerGroup 线程池会被各个 SubReactor 和 Worker 线程充分利用、(服务线程池)
    private EventLoopGroup workerGroup;
    //Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，
    // 主要作用是配置整个 Netty 程序，串联各个组件，Netty 中 Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类。
    private ServerBootstrap serverBootstrap;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private DefaultHandler defaultHandler;

    private ChannelFuture serverChannelFuture;



    @Override
    public void run() {

        build();
    }

    private void build() {
        try{
            //这里有点思想处了问题，过度想要依赖spring.什么对象都想依赖注入。这里完全可以new 折腾了半天
            bossGroup=new NioEventLoopGroup();
            workerGroup=new NioEventLoopGroup();
            serverBootstrap=new ServerBootstrap();
            serverBootstrap
                    // 1.指定线程模型
                    .group(bossGroup, workerGroup)
                    // 2.指定 IO 类型为 NIO
                    .channel(NioServerSocketChannel.class)
                    // 3.IO 处理逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // HTTP编码解码器
                            ch.pipeline().addLast(BootstrapConstant.HTTPCODE, new HttpServerCodec());
                            // 把HTTP头、HTTP体拼成完整的HTTP请求
                            ch.pipeline().addLast(BootstrapConstant.AGGREGATOR, new HttpObjectAggregator(65536));
                            // 方便大文件传输，不过实质上都是短的文本数据
                            ch.pipeline().addLast(BootstrapConstant.HTTP_CHUNKED, new ChunkedWriteHandler());
                            //处理Http请求，主要是完成HTTP协议到Websocket协议的升级
                            ch.pipeline().addLast(BootstrapConstant.WEBSOCKETHANDLER,new WebSocketServerProtocolHandler("/ws"));
                            //绑定I/O事件的处理类-->DefaultHandler
                            //这里把websocket和http一起处理了.在Handler需要进行区分处理
                            ch.pipeline().addLast(defaultHandler);
                        }
                    });
            serverChannelFuture = serverBootstrap.bind(port).sync().addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info("服务端启动成功【" + IpUtils.getHost() + ":" + ConfigFactory.redisPort + "】");
                    ConfigFactory.address = IpUtils.getHost()+":"+ ConfigFactory.redisPort;
                    RedisConfig.getInstance();
                }else{
                    log.info("服务端启动失败【" + IpUtils.getHost() + ":" + ConfigFactory.redisPort + "】");}
            });
        }catch (Exception e) {
            log.info(e.getMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void close(){
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }
}
