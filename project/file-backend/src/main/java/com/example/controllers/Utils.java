package com.example.controllers;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Utils {

    private static final String FILE_ENCODING = "UTF-8";

    public static File[] getFiles(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        if (!file.isDirectory()) {
            throw new FileNotFoundException("Not a directory: " + path);
        }
        return file.listFiles();
    }

    public static File getFile(String path, String name) throws FileNotFoundException {
        File file = new File(path + File.separator + name);
        if (!file.exists()) {
            return null;
        }
        if (!file.isFile()) {
            throw new FileNotFoundException("Not a file: " + path + "\\" + name);
        }
        return file;
    }

    public static void copyFile(File source, OutputStream destination) throws IOException {
        try (InputStream input = new FileInputStream(source)) {
            IOUtils.copy(input, destination);
        }
    }

    public static String readFile(String path) throws IOException {
        Path file = Paths.get(path);
        byte[] encoded = Files.readAllBytes(file);
        return new String(encoded, FILE_ENCODING);
    }

    public static void writeFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), FILE_ENCODING))) {
            writer.write(content);
        }
    }
}