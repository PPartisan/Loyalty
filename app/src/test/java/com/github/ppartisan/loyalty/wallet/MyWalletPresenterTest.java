package com.github.ppartisan.loyalty.wallet;

import com.github.ppartisan.loyalty.model.barcode.DetectBarcode;
import com.github.ppartisan.loyalty.model.barcode.Image;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyWalletPresenterTest {

    @Mock MyWalletView view;
    @Mock SelectImage select;
    @Mock DetectBarcode barcode;

    private MyWalletPresenter presenter;

    @BeforeClass
    public static void setUpBeforeClass() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @AfterClass
    public static void tearDownAfterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MyWalletPresenter(view, select, barcode);
    }

    @Test
    public void whenAddBarcodeClicked_thenOpenImageSelection() {
        presenter.onAddBarcodeClicked();
        verify(select).send();
    }

    @Test
    public void whenUserSelectedImage_thenShowSelectedImage() {
        final String uri = "a_valid_uri_i_promise";
        final Image image = mock(Image.class);

        when(barcode.request(uri)).thenReturn(Single.just(image));

        presenter.onImageSelected(uri);
        verify(view).showSelectedImage(image);
    }
}