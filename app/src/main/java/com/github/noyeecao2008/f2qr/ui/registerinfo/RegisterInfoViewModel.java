package com.github.noyeecao2008.f2qr.ui.registerinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterInfoViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mQRContent;

    public RegisterInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QRScan fragment");
        mQRContent = new MutableLiveData<>();
        mQRContent.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getQRContent() {
        return mQRContent;
    }

    public void setQRContent(String value){
        mQRContent.setValue(value);
    }

}