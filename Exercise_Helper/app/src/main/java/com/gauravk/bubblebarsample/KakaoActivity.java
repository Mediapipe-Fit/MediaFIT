package com.gauravk.bubblebarsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.gauravk.bubblebarsample.cfg.MyGlobal;


import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class KakaoActivity extends AppCompatActivity {
    private static final String TAG = "KakaoActivity";

    private View loginButton, logoutButton, getinButton;
    private TextView nickName;
    private TextView startText;
    private ImageView profileImage;
    private ImageView startIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);

        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);
        getinButton = findViewById(R.id.getin);
        startIcon = findViewById(R.id.start_icon);
        startText = findViewById(R.id.start_text);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null){ //토큰이 제대로 와서 로그인이 됨
                    //로그인이 되었을때 해야할 일 처리
                }
                if (throwable != null){ //오류 발생
                    //TBD
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaoActivity.this)){ //카카오톡이 깔려있어서 카카오톡으로 로그인이 가능한지 확인
                    UserApiClient.getInstance().loginWithKakaoTalk(KakaoActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(KakaoActivity.this, callback);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });

        getinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BottomBarActivity.class);
                startActivity(intent);
            }
        });

        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) { // 로그인 됐는지 안됐는지 확인

                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage); // 프로필 사진 둥글게 출력하기

                    MyGlobal.getInstance().setGender(user.getKakaoAccount().getGender().toString());
                    MyGlobal.getInstance().setAge_range(user.getKakaoAccount().getAgeRange().toString());
                    MyGlobal.getInstance().setBirthday(user.getKakaoAccount().getBirthday());
                    MyGlobal.getInstance().setEmail(user.getKakaoAccount().getEmail());
                    MyGlobal.getInstance().setNickname(user.getKakaoAccount().getProfile().getNickname());
                    MyGlobal.getInstance().setProfile(user.getKakaoAccount().getProfile().getThumbnailImageUrl());

                    Log.v("gender:", user.getKakaoAccount().getGender().toString());
                    Log.v("age_range:", user.getKakaoAccount().getAgeRange().toString());
                    Log.v("birthday:", user.getKakaoAccount().getBirthday());
                    Log.v("email:", user.getKakaoAccount().getEmail());
                    Log.v("profile:", user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                    Log.v("nickname:", user.getKakaoAccount().getProfile().getNickname());

                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                    getinButton.setVisibility(View.VISIBLE);
                    startIcon.setVisibility(View.GONE);
                    startText.setVisibility(View.GONE);
                }else{
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);
                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                    getinButton.setVisibility(View.GONE);
                    startIcon.setVisibility(View.VISIBLE);
                    startText.setVisibility(View.VISIBLE);
                }
                return null;
            }
        });
    }
}
