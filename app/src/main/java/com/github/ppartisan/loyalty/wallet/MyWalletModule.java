package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyWalletModule {

    @Provides static MyWalletPresenter presenter(
            MyWalletView view,
            SelectImage select,
            DetectBarcode request
    ) {
        return new MyWalletPresenter(view, select, request);
    }

    @Provides static SelectImage selectImage(MyWalletFragment fragment) {
        return new SelectImage(fragment);
    }

    @Module
    public static abstract class ViewModule {
        @Binds abstract MyWalletView view(MyWalletFragment fragment);
    }

}
