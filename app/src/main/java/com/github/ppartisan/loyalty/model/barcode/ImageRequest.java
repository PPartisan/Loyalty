package com.github.ppartisan.loyalty.model.barcode;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.github.ppartisan.loyalty.model.settings.Settings;
import com.google.auto.value.AutoValue;

import javax.inject.Inject;

import io.reactivex.Single;

import static android.graphics.BitmapFactory.decodeFileDescriptor;

class ImageRequest {

    private final Context context;
    private final ContentResolver resolver;
    private final Settings settings;

    @Inject
    ImageRequest(Context context, ContentResolver resolver, Settings settings) {
        this.context = context;
        this.resolver = resolver;
        this.settings = settings;
    }

    Single<Image> createScaledBitmap(String uri) {
        final RequestOptions options = new RequestOptions().override(settings.imageSize());
        final RequestBuilder<Bitmap> request = request(uri, options);
        return Single.fromFuture(request.submit()).map(Image::create);
    }

    Single<Dimens> findOriginalImageDimensions(String uri) {
        return Single.fromCallable(() -> {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try(ParcelFileDescriptor descriptor = resolver.openFileDescriptor(Uri.parse(uri), "r")) {
                if (descriptor == null) {
                    throw new NullPointerException("Null Descriptor Opening Image URI");
                }
                decodeFileDescriptor(descriptor.getFileDescriptor(), null, options);
                return Dimens.create(options.outWidth, options.outHeight);
            }
        });
    }

    private RequestBuilder<Bitmap> request(String uri, RequestOptions options) {
        return Glide.with(context)
                .asBitmap()
                .apply(options)
                .load(uri);
    }

    //Used to capture the dimension (width/height) of an original, unscaled image
    @AutoValue
    abstract static class Dimens {
        abstract int width();
        abstract int height();

        static Dimens create(int width, int height) {
            return new AutoValue_ImageRequest_Dimens(width, height);
        }
    }

    //Wraps an Android image type (i.e. Bitmap)
    @AutoValue
    abstract static class Image {
        abstract Bitmap bitmap();
        static Image create(Bitmap bitmap) {
            return new AutoValue_ImageRequest_Image(bitmap);
        }
    }

}
