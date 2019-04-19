package com.github.ppartisan.loyalty.model.barcode;

import com.github.ppartisan.loyalty.model.persistence.SaveImage.ImagePath;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CroppableImage {

    public abstract ImagePath paths();

    public abstract int originalHeight();
    public abstract int originalWidth();

    public abstract Bounds cropRegion();

    static CroppableImage create(ImagePath paths, ImageRequest.Dimens originalDimens, Bounds crop) {
        return new AutoValue_CroppableImage(paths, originalDimens.height(), originalDimens.width(), crop);
    }

    private static CroppableImage create(ImagePath paths, int originalHeight, int originalWidth, Bounds crop) {
        return new AutoValue_CroppableImage(paths, originalHeight, originalWidth, crop);
    }

}
