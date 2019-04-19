package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.github.ppartisan.loyalty.core.ActivityResultFactory.ActivityResult;
import com.github.ppartisan.loyalty.core.BaseFragment;
import com.github.ppartisan.loyalty.databinding.FragmentMyWalletBinding;
import com.github.ppartisan.loyalty.model.barcode.CroppableImage;
import com.theartofdev.edmodo.cropper.CropImage;

import javax.inject.Inject;

import static com.github.ppartisan.loyalty.R.layout.fragment_my_wallet;

public class MyWalletFragment extends BaseFragment implements MyWalletView {

    @Inject MyWalletPresenter presenter;

    private FragmentMyWalletBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        binding = DataBindingUtil.inflate(inflater, fragment_my_wallet, parent, false);
        activity().ifPresent(activity -> activity.setSupportActionBar(binding.toolbar));
        binding.fab.setOnClickListener(v -> presenter.onAddBarcodeClicked());
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);

        SelectImage.result().create(request, result, data)
                .filter(ActivityResult::isSuccessful)
                .flatMap(ActivityResult::data)
                .map(Intent::getDataString)
                .ifPresent(presenter::onImageSelected);

        CropImageResult.create(request, result, data)
                .ifPresent(presenter::onImageCropComplete);

    }

    @Override
    public void showCropImage(CroppableImage image) {
        CropImage.activity(image.paths().tempUri())
                .setInitialCropWindowRectangle(image.cropRegion().asRect())
                .start(context(),this);
    }

}
