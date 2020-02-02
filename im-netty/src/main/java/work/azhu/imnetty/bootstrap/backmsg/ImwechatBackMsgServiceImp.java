package work.azhu.imnetty.bootstrap.backmsg;


import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imnetty.common.constant.ChatConstant;

import java.util.Date;
import java.util.Map;

@Service
public class ImwechatBackMsgServiceImp implements ImwechatBackMsgService{
    @Override
    public Message loginSuccess() {
        Message message = new Message();
        message.setType(ChatConstant.LOGIN);
        message.setStatus(ChatConstant.SUCCESS);
        return message;
    }

    @Override
    public Message loginError() {
        Message message = new Message();
        message.setType(ChatConstant.LOGIN);
        message.setStatus(ChatConstant.FAIL);
        return message;
    }

    @Override
    public Message loginNotify() {
        Message message = new Message();
        message.setType(ChatConstant.NOTIFY);
        message.setStatus(ChatConstant.SUCCESS);
        return message;
    }

    @Override
    public Message sendTo(Map map) {
        return null;
    }

    @Override
    public Message getMsg(Map map) {
        Message message = new Message();

        String fromUserId = String.valueOf(map.get(ChatConstant.FROMUSERID));
        String toUserId = String.valueOf(map.get(ChatConstant.TOUSERID));
        String toGroupId = String.valueOf(map.get(ChatConstant.TOGROUPID));
        message.setFromUserId(Long.valueOf(fromUserId));
        message.setType(String.valueOf(map.get(ChatConstant.TYPE)));
        message.setContent((String) map.get("content"));
        message.setFileUrl((String) map.get("fileUrl"));
        message.setOriginalFilename((String) map.get("originalFilename"));
        message.setToUserId(Long.valueOf(toUserId.equals("null")?"0":toUserId));
        message.setToGroupId(0L);
        message.setCreateTime(new Date().getTime());
        message.setStatus(ChatConstant.SUCCESS);
        return message;
    }

    @Override
    public Message sendGroup(Map map) {
        return null;
    }

    @Override
    public String sendOffLine() {
        return "该用户不在线";
    }
}
