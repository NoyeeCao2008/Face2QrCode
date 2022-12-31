package com.github.noyeecao2008.f2qr;

import android.app.Application;

import com.github.noyeecao2008.camera.ImageProcessFactory;
import com.github.noyeecao2008.f2qr.ui.avatar.BaiduAiImageProcessor;
import com.tencent.mmkv.MMKV;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        ImageProcessFactory.init(new BaiduAiImageProcessor());
    }
}
