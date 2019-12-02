package work.azhu.imdatabase;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.test.context.junit4.SpringRunner;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imdatabase.model.UserInfo;
import work.azhu.imdatabase.service.UserDetailService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableDubbo
@EnableHystrix
@Slf4j
public class SpringbootDatasourceAopApplicationTests {

    @Autowired
    private UserDetailService userInfoService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void imwechat() {
        List<User> list = userInfoService.queryUserDetailList();
        list.stream().forEach(
                item -> {
                    log.info(item.toString());
                    //System.out.println(item.toString());
                }
        );
        User user = new User();
        user.setUserName("Azhu");
        user.setPassword("123");
        user.setEmail("2576419596");
        user.setAvatarUrl("www.azhu.work");
        userInfoService.insertUserDetail(user);
        list = userInfoService.queryUserDetailList();
        list.stream().forEach(
                item -> {
                    log.info(item.toString());
                    //System.out.println(item.toString());
                }
        );
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailById("2").toString());
    }
}
