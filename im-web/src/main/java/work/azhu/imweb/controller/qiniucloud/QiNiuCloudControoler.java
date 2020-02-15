package work.azhu.imweb.controller.qiniucloud;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import work.azhu.imcommon.common.BaseController;
import work.azhu.imcommon.model.bean.common.User;
import work.azhu.imcommon.service.DubboUserService;
import work.azhu.imweb.config.qiniu.QiNiuProperties;
import work.azhu.imweb.service.QiNiuService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2020/1/13 14:37
 * @Description
 */
@Api("七牛云上传")
@Controller
public class QiNiuCloudControoler extends BaseController {

    @Autowired
    QiNiuService qiNiuService;

    @Autowired
    private QiNiuProperties qiNiuProperties;

    @Reference(version = "${demo.service.version}")
    private DubboUserService dubboUserService;

    @RequestMapping(" index")
    public String index() {
        return " index";
    }

    @ApiOperation(value="上传图片", notes="上传图片")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadImgQiniu(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        InputStream inputStream = file.getInputStream();
        Response uploadFile = qiNiuService.uploadFile(inputStream,file.getOriginalFilename());
        boolean ok = uploadFile.isOK();
        if (ok == true) {
            System.out.println(file.getOriginalFilename() + "上传成功! ");
        }
        StringMap jsonToMap = uploadFile.jsonToMap();
        jsonToMap.put("size", file.getSize());
        jsonToMap.put("path", qiNiuProperties.getPath());
        Map<String, Object> map = jsonToMap.map();
        return map;

    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Integer> deleteQiniuImg(@RequestParam("key") String key) throws QiniuException {
        Response delete = qiNiuService.delete(key);
        boolean ok = delete.isOK();
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (ok) {
            map.put("0K", 200);
            System.out.println(key + "图片已删除...");
        }
        return map;

    }



    @ApiOperation(value="测试dubbo服务", notes="测试dubbo服务")
    @RequestMapping(value = "/testDubbo",method = RequestMethod.GET)
    @ResponseBody
    public String testDubbo(){

        User user=dubboUserService.queryUserByUserName("Left");
        return user.getAvatarUrl();

    }

    @RequestMapping(value = "/imwechat/upload",method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        InputStream inputStream = file.getInputStream();
        Response uploadFile = qiNiuService.uploadFile(inputStream,file.getOriginalFilename());
        boolean ok = uploadFile.isOK();
        if (ok == true) {
            System.out.println(file.getOriginalFilename() + "上传成功! ");
        }
        StringMap jsonToMap = uploadFile.jsonToMap();
        jsonToMap.put("size", file.getSize());
        jsonToMap.put("path", qiNiuProperties.getPath());
        Map<String, Object> map = jsonToMap.map();

        map.put("originalFilename",jsonToMap.get("key").toString());
        map.put("fileUrl","http://www.image.azhu.work/"+jsonToMap.get("key").toString());
        return resSuccessJson(map);

    }
}