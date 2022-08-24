package cn.junruo.config;

import cn.junruo.entity.Weather;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WeatherUtils {

    @Value("${config.baiduMap.districtId}")
    private String districtId;
    @Value("${config.baiduMap.ak}")
    private String ak;

    private static String stDistrictId;
    private static String stAk;

    @PostConstruct
    public void setExpDate() {
        stDistrictId = this.districtId;
        stAk = this.ak;
    }

    public static void main(String[] args) {
        System.out.println(getWeather());
    }
    public static Weather getWeather(){

        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> map = new HashMap<String,String>();
        map.put("district_id",stDistrictId);
        map.put("data_type","all");//这个是数据类型
        map.put("ak",stAk);//百度地图应用ak
        String res = restTemplate.getForObject(
                "https://api.map.baidu.com/weather/v1/?district_id={district_id}&data_type={data_type}&ak={ak}",
                String.class,
                map);
        JSONObject json = JSONObject.parseObject(res);
        JSONArray forecasts = json.getJSONObject("result").getJSONArray("forecasts");
        List<Weather> weathers = forecasts.toJavaList(Weather.class);
        JSONObject now = json.getJSONObject("result").getJSONObject("now");
        Weather weather = weathers.get(0);
        weather.setText_now(now.getString("text"));
        weather.setTemp(now.getString("temp"));
        weather.setWind_class(now.getString("wind_class"));
        weather.setWind_dir(now.getString("wind_dir"));
        return weather;
    }
}
