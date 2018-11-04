package com.p2t.p2t;

import android.os.AsyncTask;

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
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureHandler extends HandlerActivity {
    Image img;
    public PictureHandler(Image img)
    {
        this.img = img;
//        new GetOAuthToken(this,account,scope);
    }
    @Override
    protected void onTokenReceived(String token)
    {
        this.accessToken = token;
//        getData();
    }
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
        };
    }

}
