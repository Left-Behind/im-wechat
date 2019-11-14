package work.azhu.imnetty.config;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @Author Azhu
 * @Date 2019/11/7 17:43
 * @Description
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
        /*if (ConfigFactory.initNetty.getDistributed()){
            jedis = new Jedis(ConfigFactory.RedisIP,16379);
            log.info(LogConstant.REDIS_START + jedis.ping());
        }*/
    }

    public static RedisConfig getInstance(){
        return instance;
    }
}
