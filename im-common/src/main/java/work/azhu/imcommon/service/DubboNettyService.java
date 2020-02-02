package work.azhu.imcommon.service;

import java.util.Set;

public interface DubboNettyService {

    /**
     * 获取当前连接实列数量
     * @return
     */
    Integer getChannelSize();

    /**
     * 判断是该userId是否在线
     * @param userId
     * @return
     */
    Boolean queryUserIdIfOnline(String userId);

    /**
     *
     * @return
     */
    Set<String> queryUserIdSet();
}
