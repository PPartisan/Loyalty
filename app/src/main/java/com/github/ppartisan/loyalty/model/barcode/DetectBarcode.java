package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;

import com.github.ppartisan.loyalty.model.persistence.SaveImage;
import com.github.ppartisan.loyalty.model.persistence.SaveImage.ImagePath;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.Single;

@Singleton
public class DetectBarcode {

    private final ImageRequest image;
    private final DetectionRequest request;
    private final SaveImage save;

    @Inject
    DetectBarcode(ImageRequest image, DetectionRequest request, SaveImage save) {
        this.image = image;
        this.request = request;
        this.save = save;
    }

    public Single<CroppableImage> request(@NonNull String uri) {
        final Single<ImageRequest.Dimens> originalBounds = image.findOriginalImageDimensions(uri);

        final Single<Bitmap> scaledBitmap =
                image.createScaledBitmap(uri).map(ImageRequest.Image::bitmap);
        final Single<Bounds> cropBounds =
                scaledBitmap.flatMap(request::getBoundsForSingleBarcode);
        final Single<ImagePath> path =
                scaledBitmap.flatMap(save::save);

        return Single.zip(path, originalBounds, cropBounds, CroppableImage::create);
    }

}
