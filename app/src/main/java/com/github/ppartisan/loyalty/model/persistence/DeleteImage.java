package com.github.ppartisan.loyalty.model.persistence;

import java.io.File;

import javax.inject.Inject;

public class DeleteImage {

    @Inject
    DeleteImage(){}

    public boolean delete(String file) {
        return new File(file).delete();
    }

}
