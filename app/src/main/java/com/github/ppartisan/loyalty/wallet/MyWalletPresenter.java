package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.core.Presenter;
import com.github.ppartisan.loyalty.model.barcode.CroppableImage;
import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;
import com.github.ppartisan.loyalty.model.persistence.DeleteImage;
import com.google.auto.value.AutoValue;

import net.glxn.qrgen.android.QRCode;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

class MyWalletPresenter extends Presenter<MyWalletView> {

    private final SelectImage select;
    private final DeleteImage delete;
    private final DetectBarcode barcode;
    private final DelayedCropResult delayedCrop;


    MyWalletPresenter(MyWalletView view, SelectImage select, DeleteImage delete, DetectBarcode barcode, DelayedCropResult delayedCrop) {
        super(view);
        this.select = select;
        this.delete = delete;
        this.barcode = barcode;
        this.delayedCrop = delayedCrop;
    }

    void onAddBarcodeClicked() {
        select.send();
    }

    void onImageCropComplete(CropImageResult result) {
        delayedCrop.onNextCropResult(result);
    }

    void onImageSelected(String uri) {

//        final Single<CroppableImage> showCropImage =
//                barcode.request(uri).doOnSuccess(view::showCropImage);
//
//        final Disposable disposable =
//                Single.zip(showCropImage, delayedCrop.waitForCropResult(), CropResult::create)
//                        .subscribeOn(io())
//                        .observeOn(mainThread())
//                        .subscribe(result -> {
//                            delete.delete(result.tempFilePath());
//                            //todo - Persist crop info (uri and bounds);
//                        });

        final Disposable disposable = barcode.generate(uri)
                .subscribeOn(io())
                .observeOn(mainThread())
//                .doOnSuccess(path -> delete.delete(path.temp()))
                .subscribe(p -> view.showBarcode(p.temp()));
        addDisposable(disposable);
    }

    @AutoValue
    static abstract class CropResult {
        abstract String tempFilePath();
        abstract CropImageResult cropResult();

        private static CropResult create(CroppableImage image, CropImageResult cropResult) {
            return new AutoValue_MyWalletPresenter_CropResult(image.paths().temp(), cropResult);
        }
    }

}
