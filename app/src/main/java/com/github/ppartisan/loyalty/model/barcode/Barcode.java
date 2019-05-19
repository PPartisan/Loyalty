package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Rect;

import com.google.auto.value.AutoValue;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.zxing.BarcodeFormat;

import java.util.EnumSet;

import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_AZTEC;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_CODABAR;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_CODE_128;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_CODE_39;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_CODE_93;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_DATA_MATRIX;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_EAN_13;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_EAN_8;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_ITF;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_PDF417;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_QR_CODE;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_UPC_A;
import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_UPC_E;

@AutoValue
abstract class Barcode {

    private static final Barcode INVALID = create("<invalid>", Format.UNKNOWN, -1, -1);

    abstract Format format();
    abstract String content();
    abstract int height();
    abstract int width();

    boolean isValid() {
        return !this.equals(INVALID);
    }

    static Barcode from(FirebaseVisionBarcode vision) {
        if(vision == null) return invalid();
        final Rect bounds = vision.getBoundingBox();
        if(bounds == null) return INVALID;
        return create(vision.getRawValue(), Format.from(vision), bounds.height(), bounds.width());
    }

    static Barcode create(String content, Format format, int height, int width) {
        return new AutoValue_Barcode(format, content, height, width);
    }

    static Barcode invalid() {
        return INVALID;
    }

    enum Format {

        AZTEC(BarcodeFormat.AZTEC, FORMAT_AZTEC),
        CODABAR(BarcodeFormat.CODABAR, FORMAT_CODABAR),
        CODE_128(BarcodeFormat.CODE_128, FORMAT_CODE_128),
        CODE_39(BarcodeFormat.CODE_39, FORMAT_CODE_39),
        CODE_93(BarcodeFormat.CODE_93, FORMAT_CODE_93),
        DATA_MATRIX(BarcodeFormat.DATA_MATRIX, FORMAT_DATA_MATRIX),
        EAN_13(BarcodeFormat.EAN_13, FORMAT_EAN_13),
        EAN_8(BarcodeFormat.EAN_8, FORMAT_EAN_8),
        ITF(BarcodeFormat.ITF, FORMAT_ITF),
        PDF_417(BarcodeFormat.PDF_417, FORMAT_PDF417),
        QR_CODE(BarcodeFormat.QR_CODE, FORMAT_QR_CODE),
        UPC_A(BarcodeFormat.UPC_A, FORMAT_UPC_A),
        UPC_E(BarcodeFormat.UPC_E, FORMAT_UPC_E),
        UNKNOWN(null, -1);

        private final BarcodeFormat zxing;
        private final int firebase;

        Format(BarcodeFormat zxing, int firebase) {
            this.zxing = zxing;
            this.firebase = firebase;
        }

        BarcodeFormat zxing() {
            return zxing;
        }

        int firebase() {
            return firebase;
        }

        boolean isValid() {
            return !UNKNOWN.equals(this);
        }

        private static Format from(FirebaseVisionBarcode barcode) {
            final int format = barcode.getFormat();
            return EnumSet.allOf(Format.class).stream()
                    .filter(f -> format == f.firebase())
                    .findAny()
                    .orElse(UNKNOWN);
        }
    }

}
