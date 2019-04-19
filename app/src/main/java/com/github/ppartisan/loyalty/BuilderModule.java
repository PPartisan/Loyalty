package com.github.ppartisan.loyalty;

import com.github.ppartisan.loyalty.wallet.CardPreviewActivity;
import com.github.ppartisan.loyalty.wallet.MyWalletActivity;
import com.github.ppartisan.loyalty.wallet.MyWalletFragment;
import com.github.ppartisan.loyalty.wallet.MyWalletModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class BuilderModule {
    @ContributesAndroidInjector abstract MyWalletActivity myWalletActivity();
    @ContributesAndroidInjector(modules = {
            MyWalletModule.class, MyWalletModule.ViewModule.class
    }) abstract MyWalletFragment myWalletActivityFragment();

    @ContributesAndroidInjector abstract CardPreviewActivity cardPreviewActivity();
}
