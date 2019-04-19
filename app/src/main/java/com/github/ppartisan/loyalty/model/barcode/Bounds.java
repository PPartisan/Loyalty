package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.auto.value.AutoValue;

import java.util.stream.IntStream;

import androidx.annotation.Nullable;

/**
 * Represents an area of an image that contains a barcode.
 */
@AutoValue
public abstract class Bounds {

    private static final Bounds INVALID = create(0,0,0,0,0,0);

    abstract int containerHeight();
    abstract int containerWidth();
    abstract int x();
    abstract int y();
    abstract int width();
    abstract int height();

    boolean isValid() {
        return width() <= 0 && height() <= 0;
    }

    public Rect asRect() {
        return new Rect(x(), y(), x() + width(), y() + height());
    }

    /**
     * @param container {@link Rect} whose width and height encapsulate the size of the image
     * @return The Crop Bounds (x, y, width, height) in relation to the width and height of the
     * supplied {@code container}, as absolute values
     */
    public Rect asRelativeRect(Rect container) {
        return asRelativeRect(container.width(), container.height());
    }

    private Rect asRelativeRect(int containerWidth, int containerHeight) {
        final int x = (int)(percentX() * (float)containerWidth);
        final int y = (int)(percentY() * (float)containerHeight);
        final int width = (int)(percentWidth() * (float)containerWidth);
        final int height = (int)(percentHeight() * containerHeight);
        return new Rect(x, y, x + width, y + height);

    }

    private float percentX() {
        return ((float)x()/(float)containerWidth()) * 100F;
    }

    private float percentY() {
        return ((float)y()/(float)containerHeight()) * 100F;
    }

    private float percentWidth() {
        return ((float)width()/(float)containerWidth()) * 100F;
    }

    private float percentHeight() {
        return ((float)height()/(float)containerHeight()) * 100F;
    }

    private static Bounds create(int containerHeight, int containerWidth, int x, int y, int width, int height) {
        if(!areAllPositiveInts(x, y, width, height)) {
            return invalid();
        }
        return new AutoValue_Bounds(containerHeight, containerWidth, x, y, width, height);
    }

    static Bounds from(Bitmap bitmap, @Nullable Rect rect) {
        if(rect == null) {
            return invalid();
        }
        return create(bitmap.getHeight(), bitmap.getWidth(), rect.left, rect.top, rect.width(), rect.height());
    }

    public static Bounds create(Rect container, Rect crop) {
        return create(container.height(), container.width(), crop.left, crop.top, crop.width(), crop.height());
    }

    private static boolean areAllPositiveInts(int... ints) {
        return IntStream.of(ints).allMatch(i -> i >= 0);
    }

    public static Bounds invalid() {
        return INVALID;
    }
}
