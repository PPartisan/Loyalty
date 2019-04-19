package com.github.ppartisan.loyalty.core;

import android.content.Context;
import android.view.View;

import java.util.Optional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    /**
     * @return If attached, an {@link Optional} containing the {@link AppCompatActivity} this
     * {@code Fragment} is attached to. Otherwise, {@link Optional#empty()}
     */
    protected final Optional<AppCompatActivity> activity() {
        return Optional.ofNullable((AppCompatActivity)getActivity());
    }

    /**
     * @return The {@link Context} associated with this Fragment, or throw an Exception
     */
    @NonNull protected final Context context() {
        final Context context = getContext();
        if (context == null) throw new IllegalStateException();
        return context;
    }

    /**
     * @return If root view for this Fragment has been inflated, then an {@link Optional} containing
     * this Fragment's root view. Otherwise, {@link Optional#empty()}
     */
    protected final Optional<View> root() {
        return Optional.ofNullable(getView());
    }

}
