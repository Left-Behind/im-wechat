package work.azhu.imweb.util;

import io.jsonwebtoken.*;

import java.util.Map;

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
        return  claims;
    }

    public static void main(String[] args) {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMyIsInVzZXJOYW1lIjoiQXpodSIsInRpbWVzdGFtcCI6MTU4MDM4MTU5MTc0NH0.bAhztsFXMvIguxkZai51QYd9-1JlMTc7vah4oB2UwIc";
        String key="Azhu";
        String ip="183.245.168.212";
        Map map=decode(token,key,ip);
        System.out.println(map.toString());
    }
}
