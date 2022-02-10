package com.gauravk.bubblebarsample;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class kakaoapplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        KakaoSdk.init(this, "8ec1b685f221220049a64d24a77fb77e");
    }
}
