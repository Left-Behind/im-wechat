package work.azhu.imdatabase.common.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterManager;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ConditionalOnProperty(prefix = CUSTOM_DATA_SOURCE_PREFIX, name = "names")
    public DataSource dataSource(DruidDataSourceProperties properties) throws SQLException {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, CUSTOM_DATA_SOURCE_PREFIX + ".");
        String dataSourceNames = propertyResolver.getProperty("names");
        Map<Object, Object> targetDataSources = new HashMap<>();
        String writePrefix = DataSourceType.write.getType();
        String readPrefix = DataSourceType.read.getType();
        int readCnt = 0;
        for (String dsName : dataSourceNames.split(SEPARATOR)) {
            RelaxedPropertyResolver dsMap = new RelaxedPropertyResolver(propertyResolver, dsName + ".");
            properties.setUrl(dsMap.getRequiredProperty("url"));
            properties.setUsername(dsMap.getRequiredProperty("username"));
            properties.setPassword(dsMap.getRequiredProperty("password"));

            DruidDataSource druidDataSource = new DruidDataSource();
            // Filter配置
            initDruidFilters(druidDataSource);
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
        log.info("-------------------- MyAbstractRoutingDataSource inited ---------------------");
        return dataSource;
    }

    /**
     * 初始化Druid的Filters，兼容druid-spring-boot-starter的用法
     *
     * @param druidDataSource 数据源
     * @throws SQLException
     */
    private void initDruidFilters(DruidDataSource druidDataSource) throws SQLException {

        List<Filter> filters = druidDataSource.getProxyFilters();

        RelaxedPropertyResolver filterProperty = new RelaxedPropertyResolver(env, DRUID_DATA_SOURCE_PREFIX + ".filter.");

        String[] filterNameArray = DRUID_FILTER_NAMES.split(SEPARATOR);

        for (String filterName : filterNameArray) {
            filterName = filterName.trim();
            Map<String, Object> filterValueMap = filterProperty.getSubProperties(filterName + ".");
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
    }

    /**
     * Spring监控配置，主要用于service层，说明请参考Druid Github Wiki，配置_Druid和Spring关联监控配置
     * Spring监控AOP切入点，如x.y.z.service.*,配置多个英文逗号分隔
     * 注意：不要使用spring.datasource.druid.aop-patterns。使用该配置会生成一个DefaultAdvisorAutoProxyCreator，从而导致多次代理
     */
    @Bean
    public Advice druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean
    public Advisor advisor() {
        return new RegexpMethodPointcutAdvisor("com.azhu.springbootdatasourceaop.service..*.*(..)", druidStatInterceptor());
    }
}