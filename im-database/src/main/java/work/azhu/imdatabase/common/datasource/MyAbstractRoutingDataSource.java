package work.azhu.imdatabase.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Azhu
 * @date 2019/9/3 15:17
 */
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

    private final int dataSourceNumber;
    private AtomicInteger count = new AtomicInteger(0);

    public MyAbstractRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DataSourceContextHolder.getReadOrWrite();
        if (DataSourceType.read.getType().equals(typeKey) && dataSourceNumber > 0) {
            // 读 简单负载均衡
            int number = count.getAndAdd(1);
            System.out.println("读 简单负载均衡"+typeKey+number);
            System.out.println("读库数量"+dataSourceNumber);
            return number % dataSourceNumber;
        }
        return DataSourceType.write.getType();
    }
}