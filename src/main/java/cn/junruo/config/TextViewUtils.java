package cn.junruo.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class TextViewUtils {

    @Value("${config.tianKey}")
    private String tianKey;

    private static String stTianKey;

    @PostConstruct
    public void setExpDate() {
        stTianKey = this.tianKey;
    }

    //英语一句话接口
    public static Map<String,String> getEnsentence() {
        String httpUrl = "http://api.tianapi.com/ensentence/index?key="+stTianKey;//key后面填自己的天行数据key
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray newslist = jsonObject.getJSONArray("newslist");
        String en = newslist.getJSONObject(0).getString("en");
        String zh = newslist.getJSONObject(0).getString("zh");
        Map<String, String> map = new HashMap<>();
        map.put("zh",zh);
        map.put("en",en);
        return map;
    }
}
