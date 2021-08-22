package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.exception.BadRequestException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils() {}

    public static String readFileAsString(final String path) {
        try {
            ClassLoader classLoader = FileUtils.class.getClassLoader();
            File jsonFile = new File(classLoader.getResource(path).getFile());
            return new String((Files.readAllBytes((Paths.get(jsonFile.getAbsolutePath())))));
        } catch (Exception e) {
            throw new BadRequestException("Error reading file at " + path);
        }
    }
}
