package work.azhu.imnetty.common.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import work.azhu.imnetty.common.constant.ChatConstant;
import work.azhu.imnetty.common.constant.HttpConstant;

import java.io.UnsupportedEncodingException;

/**
 * @Author Azhu
 * @Date 2019/11/18 14:44
 * @Description
 */
public class HttpUtil {

    public static String checkType(FullHttpRequest msg){
        msg.retain();
        String url = msg.uri();
        System.out.println(url);
        HttpMethod method = msg.method();
        String meName = method.name();
        if (url.equals(HttpConstant.URI_GETSIZE) && meName.equals(HttpConstant.GET)){
            return HttpConstant.GETSIZE;
        }else if (url.equals(HttpConstant.URI_SENDFROMSERVER) && meName.equals(HttpConstant.POST)){
            return HttpConstant.SENDFROMSERVER;
        }else if (url.equals(HttpConstant.URI_GETLIST) && meName.equals(HttpConstant.GET)){
            return HttpConstant.GETLIST;
        }else if (url.equals(HttpConstant.URI_SENDINCHAT) && meName.equals(HttpConstant.POST)){
            return HttpConstant.SENDINCHAT;
        }else {
            return HttpConstant.NOTFINDURI;
        }
    }

    public static SendServerVO getToken(FullHttpRequest msg) throws UnsupportedEncodingException {
        msg.retain();
        SendServerVO sendServerVO = new SendServerVO();
        String content = msg.content().toString(CharsetUtil.UTF_8);
        String[] stars = content.split("&");
        for (int i=0,len=stars.length;i<len;i++){
            String item = stars[i].toString();
            String firstType = item.substring(0,5);
            String value = item.substring(6,item.length());
            if (ChatConstant.TOKEN.equals(firstType)){
                //Token
                sendServerVO.setToken(value);
            }else if(ChatConstant.VALUE.endsWith(firstType)){
                //value
                sendServerVO.setValue(value);
            }
        }
        return sendServerVO;
    }
}
