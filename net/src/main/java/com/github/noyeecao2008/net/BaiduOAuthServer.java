package com.github.noyeecao2008.net;


import com.github.noyeecao2008.bean.OAuthBean;

import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class BaiduOAuthServer {

    public static class Request extends ConcurrentHashMap<String, String> {

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
    public static Request createUpdateTokenRequest(String ak, String sk) {
        Request request = new Request();
        request.put("grant_type", "client_credentials");
        request.put("client_id", ak);
        request.put("client_secret", sk);
        return request;
    }

    public interface Api {
        /**
         * url:https://aip.baidubce.com/oauth/2.0/token
         * @param request
         * @return
         */
        @Headers("Accept-Encoding: application/json")
        @POST("oauth/2.0/token")
        Call<OAuthBean> updateToken(@Body Request request);

        @Headers("Accept-Encoding: application/json")
        @GET("oauth/2.0/token?grant_type=client_credentials")
        Call<OAuthBean> getToken(@Query("client_id") String ak,
                                 @Query("client_secret") String sk);
    }


}
