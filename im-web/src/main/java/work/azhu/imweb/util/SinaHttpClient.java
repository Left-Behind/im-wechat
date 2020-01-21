package work.azhu.imweb.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author Azhu
 * @Description //TODO
 * @Date 2019/12/18 23:29
 **/
public class SinaHttpClient {
    /**
     * 微博和QQ不一样需要获取两个而且请求方式不一样QQ-Get WeiBo-Post
     * 返送http请求获取accessToken和Uid
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject getAccessTokenAndUid(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            System.out.println("Sina返回的HttpEntity: "+result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
            return jsonObject;
        }
        httpPost.releaseConnection();
        return null;
    }

    /**
     * 这个方法QQ和WeiBo可以通用
     * @param url
     * @return
     * @throws IOException
     */
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
