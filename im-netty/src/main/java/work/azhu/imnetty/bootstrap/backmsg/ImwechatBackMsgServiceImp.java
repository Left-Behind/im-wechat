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
    public Message sendTo(Map map) {
        return null;
    }

    @Override
    public Message getMsg(Map map) {
        Message message = new Message();

        String fromUserId = (String) map.get(ChatConstant.FROMUSERID);
        String toUserId = (String) map.get(ChatConstant.TOUSERID);
        String toGroupId = (String) map.get(ChatConstant.TOGROUPID);
        message.setFromUserId(Long.valueOf(fromUserId));
        message.setType(ChatConstant.SINGLE_SENDING);
        message.setContent((String) map.get("content"));
        message.setFileUrl((String) map.get("fileUrl"));
        message.setOriginalFilename((String) map.get("originalFilename"));
        message.setToUserId(Long.valueOf(toUserId==null?"0":toUserId));
        message.setToGroupId(Long.valueOf(toGroupId==null?"0":toGroupId));
        message.setCreateTime(new Date().getTime());
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
