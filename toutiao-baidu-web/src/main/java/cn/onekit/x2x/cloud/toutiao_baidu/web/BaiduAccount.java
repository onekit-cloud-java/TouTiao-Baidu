package cn.onekit.x2x.cloud.toutiao_baidu.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config.properties"},ignoreResourceNotFound = true)
public class BaiduAccount implements ApplicationContextAware {

     @SuppressWarnings("WeakerAccess")
     static String baidu_appkey;
     @SuppressWarnings("WeakerAccess")
     static String baidu_secret;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        baidu_appkey = applicationContext.getEnvironment().getProperty("baidu.appkey");
        baidu_secret = applicationContext.getEnvironment().getProperty("baidu.secret");

    }
}
