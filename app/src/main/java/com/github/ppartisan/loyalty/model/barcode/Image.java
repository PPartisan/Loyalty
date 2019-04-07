package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Image {
    public abstract Bitmap bitmap();

    static Image create(Bitmap bitmap) {
        return new AutoValue_Image(bitmap);
    }
}
