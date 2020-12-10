package cn.onekit.x2x.cloud.toutiao_baidu.web;

import cn.onekit.thekit.FileDB;

import cn.onekit.thekit.JSON;
import cn.onekit.x2x.cloud.toutiao_baidu.ToutiaoServer;
import com.toutiao.developer.entity.ToutiaoError;
import com.toutiao.developer.entity.apps__qrcode_body;
import com.toutiao.developer.entity.apps__subscribe_notification__developer__notify_body;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class ToutiaoServerWeb {

private ToutiaoServer _toutiaoServer;
    private ToutiaoServer toutiaoServer() {
       if(_toutiaoServer==null){
           _toutiaoServer = new ToutiaoServer(BaiduAccount.baidu_appkey, BaiduAccount.baidu_secret) {

               @Override
               protected void _code_openid(String bd_code, String bd_openid) {
                   FileDB.set("[toutiao-baidu] code_openid",bd_code,bd_openid);
               }

               @Override
               protected FileDB.Data _code_openid(String bd_code) {
                   return FileDB.get("[toutiao-baidu] code",bd_code);
               }

               @Override
               protected void _openid_sessionkey(String bd_openid, String bd_sessionkey) {
                   FileDB.set("[toutiao-baidu] openid_sessionkey",bd_openid,bd_sessionkey);
               }

               @Override
               protected FileDB.Data _openid_sessionkey(String bd_openid) {
                   return FileDB.get("[toutiao-baidu] openid",bd_openid);
               }
           };
       }
       return _toutiaoServer;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/apps/token")
    public String getAccessToken(
            @RequestParam(required = false) String appid,
            @RequestParam(required = false) String secret,
            @RequestParam(required = false) String grant_type
    ) {
        try {
            return JSON.object2string(toutiaoServer().apps__token(appid, secret, grant_type));
        } catch (ToutiaoError toutiaoError) {
            return JSON.object2string(toutiaoError);
        }catch (Exception error){
            ToutiaoError toutiaoError = new ToutiaoError();
            toutiaoError.setError(500);
            toutiaoError.setErrcode(500);
            toutiaoError.setErrmsg(error.getMessage());
            return JSON.object2string(toutiaoError);
        }


    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/apps/jscode2session")
    public String code2Session(
            @RequestParam(required=false) String appid,
            @RequestParam(required=false) String secret,
            @RequestParam(required=false) String code,
            @RequestParam(required=false)  String anonymous_code
    )  {
        try {
            return JSON.object2string(toutiaoServer().apps__jscode2session(appid, secret, code, anonymous_code));
        }catch (ToutiaoError toutiaoError) {
            return JSON.object2string(toutiaoError);
        }catch (Exception error){
            ToutiaoError toutiaoError = new ToutiaoError();
            toutiaoError.setError(500);
            toutiaoError.setErrcode(500);
            toutiaoError.setErrmsg(error.getMessage());
            return JSON.object2string(toutiaoError);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/apps/qrcode",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] createQRCode(
            @RequestBody String body
    )  {
        try {
            return toutiaoServer().apps__qrcode(JSON.string2object(body, apps__qrcode_body.class));
        } catch (ToutiaoError toutiaoError) {
            return JSON.object2string(toutiaoError).getBytes();
        }catch (Exception error){
            ToutiaoError toutiaoError = new ToutiaoError();
            toutiaoError.setError(500);
            toutiaoError.setErrcode(500);
            toutiaoError.setErrmsg(error.getMessage());
            return JSON.object2string(toutiaoError).getBytes();
        }
    }
    @RequestMapping(method = RequestMethod.POST, value = "api/apps/subscribe_notification/developer/v1/notify")
    public String subscribe(
            @RequestBody String body
    ){
        try {
            return JSON.object2string(toutiaoServer().apps__subscribe_notification__developer__notify(JSON.string2object(body, apps__subscribe_notification__developer__notify_body.class)));
        } catch (ToutiaoError toutiaoError) {
            return JSON.object2string(toutiaoError);
        }catch (Exception error) {
            ToutiaoError toutiaoError = new ToutiaoError();
            toutiaoError.setError(500);
            toutiaoError.setErrcode(500);
            toutiaoError.setErrmsg(error.getMessage());
            return JSON.object2string(toutiaoError);
        }

    }



}