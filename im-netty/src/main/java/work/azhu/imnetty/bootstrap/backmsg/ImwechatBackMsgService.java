package work.azhu.imnetty.bootstrap.backmsg;

import work.azhu.imcommon.model.bean.common.Message;

import java.util.Map;

public interface ImwechatBackMsgService {

    /**
     * 登录成功返回信息
     * @return {@link Message} Json
     */
    Message loginSuccess();

    /**
     * 登录失败返回信息
     * @return {@link Message} Json
     */
    Message loginError();


    /**
     * 发送给某人的信息
     * @param map
     * @return
     */
    Message sendTo(Map map);

    /**
     * 某人接收到他人发送给他的消息
     * @param map
     * @return
     */
    Message getMsg(Map map);

    /**
     * 发送消息到群里
     * @param map
     * @return
     */
    Message sendGroup(Map map);

    String sendOffLine();
}
