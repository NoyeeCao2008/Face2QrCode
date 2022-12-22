package com.github.noyeecao2008.f2qr;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
