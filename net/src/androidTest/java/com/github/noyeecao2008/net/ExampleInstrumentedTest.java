package com.github.noyeecao2008.net;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.noyeecao2008.bean.FaceAddResultBean;
import com.github.noyeecao2008.bean.FaceSearchBean;
import com.github.noyeecao2008.bean.OAuthBean;
import com.tencent.mmkv.MMKV;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "auth_tst";

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MMKV.initialize(appContext);
        Log.i(TAG, "MMKV.initializ");
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.github.noyeecao2008.net.test", appContext.getPackageName());
    }

    @Test
    public void testOAuth() {
        CountDownLatch latch = new CountDownLatch(1);
        String ak = BuildConfig.BaiduAiAK;
        String sk = BuildConfig.BaiduAiSK;

        Network.getBaiduOAuthServer()
                .getToken(ak, sk)
                .enqueue(new Callback<OAuthBean>() {
                    @Override
                    public void onResponse(Call<OAuthBean> call, Response<OAuthBean> response) {

                        OAuthBean bean = response.body();
                        Log.i(TAG, "bean:" + bean);
                        Log.i(TAG, "body:" + response.raw().body());

                        BaiduOAuthServer.saveAccessToken(bean);

                        if (response.errorBody() == null) {
                            latch.countDown();
                            return;
                        }

                        try {
                            Log.i(TAG, "errorBody:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Call<OAuthBean> call, Throwable t) {
                        Log.i(TAG, "fail:", t);
                        latch.countDown();
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToken() {
        Log.i(TAG, "testReadToken:" + BaiduOAuthServer.getAccessToken());
    }

    @Test
    public void testAddFace() {
        CountDownLatch latch = new CountDownLatch(1);

        RequestForFaceAdd request = new RequestForFaceAdd("ny", NoyeeFace.FACE_2);
        Network.getBaiduFaceServer()
                .addFace(request, BaiduOAuthServer.getAccessToken())
                .enqueue(new Callback<FaceAddResultBean>() {
                    @Override
                    public void onResponse(Call<FaceAddResultBean> call,
                                           Response<FaceAddResultBean> response) {
                        FaceAddResultBean bean = response.body();
                        Log.i(TAG, "bean:" + bean);
                        Log.i(TAG, "body:" + response.raw().body());
                        Log.i(TAG, "raw:" + response.raw().toString());

                        if (response.errorBody() == null) {
                            latch.countDown();
                            return;
                        }

                        try {
                            Log.i(TAG, "errorBody:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Call<FaceAddResultBean> call, Throwable t) {
                        Log.e(TAG, "fail", t);
                        latch.countDown();
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchFace(){
        CountDownLatch latch = new CountDownLatch(1);

        RequestForFaceSearch request = new RequestForFaceSearch( NoyeeFace.FACE_1);
        Network.getBaiduFaceServer()
                .search(request, BaiduOAuthServer.getAccessToken())
                .enqueue(new Callback<FaceSearchBean>() {
                    @Override
                    public void onResponse(Call<FaceSearchBean> call,
                                           Response<FaceSearchBean> response) {
                        FaceSearchBean bean = response.body();
                        Log.i(TAG, "bean:" + bean);
                        Log.i(TAG, "body:" + response.raw().body());
                        Log.i(TAG, "raw:" + response.raw().toString());

                        if (response.errorBody() == null) {
                            latch.countDown();
                            return;
                        }

                        try {
                            Log.i(TAG, "errorBody:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
    }
}