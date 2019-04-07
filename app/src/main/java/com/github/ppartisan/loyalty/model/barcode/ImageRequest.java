package com.github.ppartisan.loyalty.model.barcode;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.github.ppartisan.loyalty.model.settings.Settings;

import java.util.concurrent.Future;

import javax.inject.Inject;

import io.reactivex.Single;

class ImageRequest {

    private final Context context;
    private final Settings settings;

    @Inject
    ImageRequest(Context context, Settings settings) {
        this.context = context;
        this.settings = settings;
    }

    Single<Image> loadImage(String uri) {
        final RequestOptions options = new RequestOptions().override(settings.imageSize());
        final Future<Bitmap> target = request(uri, options).submit();
        return Single.fromFuture(target).map(Image::create);
    }

    private RequestBuilder<Bitmap> request(String uri, RequestOptions options) {
        return Glide.with(context)
                .asBitmap()
                .apply(options)
                .load(uri);
    }

    Image crop(Image original, Crop.Bounds bounds) {
        final Bitmap cropped = Bitmap.createBitmap(
                original.bitmap(), bounds.x(), bounds.y(), bounds.width(), bounds.height()
        );
        return Image.create(cropped);
    }

}
