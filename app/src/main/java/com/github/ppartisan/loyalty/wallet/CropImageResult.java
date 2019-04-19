package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;
import android.graphics.Rect;

import com.github.ppartisan.loyalty.core.ActivityResultFactory;
import com.github.ppartisan.loyalty.model.barcode.Bounds;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Optional;

import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;

class CropImageResult {

    private final CropImage.ActivityResult result;
    private final int code;

    private CropImageResult(CropImage.ActivityResult result, int code) {
        this.result = result;
        this.code = code;
    }

    static Optional<CropImageResult> create(int request, int result, @Nullable Intent data) {
        if(request == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            return ActivityResultFactory.forRequestCode(request)
                    .create(request, result, data)
                    .map(res -> {
                        final CropImage.ActivityResult ar = CropImage.getActivityResult(data);
                        return new CropImageResult(ar, result);
                    });
        } else {
            return Optional.empty();
        }
    }

    boolean isSuccessful() {
        return code == RESULT_OK;
    }

    boolean isCancelled() {
        return code == RESULT_CANCELED;
    }

    Bounds bounds() {
        return (code == RESULT_OK) ? createBounds() : Bounds.invalid();
    }

    String path() {
        return result.getOriginalUri().toString();
    }

    private Bounds createBounds() {
        final Rect imageRect = result.getWholeImageRect();
        final Rect cropRect = result.getCropRect();
        return Bounds.create(imageRect, cropRect);
    }

    Optional<Exception> error() {
        switch (code) {
            case RESULT_OK:
                return Optional.empty();
            case CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                return Optional.of(result.getError());
            default:
                return Optional.of(new RuntimeException(String.format("Error code '%d'", code)));
        }
    }

}
