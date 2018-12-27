package com.leo.alidnns;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author chao.li@quvideo.com
 * @date 2018/12/27 16:59
 */
public class IPUtil {

    /**
     * 获取公网ip
     * @return
     */
    public static String getPublicIp() throws IOException {
        String url = "http://icanhazip.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request ipRequest = new Request.Builder()
                .url(url)
                .build();
        Response ipResponse= okHttpClient.newCall(ipRequest).execute();
        if(ipResponse.isSuccessful()){
            if (ipResponse.body() != null) {
                ipResponse.close();
                return ipResponse.body().string();
            }
        }
        ipResponse.close();
        return null;
    }
}
