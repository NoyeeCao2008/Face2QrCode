package com.github.noyeecao2008.net;


import com.github.noyeecao2008.bean.OAuthBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.tencent.mmkv.MMKV;


public class BaiduOAuthServer {
    private static final String KEY_ACCESSTOKEN = "access_token";
    private static final String KEY_EXPIRES_AT = "expires_at";
    public static final String HOST = "https://aip.baidubce.com/";

    public static String getAccessToken() {
        MMKV kv = MMKV.defaultMMKV();
        Long expireAt = kv.getLong(KEY_EXPIRES_AT, 0);
        if (expireAt < System.currentTimeMillis()) {
            return "";
        }

        return kv.getString(KEY_ACCESSTOKEN, "");
    }

    public static void saveAccessToken(OAuthBean bean) {
        MMKV kv = MMKV.defaultMMKV();
        kv.putString(KEY_ACCESSTOKEN, bean.accessToken);
        kv.putLong(KEY_EXPIRES_AT, bean.expiresIn * 1000 + System.currentTimeMillis());
    }

    /**
     * grant_type： 必须参数，固定为client_credentials；
     * client_id： 必须参数，应用的API Key；
     * client_secret： 必须参数，应用的Secret Key；
     *
     * @param ak
     * @param sk
     * @return
     */
    public static RequestForPost createUpdateTokenRequest(String ak, String sk) {
        RequestForPost request = new RequestForPost()
                .param("grant_type", "client_credentials")
                .param("client_id", ak)
                .param("client_secret", sk);
        return request;
    }

    public interface Api {
        /**
         * url:https://aip.baidubce.com/oauth/2.0/token
         *
         * @param ak
         * @param sk
         * @return
         */
        @Headers("Accept-Encoding: application/json")
        @GET("oauth/2.0/token?grant_type=client_credentials")
        Call<OAuthBean> getToken(@Query("client_id") String ak,
                                 @Query("client_secret") String sk);
    }


}
