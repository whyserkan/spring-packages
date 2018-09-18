package com.serkan.packagesspring.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public final class ServiceTestHelper {
    private ServiceTestHelper() {
    }
    public static String read(Resource resource) throws IOException {
        return Files.readAllLines(Paths.get(resource.getURI()))
                .stream()
                .collect(Collectors.joining());
    }
}
