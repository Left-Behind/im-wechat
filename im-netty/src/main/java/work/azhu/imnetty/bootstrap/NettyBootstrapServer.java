package work.azhu.imnetty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    @Resource(name ="NioEventLoopGroup" )
    private EventLoopGroup bossGroup;
    //workerGroup 线程池会被各个 SubReactor 和 Worker 线程充分利用、(服务线程池)
    @Resource(name ="NioEventLoopGroup" )
    private EventLoopGroup workerGroup;

    //Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，
    // 主要作用是配置整个 Netty 程序，串联各个组件，Netty 中 Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类。
    @Autowired
    private ServerBootstrap serverBootstrap;

    @Value("${netty,port}")
    private int port;

    @Autowired
    @Qualifier("DefaultHandler")
    private ChannelHandler DefaultHandler;

    private ChannelFuture serverChannelFuture;

    @Override
    public void run() {
        build();
    }

    private void build() {
        try{
            serverBootstrap
                    // 1.指定线程模型
                    .group(bossGroup, workerGroup)
                    // 2.指定 IO 类型为 NIO
                    .channel(NioServerSocketChannel.class)
                    // 3.IO 处理逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            log.info("绑定I/O事件的处理类-->DefaultHandler");
                            ch.pipeline().addLast(DefaultHandler);
                        }
                    });
            serverChannelFuture = serverBootstrap.bind(port).sync();
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
