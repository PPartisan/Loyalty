package com.github.ppartisan.loyalty;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class Loyalty extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }

}
