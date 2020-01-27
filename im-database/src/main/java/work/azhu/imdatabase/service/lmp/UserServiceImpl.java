package work.azhu.imdatabase.service.lmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imdatabase.common.annotation.Master;
import work.azhu.imdatabase.mapper.UserMapper;
import work.azhu.imdatabase.service.UserDetailService;

import java.util.List;

/**
 * @Author: Azhu
 * @Date: 2019/5/8 13:07
 * Description:
 */
@Service
public class UserServiceImpl implements UserDetailService {

    @Autowired
    private UserMapper userDetailMapper;

    @Override
    public User queryUserDetailById(String userId) {
        return userDetailMapper.queryUserDetailById(userId);
    }

    @Override
    public List<User> queryUserDetailList() {
        return userDetailMapper.queryUserDetailList();
    }

    @Override
    public Integer updateUserInfo(User userInfo) {
        return userDetailMapper.updateUserDetailById(userInfo);
    }

    @Override
    public Integer deleteUserDetailById(String id) {
        return userDetailMapper.deleteUserDetailById(id);
    }

    @Override
    public Integer insertUserDetail(User userInfo) {
        return userDetailMapper.insertUserDetail(userInfo);
    }

    @Master
    @Override
    public User queryUserDetailByIdMaster(String id) {
        return userDetailMapper.queryUserDetailById(id);
    }
}
