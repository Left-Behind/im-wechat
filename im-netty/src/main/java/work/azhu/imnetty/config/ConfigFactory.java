package work.azhu.imnetty.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author Azhu
 * @Date 2019/11/7 17:48
 * @Description
 */
public class ConfigFactory {

    /** Redis的ip地址 */
    @Value("${spring.redis.host}")
    public static String RedisIP;

   /* *//** 用户校验伪接口 *//*
    public static InChatVerifyService inChatVerifyService;

    *//** 用户获取数据伪接口 *//*
    public static InChatToDataBaseService inChatToDataBaseService;

    *//** 系统信息枚举服务接口 *//*
    public static FromServerService fromServerService;

    *//** InChat项目配置 *//*
    public static InitNetty initNetty;

    public static TextData textData;*/
}
