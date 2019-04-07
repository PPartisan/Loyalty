package com.github.ppartisan.loyalty.core;

import android.view.View;

import java.util.Optional;

import androidx.appcompat.app.AppCompatActivity;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    /**
     * @return If attached, an {@link Optional} containing the {@link AppCompatActivity} this
     * {@code Fragment} is attached to. Otherwise, {@link Optional#empty()}
     */
    protected Optional<AppCompatActivity> activity() {
        return Optional.ofNullable((AppCompatActivity)getActivity());
    }

    /**
     * @return If root view for this Fragment has been inflated, then an {@link Optional} containing
     * this Fragment's root view. Otherwise, {@link Optional#empty()}
     */
    protected Optional<View> root() {
        return Optional.ofNullable(getView());
    }

}
