package com.github.ppartisan.loyalty.model.barcode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DetectBarcodeTest {

    @Mock ImageRequest image;
    @Mock DetectionRequest detection;

    private TestObserver<Image> test;
    private DetectBarcode barcode;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        barcode = new DetectBarcode(image, detection);
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
    public void givenInvalidUri_whenRequest_thenError() {
        final String invalidUri = "invalid_uri";
        final Throwable exception = new RuntimeException();
        when(image.loadImage(invalidUri)).thenReturn(Single.error(exception));

        test = barcode.request(invalidUri).test();

        test.assertError(exception);
    }

    @Test
    public void givenImageContainsNoDetectableBarcode_whenRequest_thenReturnOriginalImage() {
        final Image original = mock(Image.class);
        when(image.loadImage(anyString())).thenReturn(Single.just(original));

        final Crop.Bounds bounds = mock(Crop.Bounds.class);
        when(bounds.isValid()).thenReturn(false);

        final DetectionRequest.Result result = mock(DetectionRequest.Result.class);
        when(result.bounds()).thenReturn(bounds);
        when(result.original()).thenReturn(original);

        when(detection.getBoundsForSingleBarcode(original)).thenReturn(Single.just(result));

        test = barcode.request("valid").test();

        test.assertValue(original);
    }

    @Test
    public void givenImageContainsDetectableBarcode_whenRequest_thenReturnCroppedImage() {
        final Image original = mock(Image.class);
        when(image.loadImage(anyString())).thenReturn(Single.just(original));

        final Crop.Bounds bounds = mock(Crop.Bounds.class);
        when(bounds.isValid()).thenReturn(true);

        final DetectionRequest.Result result = mock(DetectionRequest.Result.class);
        when(result.bounds()).thenReturn(bounds);
        when(result.original()).thenReturn(original);

        final Image cropped = mock(Image.class);
        when(image.crop(original, bounds)).thenReturn(cropped);

        when(detection.getBoundsForSingleBarcode(original)).thenReturn(Single.just(result));

        test = barcode.request("valid").test();

        test.assertValue(cropped);
    }
}