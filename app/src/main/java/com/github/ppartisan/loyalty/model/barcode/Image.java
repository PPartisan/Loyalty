package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;

import com.google.auto.value.AutoValue;

//Wraps an Android image type (i.e. Bitmap)
@AutoValue
abstract class Image {
    abstract Bitmap bitmap();
    static Image create(Bitmap bitmap) {
        return new AutoValue_Image(bitmap);
    }
}
