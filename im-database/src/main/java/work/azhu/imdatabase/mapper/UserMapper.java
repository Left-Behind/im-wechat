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

    List<User> queryUserDetailList();

    /**
     * 根据账号id查询用户信息
     * @param id
     * @return
     */
    User queryUserDetailById(String id);

    /**
     * 根据账号id删除用户
     * @param id
     * @return
     */
    Integer deleteUserDetailById(String id);

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

}