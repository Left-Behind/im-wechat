package work.azhu.imcommon.model.bean.common;

import lombok.Data;

import java.util.Date;

/**
 * @Author Azhu
 * @Date 2019/12/2 11:26
 * @Description: 用户类
 */
@Data
public class User {

    /**账号*/
    private Long id;

    /**用户名*/
    private String userName;

    /**密码*/
    private String password;

    /**邮箱*/
    private String email;

    /**头像地址*/
    private String avatarUrl;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;
}
