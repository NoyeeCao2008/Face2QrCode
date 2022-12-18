package com.github.noyeecao2008.bean;

import com.google.gson.annotations.SerializedName;

public class OAuthBean {
    //    access_token： 要获取的Access Token；
    @SerializedName("access_token")
    public String accessToken;

    //    expires_in： Access Token的有效期(秒为单位，有效期30天)；
    @SerializedName("expires_in")
    public String expiresIn;
}
