package com.example.usb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usb.R;
import com.example.usb.adapters.PostAdapter;
import com.example.usb.callbacks.GetPostsCallback;
import com.example.usb.callbacks.PublishPostCallback;
import com.example.usb.entities.Post;
import com.example.usb.requests.PostsRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.internal.ShareFeedContent;

import java.util.ArrayList;
import java.util.List;

public class PostFeedActivity extends Activity implements
        GetPostsCallback.IGetPostsResponse,
        PublishPostCallback.IPublishPostResponse,
        AccessToken.AccessTokenRefreshCallback,
        FacebookCallback<LoginResult> {

    private static final int PICK_IMAGE = 1;

    private ListView mPostsListView;
    private Button mComposeText;
    private Uri mAttachmentUri;
    CallbackManager mCallbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, this);

        PostAdapter postAdapter= new PostAdapter(this, new ArrayList<Post>());
        mPostsListView = findViewById(R.id.post_list_view);
        mPostsListView.setAdapter(postAdapter);

        // Compose post
        shareDialog = new ShareDialog(this);
        mComposeText = findViewById(R.id.compose_text);
        mComposeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(PostFeedActivity.this, "Share successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(PostFeedActivity.this, "Share cancelled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(PostFeedActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("Facebook은 권한 줄때 너무 깐깐하게 검사한다")
                        .setContentUrl(Uri.parse("http://socrip3.kaist.ac.kr:5280/form"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(linkContent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPosts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            mAttachmentUri = data.getData();
        }
    }


    // From GetPostsCallback
    @Override
    public void onGetPostsCompleted(List<Post> posts) {
        if (posts != null && !posts.isEmpty()) {
            mPostsListView.setAdapter(new PostAdapter(PostFeedActivity.this, posts));
        }
    }

    // From PublishPostCallback
    @Override
    public void onPublishCompleted() {
        getPosts();
    }

    // From AccessTokenRefreshCallback
    @Override
    public void OnTokenRefreshed(AccessToken accessToken) {
        makePost();
    }

    @Override
    public void OnTokenRefreshFailed(FacebookException e) {
        // Handle exception ...
    }

    // From FacebookLogin
    @Override
    public void onSuccess(LoginResult loginResult) {
        // Refresh token cached on device after login succeeds
        AccessToken.refreshCurrentAccessTokenAsync(this);
    }

    @Override
    public void onCancel() {
        // Handle user cancel ...
    }

    @Override
    public void onError(FacebookException e) {
        // Handle exception ...
    }

    // Private methods
    private void getPosts() {
        PostsRequest.makeGetPostsRequest(new GetPostsCallback(this).getCallback());
    }

    private void makePost() {
        if (mAttachmentUri == null) {
            PostsRequest.makePublishPostRequest(
                    mComposeText.getText().toString(),
                    new PublishPostCallback(this).getGraphRequestCallback());
        } else {
            PostsRequest.makePublishPostRequest(
                    mAttachmentUri,
                    new PublishPostCallback(this).getShareCallback());
        }
        mComposeText.setText("");
        mAttachmentUri = null;
    }
}