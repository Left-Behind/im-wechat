package work.azhu.imcommon.model.bean.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Azhu
 * @Date 2019/12/2 11:26
 * @Description: 消息类
 */
@Data
public class Message implements Serializable {

    private Long id;

    /**发送者*/
    private Long fromUserId;

    /**接收者*/
    private Long toUserId;

    /**接收群组*/
    private Long toGroupId;

    /**消息文本*/
    private String content;

    /**消息类型*/
    private String type;

    /**文件路径*/
    private String fileUrl;

    /**文件名*/
    private String originalFilename;

    /**发送时间*/
    private Long createTime;

    /**返回消息状态*/
    private String status;

}

