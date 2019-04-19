package com.github.ppartisan.loyalty.model.barcode;

import android.graphics.Bitmap;
import android.net.Uri;

import com.github.ppartisan.loyalty.model.persistence.SaveImage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DetectBarcodeTest {

    @Mock ImageRequest image;
    @Mock DetectionRequest detection;
    @Mock SaveImage save;

    @Mock ImageRequest.Dimens dimens;

    @Mock Bounds bounds;

    @Mock ImageRequest.Image img;
    @Mock Bitmap bitmap;

    @Mock SaveImage.ImagePath path;

    private TestObserver<CroppableImage> test;
    private DetectBarcode barcode;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(image.findOriginalImageDimensions(anyString())).thenReturn(Single.just(dimens));

        when(img.bitmap()).thenReturn(bitmap);
        when(image.createScaledBitmap(anyString())).thenReturn(Single.just(img));

        when(detection.getBoundsForSingleBarcode(bitmap)).thenReturn(Single.just(bounds));

        when(save.save(bitmap)).thenReturn(Single.just(path));

        barcode = new DetectBarcode(image, detection, save);
    }

    @After
    public void tearDown() {
        Optional.ofNullable(test)
                .filter(disposable -> !disposable.isDisposed())
                .ifPresent(TestObserver::dispose);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullImageUri_whenRequest_thenThrowNPE() {
        barcode.request(null);
    }


    @Test
    public void whenRequest_thenReturnCroppaleImageWithOriginalImageDimensions() {
        when(dimens.height()).thenReturn(500);
        when(dimens.width()).thenReturn(500);
        when(image.findOriginalImageDimensions(anyString())).thenReturn(Single.just(dimens));

        test = barcode.request("").test();

        test.assertValueCount(1);
        final CroppableImage result = test.values().get(0);
        assertEquals(dimens.height(), result.originalHeight());
        assertEquals(dimens.width(), result.originalWidth());
    }

    @Test
    public void whenRequest_thenReturnCroppableImageWithScaledBitmapDimensions() {
        when(bounds.containerHeight()).thenReturn(200);
        when(bounds.containerWidth()).thenReturn(150);

        test = barcode.request("").test();

        test.assertValueCount(1);
        final CroppableImage result = test.values().get(0);
        assertEquals(bounds.containerHeight(), result.cropRegion().containerHeight());
        assertEquals(bounds.containerWidth(), result.cropRegion().containerWidth());
    }

    @Test
    public void whenRequest_thenReturnCroppableImageWithBoundsForCropArea() {
        when(bounds.x()).thenReturn(20);
        when(bounds.y()).thenReturn(20);
        when(bounds.width()).thenReturn(200);
        when(bounds.height()).thenReturn(100);

        test = barcode.request("").test();

        test.assertValueCount(1);
        final CroppableImage result = test.values().get(0);
        assertEquals(bounds.x(), result.cropRegion().x());
        assertEquals(bounds.y(), result.cropRegion().y());
        assertEquals(bounds.width(), result.cropRegion().width());
        assertEquals(bounds.height(), result.cropRegion().height());
    }

    @Test
    public void whenRequest_thenReturnCroppableImageWithTemporaryFilePathAndGalleryContentUri() {
        final Uri uri = mock(Uri.class);
        when(path.original()).thenReturn(uri);
        when(path.temp()).thenReturn("file:///temp/file/path.jpg");

        test = barcode.request("").test();

        test.assertValueCount(1);
        final CroppableImage result = test.values().get(0);
        assertEquals(path.original(), result.paths().original());
        assertEquals(path.temp(), result.paths().temp());
    }

    @Test
    public void givenErrorSavingFile_whenRequest_thenPropagateError() {
        when(save.save(bitmap)).thenReturn(Single.error(new RuntimeException()));

        test = barcode.request("").test();

        test.assertError(RuntimeException.class);
    }

    @Test
    public void givenErrorCalculatingBarcodeBounds_whenRequest_thenPropagateError() {
        when(detection.getBoundsForSingleBarcode(bitmap)).thenReturn(Single.error(new RuntimeException()));

        test = barcode.request("").test();

        test.assertError(RuntimeException.class);
    }
}