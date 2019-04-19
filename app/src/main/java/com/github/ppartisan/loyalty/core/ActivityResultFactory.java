package com.github.ppartisan.loyalty.core;

import android.content.Intent;

import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ActivityResultFactory {

    private final int request;

    private ActivityResultFactory(int request){
        this.request = request;
    }

    public static ActivityResultFactory forRequestCode(int request) {
        return new ActivityResultFactory(request);
    }

    /**
     * Invoke this from {@link Fragment#onActivityResult(int, int, Intent)} and pass all arguments
     *
     * @param request The request code
     * @param result The result code
     * @param intent The Intent
     * @return An {@link Optional} containing the ActivityResult if the request code matches for
     * this Factory. Otherwise, returns {@link Optional#empty()}
     */
    public Optional<ActivityResult> create(int request, int result, @Nullable Intent intent) {
        return (request == this.request)
                ? Optional.of(new ActivityResult(result, intent))
                : Optional.empty();
    }


    public static class ActivityResult {

        private final int result;
        @Nullable
        private final Intent intent;

        private ActivityResult(int result, @Nullable Intent intent) {
            this.result = result;
            this.intent = intent;
        }

        public boolean isSuccessful() {
            return result == RESULT_OK;
        }

        public Optional<Intent> data() {
            return Optional.ofNullable(intent);
        }

    }

}
