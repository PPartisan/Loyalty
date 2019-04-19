package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;

import com.github.ppartisan.loyalty.core.ActivityResultFactory;

import androidx.fragment.app.Fragment;

import static android.content.Intent.ACTION_GET_CONTENT;

/**
 * API for selecting an Image via a user-determined image-picker, and receiving the URI of the
 * selected image.
 *
 * The response will come via the {@link Fragment#onActivityResult(int, int, Intent)} callback,
 * which can be used to create a {@link ActivityResultFactory}
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

    static ActivityResultFactory result() {
        return ActivityResultFactory.forRequestCode(REQUEST_CODE);
    }

}
