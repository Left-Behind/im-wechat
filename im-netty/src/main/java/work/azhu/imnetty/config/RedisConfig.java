package work.azhu.imnetty.config;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import work.azhu.imnetty.common.constant.LogConstant;

/**
 * @Author Azhu
 * @Date 2019/11/15 15:46
 * @Description: Redis配置信息
 */
@Slf4j
public class RedisConfig {
    /** 单例模式 */
    private static RedisConfig instance = new RedisConfig();
    
    public static Jedis jedis;

    /**
     * 如果配置启动分布式，则自动初始化jedis
     * */
    private RedisConfig(){
        if (ConfigFactory.isDistributed){
            this.jedis = new Jedis(ConfigFactory.redisIP, ConfigFactory.redisPort);
            log.info(LogConstant.REDIS_START + jedis.ping());
        }
    }

    public static RedisConfig getInstance(){
        return instance;
    }
}
