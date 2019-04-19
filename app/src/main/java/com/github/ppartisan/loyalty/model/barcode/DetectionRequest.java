package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Single;

import static java.util.concurrent.TimeUnit.SECONDS;

class DetectionRequest {

    private static final int TIMEOUT_SECONDS = 3;

    private final FirebaseVisionBarcodeDetector detector;

    @Inject
    DetectionRequest(FirebaseVisionBarcodeDetector detector) {
        this.detector = detector;
    }

    Single<Bounds> getBoundsForSingleBarcode(final Bitmap bitmap) {
        final Single<Rect> cropAreaAsRect = Single.fromCallable(() -> FirebaseVisionImage.fromBitmap(bitmap))
                .map(detector::detectInImage)
                .map(DetectionRequest::awaitBarcodeDetectionResults)
                .map(DetectionRequest::mapFirstMatchToRect);

        return  Single.zip(Single.fromCallable(() -> bitmap), cropAreaAsRect, Bounds::from)
                .onErrorReturnItem(Bounds.invalid());
    }

    private static Rect mapFirstMatchToRect(List<FirebaseVisionBarcode> barcodes) {
        final Rect empty = new Rect(0,0,0,0);
        if(barcodes == null || barcodes.isEmpty()) {
            return empty;
        }

        return barcodes.stream().limit(1)
                .map(FirebaseVisionBarcode::getBoundingBox)
                .findAny()
                .orElse(empty);
    }

    private static List<FirebaseVisionBarcode> awaitBarcodeDetectionResults(Task<List<FirebaseVisionBarcode>> task)
            throws InterruptedException, ExecutionException, TimeoutException {
        return Tasks.await(task, TIMEOUT_SECONDS, SECONDS);
    }

}
