package work.azhu.imnetty.common.base;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpMessage;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;

import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/14 18:05
 * @Description: 业务层伪接口
 */
public abstract class HandlerService implements HandlerApi {

    /**
     * HTTP获取在线用户标签列表
     * @param channel {@link Channel} 链接实例
     */
    public abstract void getList(Channel channel);

    /**
     * HTTP获取在线用户数
     * @param channel {@link Channel} 链接实例
     */
    public abstract void getSize(Channel channel);

    /**
     * HTTP以服务端向指定用户发送通知
     * @param channel 链接实例
     * @param sendServerVO
     */
    public abstract void sendFromServer(Channel channel, SendServerVO sendServerVO);

    /**
     * HTTP以服务端处理发送
     * @param channel
     */
    public abstract void sendInChat(Channel channel, FullHttpMessage msg);

    /**
     * 用户未找到匹配Uri
     * @param channel {@link Channel} 链接实例
     */
    public abstract void notFindUri(Channel channel);

    /**
     * 登录类型
     * @param channel {@link Channel} 链接实例
     * @param map {@link Map} 数据信息
     * @return {@link Boolean} 成功失败
     */
    public abstract boolean login(Channel channel, Map<String,Object> map);

    /**
     * 发送给自己
     * @param channel {@link Channel} 链接实例
     * @param maps {@link Map} 数据信息
     */
    public abstract void sendMeText(Channel channel,Map<String,Object> maps);

    /**
     * 发送给某人
     * @param channel {@link Channel} 链接实例
     * @param maps {@link Map} 数据信息
     */
    public abstract void sendToText(Channel channel, Map<String,Object> maps);

    /**
     * 发送给群聊
     * @param channel {@link Channel} 链接实例
     * @param maps {@link Map} 数据信息
     */
    public abstract void sendGroupText(Channel channel, Map<String, Object> maps);

    /**
     * 登录校验
     * @param channel {@link Channel} 链接实例
     * @param maps {@link Map} 数据信息
     */
    public abstract void verify(Channel channel, Map<String, Object> maps);

    /**
     * 发送图片给个人
     * @param channel {@link Channel} 链接实例
     * @param maps {@link Map} 数据信息
     */
    public abstract void sendPhotoToMe(Channel channel, Map<String, Object> maps);
}