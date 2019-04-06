package com.github.ppartisan.loyalty.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class MyWalletPresenterTest {

    @Mock MyWalletView view;
    @Mock SelectImage select;

    private MyWalletPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MyWalletPresenter(view, select);
    }

    @Test
    public void whenAddBarcodeClicked_thenOpenImageSelection() {
        presenter.onAddBarcodeClicked();
        verify(select).send();
    }

    @Test
    public void whenUserSelectedImage_thenShowSelectedImage() {
        final String uri = "a_valid_uri_i_promise";
        presenter.onImageSelected(uri);
        verify(view).showSelectedImage(uri);
    }
}