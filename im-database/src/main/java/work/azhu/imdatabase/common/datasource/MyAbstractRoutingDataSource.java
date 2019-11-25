package work.azhu.imdatabase.common.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Azhu
 * @date 2019/9/3 15:17
 */
@Slf4j
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

    /**读库的数量*/
    private final int dataSourceNumber;

    /**轮询计数器 初始为0  AtomicInteger是线程安全的*/
    private AtomicInteger count = new AtomicInteger(0);

    public MyAbstractRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        //获取当前ThreadLocal 数据源
        String typeKey = DataSourceContextHolder.getReadOrWrite();
        //判断是否为读库 且读库数据源数量大于0
        if (DataSourceType.read.getType().equals(typeKey) && dataSourceNumber > 0) {
            // 读 简单负载均衡
            int number = count.getAndAdd(1);
            log.info("读库---负载均衡(轮询)"+"             读库的数量: "+dataSourceNumber);
            log.info("当前读库: read"+number%dataSourceNumber);
            //防止超出Interger范围
            if (number>1000000){
                count.set(0);
            }
            return number % dataSourceNumber;
        }
        //否则直接返回写库
        return DataSourceType.write.getType();
    }
}