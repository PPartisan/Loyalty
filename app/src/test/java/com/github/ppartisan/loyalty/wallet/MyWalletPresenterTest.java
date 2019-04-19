package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.CroppableImage;
import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;
import com.github.ppartisan.loyalty.model.persistence.DeleteImage;
import com.github.ppartisan.loyalty.model.persistence.SaveImage;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyWalletPresenterTest {

    private static final TestScheduler SCHEDULER = new TestScheduler();

    @Mock MyWalletView view;
    @Mock SelectImage select;
    @Mock DeleteImage delete;
    @Mock DetectBarcode barcode;
    @Mock DelayedCropResult delayedCrop;

    @Mock CroppableImage croppableImage;

    @Mock SaveImage.ImagePath path;
    @Mock CropImageResult cropImageResult;

    private MyWalletPresenter presenter;

    @BeforeClass
    public static void setUpBeforeClass() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> SCHEDULER);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> SCHEDULER);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(croppableImage.paths()).thenReturn(path);
        when(path.temp()).thenReturn("");

        when(delayedCrop.waitForCropResult()).thenReturn(Single.just(cropImageResult));

        when(barcode.request(any())).thenReturn(Single.just(croppableImage));

        presenter = new MyWalletPresenter(view, select, delete, barcode, delayedCrop);
    }

    @Test
    public void whenAddBarcodeClicked_thenOpenImageSelection() {
        presenter.onAddBarcodeClicked();
        verify(select).send();
    }

    @Test
    public void whenUserSelectedImage_thenRequestBarcodeMatchForImageUri() {
        presenter.onImageSelected("valid_uri");
        verify(barcode).request("valid_uri");
    }

    @Test
    public void givenImageContainsParsableBarcode_whenUserSelectedImage_thenShowCroppedImage() {
        presenter.onImageSelected("valid_uri");
        SCHEDULER.advanceTimeBy(100, MILLISECONDS);
        verify(view).showCropImage(croppableImage);
    }

    @Test
    public void givenErrorParsingBarcode_whenUserSelectedImage_thenThrowException() {
        when(barcode.request(any())).thenReturn(Single.error(new RuntimeException("Test Error")));
        try {
            presenter.onImageSelected("valid_uri");
        } catch (Exception e) {
            assertEquals("Test Error", e.getMessage());
        }
    }

    @Test
    public void whenUserSelectedImage_thenWaitForCropResultBeforeDeletingTempFile() {
        when(delayedCrop.waitForCropResult()).thenReturn(
                Single.timer(5, SECONDS, SCHEDULER). map(ignore -> cropImageResult)
        );
        presenter.onImageSelected("valid_uri");

        verify(delete, never()).delete(any());
        SCHEDULER.advanceTimeBy(3, SECONDS);
        verify(delete, never()).delete(any());
        SCHEDULER.advanceTimeBy(3, SECONDS);

        verify(delete).delete(any());
    }
}