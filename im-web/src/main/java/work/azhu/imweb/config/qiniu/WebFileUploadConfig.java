package work.azhu.imweb.config.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;

/**
 * @Author Azhu
 * @Date 2020/1/13 13:42
 * @Description
 */
@Configuration
@ConditionalOnClass({Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class})
@ConditionalOnProperty(prefix = "spring.http.multipart",name = "enable",matchIfMissing = true)
@EnableConfigurationProperties(MultipartProperties.class)
public class WebFileUploadConfig {

    /**
     * 七牛云配置文件
     */
    @Autowired
    private QiNiuProperties qiNiuProperties;

    private final MultipartProperties multipartProperties;

    public WebFileUploadConfig(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnBean(MultipartResolver.class)
    public StandardServletMultipartResolver multipartResolver(){
        StandardServletMultipartResolver multipartResolver=new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }

    /**
     * 华东 Zone.zone0()
     * 华北 Zone.zone1()
     * 华南 Zone.zone2()
     * 北美 Zone.zoneNa0()
     * @return
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig(){
        return new com.qiniu.storage.Configuration(Zone.zone0());
    }

    /**
     * 构建一个七牛上传工具实列
     * @return
     */
    @Bean
    public UploadManager uploadManager(){
        return new UploadManager(qiniuConfig());
    }

    /**
     * 认证信息实列，密钥配置
     * @return
     */
    @Bean
    public Auth auth(){
        return Auth.create(qiNiuProperties.getAccessKey(),qiNiuProperties.getSecretKey());
    }

    /**
     * 构建七牛云空间管理实列
     * @return
     */
    @Bean
    public BucketManager bucketManager(){
        return  new BucketManager(auth(),qiniuConfig());
    }

    /**
     * 配置gson为json解析
     * @return
     */
    @Bean
    public Gson gson(){
        return new Gson();
    }
}
