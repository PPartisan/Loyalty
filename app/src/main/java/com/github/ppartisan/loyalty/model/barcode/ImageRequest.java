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

    Image crop(Image original, Bounds bounds) {
        final Bounds padded = pad(original.bitmap(), bounds);
        final Bitmap cropped = Bitmap.createBitmap(
                original.bitmap(), padded.x(), padded.y(), padded.width(), padded.height()
        );
        return Image.create(cropped);
    }

    private Bounds pad(Bitmap bitmap, Bounds bounds) {
        final int bHeight = bitmap.getHeight();
        final int bWidth = bitmap.getWidth();

        final int paddedX = padCoord(bounds.x());
        final int paddedY = padCoord(bounds.y());
        final int paddedW = padDimen(bitmap.getWidth(), bounds.width());
        final int paddedH = padDimen(bitmap.getHeight(), bounds.height());

        final int right = paddedX + paddedW;
        final int bottom = paddedY + paddedH;

        final boolean isWithinMaxBounds = right < bWidth && bottom < bHeight;

        return isWithinMaxBounds
                ? Bounds.create(paddedX, paddedY, paddedW, paddedH)
                : bounds;
    }

    private int padCoord(int in) {
        return Math.max(in - settings.padding(), 0);
    }

    private int padDimen(int bound, int in) {
        return Math.min(bound, in + (settings.padding()*2));
    }

}
