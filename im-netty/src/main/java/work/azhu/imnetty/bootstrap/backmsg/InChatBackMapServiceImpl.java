package work.azhu.imnetty.bootstrap.backmsg;

import org.springframework.stereotype.Service;
import work.azhu.imnetty.common.constant.ChatConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Azhu
 * @Description //TODO
 * @Date 2019/11/17 12:56
 **/
@Service
public class InChatBackMapServiceImpl implements InChatBackMapService{

    @Override
    public Map<String, String> loginSuccess() {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.LOGIN);
        backMap.put(ChatConstant.SUCCESS,ChatConstant.TRUE);
        return backMap;
    }

    @Override
    public Map<String, String> loginError() {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.LOGIN);
        backMap.put(ChatConstant.SUCCESS,ChatConstant.FALSE);
        return backMap;
    }

    @Override
    public Map<String, String> sendMe(String value) {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.SENDME);
        backMap.put(ChatConstant.VALUE,value);
        return backMap;
    }

    @Override
    public Map<String, String> sendBack(String otherOne, String value) {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.SENDTO);
        backMap.put(ChatConstant.VALUE,value);
        backMap.put(ChatConstant.ONE,otherOne);
        return backMap;
    }

    @Override
    public Map<String, String> getMsg(String token, String value) {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.SENDTO);
        backMap.put(ChatConstant.FROM,token);
        backMap.put(ChatConstant.VALUE,value);
        return backMap;
    }

    @Override
    public Map<String, String> sendGroup(String token, String value, String groupId) {
        Map<String,String> backMap = new HashMap<String,String>();
        backMap.put(ChatConstant.TYPE,ChatConstant.SENDGROUP);
        backMap.put(ChatConstant.FROM,token);
        backMap.put(ChatConstant.VALUE,value);
        backMap.put(ChatConstant.GROUPID,groupId);
        return backMap;
    }
}
