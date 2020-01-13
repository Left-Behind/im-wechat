package work.azhu.imweb.controller.qiniucloud;

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
public class QiNiuCloudControoler {

    @Autowired
    QiNiuService qiNiuService;

    @Autowired
    private QiNiuProperties qiNiuProperties;

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

}