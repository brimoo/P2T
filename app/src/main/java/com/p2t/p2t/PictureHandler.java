package com.p2t.p2t;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.accounts.Account;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.Batch;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.p2t.p2t.MainActivity.REQUEST_CODE_PICK_ACCOUNT;

public class PictureHandler extends AppCompatActivity {
    private Image img;
    private String accessToken;
    private Account account;
    private String text;
    private String SCOPE ="oauth2:https://www.googleapis.com/auth/cloud-platform";
    public PictureHandler()
    {

    }
    public PictureHandler(Image im)
    {
        img = im;
        new GetOAuthToken(this,account,SCOPE).execute();
    }
    protected void onTokenReceived(String token)
    {
        accessToken = token;
        try {
            getData();
        }
        catch(IOException e){}
    }
    @SuppressLint("StaticFieldLeak")
    private void getData() throws IOException {
        new AsyncTask<Object, Void, BatchAnnotateImagesResponse>() {
            @Override
            protected BatchAnnotateImagesResponse doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory json = GsonFactory.getDefaultInstance();
                    Vision.Builder build = new Vision.Builder(httpTransport, json, credential);
                    Vision v = build.build();
                    List<Feature> featureList = new ArrayList<>();
                    Feature textD = new Feature();
                    textD.setType("TEXT_DETECTION");
//        textD.setMaxResults(64);
                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = img;
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest = v.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    return annotateRequest.execute();
                }
                catch(GoogleJsonResponseException e){}
                catch(IOException e){}
                return null;
            }
            protected void onPostExecute(BatchAnnotateImagesResponse response)
            {
                text = getText(response);

            }
        }.execute();
    }

    private String getText(BatchAnnotateImagesResponse response)
    {
        String str = "";
        List<EntityAnnotation> texts = response.getResponses().get(0).getTextAnnotations();
        if(texts!=null)
        {
            for(EntityAnnotation t :texts)
            {
                str = str + String.format(Locale.getDefault(), "%s: %s", t.getLocale(), t.getDescription());
                str = str + " ";
            }
        }
        else
            str = "No Text Detected\n";
        return str;
    }

    private void pickAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public void getToken(){
        if(account == null)
        {
            pickAccount();
        }
        else
        {
            new GetOAuthToken(this, account, SCOPE).execute();
        }
    }

    public String getText()
    {
        return text;
    }

    public void setAccount(Account acc)
    {
        account = acc;
    }

}
