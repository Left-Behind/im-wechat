package work.azhu.imdatabase.mapper;



import org.apache.ibatis.annotations.Mapper;
import work.azhu.imcommon.model.bean.common.User;

import java.util.List;

/**
 * @Author: Azhu
 * @Date: 2019/5/1 17:43
 * Description:
 */
@Mapper
public interface UserMapper {


    Integer insertUserDetail(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> queryUserDetailList();

    /**
     * 根据账号userId查询用户信息
     * @param userId
     * @return
     */
    User queryUserDetailById(String userId);

    /**
     * 根据账号userId删除用户
     * @param userId
     * @return
     */
    Integer deleteUserDetailById(String userId);

    /**
     * 根据账号id修改用户信息
     * @param user
     * @return
     */
    Integer updateUserDetailById(User user);


    /**
     * 根据账号userName查询用户信息
     * @param userName
     * @return
     */
    User queryUserByUserName(String userName);

    /**
     * 判断是否第三方登录
     * @param token
     * @return
     */
    User queryUserByAccessToken(String token);

}