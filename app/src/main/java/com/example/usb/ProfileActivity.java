package com.example.usb;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.usb.callbacks.GetUserCallback;
import com.example.usb.entities.User;
import com.example.usb.requests.UserRequest;
import com.facebook.drawee.backends.pipeline.Fresco;

public class ProfileActivity extends Activity implements GetUserCallback.IGetUserResponse {
    private ImageView mProfilePhotoView;
    private TextView mName;
    private TextView mId;
    private TextView mEmail;
    private TextView mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);

        mProfilePhotoView = findViewById(R.id.profile_photo);
        mName = findViewById(R.id.name);
        mId = findViewById(R.id.id);
        mEmail = findViewById(R.id.email);
        mPermissions = findViewById(R.id.permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRequest.makeUserRequest(new GetUserCallback(ProfileActivity.this).getCallback());
    }

    @Override
    public void onCompleted(User user) {
        Glide.with(this).load(user.getPicture()).into(mProfilePhotoView);
        //mProfilePhotoView.setImageURI(user.getPicture());
        mName.setText(user.getName());
        mId.setText(user.getId());
        if (user.getEmail() == null) {
            mEmail.setText(R.string.no_email_perm);
            mEmail.setTextColor(Color.RED);
        } else {
            mEmail.setText(user.getEmail());
            mEmail.setTextColor(Color.BLACK);
//            user.getEmail() --> DB랑 보냄
        }
        mPermissions.setText(user.getPermissions());
    }
}