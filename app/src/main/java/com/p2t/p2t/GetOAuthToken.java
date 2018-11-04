package com.p2t.p2t;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

public class GetOAuthToken extends AsyncTask<Void, Void, Void> {
    Activity activity;
    Account account;
    String scope;

    GetOAuthToken(Activity activity, Account account, String scope)
    {
        this.activity=activity;
        this.account=account;
        this.scope=scope;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
<<<<<<< HEAD
        try{
            String token = fetchToken();
            if(token !=null){
                ((HandlerActivity)activity).onTokenReceived(token);
            }
        }
        catch(IOException e)
        {

        }
=======
//        try{
//
//        }
//        catch(IOException e)
//        {
//
//        }
>>>>>>> 770c9e3f5f95703d4565c402becdea2b857a6952
        return null;
    }
    private String fetchToken() throws IOException{
        String token;
        try{
            token = GoogleAuthUtil.getToken(activity, account, scope);
            GoogleAuthUtil.clearToken(activity,token);
            token = GoogleAuthUtil.getToken(activity,account,scope);
            return token;
        }catch(UserRecoverableAuthException e) {

        }catch (GoogleAuthException fatalE){

        }
        return null;
    }
}
