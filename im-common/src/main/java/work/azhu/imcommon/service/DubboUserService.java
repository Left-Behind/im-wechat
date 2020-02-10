package work.azhu.imcommon.service;

import work.azhu.imcommon.model.bean.common.User;

import java.util.List;

public interface DubboUserService {

    /**
     * 查询所有用户
     * @return
     */
    List<User> queryUserDetailList();

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
     * 校验token值是否正确,返回用户信息
     * @param token
     * @param ip
     * @return
     */
    User verifyToken(String token,String ip);


    void insertUserDetail(User user);
}
