package com.github.noyeecao2008.f2qr.ui.avatar;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.noyeecao2008.bean.FaceAddResultBean;
import com.github.noyeecao2008.bean.FaceSearchBean;
import com.github.noyeecao2008.bean.OAuthBean;
import com.github.noyeecao2008.camera.ImageProcessFactory;
import com.github.noyeecao2008.net.BaiduOAuthServer;
import com.github.noyeecao2008.net.BuildConfig;
import com.github.noyeecao2008.net.Network;
import com.github.noyeecao2008.net.RequestForFaceAdd;
import com.github.noyeecao2008.net.RequestForFaceSearch;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiduAiImageProcessor implements ImageProcessFactory.ImageProcesor {

    private static final String TAG = BaiduAiImageProcessor.class.getSimpleName();

    @NonNull
    @Override
    public String base64ToFaceId(@NonNull String imageBase64, boolean addFace) {
        String authToken = BaiduOAuthServer.getAccessToken();
        if (TextUtils.isEmpty(authToken)) {
            authToken = loadAuthToken();
            if (TextUtils.isEmpty(authToken)) {
                Log.e(TAG, "empty auth token");
                return "";
            }
        }
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "authToken = " + authToken);
        }
        if (addFace) {
            return addFace(imageBase64, authToken);
        } else {
            return searchFace(imageBase64, authToken);
        }
    }

    private String addFace(String imageBase64, String authToken) {
        final String[] result = {""};
        RequestForFaceAdd req = new RequestForFaceAdd("", imageBase64);
        CountDownLatch latch = new CountDownLatch(1);
        Network.getBaiduFaceServer().addFace(req, authToken)
                .enqueue(
                        new Callback<FaceAddResultBean>() {
                            @Override
                            public void onResponse(Call<FaceAddResultBean> call, Response<FaceAddResultBean> response) {
                                FaceAddResultBean.ResultBean bean = response.body().getResult();
                                if (bean == null) {
                                    result[0] = "";
                                    Log.e(TAG, "addFace.onResponse empty bean");
                                } else {
                                    result[0] = bean.getFace_token();
                                    Log.d(TAG, "getFaceToken:" + bean.getFace_token());
                                }
                                latch.countDown();
                            }

                            @Override
                            public void onFailure(Call<FaceAddResultBean> call, Throwable t) {
                                latch.countDown();
                                result[0] = "";
                                Log.e(TAG, "addFace.onFailure");
                            }
                        }
                );
        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "", e);
        }
        return result[0];
    }

    private String searchFace(String imageBase64, String authToken) throws IllegalArgumentException {
        final String[] faceId = {""};
        final String[] errMsg = {""};
        CountDownLatch latch = new CountDownLatch(1);

        RequestForFaceSearch request = new RequestForFaceSearch(imageBase64);
        Network.getBaiduFaceServer()
                .search(request, authToken)
                .enqueue(new Callback<FaceSearchBean>() {
                    @Override
                    public void onResponse(Call<FaceSearchBean> call,
                                           Response<FaceSearchBean> response) {
                        FaceSearchBean bean = response.body();
                        if (BuildConfig.DEBUG) {
                            Log.i(TAG, "bean:" + bean);
                        }
                        if (bean == null) {
                            latch.countDown();
                            return;
                        }

                        FaceSearchBean.ResultBean result = bean.getResult();
                        if (result == null) {
                            errMsg[0] = bean.getError_msg();
                            latch.countDown();
                            return;
                        }

                        faceId[0] = result.getFace_token();
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Call<FaceSearchBean> call, Throwable t) {
                        Log.e(TAG, "fail", t);
                        latch.countDown();
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(errMsg[0])) {
            throw new IllegalArgumentException(errMsg[0]);
        }
        return faceId[0];
    }

    private String loadAuthToken() {
        final String[] authTokenResult = {""};
        CountDownLatch latchToken = new CountDownLatch(1);
        Network.getBaiduOAuthServer().getToken(BuildConfig.BaiduAiAK, BuildConfig.BaiduAiSK).enqueue(new Callback<OAuthBean>() {
            @Override
            public void onResponse(Call<OAuthBean> call, Response<OAuthBean> response) {
                OAuthBean bean = response.body();
                if (bean == null) {
                    authTokenResult[0] = "";
                    Log.e(TAG, "getToken.onResponse empty bean");

                } else {
                    authTokenResult[0] = bean.accessToken;
                    BaiduOAuthServer.saveAccessToken(bean);
                    Log.d(TAG, "getToken:" + bean.accessToken);
                }
                latchToken.countDown();
            }

            @Override
            public void onFailure(Call<OAuthBean> call, Throwable t) {
                authTokenResult[0] = "";
                latchToken.countDown();
                Log.e(TAG, "getToken.onFailure");
            }
        });
        try {
            latchToken.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "", e);
        }
        return authTokenResult[0];
    }
}
