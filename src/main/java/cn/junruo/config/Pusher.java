package cn.junruo.config;


import cn.junruo.entity.Weather;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class Pusher {

    @Value("${config.wechart.appID}")
    private String appId;
    @Value("${config.wechart.appsecret}")
    private String secret;
    @Value("${config.templateID}")
    private String templateID;
    @Value("${config.baiduMap.name}")
    private String name;
    @Value("${config.openID}")
    private String[] arrOpenID;

    @Value("${config.memorialDay.myName}")
    private String myName;
    @Value("${config.memorialDay.youName}")
    private String youName;

    private static String stAppId;
    private static String stSecret;
    private static String stTemplateID;
    private static String stName;
    private static String[] stArrOpenID;

    private static String stMyName;
    private static String stYouName;

    @PostConstruct
    public void setExpDate() {
        stAppId = this.appId;
        stSecret = this.secret;
        stArrOpenID = this.arrOpenID;
        stTemplateID = this.templateID;
        stName = this.name;
        stMyName = this.myName;
        stYouName = this.youName;
    }

    public static void main(String[] args) {
        init();
    }

    public static void init(){
        for (String s : stArrOpenID) {
            push(s);
        }
    }

    public static void push(String openId){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(stAppId);
        wxStorage.setSecret(stSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)//这里填需要推送的人的openid
                .templateId(stTemplateID)//这里填模板id
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        Weather weather = WeatherUtils.getWeather();
        Map<String, String> map = TextViewUtils.getEnsentence();
        templateMessage.addData(new WxMpTemplateData("date",weather.getDate() + "  "+ weather.getWeek(),"#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("city",stName,"#0000ff"));
        templateMessage.addData(new WxMpTemplateData("text_now",weather.getText_now(),"#00FFFF"));
        templateMessage.addData(new WxMpTemplateData("temp",weather.getTemp() + "","#EE212D"));
        templateMessage.addData(new WxMpTemplateData("low",weather.getLow() + "","#173177"));
        templateMessage.addData(new WxMpTemplateData("high",weather.getHigh()+ "","#FF6347" ));
        templateMessage.addData(new WxMpTemplateData("wind_dir",weather.getWind_dir()+ "","#B95EA3" ));
        templateMessage.addData(new WxMpTemplateData("wind_class",weather.getWind_class()+ "","#42B857" ));

        templateMessage.addData(new WxMpTemplateData("love_day",MemorialDayUtils.getLianAi()+"","#FF1493"));
        templateMessage.addData(new WxMpTemplateData("my_name",stMyName+"","#FFA500"));
        templateMessage.addData(new WxMpTemplateData("my_birthday",MemorialDayUtils.getBirthdayMy()+"","#FFA500"));
        templateMessage.addData(new WxMpTemplateData("you_name",stYouName+"","#FFA500"));
        templateMessage.addData(new WxMpTemplateData("you_birthday",MemorialDayUtils.getBirthdayYou()+"","#FFA500"));

        templateMessage.addData(new WxMpTemplateData("zhText",map.get("zh") +"","#C71585"));
        templateMessage.addData(new WxMpTemplateData("enText",map.get("en") +"","#a61a72"));

        String remark = "❤";
        if(MemorialDayUtils.getBirthdayMy()  == 0){
            remark = "今天是"+stMyName+"生日，生日快乐呀！";
        }
        if(MemorialDayUtils.getBirthdayYou()  == 0){
            remark = "今天是"+stYouName+"生日，生日快乐呀！";
        }
        templateMessage.addData(new WxMpTemplateData("remark",remark,"#FF0000"));

        try {
            System.out.println(templateMessage.toJson());
            System.out.println(wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
