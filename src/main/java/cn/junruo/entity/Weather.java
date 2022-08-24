package cn.junruo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    //日期
    String date;
    //星期
    String week;

    // 当前天气
    String text_now;
    // 当前温度
    String temp;
    // 风向
    String wind_dir;
    // 风级大小
    String wind_class;

    //最低温度
    String low;
    //最高温度
    String high;

    //白天天气
    String text_day;
    //白天风向
    String wd_day;
    //白天风力
    String wc_day;

    //晚上天气
    String text_night;
    //晚上风向
    String wd_night;
    //晚上风力
    String wc_night;

}
