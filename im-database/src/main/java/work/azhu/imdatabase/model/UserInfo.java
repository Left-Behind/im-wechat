package work.azhu.imdatabase.model;

import lombok.Data;

/**
 * @Author: Azhu
 * @Date: 2019/5/1 11:52
 * Description:用户类记录每个人的信息
 */
@Data
public class UserInfo {

    private Integer userId;
    private String userName;
    private String password;
    private String avatarUrl;
    private String email;
}
