package work.azhu.imdatabase.service.lmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imdatabase.mapper.UserInfoMapper;
import work.azhu.imdatabase.model.UserInfo;
import work.azhu.imdatabase.service.UserInfoService;

import java.util.List;

/**
 * @Author: Azhu
 * @Date: 2019/5/8 13:07
 * Description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public UserInfo queryUserInfoByUserId(Integer userId) {
        return userInfoMapper.queryUserInfoByUserId(userId);
    }

    @Override
    public List<UserInfo> queryAllUserInfo() {
        return userInfoMapper.queryAllUserInfoList();
    }

    @Override
    public Integer updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateUserInfo(userInfo);
    }

    @Override
    public Integer deleteUserInfoById(Integer id) {
        return userInfoMapper.deleteUserInfoById(id);
    }

    @Override
    public Integer insertUserInfo(UserInfo userInfo) {
        return  userInfoMapper.insertUserInfo(userInfo);
    }
}
