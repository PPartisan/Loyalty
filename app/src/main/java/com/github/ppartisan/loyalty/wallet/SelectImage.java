package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;

import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;

/**
 * API for selecting an Image via a user-determined image-picker, and receiving the URI of the
 * selected image.
 *
 * The response will come via the {@link Fragment#onActivityResult(int, int, Intent)} callback,
 * which can be used to create a {@link SelectImage.Result}
 */
class SelectImage {

    //Can't use Resource ids for this unfortunately, as may fall out of valid range
    private static final int REQUEST_CODE = 50318;

    private final Fragment host;

    SelectImage(Fragment host) {
        this.host = host;
    }

    void send() {
        host.startActivityForResult(pickImageIntent(), REQUEST_CODE);
    }

    private static Intent pickImageIntent() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(ACTION_GET_CONTENT);
        return intent;
    }

    static class Result {

        private final int result;
        @Nullable
        private final Intent intent;

        private Result(int result, @Nullable Intent intent) {
            this.result = result;
            this.intent = intent;
        }

        /**
         * Invoke this from {@link Fragment#onActivityResult(int, int, Intent)} and pass all arguments
         *
         * @param request The request code
         * @param result The result code
         * @param intent The Intent
         * @return An {@link Optional} containing the Result if the request code matches that for
         * {@link SelectImage}. Otherwise, returns {@link Optional#empty()}
         */
        static Optional<Result> create(int request, int result, @Nullable Intent intent) {
            return (request == REQUEST_CODE)
                    ? Optional.of(new Result(result, intent))
                    : Optional.empty();
        }

        boolean isSuccessful() {
            return result == RESULT_OK;
        }

        Optional<Intent> data() {
            return Optional.ofNullable(intent);
        }

    }

}
