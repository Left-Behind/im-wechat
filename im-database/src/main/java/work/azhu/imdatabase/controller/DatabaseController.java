package work.azhu.imdatabase.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import work.azhu.imcommon.common.BaseController;
import work.azhu.imcommon.model.bean.common.Message;
import work.azhu.imcommon.util.SnowflakeIdWorker;
import work.azhu.imdatabase.mapper.MessageMapper;
import work.azhu.imdatabase.mapper.UserMapper;


@Api("测试数据库接口")
@Controller
public class DatabaseController extends BaseController {


    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;


    @ApiOperation(value="插入消息数据", notes="insertMessage")
    @RequestMapping(value = "/insertMessage",method = RequestMethod.POST)
    @ResponseBody
    public String insertMessage(){

        Message message = new Message();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(2, 1);
        message.setId(idWorker.nextId());
        message.setContent("测试消息");
        message.setFromUserId(1L);
        messageMapper.insertMessage(message);

        return "suceess";
    }

    @ApiOperation(value="查询用户的消息数据", notes="queryMessageByUserId")
    @RequestMapping(value = "/queryMessageByUserId",method = RequestMethod.POST)
    @ResponseBody
    public String queryMessageByUserId(@RequestParam("userId") Long userId ){

        return resSuccessJson(messageMapper.queryMessageByUserId(userId));
    }

    @ApiOperation(value="查询群组的消息数据", notes="queryMessageByGroupId")
    @RequestMapping(value = "/queryMessageByGroupId",method = RequestMethod.POST)
    @ResponseBody
    public String queryMessageByGroupId(@RequestParam("groupId") Long groupId){


        return resSuccessJson(messageMapper.queryMessageByGroupId(groupId));
    }

}
