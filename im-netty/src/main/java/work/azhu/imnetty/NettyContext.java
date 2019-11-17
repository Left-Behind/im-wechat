package work.azhu.imnetty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import work.azhu.imnetty.bootstrap.NettyBootstrapServer;
import work.azhu.imnetty.config.ConfigFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author Azhu
 * @Date 2019/11/14 19:09
 * @Description
 */
@Slf4j
@Component
@Scope("singleton")
public class NettyContext {

    @Autowired
    private NettyBootstrapServer nettyBootstrapServer;

    private Thread nettyThread;

    @Value("${spring.redis.host}")
    public  String redisIP;

    @Value("${netty.isDistributed}")
    public  Boolean isDistributed;

    @Value("${spring.redis.port}")
    public  Integer redisPort;

    /**
     * 描述：Tomcat加载完ApplicationContext-main和netty文件后：
     *       启动Netty WebSocket服务器；
     */
    @PostConstruct // Constructor >> @Autowired >> @PostConstruct
    // @PostConstruct注解的方法将会在依赖注入完成后被自动调用
    public void init() {
        nettyThread =new Thread(nettyBootstrapServer);
        log.info("开启独立线程，启动Netty WebSocket服务器...");
        //logger.info( System.getProperty("user.dir"))
        config();
        nettyThread.start();
    }

    /**
     * 给配置工厂赋值
     */
    private void config() {
        ConfigFactory.redisPort=redisPort;
        ConfigFactory.redisIP=redisIP;
        ConfigFactory.isDistributed=isDistributed;
    }
    /**
     * 描述：Tomcat服务器关闭前需要手动关闭Netty Websocket相关资源，否则会造成内存泄漏。
     *      1. 释放Netty Websocket相关连接；
     *      2. 关闭Netty Websocket服务器线程
     */
    @SuppressWarnings("deprecation")
    @PreDestroy //当Bean在容器销毁之前，调用被@PreDestroy注解的方法
    public void close() {
        log.info("正在释放Netty Websocket相关连接...");
        nettyBootstrapServer.close();
        log.info("正在关闭Netty Websocket服务器线程...");
        nettyThread.stop();
        log.info("系统成功关闭！");
    }
}
