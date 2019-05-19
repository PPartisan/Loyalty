package com.github.ppartisan.loyalty.model.barcode;

import java.nio.charset.Charset;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class BarcodeModule {

    static final String ISO_8859_1 = "ISO-8859-1";

    @Provides @Named(ISO_8859_1) static Charset charset() {
        return Charset.forName(ISO_8859_1);
    }

}
