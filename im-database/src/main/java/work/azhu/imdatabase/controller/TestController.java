package work.azhu.imdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import work.azhu.imdatabase.model.UserInfo;
import work.azhu.imdatabase.service.UserInfoService;

import java.util.List;

/**
 * @Author Azhu
 * @Date 2019/11/25 19:22
 * @Description
 */
@Controller
public class TestController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/test")
    public void test(){
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
}
