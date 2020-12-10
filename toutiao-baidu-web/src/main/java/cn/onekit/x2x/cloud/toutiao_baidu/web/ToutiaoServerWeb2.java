package cn.onekit.x2x.cloud.toutiao_baidu.web;

import cn.onekit.thekit.FileDB;
import cn.onekit.thekit.JSON;

import cn.onekit.x2x.cloud.toutiao_baidu.ToutiaoServer2;
import com.toutiao.developer.entity.v2.ToutiaoError2;
import com.toutiao.developer.entity.v2.tags__image_body;
import com.toutiao.developer.entity.v2.tags__text__antidirt_body;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class ToutiaoServerWeb2 {
    private ToutiaoServer2 _toutiaoServer2;
    private ToutiaoServer2 toutiaoServer2(){
        if(_toutiaoServer2 == null){
            _toutiaoServer2 = new ToutiaoServer2(BaiduAccount.baidu_appkey,BaiduAccount.baidu_secret) {

            };
        }
        return _toutiaoServer2;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/api/v2/tags/text/antidirt")
    public String checkContent(
            HttpServletRequest request,
            @RequestBody String body
    )  {
        String x_Token = request.getHeader("X-Token");
        try {
            return JSON.object2string(toutiaoServer2().tags__text__antidirt(x_Token, JSON.string2object(body, tags__text__antidirt_body.class)));
        } catch (ToutiaoError2 error) {
            return JSON.object2string(error);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "api/v2/tags/image/")
    public String checkImage(
            HttpServletRequest request,
            @RequestBody String body
    )  {
        String x_Token = request.getHeader("X-Token");
        try {
            return JSON.object2string(toutiaoServer2().tags__image(x_Token, JSON.string2object(body, tags__image_body.class)));
        } catch (ToutiaoError2 error) {
            return JSON.object2string(error);
        }
    }
}
