package com.github.ppartisan.loyalty.model.settings;

import android.content.SharedPreferences;

import com.github.ppartisan.loyalty.model.settings.Values.Value;

import javax.inject.Inject;

public class Settings {

    private final SharedPreferences prefs;
    private final Values values;

    @Inject
    Settings(SharedPreferences prefs, Values values) {
        this.prefs = prefs;
        this.values = values;
    }

    public int imageSize() {
        final Value<Integer> value = values.imageSize();
        return prefs.getInt(value.key(), value.fallback());
    }

}
