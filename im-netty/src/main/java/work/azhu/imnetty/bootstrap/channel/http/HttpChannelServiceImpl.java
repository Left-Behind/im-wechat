package work.azhu.imnetty.bootstrap.channel.http;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.azhu.imcommon.model.bean.netty.SendInChat;
import work.azhu.imcommon.model.bean.netty.vo.SendServerVO;
import work.azhu.imnetty.bootstrap.channel.cache.WsCacheMap;
import work.azhu.imnetty.common.constant.HttpConstant;
import work.azhu.imnetty.common.constant.LogConstant;
import work.azhu.imnetty.common.utils.RedisUtil;

import java.util.Date;
import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/18 10:12
 * @Description
 */
@Service
@Slf4j
public class HttpChannelServiceImpl implements HttpChannelService {
    @Override
    public void getSize(Channel channel) {
        /*FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpConstant.CONTENT_TYPE,HttpConstant.APPLICATION_JSON);
        GetSizeVO getSizeVO = new GetSizeVO(WsCacheMap.getSize(),new Date());
        ResultVO<GetSizeVO> resultVO = new ResultVO<>(HttpResponseStatus.OK.code(),getSizeVO);
        Gson gson = new Gson();
        ByteBuf buf = Unpooled.copiedBuffer(gson.toJson(resultVO), CharsetUtil.UTF_8);
        response.content().writeBytes(buf);
        channel.writeAndFlush(response);
        close(channel);*/
    }

    @Override
    public void sendFromServer(Channel channel, SendServerVO serverVO) {
        /*if (serverVO.getToken() == ""){
            notFindUri(channel);
        }
        Channel userChannel = WsCacheMap.getByToken(serverVO.getToken());
        if (userChannel == null){
            log.info(LogConstant.HTTPCHANNELSERVICEIMPL_NOTFINDLOGIN);
            notFindToken(channel);
        }
        String value = fromServerService.findByCode(Integer.parseInt(serverVO.getValue()));
        SendServer sendServer = new SendServer(value);
        try {
            userChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(sendServer)));
            sendServer(channel, NotInChatConstant.SEND_SUCCESS);
        }catch (Exception e){
            log.info(LogConstant.HTTPCHANNELSERVICEIMPL_SEND_EXCEPTION);
        }*/
    }

    private void sendServer(Channel channel,String msg){
        /*FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        response.headers().set(HttpConstant.CONTENT_TYPE,HttpConstant.APPLICATION_JSON);
        NotFindUriVO notFindUriVO = new NotFindUriVO(msg);
        ResultVO<NotFindUriVO> resultVO = new ResultVO<>(HttpResponseStatus.BAD_REQUEST.code(),notFindUriVO);
        Gson gson = new Gson();
        ByteBuf buf = Unpooled.copiedBuffer(gson.toJson(resultVO), CharsetUtil.UTF_8);
        response.content().writeBytes(buf);
        channel.writeAndFlush(response);
        close(channel);*/
    }

    private void notFindToken(Channel channel) {
        //sendServer(channel,NotInChatConstant.NOT_FIND_LOGIN);
    }

    @Override
    public void notFindUri(Channel channel) {
        //sendServer(channel,NotInChatConstant.NOT_FIND_URI);
    }

    @Override
    public void close(Channel channel) {
        log.info(LogConstant.HTTPCHANNELSERVICEIMPL_CLOSE);
        channel.close();
    }

    @Override
    public void getList(Channel channel) {
        /*FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpConstant.CONTENT_TYPE,HttpConstant.APPLICATION_JSON);
        GetListVO getListVO = new GetListVO(WsCacheMap.getTokenList());
        ResultVO<GetListVO> resultVO = new ResultVO<>(HttpResponseStatus.OK.code(),getListVO);
        Gson gson = new Gson();
        ByteBuf buf = Unpooled.copiedBuffer(gson.toJson(resultVO), CharsetUtil.UTF_8);
        response.content().writeBytes(buf);
        channel.writeAndFlush(response);
        close(channel);*/
    }

    @Override
    public void sendInChat(String token, Map msg) {
        String address = RedisUtil.getAddress(RedisUtil.convertMD5(WsCacheMap.getByJedis(token)));
        String[] str = address.split(":");
        try {
            HttpClient.getInstance().send(str[0],Integer.parseInt(str[1]),token,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendByInChat(Channel channel, SendInChat sendInChat) {
        Gson gson = new Gson();
        Channel other = WsCacheMap.getByToken(sendInChat.getToken());
        try {
            other.writeAndFlush(new TextWebSocketFrame(gson.toJson(sendInChat.getFrame())));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
//        response.headers().set(HttpConstant.CONTENT_TYPE,HttpConstant.APPLICATION_JSON);
//        channel.writeAndFlush(response);
        close(channel);
    }
}
