package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.ppartisan.loyalty.core.ActivityResultFactory;
import com.github.ppartisan.loyalty.databinding.ActivityCardPreviewBinding;

import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import dagger.android.support.DaggerAppCompatActivity;

import static com.github.ppartisan.loyalty.R.layout.activity_card_preview;

public class CardPreviewActivity extends DaggerAppCompatActivity {

    private static final int REQUEST_CODE = 33376;
    private static final String PATH_EXTRA = "path_extra";

    private ActivityCardPreviewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, activity_card_preview);

        path().ifPresent(this::loadPreview);

        binding.done.setOnClickListener(v -> respond(RESULT_OK));
        binding.cancel.setOnClickListener(v -> respond(RESULT_CANCELED));
    }

    private void respond(final int code) {
        final Intent response = path().map(Uri::parse)
                .map(CardPreviewActivity::wrapUriInIntent)
                .orElseGet(Intent::new);
        setResult(code, response);
        finish();
    }

    private Optional<String> path() {
        return Optional.ofNullable(getIntent()).map(intent -> intent.getStringExtra(PATH_EXTRA));
    }

    private void loadPreview(String path) {
        Glide.with(this)
                .load(path)
                .circleCrop()
                .into(binding.include.preview);

    }

    static void launch(Fragment fragment, String path) {
        final Intent intent = new Intent(fragment.getContext(), CardPreviewActivity.class);
        intent.putExtra(PATH_EXTRA,path);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    static ActivityResultFactory result() {
        return ActivityResultFactory.forRequestCode(REQUEST_CODE);
    }

    private static Intent wrapUriInIntent(Uri uri) {
        final Intent intent = new Intent();
        intent.setData(uri);
        return intent;
    }

}


