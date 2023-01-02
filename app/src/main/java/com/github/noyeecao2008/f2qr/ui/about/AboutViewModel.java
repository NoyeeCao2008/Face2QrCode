package com.github.noyeecao2008.f2qr.ui.about;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.noyeecao2008.f2qr.BuildConfig;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> mVersion;

    public AboutViewModel() {
        mVersion = new MutableLiveData<>();
        mVersion.setValue("v" + BuildConfig.VERSION_NAME
                + "(" + BuildConfig.VERSION_CODE + ") buildOn:" + BuildConfig.BUILD_TIME);
    }

    public LiveData<String> getText() {
        return mVersion;
    }
}