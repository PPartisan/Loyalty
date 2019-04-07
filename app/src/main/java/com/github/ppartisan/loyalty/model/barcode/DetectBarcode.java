package com.github.ppartisan.loyalty.model.barcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.Single;

import static java.util.Objects.requireNonNull;

@Singleton
public class DetectBarcode {

    private final ImageRequest image;
    private final DetectionRequest request;

    @Inject
    DetectBarcode(ImageRequest image, DetectionRequest request) {
        this.image = image;
        this.request = request;
    }

    public Single<Image> request(@NonNull String uri) {
        return image.loadImage(requireNonNull(uri))
                .flatMap(request::getBoundsForSingleBarcode)
                .map(result -> {
                    final Bounds bounds = result.bounds();
                    final Image original = result.original();
                    return (bounds.isValid()) ? image.crop(original, bounds) : original;
                });
    }



}
