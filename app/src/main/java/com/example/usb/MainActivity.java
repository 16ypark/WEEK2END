package com.example.usb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.usb.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareDialog;

public class MainActivity extends Activity {

    private static final int RESULT_PROFILE_ACTIVITY = 1;
    private static final int RESULT_POSTS_ACTIVITY = 2;
    private static final int RESULT_PERMISSIONS_ACTIVITY = 3;
    private static final int PICK_IMAGE = 100;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login_button);


        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }
        // 프로필 띄우기
        Button gotoProfileButton = findViewById(R.id.btn_profile);
        gotoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent profileIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                    startActivityForResult(profileIntent, RESULT_PROFILE_ACTIVITY);
                } else {
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
            }
        });

        // 관리자 유저 뉴스피드 보기
        final Button gotoPostsFeedButton = findViewById(R.id.btn_posts);
        gotoPostsFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent postFeedIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                    startActivityForResult(postFeedIntent, RESULT_POSTS_ACTIVITY);
                } else {
                    Intent postsFeedIntent = new Intent(MainActivity.this, PostFeedActivity.class);
                    startActivity(postsFeedIntent);
                }
            }
        });

        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // 사진 조작
        Button draw = findViewById(R.id.draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent drawIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                    startActivityForResult(drawIntent, PICK_IMAGE);
                } else {
                    Intent drawIntent = new Intent(MainActivity.this, Draw.class);
                    startActivity(drawIntent);
                }
            }
        });

        //3번째 탭은 버튼으로 ㅎㅎ;
        Button backtotab = findViewById(R.id.backtotab);
        backtotab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CombineClass.class));
            }
        });

        // 로그아웃 버튼
        Button fbLogoutButton = findViewById(R.id.btn_fb_logout);
        fbLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case RESULT_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
                break;
            case RESULT_POSTS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Intent postFeedIntent = new Intent(MainActivity.this, PostFeedActivity.class);
                    startActivity(postFeedIntent);
                }
                break;
            case RESULT_PERMISSIONS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Intent permissionsIntent = new Intent(MainActivity.this, PermissionsActivity.class);
                    startActivity(permissionsIntent);
                }
                break;
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Intent drawIntent = new Intent(MainActivity.this, Draw.class);
                    startActivity(drawIntent);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}