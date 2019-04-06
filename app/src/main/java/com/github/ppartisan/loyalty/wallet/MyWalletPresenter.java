package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.util.Presenter;

class MyWalletPresenter extends Presenter<MyWalletView> {

    private final SelectImage select;

    MyWalletPresenter(MyWalletView view, SelectImage select) {
        super(view);
        this.select = select;
    }

    @Override
    protected void onAttached() {}

    @Override
    protected void onDetached() {}

    void onAddBarcodeClicked() {
        select.send();
    }

    void onImageSelected(String s) {
        view().showSelectedImage(s);
    }

    /*
    todo:
    - FAB click (Launch add new barcode)
    - Create new adapter item
    - Bind adapter item
     */

}
