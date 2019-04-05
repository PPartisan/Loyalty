package com.github.ppartisan.loyalty.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ppartisan.loyalty.R;

import androidx.annotation.NonNull;
import dagger.android.support.DaggerFragment;

public class MyWalletFragment extends DaggerFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_my_wallet, parent, false);
    }
}
