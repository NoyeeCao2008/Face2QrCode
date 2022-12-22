package com.github.noyeecao2008.net;

import com.github.noyeecao2008.bean.FaceAddResultBean;
import com.github.noyeecao2008.bean.FaceSearchBean;
import com.github.noyeecao2008.bean.OAuthBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * https://ai.baidu.com/ai-doc/FACE/Gk37c1uzc
 */
public class BaiduFaceServer {

    public static final String HOST = "https://aip.baidubce.com/rest/2.0/face/v3/";

    public interface Api {
        /**
         * HTTP方法：POST
         * 请求URL： https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add
         * access_token	通过API Key和Secret Key获取的access_token,参考“Access Token获取”
         *
         * @param request
         * @return
         */
        @Headers("Accept-Encoding: application/json")
        @POST("faceset/user/add")
        Call<FaceAddResultBean> addFace(@Body RequestForPost request, @Query("access_token") String token);
        
        /**
         * https://aip.baidubce.com/rest/2.0/face/v3/search
         *
         * @param request
         * @return
         */
        @Headers("Accept-Encoding: application/json")
        @POST("search")
        Call<FaceSearchBean> search(@Body RequestForFace request, @Query("access_token") String token);
    }
}
