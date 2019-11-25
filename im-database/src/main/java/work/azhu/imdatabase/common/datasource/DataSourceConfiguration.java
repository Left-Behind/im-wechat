package work.azhu.imdatabase.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Azhu
 * @date 2019/9/3 15:16
 */
@Configuration
@ConditionalOnClass({DruidDataSource.class})
@EnableConfigurationProperties(DruidDataSourceProperties.class)
@Slf4j
public class DataSourceConfiguration {

    private static final String CUSTOM_DATA_SOURCE_PREFIX = "custom.datasource";

    private static final String DRUID_DATA_SOURCE_PREFIX = "spring.datasource.druid";

    private static final String SEPARATOR = ",";

    /**
     * 详见druid.jar:META-INF/druid-filter.properties
     */
    private static final String DRUID_FILTER_NAMES = "stat,mergeStat,counter,config,encoding,slf4j,log4j,log4j2,commonlogging,wall";

    @Autowired
    private Environment env;

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = CUSTOM_DATA_SOURCE_PREFIX, name = "names")//配置文件里有prefix才会注入bean
    public DataSource dataSource(DruidDataSourceProperties properties) throws SQLException {
        //获取配置文件
        Properties property= getPropertyByEnv();
        String dataSourceNames = property.getProperty("names");
        Map<Object, Object> targetDataSources = new HashMap<>();
        String writePrefix = DataSourceType.write.getType();
        String readPrefix = DataSourceType.read.getType();
        int readCnt = 0;
        //循环配置druid数据源
        for (String dsName : dataSourceNames.split(SEPARATOR)) { ;
            properties.setUrl(property.getProperty(dsName+".url"));
            properties.setUsername(property.getProperty(dsName+".username"));
            properties.setPassword(property.getProperty(dsName+".password"));

            DruidDataSource druidDataSource = new DruidDataSource();
            // Filter配置
            //initDruidFilters(druidDataSource);
            druidDataSource.configFromPropety(properties.toProperties());
            druidDataSource.init();
            // 一写多读
            Object dsKey = dsName.startsWith(readPrefix) ? readCnt++ : writePrefix;
            targetDataSources.put(dsKey, druidDataSource);
        }
        System.out.println("所有读库数据源数量"+readCnt);
        MyAbstractRoutingDataSource dataSource = new MyAbstractRoutingDataSource(readCnt);
        // 管理所有的数据源
        dataSource.setTargetDataSources(targetDataSources);
        // 默认数据源（找不到数据源的时候使用）
        dataSource.setDefaultTargetDataSource(targetDataSources.get(writePrefix));
        log.info("-------------------- MyAbstractRoutingDataSource inited successfull ---------------------");
        return dataSource;
    }

    /**
     * 初始化Druid的Filters，兼容druid-spring-boot-starter的用法
     *
     * @param druidDataSource 数据源
     * @throws SQLException
     */
     /*private void initDruidFilters(DruidDataSource druidDataSource) throws SQLException {

        List<Filter> filters = druidDataSource.getProxyFilters();
         Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(env);
         Binder binder = new Binder(sources);
         BindResult<Properties> bindResult = binder.bind(DRUID_DATA_SOURCE_PREFIX + ".filter.", Properties.class);
         Properties filterProperty= bindResult.get();

        String[] filterNameArray = DRUID_FILTER_NAMES.split(SEPARATOR);

        for (String filterName : filterNameArray) {
            filterName = filterName.trim();
            Map<String, Object> filterValueMap = filterProperty.getOrDefault(filterName + ".");
            String enabledVal = (String) filterValueMap.get("enabled");
            // stat缺省启用
            boolean enabled = "stat".equalsIgnoreCase(filterName) && enabledVal == null || "true".equalsIgnoreCase(enabledVal);
            if (enabled) {
                // 加载对应的filter
                FilterManager.loadFilter(filters, filterName);
                Filter filter = filters.get(filters.size() - 1);
                // 绑定配置
                MutablePropertyValues propertyValues = new MutablePropertyValues(filterValueMap);
                RelaxedDataBinder dataBinder = new RelaxedDataBinder(filter);
                dataBinder.bind(propertyValues);
            }
        }
    }*/

    /**
     * Spring监控配置，主要用于service层，说明请参考Druid Github Wiki，配置_Druid和Spring关联监控配置
     * Spring监控AOP切入点，如x.y.z.service.*,配置多个英文逗号分隔
     * 注意：不要使用spring.datasource.druid.aop-patterns。使用该配置会生成一个DefaultAdvisorAutoProxyCreator，从而导致多次代理
     */
    /*@Bean
    public Advice druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean
    public Advisor advisor() {
        return new RegexpMethodPointcutAdvisor("com.azhu.springbootdatasourceaop.service..*.*(..)", druidStatInterceptor());
    }*/

    public Properties getPropertyByEnv(){
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(env);
        Binder binder = new Binder(sources);
        BindResult<Properties> bindResult = binder.bind(CUSTOM_DATA_SOURCE_PREFIX, Properties.class);
        return bindResult.get();
    }
}