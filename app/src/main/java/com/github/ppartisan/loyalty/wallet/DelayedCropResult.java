package com.github.ppartisan.loyalty.wallet;

import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

class DelayedCropResult {

    private final PublishProcessor<CropImageResult> imageCropResult;

    DelayedCropResult(PublishProcessor<CropImageResult> imageCropResult) {
        this.imageCropResult = imageCropResult;
    }

    Single<CropImageResult> waitForCropResult() {
        return imageCropResult.take(1).singleOrError();
    }

    void onNextCropResult(CropImageResult result) {
        imageCropResult.onNext(result);
    }

}
