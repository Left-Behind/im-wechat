package work.azhu.imweb;

import org.junit.Test;
import work.azhu.imweb.util.JwtUtil;

import java.util.Map;

/**
 * @Author Azhu
 * @Date 2020/1/3 16:37
 * @Description
 */
public class jwtTest {

    @Test
    public void encode(){
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzEyIiwidXNlck5hbWUiOiIxMjEyIiwidGltZXN0YW1wIjoxNTc4MDQwNTkwOTY3fQ.GbdsLEqBM9Mymp8HFC55Pcxu2g9tiAT4_2EO3LRmPd8";
        String key="Azhu";
        String salt="0:0:0:0:0:0:0:1";
        Map<String,Object> map= JwtUtil.decode(jwt,key,salt);
        System.out.println(map);
    }
}
