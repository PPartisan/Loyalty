package com.github.ppartisan.loyalty.model.persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.util.Log;

import com.google.auto.value.AutoValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.os.Environment.DIRECTORY_PICTURES;

public class SaveImage {

    private final Context context;

    @Inject
    SaveImage(Context context) {
        this.context = context;
    }

    private static final DateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("yyyMMdd_HHmmss", Locale.UK);

    private File createImageFile() throws IOException {
        final File directory = context.getExternalFilesDir(DIRECTORY_PICTURES);
        if(directory == null) {
            throw new NullPointerException("Could not access Pictures Directory");
        }
        return File.createTempFile(createFileName(), ".jpg", directory);
    }

    public Single<ImagePath> save(Bitmap bitmap) {
        final Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        return Single.fromCallable(this::createImageFile).flatMap(file -> {
            try(FileOutputStream out = new FileOutputStream(file)) {
                copy.compress(PNG, 100, out);
                out.flush();
            }
            return parsePaths(file);
        });
    }

    private Single<ImagePath> parsePaths(File file) {
        final String[] paths = new String[] { file.getAbsolutePath() };
        return Single.create(emitter -> {
            final OnScanCompletedListener listener =
                    (path, uri) -> emitter.onSuccess(ImagePath.create(path, uri));
            MediaScannerConnection.scanFile(context, paths, null, listener);
        });
    }

    private static String createFileName() {
        final Date now = Calendar.getInstance().getTime();
        return String.format("barcode_%s", TIMESTAMP_FORMAT.format(now));
    }

    @AutoValue
    public static abstract class ImagePath {
        /**
         * @return An absolute file path, pointing to a temporary image file that is a copied and
         * scaled version of the image referenced by {@link #original()}
         */
        public abstract String temp();

        public Uri tempUri() {
            return Uri.fromFile(new File(temp()));
        }

        /**
         * @return A content {@link Uri}, pointing to the original, unscaled image
         */
        public abstract Uri original();

        private static ImagePath create(String temp, Uri original) {
            return new AutoValue_SaveImage_ImagePath(temp, original);
        }
    }

}
