package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.CroppableImage;

interface MyWalletView {
    void showCropImage(CroppableImage image);
    void showBarcode(String path);
}
