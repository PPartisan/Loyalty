package com.github.ppartisan.loyalty.model.barcode;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.auto.value.AutoValue;
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

    Single<Result> getBoundsForSingleBarcode(final Image image) {
        return Single.fromCallable(() -> FirebaseVisionImage.fromBitmap(image.bitmap()))
                .map(detector::detectInImage)
                .map(DetectionRequest::awaitBarcodeDetectionResults)
                .map(DetectionRequest::mapFirstMatchToBounds)
                .map(bounds -> Result.create(image, bounds))
                .onErrorReturnItem(Result.create(image, Bounds.invalid()));
    }

    private static Bounds mapFirstMatchToBounds(List<FirebaseVisionBarcode> barcodes) {
        if(barcodes == null || barcodes.isEmpty()) {
            return Bounds.invalid();
        }

        return barcodes.stream().limit(1)
                .map(FirebaseVisionBarcode::getBoundingBox)
                .map(Bounds::from)
                .findAny()
                .orElseGet(Bounds::invalid);
    }

    private static List<FirebaseVisionBarcode> awaitBarcodeDetectionResults(Task<List<FirebaseVisionBarcode>> task)
            throws InterruptedException, ExecutionException, TimeoutException {
        return Tasks.await(task, TIMEOUT_SECONDS, SECONDS);
    }

    @AutoValue
    static abstract class Result {
        abstract Image original();
        abstract Bounds bounds();

        private static Result create(Image original, Bounds bounds) {
            return new AutoValue_DetectionRequest_Result(original, bounds);
        }
    }

}
