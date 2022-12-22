package com.github.noyeecao2008.net;

public class RequestForFace extends RequestForPost {

    protected RequestForFace() {
        this.param("access_token", BaiduOAuthServer.getAccessToken());
    }

    protected String getDeviceId() {
        String deviceId = "ttt";// todo get deviceId
        return deviceId;
    }

    protected String getGroupId() {
        String groupID = "F2QR_" + getDeviceId();
        return groupID;
    }
}
