package work.azhu.imnetty.bootstrap.channel;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpMessage;
import org.springframework.stereotype.Service;
import work.azhu.imnetty.common.base.HandlerService;

import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/15 10:22
 * @Description
 */
@Service
public class HandlerServiceImp extends HandlerService {
    @Override
    public void getList(Channel channel) {

    }

    @Override
    public void getSize(Channel channel) {

    }

    @Override
    public void sendInChat(Channel channel, FullHttpMessage msg) {

    }

    @Override
    public void notFindUri(Channel channel) {

    }

    @Override
    public boolean login(Channel channel, Map<String, Object> map) {
        return false;
    }

    @Override
    public void sendMeText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendToText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendGroupText(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void verify(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void sendPhotoToMe(Channel channel, Map<String, Object> maps) {

    }

    @Override
    public void close(Channel channel) {

    }
}
