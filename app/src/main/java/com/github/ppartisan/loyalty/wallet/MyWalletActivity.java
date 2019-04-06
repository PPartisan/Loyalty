package com.github.ppartisan.loyalty.wallet;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;

import static com.github.ppartisan.loyalty.R.layout.activity_my_wallet;

public class MyWalletActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_my_wallet);
    }

}
