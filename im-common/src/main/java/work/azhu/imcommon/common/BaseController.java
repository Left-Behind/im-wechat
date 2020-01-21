package work.azhu.imcommon.common;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import work.azhu.imcommon.util.CookieUtil;
import work.azhu.imcommon.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有项目的基础类 类BaseController.java的实现描述：
 *
 * @author chenxb 2018年3月12日 上午10:08:26
 */
@Component
public class BaseController {

	@Resource
	public HttpServletRequest request;

	@Resource
	public HttpServletResponse response;

	
	public String resSuccessJson() {
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("code", 200);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		return JsonUtil.toJson(map);
	}
	
	public String resHotelSuccessJson() {
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "SUCCESS");
		map.put("code", 0);
		return JsonUtil.toJson(map);
	}

	public String resSuccessJson(Object object) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("result", object);
		map.put("code", 200);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		return JsonUtil.toJson(map);
	}

	public String resFailJson(String message) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("message", message);
		map.put("code", 200);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		return JsonUtil.toJson(map);
	}
	
	public String resFailJson(String message, String detailMessage) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("message", message);
		map.put("detailMessage", detailMessage);
		map.put("code", 200);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		return JsonUtil.toJson(map);
	}

	public String resFailJson(String message, Integer code) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("message", message);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		map.put("code", code);
		return JsonUtil.toJson(map);
	}
	
	public String resFailJson(String message, Integer code, String exMsg, Integer severity) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("message", message);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		map.put("code", code);
		map.put("exMsg", exMsg);
		map.put("severity", severity);
		return JsonUtil.toJson(map);
	}

	public String resFailJsonObj(String message, Object obj) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("message", message);
		map.put("result", obj);
		map.put("code", 200);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		return JsonUtil.toJson(map);
	}
	
	public String resSuccessJson(Object object, Object patch) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("code", 200);
		map.put("result", object);
		map.put("nowTime", System.currentTimeMillis() / 1000);
		map.put("patch", patch);
		return JsonUtil.toJson(map);
	}

	public String getIp(){

		String ip= request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
		if(StringUtils.isBlank(ip)){
			ip = request.getRemoteAddr();// 从request中获取ip
			if(StringUtils.isBlank(ip)){
				ip = "127.0.0.1";
			}
		}
		return ip;
	}

	/**
	 *
	 * @param key
	 * @param map
	 * @return 根据IP,key,map返回jwt
	 */
	public String getJwt(String key, Map<String, Object> map) {
		String ip =getIp();
		System.out.println("ip: "+ip);
		String jwtToken= JwtUtil.encode(key,map,ip);
		CookieUtil.setCookie(request,response,"token",jwtToken,60*60*2,true);
		return jwtToken;
	}


}
