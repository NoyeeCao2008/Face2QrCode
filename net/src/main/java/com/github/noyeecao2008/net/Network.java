package com.github.noyeecao2008.net;

import com.github.noyeecao2008.net.gson.GsonConverterFactory;
import com.github.noyeecao2008.net.gson.GsonRequestBodyConverter;
import com.github.noyeecao2008.net.gson.GsonResponseBodyConverter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class Network {
    private static Gson sGson;
    private static BaiduOAuthServer.Api sBaiduOAuthServer;
    private static OkHttpClient sOkHttpClient;
    private static final String BAIDU_HOST = "https://aip.baidubce.com/";

    static {
        initOkHttpClient();
    }

    public static BaiduOAuthServer.Api getBaiduOAuthServer() {
        if (sBaiduOAuthServer == null) {
            synchronized (BaiduOAuthServer.class) {
                if (sBaiduOAuthServer == null) {
                    Retrofit.Builder builder = getRetrofitBuilder(BAIDU_HOST);
                    Retrofit retrofit = builder.build();
                    sBaiduOAuthServer = retrofit.create(BaiduOAuthServer.Api.class);
                }
            }
        }
        return sBaiduOAuthServer;
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
