package work.azhu.imdatabase.common.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author Azhu
 * @date 2019/9/3 15:14
 * druid默认配置
 */
@Data
@ConfigurationProperties(DruidDataSourceProperties.DRUID_PREFIX)
public class DruidDataSourceProperties {

    public static final String DRUID_PREFIX = "spring.datasource.druid";

    /**
     * 数据库url
     */
    private String url;

    /**
     * 数据库帐号
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 初始化时建立物理连接的个数, 默认0。初始化发生在显示调用init方法，或者第一次getConnection时
     */
    private Integer initialSize = 5;

    /**
     * 最小连接池数量
     */
    private Integer minIdle = 5;

    /**
     * 最大连接池数量，默认8
     */
    private Integer maxActive = 200;

    /**
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
     */
    private Long maxWait = 60000L;

    private Boolean useUnfairLock;

    /**
     * 是否缓存preparedStatement，也就是PSCache，默认false。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
     */
    private Boolean poolPreparedStatements = false;

    /**
     * 要启用PSCache，默认-1。必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
     */
    private Integer maxPoolPreparedStatementPerConnectionSize = -1;

    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
     */
    private String validationQuery = "select 'x'";

    /**
     * 申请连接时执行validationQuery检测连接是否有效，默认true。做了这个配置会降低性能。
     */
    private Boolean testOnBorrow = false;

    /**
     * 归还连接时执行validationQuery检测连接是否有效，默认false。做了这个配置会降低性能。
     */
    private Boolean testOnReturn = false;

    /**
     * 默认false，建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
     */
    private Boolean testWhileIdle = true;

    /**
     * 单位毫秒，默认1分钟。有两个含义：
     * 1) Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接。
     * 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    private Long timeBetweenEvictionRunsMillis = 60000L;

    /**
     * 连接保持空闲而不被驱逐的最小时间，单位是毫秒
     */
    private Long minEvictableIdleTimeMillis = 300000L;

    private Long maxEvictableIdleTimeMillis;

    /**
     * 合并多个DruidDataSource的监控数据
     */
    private Boolean useGlobalDataSourceStat = true;

    /**
     * 连接池异步初始化
     */
    private boolean asyncInit = true;

    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
     */
    private String filters ="stat";

    /**
     * 配置timeBetweenLogStatsMillis>0之后，DruidDataSource会定期把监控数据输出到日志中。单位毫秒
     */
    private Long timeBetweenLogStatsMillis;

    private Integer statSqlMaxSize;

    private Boolean clearFiltersEnable;

    private Boolean resetStatEnable;

    private Integer notFullTimeoutRetryCount;

    private Integer maxWaitThreadCount;

    private Boolean failFast;

    private Boolean phyTimeoutMillis;

    /**
     * 连接池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作。
     */
    private Boolean keepAlive;

    /**
     * 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
     */
    private String driverClassName;

    /**
     * 物理连接初始化的时候执行的sql
     */
    private String connectionInitSqls;

    /**
     * 转为Properties
     *
     * @return
     */
    public Properties toProperties() {
        Properties properties = new Properties();
        notNullAdd(properties, "url", this.url);
        notNullAdd(properties, "username", this.username);
        notNullAdd(properties, "password", this.password);
        notNullAdd(properties, "initialSize", this.initialSize);
        notNullAdd(properties, "minIdle", this.minIdle);
        notNullAdd(properties, "maxActive", this.maxActive);
        notNullAdd(properties, "maxWait", this.maxWait);
        notNullAdd(properties, "useUnfairLock", this.useUnfairLock);
        notNullAdd(properties, "poolPreparedStatements", this.poolPreparedStatements);
        notNullAdd(properties, "maxPoolPreparedStatementPerConnectionSize", this.maxPoolPreparedStatementPerConnectionSize);
        notNullAdd(properties, "validationQuery", this.validationQuery);
        notNullAdd(properties, "testOnBorrow", this.testOnBorrow);
        notNullAdd(properties, "testOnReturn", this.testOnReturn);
        notNullAdd(properties, "testWhileIdle", this.testWhileIdle);
        notNullAdd(properties, "timeBetweenEvictionRunsMillis", this.timeBetweenEvictionRunsMillis);
        notNullAdd(properties, "minEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
        notNullAdd(properties, "maxEvictableIdleTimeMillis", this.maxEvictableIdleTimeMillis);
        notNullAdd(properties, "useGlobalDataSourceStat", this.useGlobalDataSourceStat);
        notNullAdd(properties, "asyncInit", this.asyncInit);
        notNullAdd(properties, "filters", this.filters);
        notNullAdd(properties, "timeBetweenLogStatsMillis", this.timeBetweenLogStatsMillis);
        notNullAdd(properties, "stat.sql.MaxSize", this.statSqlMaxSize);
        notNullAdd(properties, "clearFiltersEnable", this.clearFiltersEnable);
        notNullAdd(properties, "resetStatEnable", this.resetStatEnable);
        notNullAdd(properties, "notFullTimeoutRetryCount", this.notFullTimeoutRetryCount);
        notNullAdd(properties, "maxWaitThreadCount", this.maxWaitThreadCount);
        notNullAdd(properties, "failFast", this.failFast);
        notNullAdd(properties, "phyTimeoutMillis", this.phyTimeoutMillis);
        notNullAdd(properties, "keepAlive", this.keepAlive);
        notNullAdd(properties, "driverClassName", this.driverClassName);
        notNullAdd(properties, "initConnectionSqls", this.connectionInitSqls);
        return properties;
    }

    private void notNullAdd(Properties properties, String key, Object value) {
        if (value != null) {
            properties.setProperty("druid." + key, value.toString());
        }
    }
}