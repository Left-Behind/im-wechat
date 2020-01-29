package work.azhu.imcommon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    public static String encode(String key, Map<String,Object> param, String salt){
        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);
        jwtBuilder = jwtBuilder.setClaims(param);
        String token = jwtBuilder.compact();
        return token;

    }


    public  static Map<String,Object>  decode(String token ,String key,String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
           return null;
        }
        log.info("JWT解码数据");
        log.info(JSON.toJSONString(claims, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));
        return  claims;
    }

    public static void main(String[] args) {
        String key="Azhu";
        Map<String,Object> map=new HashMap();
        map.put("userName","Azhu");
        map.put("password","123");
        map.put("time",System.currentTimeMillis());
        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));
        String ip="183.245.169.207";
        String jwt="";
        System.out.println(encode(key,map,ip));
        long start=System.currentTimeMillis();
        for(int i=0;i<100;i++){
            long start1=System.currentTimeMillis();
            jwt=encode(key,map,ip);
            long end1=System.currentTimeMillis();
            System.out.println("加密时间："+(end1-start1));
        }
        long end=System.currentTimeMillis();
        for(int i=1;i<100;i++){
            start=System.currentTimeMillis();
            Map<String,Object> claims=decode(jwt,key,ip);
            end=System.currentTimeMillis();
            System.out.println("解码时间："+(end-start));
        }


    }
}
