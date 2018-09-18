package com.serkan.packagesspring.exception;

public final class ProductNotFoundException extends Exception {
    private ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException notFound(String id) {
        return new ProductNotFoundException("No product returned from service -> id: [" + id+ "]");
    }
}
