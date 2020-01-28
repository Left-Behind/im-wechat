package work.azhu.imdatabase.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imdatabase.service.UserDetailService;

import java.util.List;

/**
 * @Author Azhu
 * @Date 2019/11/25 19:22
 * @Description
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private UserDetailService userInfoService;

    @RequestMapping("/test")
    public String test(){
        log.info(userInfoService.queryUserDetailById("1").toString());
        User user = new User();
        user.setUserName("Azhu");
        user.setPassword("123");
        user.setEmail("2576419596");
        user.setAvatarUrl("www.azhu.work");
        userInfoService.insertUserDetail(user);
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailById("1").toString());
        return "中勒";
    }
    @RequestMapping("/test2")
    public String test2(){
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailById("1").toString());
        return "中勒";
    }

    @RequestMapping("/test3")
    public String Master(){
        log.info(userInfoService.queryUserDetailById("1").toString());
        log.info(userInfoService.queryUserDetailByIdMaster("2").toString());
        log.info(userInfoService.queryUserDetailByIdMaster("3").toString());
        log.info(userInfoService.queryUserDetailByIdMaster("4").toString());
        return "中勒";
    }




    @RequestMapping("/test1")
    public String test1(){
       return "测试Jrebelsssss";
    }
}
