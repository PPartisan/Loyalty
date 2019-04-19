package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;
import com.github.ppartisan.loyalty.model.persistence.DeleteImage;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.processors.PublishProcessor;

@Module
public abstract class MyWalletModule {

    @Provides static MyWalletPresenter presenter(
            MyWalletView view,
            SelectImage select,
            DeleteImage delete,
            DetectBarcode request,
            DelayedCropResult delayCrop
    ) {
        return new MyWalletPresenter(view, select, delete, request, delayCrop);
    }

    @Provides static DelayedCropResult delayedCropResult() {
        return new DelayedCropResult(PublishProcessor.create());
    }

    @Provides static SelectImage selectImage(MyWalletFragment fragment) {
        return new SelectImage(fragment);
    }

    @Module
    public static abstract class ViewModule {
        @Binds abstract MyWalletView view(MyWalletFragment fragment);
    }

}
