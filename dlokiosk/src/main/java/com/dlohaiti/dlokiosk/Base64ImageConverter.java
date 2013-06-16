package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.util.support.Base64;

import java.io.ByteArrayOutputStream;

@Singleton
public class Base64ImageConverter {
    private final Context context;

    @Inject
    public Base64ImageConverter(Context context) {
        this.context = context;
    }

    public Bitmap fromBase64EncodedString(String encodedImage) {
        Bitmap resource;
        try {
            byte[] imageData = Base64.decode(encodedImage);
            resource = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } catch (Exception e) {
            resource = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
        }
        return resource;
    }

    public String toBase64EncodedString(Bitmap imageResource) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageResource.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT);
    }
}
