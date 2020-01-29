package work.azhu.imdatabase.service.dubbo;


import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.service.DubboUserService;
import work.azhu.imcommon.util.JwtUtil;
import work.azhu.imdatabase.mapper.UserMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Service(version = "${demo.service.version}")
public class DubboUserServiceImpl implements DubboUserService {

    @Autowired
    public UserMapper userMapper;

    @Value("${jwt.key}")
    private String key;


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


    @Override
    public User verifyToken(String token,String ip) {

        Map<String,Object> map=JwtUtil.decode(token,key,ip);
        if(map!=null&&map.get("userName")!=null){
            String userName = map.get("userName").toString();
            return userMapper.queryUserByUserName(userName);
        }
        return null;
    }
}
