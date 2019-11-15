package work.azhu.imnetty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author Azhu
 * @Date 2019/11/15 15:51
 * @Description
 */
public  class ConfigFactory {

    public static String redisIP;

    public static Boolean isDistributed;

    public static Integer redisPort;

    public static String address;



}
