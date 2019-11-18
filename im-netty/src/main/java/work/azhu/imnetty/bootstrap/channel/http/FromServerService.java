package work.azhu.imnetty.bootstrap.channel.http;

/**
 * @Author Azhu
 * @Date 2019/11/18 9:53
 * @Description
 */
public interface FromServerService {

    Integer getCode();

    String findByCode(Integer code);
}
