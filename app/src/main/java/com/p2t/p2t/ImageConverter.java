package com.p2t.p2t;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter {

    private static Bitmap resizeBitmap(Bitmap bm)
    {
        int maxDimension = 1024;
        int originalWidth = bm.getWidth();
        int originalHeight = bm.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bm, resizedWidth, resizedHeight, false);
    }

    public static Image getBase64Image(ContentResolver cr, Uri photoURI)
    {
        try {
            Bitmap bitmap = resizeBitmap(MediaStore.Images.Media.getBitmap(cr, photoURI));
            Image image = new Image();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            image.encodeContent(imageBytes);
            return image;
        } catch (IOException e) {
            // No bueno
        }
        return null;
    }

}
