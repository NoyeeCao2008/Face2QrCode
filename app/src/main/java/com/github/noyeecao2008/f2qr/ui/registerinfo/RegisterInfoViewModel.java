package com.github.noyeecao2008.f2qr.ui.registerinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.noyeecao2008.camera.ImageProcessFactory;
import com.github.noyeecao2008.util.MD5;

public class RegisterInfoViewModel extends ViewModel {
    private String mUserId;
    private final MutableLiveData<ImageProcessFactory.FaceInfo> mFaceInfo;
    private final MutableLiveData<String> mQRContent;

    public RegisterInfoViewModel() {
        mFaceInfo = new MutableLiveData<>();
        mFaceInfo.setValue(null);
        mQRContent = new MutableLiveData<>();
        mQRContent.setValue("");
    }

    public String getUserId() {
        return mUserId;
    }

    public LiveData<ImageProcessFactory.FaceInfo> getFaceInfo() {
        return mFaceInfo;
    }

    public LiveData<String> getQRContent() {
        return mQRContent;
    }

    public void setFaceInfo(ImageProcessFactory.FaceInfo value) {
        mFaceInfo.setValue(value);
    }

    public void setQRContent(String value) {
        mQRContent.setValue(value);
        mUserId = MD5.digest(value);
    }

}