package work.azhu.imweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import work.azhu.imcommon.common.BaseController;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.service.DubboMessageService;
import work.azhu.imcommon.service.DubboNettyService;
import work.azhu.imcommon.service.DubboUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api("用户信息Controller")
@Controller
@RequestMapping("chatroom")
public class UserController extends BaseController {

    @Value("${jwt.key}")
    private String key;

    @Reference(version = "${demo.service.version}")
    private DubboUserService dubboUserService;

    @Reference(version = "${demo.service.version}")
    private DubboNettyService dubboNettyService;

    @Reference(version = "${demo.service.version}")
    private DubboMessageService dubboMessageService;

    @ApiOperation(value="获取用户信息", notes="获取用户信息")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public String getUserInfo(@RequestBody JSONObject object){
        String token = object.getString("token");
        Map<String,Object> map = getMapByJwt(key,token);
        String userName = (String) map.get("userName");
        User user = dubboUserService.queryUserByUserName(userName);
        return resSuccessJson(user);
    }

    @ApiOperation(value="获取当前好友列表", notes="获取当前好友列表")
    @RequestMapping(value = "/getFriendList",method = RequestMethod.GET)
    @ResponseBody
    public String getUserInfo(@RequestParam("userId") String userId){
        List<User> friendList = dubboUserService.queryUserDetailList();
        List<User> reslutList = new ArrayList<>();
        friendList.stream().forEach(item->{
            String friendUserId = item.getId().toString();
            if(/*dubboNettyService.queryUserIdIfOnline(friendUserId)&&*/!friendUserId.equals(userId)){
                reslutList.add(item);
            }
        });
        return resSuccessJson(reslutList);
    }
    @ApiOperation(value="获取当前用户消息", notes="获取当前用户消息")
    @RequestMapping(value = "/getMessageList",method = RequestMethod.GET)
    @ResponseBody
    public String getMessageList(@RequestParam("userId") String userId){
        List<Message> messageList = dubboMessageService.queryMessageByUserId(Long.valueOf(userId));
        return resSuccessJson(messageList);
    }




}
