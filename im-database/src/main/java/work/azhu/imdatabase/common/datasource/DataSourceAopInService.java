package work.azhu.imdatabase.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author Azhu
 * @date 2019/9/3 15:18
 */
@Aspect
//@EnableAspectJAutoProxy(exposeProxy=true,proxyTargetClass=true)
@Component
@ConditionalOnClass({DruidDataSource.class})
@Slf4j
public class DataSourceAopInService implements PriorityOrdered {

    @Before("!@annotation(work.azhu.imdatabase.common.annotation.Master) "
            + " && (execution(* work.azhu.imdatabase.service..*.find*(..)) "
            + " || execution(* work.azhu.imdatabase.service..*.get*(..)) "
            + " || execution(* work.azhu.imdatabase.service..*.query*(..)))")
    public void setReadDataSourceType() {
        //如果已经开启写事务了，那之后的所有读都从写库读
        if(!DataSourceType.write.getType().equals(DataSourceContextHolder.getReadOrWrite())){
            DataSourceContextHolder.setRead();
            log.info("当前数据库类型: 读库"+DataSourceContextHolder.getLocal().get());
        }

    }

    @Before("@annotation(work.azhu.imdatabase.common.annotation.Master) "
            + " || execution(* work.azhu.imdatabase.service..*.insert*(..)) "
            + " || execution(* work.azhu.imdatabase.service..*.update*(..))"
            + " || execution(* work.azhu.imdatabase.service..*.add*(..))"
            + " || execution(* work.azhu.imdatabase.service..*.del*(..))"
            + " || execution(* work.azhu.imdatabase.service..*.save*(..))"
            + " || execution(* work.azhu.imdatabase.service..*.create*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.setWrite();
        //这里有个小问题,只要一个请求了使用到了写库,后面的操作都会在写库操作,除非调用另一个请求
        log.info("如果已经开启写事务了，那之后的所有读都从写库读");
        log.info("当前数据库类型: 写库"+DataSourceContextHolder.getLocal().get());
    }

    @Override
    public int getOrder() {
        /**
         * 值越小，越优先执行
         * 要优于事务的执行
         * 在启动类中加上了@EnableTransactionManagement(order = 10)
         */
        return 1;
    }

}