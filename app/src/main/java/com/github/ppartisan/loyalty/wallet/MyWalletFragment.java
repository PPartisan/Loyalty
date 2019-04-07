package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.ppartisan.loyalty.R;
import com.github.ppartisan.loyalty.model.barcode.Image;
import com.github.ppartisan.loyalty.core.BaseFragment;
import com.github.ppartisan.loyalty.wallet.SelectImage.Result;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.github.ppartisan.loyalty.R.id.fab;
import static com.github.ppartisan.loyalty.R.id.toolbar;
import static com.github.ppartisan.loyalty.R.layout.fragment_my_wallet;

public class MyWalletFragment extends BaseFragment implements MyWalletView {

    @Inject MyWalletPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        final View view = inflater.inflate(fragment_my_wallet, parent, false);
        activity().ifPresent(activity -> activity.setSupportActionBar(view.findViewById(toolbar)));
        view.findViewById(fab).setOnClickListener(v -> presenter.onAddBarcodeClicked());
        return view;
    }

    @Override
    public void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);
        Result.create(request, result, data)
                .filter(Result::isSuccessful)
                .flatMap(Result::data)
                .map(Intent::getDataString)
                .ifPresent(presenter::onImageSelected);
    }

    @Override
    public void showSelectedImage(Image image) {
        root().ifPresent(v -> Glide.with(this)
                .load(image.bitmap())
                .into(v.<ImageView>findViewById(R.id.preview))
        );
    }

}
