package cn.onekit.x2x.cloud.toutiao_baidu;

import cn.onekit.thekit.FileDB;
import com.baidu.openapi.sdk.BaiduOpenSDK;
import com.baidu.spapi.sdk.BaiduSpSDK;
import com.toutiao.developer.ToutiaoAPI2;
import com.toutiao.developer.entity.v2.*;

public abstract class ToutiaoServer2 implements ToutiaoAPI2 {

    private final String baidu_appkey;
    private final String baidu_secret;
    private BaiduSpSDK baiduSpSDK = new BaiduSpSDK("https://spapi.baidu.com");
    private BaiduOpenSDK baiduOpenSDK = new BaiduOpenSDK("https://openapi.baidu.com");
    protected ToutiaoServer2(
            String baidu_appkey, String baidu_secret) {
        this.baidu_appkey = baidu_appkey;
        this.baidu_secret = baidu_secret;
    }

    //////////////////////////////////////


    public tags__text__antidirt_response tags__text__antidirt(String tt_X_Token, tags__text__antidirt_body tt_body) throws ToutiaoError2 {
        return null;
    }

    public tags__image_response tags__image(String tt_X_Token, tags__image_body tt_body) throws ToutiaoError2 {
        return null;
    }
}
