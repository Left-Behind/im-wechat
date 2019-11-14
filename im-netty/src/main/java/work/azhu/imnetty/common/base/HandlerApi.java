package work.azhu.imnetty.common.base;

import io.netty.channel.Channel;

/**
 * @Author Azhu
 * @Date 2019/11/14 17:44
 * @Description
 */
public interface HandlerApi {
    void close(Channel channel);
}
