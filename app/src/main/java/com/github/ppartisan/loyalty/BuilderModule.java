package com.github.ppartisan.loyalty;

import com.github.ppartisan.loyalty.wallet.MyWalletActivity;
import com.github.ppartisan.loyalty.wallet.MyWalletFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class BuilderModule {
    @ContributesAndroidInjector abstract MyWalletActivity myWalletActivity();
    @ContributesAndroidInjector abstract MyWalletFragment myWalletActivityFragment();
}
