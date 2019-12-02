package work.azhu.imcommon.model.bean.common;

import lombok.Data;

import java.util.Date;

/**
 * @Author Azhu
 * @Date 2019/12/2 11:26
 * @Description: 消息类
 */
@Data
public class message {

    private Long id;

    /**发送者*/
    private String fromUserId;

    /**接收者*/
    private String toUserId;

    /**接收群组*/
    private String toGroupId;

    /**消息文本*/
    private String content;

    /**消息类型*/
    private Integer type;

    /**发送时间*/
    private Date createTime;
}

