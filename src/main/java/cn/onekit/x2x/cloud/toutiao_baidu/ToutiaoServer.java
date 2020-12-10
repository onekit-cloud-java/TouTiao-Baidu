package cn.onekit.x2x.cloud.toutiao_baidu;

import cn.onekit.thekit.FileDB;
import com.baidu.openapi.entity.*;
import com.baidu.openapi.sdk.BaiduOpenSDK;
import com.baidu.spapi.entity.oauth_jscode2sessionkey_body;
import com.baidu.spapi.entity.oauth_jscode2sessionkey_response;
import com.baidu.spapi.sdk.BaiduSpSDK;
import com.toutiao.developer.ToutiaoAPI;
import com.toutiao.developer.entity.*;



public abstract class ToutiaoServer implements ToutiaoAPI {

    private final String baidu_appkey;
    private final String baidu_secret;
    private BaiduSpSDK baiduSpSDK = new BaiduSpSDK("https://spapi.baidu.com");
    private BaiduOpenSDK baiduOpenSDK = new BaiduOpenSDK("https://openapi.baidu.com");
    protected ToutiaoServer(
            String baidu_appkey, String baidu_secret) {
        this.baidu_appkey = baidu_appkey;
        this.baidu_secret = baidu_secret;
    }
    final String grant_type = "client_credentials";
    final String scope = "smartapp_snsapi_base";

    //////////////////////////////////////
    abstract protected void _code_openid(String bd_code, String bd_openid);
    abstract protected FileDB.Data _code_openid(String bd_code);
    abstract protected void _openid_sessionkey(String bd_openid, String bd_sessionkey);
    abstract protected FileDB.Data _openid_sessionkey(String bd_openid);



    public apps__token_response apps__token(String tt_appid, String tt_secret, String tt_grant_type) throws ToutiaoError {
        apps__token_response tt_respose = new apps__token_response();
        token_response bd_response = baiduOpenSDK.token(grant_type, baidu_appkey, baidu_secret, scope);
        tt_respose.setAccess_token(bd_response.getAccess_token());
        tt_respose.setExpires_in(Long.parseLong(bd_response.getExpires_in()));
        return tt_respose;
    }

    public apps__jscode2session_response apps__jscode2session(String tt_appid, String tt_secret, String tt_code, String tt_anonymous_code) throws ToutiaoError {

        boolean isCode =  tt_code!=null;
        final String code = isCode?tt_code:tt_anonymous_code;
        FileDB.Data bd_openid_data = _code_openid(code);
        FileDB.Data bd_session_key_data = null;
        if(bd_openid_data!=null){
            String wx_openid = bd_openid_data.value;
            bd_session_key_data = _openid_sessionkey(wx_openid);
            apps__jscode2session_response tt_response = new apps__jscode2session_response();
            tt_response.setAnonymous_openid(wx_openid);
            tt_response.setOpenid(wx_openid);
            tt_response.setSession_key(bd_session_key_data.value);
            return tt_response;
        }

        oauth_jscode2sessionkey_body body = new oauth_jscode2sessionkey_body();
        body.setCode(tt_code);
        body.setClient_id(tt_appid);
        body.setSk(tt_secret);
        oauth_jscode2sessionkey_response bd_response = baiduSpSDK.oauth_jscode2sessionkey(body);
        assert false;
        bd_openid_data.value =bd_response.getOpenid();

        bd_session_key_data.value =bd_response.getSession_key();
        ///////////////
        _code_openid(code,bd_openid_data.value);
        _openid_sessionkey(bd_session_key_data.value,bd_session_key_data.value);
        ////////////
        apps__jscode2session_response tt_response = new apps__jscode2session_response();
        tt_response.setAnonymous_openid(bd_openid_data.value);
        tt_response.setOpenid(bd_openid_data.value);
        tt_response.setSession_key(bd_session_key_data.value);


        return tt_response;
    }

    public byte[] apps__qrcode(apps__qrcode_body tt_body) throws ToutiaoError {

        smartapp_qrcode_get_body bd_body = new smartapp_qrcode_get_body();
        bd_body.setWidth(tt_body.getWidth());
        bd_body.setPath(tt_body.getPath());
        bd_body.setMf(1);
        try {
            return baiduOpenSDK.smartapp_qrcode_get(tt_body.getAccess_token(),bd_body);
        } catch (BaiduError baiduError) {
            ToutiaoError tt_error = new ToutiaoError();
            tt_error.setError(74077);
            tt_error.setErrcode(baiduError.getErrno());
            tt_error.setErrmsg(baiduError.getErrmsg());
            throw tt_error;
        }
    }

    public apps__subscribe_notification__developer__notify_response apps__subscribe_notification__developer__notify(apps__subscribe_notification__developer__notify_body tt_body) throws ToutiaoError {
        apps__subscribe_notification__developer__notify_response tt_response = new apps__subscribe_notification__developer__notify_response();
        smartapp_message_custom_send_body bd_body = new smartapp_message_custom_send_body();
        bd_body.setOpen_id(tt_body.getOpen_id());
        bd_body.setUser_type(1);
        bd_body.setMsg_type("text");
        bd_body.setContent(tt_body.getData().toString());
        smartapp_message_custom_send_response bd_response = baiduOpenSDK.smartapp_message_custom_send(tt_body.getAccess_token(), bd_body);
        tt_response.setErr_no(bd_response.getErrno());
        tt_response.setErr_tips(bd_response.getMsg());
        return tt_response;
    }
}
