package com.example.administrator.essim.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class Common {
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有日期数据哦";
    }

    public static boolean hasData() {
        // 每天11:15分 pixiv 才会开放排行榜数据
        Calendar now = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.set(Calendar.HOUR_OF_DAY, 11);
        future.set(Calendar.MINUTE, 5);
        return future.before(now);
    }
}
