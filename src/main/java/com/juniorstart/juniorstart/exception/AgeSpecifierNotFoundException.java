package com.juniorstart.juniorstart.exception;

public class AgeSpecifierNotFoundException extends IllegalStateException {

    public AgeSpecifierNotFoundException() {
        super("Provided age format cannot be parsed! Should be ^(eq|neq|gt|gte|lt|lte|bt|)([0-9])+$");
    }
}
