package com.p2t.p2t;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
//this class was created in case we also must use OAuth Tokens for the audio API
public abstract class HandlerActivity extends AppCompatActivity {
    protected String accessToken;
    abstract protected void onTokenReceived(String token);
}
