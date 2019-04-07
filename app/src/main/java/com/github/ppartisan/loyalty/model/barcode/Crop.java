package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;
import com.google.auto.value.AutoValue;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class Crop extends BitmapTransformation {

    private static final String ID = "com.github.ppartisan.loyalty.model.barcode.Crop";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    private final Bounds bounds;

    private Crop(Bounds bounds) {
        this.bounds = bounds;
    }

    static Crop create(Bounds bounds) {
        return new Crop(bounds);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return Bitmap.createBitmap(toTransform,bounds.x(),bounds.y(),bounds.width(),bounds.height());
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        final byte[] rectData = ByteBuffer.allocate(16)
                .putInt(bounds.x())
                .putInt(bounds.y())
                .putInt(bounds.width())
                .putInt(bounds.height())
                .array();
        messageDigest.update(rectData);
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID, Util.hashCode(bounds.hashCode()));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Crop) {
            final Crop other = (Crop) obj;
            return Objects.equals(other.bounds, this.bounds);
        }
        return false;
    }

    @AutoValue
    static abstract class Bounds {

        private static final Bounds INVALID = create(0,0,0,0);

        abstract int x();
        abstract int y();
        abstract int width();
        abstract int height();

        boolean isValid() {
            return width() > 0 && height() > 0;
        }

        private static Bounds create(int x, int y, int width, int height) {
            return new AutoValue_Crop_Bounds(x, y, width, height);
        }

        static Bounds from(@Nullable Rect rect) {
            if(rect == null) {
                return invalid();
            }
            return create(rect.left, rect.top, rect.width(), rect.height());
        }

        static Bounds invalid() {
            return INVALID;
        }
    }
}
