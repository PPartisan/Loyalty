package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Rect;

import com.google.auto.value.AutoValue;

import java.util.stream.IntStream;

import androidx.annotation.Nullable;

@AutoValue
abstract class Bounds {

    private static final Bounds INVALID = create(0,0,0,0);

    abstract int x();
    abstract int y();
    abstract int width();
    abstract int height();

    boolean isValid() {
        return width() > 0 && height() > 0;
    }

    static Bounds create(int x, int y, int width, int height) {
        if(!areAllPositiveInts(x, y, width, height)) {
            return invalid();
        }
        return new AutoValue_Bounds(x, y, width, height);
    }

    static Bounds from(@Nullable Rect rect) {
        if(rect == null) {
            return invalid();
        }
        return create(rect.left, rect.top, rect.width(), rect.height());
    }

    private static boolean areAllPositiveInts(int... ints) {
        return IntStream.of(ints).allMatch(i -> i >= 0);
    }

    static Bounds invalid() {
        return INVALID;
    }
}
