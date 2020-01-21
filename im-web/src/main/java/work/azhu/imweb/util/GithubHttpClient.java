package work.azhu.imweb.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/12/19 12:52
 * @Description
 */
public class GithubHttpClient {
    /**
     * 返送http请求获取accessToken
     * @param url
     * @return
     * @throws IOException
     */
    public static String getAccessToken(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);

        // 如果响应200成功,解析响应结果
        if(response.getStatusLine().getStatusCode()==200){
            // 获取响应的内容
            HttpEntity entity = response.getEntity();
            //response.getEntity()所得到的流是不可重复读取的，所得的实体只能读取一次,读取一次后,
            // 流就关闭了。EntityUtils.toString(responseEntity)被调用一次后就会自动销毁，而我调用了2次，所以就报错了。
            String reslut=EntityUtils.toString(entity);
            System.out.println("Github返回的HttpEntity: "+reslut);
            return reslut;
        }
        return null;
    }

    /**
     * 将字符串转换成map
     * @param entity
     * @return
     */
    public static Map<String,String> getMap(String entity) {

        Map<String, String> map = new HashMap<>();
        // 以&来解析字符串
        String[] result = entity.split("\\&");

        for (String str : result) {
            // 以=来解析字符串
            String[] split = str.split("=");
            // 将字符串存入map中
            if (split.length == 1) {
                map.put(split[0], null);
            } else {
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

    public static JSONObject getUserInfo(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }

}
