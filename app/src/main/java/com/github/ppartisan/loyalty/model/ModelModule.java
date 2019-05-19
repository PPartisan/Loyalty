package com.github.ppartisan.loyalty.model;

import com.github.ppartisan.loyalty.model.barcode.BarcodeModule;

import dagger.Module;

@Module(includes = BarcodeModule.class)
public abstract class ModelModule {}
