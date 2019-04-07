package com.github.ppartisan.loyalty.model.settings;

import android.content.Context;

import com.google.auto.value.AutoValue;

import javax.inject.Inject;

import androidx.annotation.StringRes;

import static com.github.ppartisan.loyalty.R.string.key_preferred_padding;
import static com.github.ppartisan.loyalty.R.string.key_preferred_size;

class Values {

    private final Context context;

    @Inject
    Values(Context context) {
        this.context = context;
    }

    Value<Integer> imageSize() {
        return Value.create(getKey(key_preferred_size), 1200);
    }

    Value<Integer> imagePadding() {
        return Value.create(getKey(key_preferred_padding), 20);
    }

    private String getKey(@StringRes int id) {
        return context.getString(id);
    }

    @AutoValue
    static abstract class Value<T> {
        abstract String key();
        abstract T fallback();

        private static <T> Value<T> create(String key,T fallback) {
            return new AutoValue_Values_Value<>(key, fallback);
        }
    }
}
