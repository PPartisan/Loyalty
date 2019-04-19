package com.github.ppartisan.loyalty;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_ALL_FORMATS;

@Module
abstract class ApplicationModule {
    @Binds abstract Context context(Application application);

    @Provides static SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides static ContentResolver contentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides static FirebaseVision firebaseVision() {
        return FirebaseVision.getInstance();
    }

    @Provides static FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector(FirebaseVision firebaseVision) {
        return firebaseVision.getVisionBarcodeDetector(firebaseVisionBarcodeDetectorOptions());
    }

    private static FirebaseVisionBarcodeDetectorOptions firebaseVisionBarcodeDetectorOptions() {
        return new FirebaseVisionBarcodeDetectorOptions.Builder().setBarcodeFormats(FORMAT_ALL_FORMATS).build();
    }
}