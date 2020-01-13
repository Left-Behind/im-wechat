package work.azhu.imweb.service.Impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.azhu.imweb.config.qiniu.QiNiuProperties;
import work.azhu.imweb.service.QiNiuService;

import java.io.File;
import java.io.InputStream;

/**
 * @Author Azhu
 * @Date 2020/1/13 14:07
 * @Description
 */
@Service
public class QiNiuServiceImpl implements QiNiuService, InitializingBean {

    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private Auth auth;
    @Autowired
    private QiNiuProperties qiNiuProperties;

    private StringMap putPolicy;

    @Override
    public Response uploadFile(File file,String key)throws QiniuException {
        Response response = this.uploadManager.put(file,key,getUploadToken());
        int retry = 0;
        while(response.needRetry()&&retry<3){
            response = this.uploadManager.put(file,key,getUploadToken());
            retry++;
        }
        return response;
    }

    @Override
    public Response uploadFile(InputStream inputStream,String key)throws QiniuException {
        Response response = this.uploadManager.put(inputStream,key,getUploadToken(),null,null);
        int retry = 0;
        while(response.needRetry()&&retry<3){
            response = this.uploadManager.put(inputStream,key,getUploadToken(),null,null);
            retry++;
        }
        return response;
    }

    /**
     * 删除
     * @param key
     * @return
     */
    @Override
    public Response delete(String key) throws QiniuException {
        Response response = bucketManager.delete(qiNiuProperties.getBucket(),key);
        int rety = 0;
        while(response.needRetry()&&rety<3){
            response = bucketManager.delete(qiNiuProperties.getBucket(),key);
        }
        return response;
    }

    /**
     * 实现回调接口,在设置属性之后
     * putPolicy: 上传策略
     * 上传策略是资源上传时附带的一组配置设定
     * 通过这组配置信息,七牛云存储可以了解用户上传的需求
     * 他将上传什么资源，上传到那个空间，上传结果是通过回调通知还是使用重定向
     * 上传策略同时还参与请求验证
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy. put("returnBody" ,
                "{\"key\":\"$(key)\","
                +"\"hash\":\"$(etag)\","
                +"\"bucket\":\"$(bucket)\","
                +"\"width\":$(imageInfo.width),"
                +"\"height\":${imageInfo.height}}");
    }


    /**
     * 获取上传凭证
     * @return
     */
    private String getUploadToken(){
        return this.auth.uploadToken(qiNiuProperties.getBucket(),null,3600,putPolicy.put("insertOnly",0));
    }
}
