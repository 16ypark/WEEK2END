package com.example.usb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.usb.fragment.Fragment1;
import com.example.usb.fragment.Fragment2;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

public class CombineClass extends AppCompatActivity {

    Toolbar toolbar;
    LoginButton loginButton;
    Fragment1 fragment1;
    Fragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_combine);
        loginButton = findViewById(R.id.login_button);
        // 로그인 안하면 못 들간다~
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(CombineClass.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }

        //로그인 후 화면 실행
        //setSupportActionBar : 액션바 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //액션바 기본 타이틀 보여지지 않게
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //Fragment : 탭 클릭시 보여줄 화면들
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        //3번째 탭은 버튼으로 ㅎㅎ;
        Button tofacebook = findViewById(R.id.tofacebook);
        tofacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CombineClass.this, MainActivity.class));
            }
        });

        //기본으로 첫번째 Fragment를 보여지도록 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();


        //TabLayout에 Tab 3개 추가
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Contacts"));
        tabs.addTab(tabs.newTab().setText("Album"));
        tabs.addTab(tabs.newTab().setText("EXIT"));

        //탭 선택리스너
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            //탭선택시
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("CombineClass", "선택된 탭 : "+position);

                Fragment selected = null;
                if(position==0){
                    selected = fragment1;
                }else if(position==1){
                    selected = fragment2;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }
            //탭선택해제시
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //선탭된탭을 다시 클릭시
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}