package com.github.noyeecao2008.net;

import com.github.noyeecao2008.net.gson.GsonConverterFactory;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Network {
    private static Gson sGson;
    private static BaiduOAuthServer.Api sBaiduOAuthServer;
    private static BaiduFaceServer.Api sBaiduFaceServer;
    private static OkHttpClient sOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static BaiduOAuthServer.Api getBaiduOAuthServer() {
        if (sBaiduOAuthServer == null) {
            synchronized (BaiduOAuthServer.class) {
                if (sBaiduOAuthServer == null) {
                    Retrofit.Builder builder = getRetrofitBuilder(BaiduOAuthServer.HOST);
                    Retrofit retrofit = builder.build();
                    sBaiduOAuthServer = retrofit.create(BaiduOAuthServer.Api.class);
                }
            }
        }
        return sBaiduOAuthServer;
    }

    public static BaiduFaceServer.Api getBaiduFaceServer() {
        if (sBaiduFaceServer == null) {
            synchronized (BaiduFaceServer.class) {
                if (sBaiduFaceServer == null) {
                    Retrofit.Builder builder = getRetrofitBuilder((BaiduFaceServer.HOST));
                    Retrofit retrofit = builder.build();
                    sBaiduFaceServer = retrofit.create(BaiduFaceServer.Api.class);
                }
            }
        }
        return sBaiduFaceServer;
    }

    private static void initOkHttpClient() {
        OkHttpClient.Builder cb = new OkHttpClient.Builder();
        sOkHttpClient = cb.build();
    }

    private static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(baseUrl)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(initGson()));
        return retrofitBuilder;
    }

    private static Gson initGson() {
        sGson = new Gson();
        return sGson;
    }
}
