package work.azhu.imdatabase.service.dubbo;


import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.service.DubboUserService;
import work.azhu.imdatabase.mapper.UserMapper;

import java.util.List;

@Slf4j
@Service(version = "${demo.service.version}")
public class DubboUserServiceImpl implements DubboUserService {

    @Autowired
    public UserMapper userMapper;

    @Override
    public List<User> queryUserDetailList() {
        return userMapper.queryUserDetailList();
    }

    @Override
    public User queryUserByUserName(String userName) {
        return userMapper.queryUserByUserName(userName);
    }

    @Override
    public User queryUserByAccessToken(String token) {
        return userMapper.queryUserByAccessToken(token);
    }

    @Override
    public User queryUserDetailById(String userId) {
        return userMapper.queryUserDetailById(userId);
    }

    @Override
    public Integer deleteUserDetailById(String userId) {
        return userMapper.deleteUserDetailById(userId);
    }

    @Override
    public Integer updateUserDetailById(User user) {
        return userMapper.updateUserDetailById(user);
    }
}
