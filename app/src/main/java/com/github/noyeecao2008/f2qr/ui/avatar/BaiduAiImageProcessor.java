package com.github.noyeecao2008.f2qr.ui.avatar;

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

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiduAiImageProcessor implements ImageProcessFactory.ImageProcesor {

    private static final String TAG = BaiduAiImageProcessor.class.getSimpleName();

    private ImageProcessFactory.FaceInfo addFace(String imageBase64, String authToken, String userId) {
        final String[] faceId = {""};
        final String[] errMsg = {""};

        RequestForFaceAdd req = new RequestForFaceAdd(userId, imageBase64);
        CountDownLatch latch = new CountDownLatch(1);
        Network.getBaiduFaceServer().addFace(req, authToken)
                .enqueue(
                        new Callback<FaceAddResultBean>() {
                            @Override
                            public void onResponse(Call<FaceAddResultBean> call, Response<FaceAddResultBean> response) {
                                FaceAddResultBean.ResultBean bean = response.body().getResult();
                                if (bean == null) {
                                    errMsg[0] = "null result";
                                    Log.e(TAG, "addFace.onResponse empty bean");
                                } else {
                                    faceId[0] = userId;
                                    Log.d(TAG, "getFaceToken:" + bean.getFace_token());
                                }
                                latch.countDown();
                            }

                            @Override
                            public void onFailure(Call<FaceAddResultBean> call, Throwable t) {
                                latch.countDown();
                                errMsg[0] = "addFace.onFailure";
                                Log.e(TAG, "addFace.onFailure");
                            }
                        }
                );
        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "", e);
        }
        return new ImageProcessFactory.FaceInfo(faceId[0], imageBase64, errMsg[0]);
    }

    private ImageProcessFactory.FaceInfo searchFace(String imageBase64, String authToken) throws IllegalArgumentException {
        final String[] userId = {""};
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
                        if (result == null || result.getUser_list() == null
                                || result.getUser_list().size() == 0
                                || result.getUser_list().get(0) == null) {
                            errMsg[0] = bean.getError_msg();
                            latch.countDown();
                            return;
                        }

                        userId[0] = result.getUser_list().get(0).getUser_id();
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
        return new ImageProcessFactory.FaceInfo(userId[0], imageBase64, errMsg[0]);
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

    @NonNull
    @Override
    public ImageProcessFactory.FaceInfo searchUserId(@NonNull String imageBase64) {
        String authToken = BaiduOAuthServer.getAccessToken();
        if (TextUtils.isEmpty(authToken)) {
            authToken = loadAuthToken();
            if (TextUtils.isEmpty(authToken)) {
                Log.e(TAG, "empty auth token");
                return new ImageProcessFactory.FaceInfo("", "", "empty auth token");
            }
        }
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "authToken = " + authToken);
        }

        return searchFace(imageBase64, authToken);
    }

    @NonNull
    @Override
    public ImageProcessFactory.FaceInfo addUserId(@NonNull String imageBase64, @NonNull String userId) {
        String authToken = BaiduOAuthServer.getAccessToken();
        if (TextUtils.isEmpty(authToken)) {
            authToken = loadAuthToken();
            if (TextUtils.isEmpty(authToken)) {
                Log.e(TAG, "empty auth token");
                return new ImageProcessFactory.FaceInfo("", "", "empty auth token");
            }
        }
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "authToken = " + authToken);
        }
        return addFace(imageBase64, authToken, userId);
    }
}
