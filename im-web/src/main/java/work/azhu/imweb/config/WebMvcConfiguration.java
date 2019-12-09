package work.azhu.imweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import work.azhu.imweb.interceptors.AuthInterceptor;

/**
 * @Author Azhu
 * @Date 2019/12/9 10:06
 * @Description:
 */
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        super.addInterceptors(registry);
    }
}
