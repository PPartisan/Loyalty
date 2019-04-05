package com.github.ppartisan.loyalty.wallet;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import dagger.android.support.DaggerAppCompatActivity;

import static com.github.ppartisan.loyalty.R.id.fab;
import static com.github.ppartisan.loyalty.R.id.toolbar;
import static com.github.ppartisan.loyalty.R.layout.activity_my_wallet;
import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class MyWalletActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_my_wallet);

        setSupportActionBar(findViewById(toolbar));

        findViewById(fab).setOnClickListener(MyWalletActivity::post);
    }

    private static void post(View v) {
        Snackbar.make(v, "ToDo", LENGTH_LONG).show();
    }

}
