package com.github.ppartisan.loyalty.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ppartisan.loyalty.util.BaseFragment;
import com.github.ppartisan.loyalty.wallet.SelectImage.Result;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.github.ppartisan.loyalty.R.id.fab;
import static com.github.ppartisan.loyalty.R.id.toolbar;
import static com.github.ppartisan.loyalty.R.layout.fragment_my_wallet;
import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

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
    public void showSelectedImage(String uri) {
        root().ifPresent(v -> Snackbar.make(v, String.format("Image Uri:\n%s", uri), LENGTH_LONG).show());
    }

}
