package com.example.usb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.com.example.screw.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PROFILE_ACTIVITY = 1;
    private static final int RESULT_POSTS_ACTIVITY = 2;
    private static final int RESULT_PERMISSIONS_ACTIVITY = 3;
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_VIDEO_CODE = 1000;
    LoginButton loginButton;
    Button btnShareLink, btnSharePhoto, btnShareVideo;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Uri imageUri;
    ImageView imageView;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Make a button which leads to profile information of the user
        Button gotoProfileButton = findViewById(R.id.btn_profile);
        gotoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent profileIntent = new Intent(MainActivity.this, FacebookLoginActivity
                            .class);
                    startActivityForResult(profileIntent, RESULT_PROFILE_ACTIVITY);
                } else {
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
            }
        });
/*
        // Make a button which leads to posts made by the user
        Button gotoPostsFeedButton = findViewById(R.id.btn_posts);
        gotoPostsFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                    startActivityForResult(loginIntent, RESULT_POSTS_ACTIVITY);
                } else {
                    Intent postsFeedIntent = new Intent(MainActivity.this, PostFeedActivity.class);
                    startActivity(postsFeedIntent);
                }
            }
        });

        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // Make a button which posts links
        btnShareLink = findViewById(R.id.btnShareLink);
        btnShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this, "Share successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Share cancelled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("This is our 개발자's 헛수고")
                        .setContentUrl(Uri.parse("http://socrip3.kaist.ac.kr:5280/form"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(linkContent);
                }
            }
        });

        btnSharePhoto = findViewById(R.id.btnSharePhoto);
        btnSharePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create callback
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this, "Share successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Share cancelled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //Fetch Photo from link and convert to bitmap
                Picasso.with(getBaseContext())
                        .load("https://c1.staticflickr.com/5/4727/24485717657_b268b2fd17_b.jpg")
                        .into(target);
            }
        });

        btnShareVideo = findViewById(R.id.btnShareVideo);
        btnShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Choose Video dialog
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select video"), REQUEST_VIDEO_CODE);
            }
        });


        */
        // Make a logout button
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Intent permissionsIntent = new Intent(MainActivity.this, PermissionsActivity
                            .class);
                    startActivity(permissionsIntent);
                }
                break;
            case REQUEST_VIDEO_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedVideo = data.getData();

                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(selectedVideo)
                            .build();

                    ShareVideoContent videoContent = new ShareVideoContent.Builder()
                            .setContentTitle("MAD Camp Video Upload Test")
                            .setContentDescription("This is random Video. It's a waste of time to watch")
                            .setVideo(video)
                            .build();

                    if (shareDialog.canShow(ShareVideoContent.class))
                        shareDialog.show(videoContent);
                }
                break;
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PICK_IMAGE);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
        /*
        //setSupportActionBar : 액션바 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //액션바 기본 타이틀 보여지지 않게
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //Fragment : 탭 클릭시 보여줄 화면들
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        //기본으로 첫번째 Fragment를 보여지도록 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();


        //TabLayout에 Tab 3개 추가
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Contacts"));
        tabs.addTab(tabs.newTab().setText("Album"));
        tabs.addTab(tabs.newTab().setText("Painter"));

        //탭 선택리스너
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            //탭선택시
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : "+position);

                Fragment selected = null;
                if(position==0){
                    selected = fragment1;
                }else if(position==1){
                    selected = fragment2;
                }else if(position==2){
                    selected = fragment3;
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
*/
}