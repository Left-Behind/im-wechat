package work.azhu.imdatabase;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.test.context.junit4.SpringRunner;
import work.azhu.imdatabase.model.UserInfo;
import work.azhu.imdatabase.service.UserInfoService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableDubbo
@EnableHystrix
public class SpringbootDatasourceAopApplicationTests {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void contextLoads() {
    }
    @Test
    public void queryAllUserInfoList() throws InterruptedException {
        List<UserInfo> list=userInfoService.queryAllUserInfo();
        for (UserInfo userInfo : list) {
            System.out.println(userInfo.toString());
        }
        UserInfo userInfo=userInfoService.queryUserInfoByUserId(1);
        UserInfo userInfo1=userInfoService.queryUserInfoByUserId(2);
        userInfo.setUserName("www.azhu.work");
        userInfo.setUserId(8);
        userInfo.setPassword("sdfsdfasd");
        userInfo.setEmail("sdfdsf");
        userInfo.setAvatarUrl("12315646489");
        Integer insert=userInfoService.insertUserInfo(userInfo);
        userInfo1=userInfoService.queryUserInfoByUserId(3);
        Integer update=userInfoService.updateUserInfo(userInfo);
        //Thread.sleep(500);
        userInfo1=userInfoService.queryUserInfoByUserId(4);

    }
    @Test
    public void DEL(){
        userInfoService.deleteUserInfoById(6);
    }
}
