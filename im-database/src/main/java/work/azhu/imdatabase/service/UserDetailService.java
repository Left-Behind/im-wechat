package work.azhu.imdatabase.service;


import work.azhu.imcommon.model.bean.common.User;

import java.util.List;

/**
 * @Author: Azhu
 * @Date: 2019/5/8 13:05
 * Description:
 */
public interface UserDetailService {

    /**
     * 根据userId查询用户信息
     * @param userId
     * @return
     */
    User queryUserDetailById(String userId);

    /**
     * 返回所有用户
     * @return
     */
    List<User> queryUserDetailList();

    /**
     * 根据id修改用户信息
     * @param userInfo
     * @return
     */
    Integer updateUserInfo(User userInfo);

    /**
     * 根据id删除用户账号
     * @param id
     * @return
     */
    Integer deleteUserDetailById(String id);

    /**
     * 新增用户账号
     * @param userInfo
     * @return
     */
    Integer insertUserDetail(User userInfo);
}
