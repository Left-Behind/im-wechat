package work.azhu.imcommon.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.List;

/**
 * json工具类
 * 
 * @author chenxb
 * @param <T>
 */
public class JsonUtil<T>{
	  
		//转化成json字符串
	    public static String toJson(Object object) {
	        return JSON.toJSONString(object,
	        		SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	    }
	      
	    // json转化为对象 object map
	    public static <T> T jsonToBean(String text, Class<T> clazz) {  
	        return JSON.parseObject(text, clazz);
	    }  
	  
	  
	    // json转换为List  
	    public static <T> List<T> jsonToList(String text, Class<T> clazz) {  
	        return JSON.parseArray(text, clazz);
	    } 
	    
	    /**
	       * @Description: 将数据转换到相应的容器
	       * @param bean
	       * @param clazz
	       * @return
	       * @throws
	       * @author SunF
	       * @date 2018/6/20 10:28 
	       */
	    public static <T> T convertValue(Object bean, Class<T> clazz){
    	  ObjectMapper mapper = new ObjectMapper();
	      return mapper.convertValue(bean, clazz);
	    }
	    
	    public static void main(String[] args) {
	    	Calendar temp = Calendar.getInstance();
	    	temp.set(Calendar.MILLISECOND, 99999);  
			System.out.println(JSON.toJSONString(temp.getTime(), SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat));
		}
}
