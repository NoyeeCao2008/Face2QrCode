package com.github.noyeecao2008.f2qr.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.noyeecao2008.camera.ImageProcessFactory.FaceInfo;

public class ScanViewModel extends ViewModel {

    private final MutableLiveData<FaceInfo> mFaceInfo;
    private final MutableLiveData<String> mQrcode;
    private final MutableLiveData<String> mRegistrationAvatar;

    public ScanViewModel() {
        mFaceInfo = new MutableLiveData<>();
        mFaceInfo.setValue(null);
        mQrcode = new MutableLiveData<>();
        mQrcode.setValue("");
        mRegistrationAvatar = new MutableLiveData<>();
        mRegistrationAvatar.setValue("");
    }

    public LiveData<FaceInfo> getFaceInfo() {
        return mFaceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        mFaceInfo.setValue(faceInfo);
    }

    public LiveData<String> getQrcode() {
        return mQrcode;
    }

    public void setQrcode(String qrcode) {
        mQrcode.setValue(qrcode);
    }

    public void setRegistrationAvatar(String avatar) {
        mRegistrationAvatar.setValue(avatar);
    }

    public LiveData<String> getRegistrationAvatar(){
        return mRegistrationAvatar;
    }
}