package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;
import com.github.ppartisan.loyalty.core.Presenter;

import io.reactivex.disposables.Disposable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

class MyWalletPresenter extends Presenter<MyWalletView> {

    private final SelectImage select;
    private final DetectBarcode barcode;

    MyWalletPresenter(MyWalletView view, SelectImage select, DetectBarcode barcode) {
        super(view);
        this.select = select;
        this.barcode = barcode;
    }

    void onAddBarcodeClicked() {
        select.send();
    }

    void onImageSelected(String uri) {
        final Disposable disposable = barcode.request(uri)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(view::showSelectedImage);
        addDisposable(disposable);
    }

    /*
    todo:
    - FAB click (Launch add new barcode)
    - Create new adapter item
    - Bind adapter item
     */

}
