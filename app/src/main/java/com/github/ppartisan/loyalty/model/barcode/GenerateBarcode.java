package com.github.ppartisan.loyalty.model.barcode;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.glxn.qrgen.android.MatrixToImageWriter;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;

import static com.github.ppartisan.loyalty.model.barcode.BarcodeModule.ISO_8859_1;
import static com.google.zxing.EncodeHintType.CHARACTER_SET;
import static java.util.Collections.singletonMap;

class GenerateBarcode {

    private final Charset charset;

    @Inject
    GenerateBarcode(@Named(ISO_8859_1) Charset charset) {
        this.charset = charset;
    }

    Single<Image> generate(Barcode barcode) {
        return Single.fromCallable(() -> Image.create(MatrixToImageWriter.toBitmap(getBitMatrix(barcode))));
    }

    private BitMatrix getBitMatrix(Barcode barcode) throws WriterException, CharacterCodingException {
        return new MultiFormatWriter().encode(
                getEncodedString(barcode.content()),
                barcode.format().zxing(),
                barcode.width(),
                barcode.height(),
                singletonMap(CHARACTER_SET, charset.name())
        );
    }

    private String getEncodedString(String in) throws CharacterCodingException {
        final byte[] bytes = charset.newEncoder().encode(CharBuffer.wrap(in)).array();
        return new String(bytes, charset);
    }

}
