package work.azhu.imcommon.model.bean.netty;
;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @Author Azhu
 * @Date 2019/11/18 9:56
 * @Description
 */
@Data
@AllArgsConstructor
public class SendInChat {

    private String token;

    private Map<String,String> frame;

}
