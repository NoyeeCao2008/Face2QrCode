package com.github.noyeecao2008.net;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.noyeecao2008.bean.OAuthBean;

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
//                .updateToken(BaiduOAuthServer.createUpdateTokenRequest(ak, sk))
                .getToken(ak, sk)
                .enqueue(new Callback<OAuthBean>() {
                    @Override
                    public void onResponse(Call<OAuthBean> call, Response<OAuthBean> response) {

                        OAuthBean bean = response.body();
                        Log.i("auth", "bean:" + bean);
                        Log.i("auth", "body:" + response.raw().body());
                        if (response.errorBody() == null) {
                            latch.countDown();
                            return;
                        }

                        try {
                            Log.i("auth", "errorBody:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Call<OAuthBean> call, Throwable t) {
                        Log.i("auth", "fail:", t);
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