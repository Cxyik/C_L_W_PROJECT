package com.example.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DiskRootController {
    @PostMapping("/root-disks")
    public List<String> getRootDirectories(@RequestBody Map<String, String> requestBody) {
        String selectedDisk = requestBody.get("disk");
        File rootDirectory = new File(selectedDisk);
        File[] rootFiles = rootDirectory.listFiles();

        List<String> rootDirectories = new ArrayList<>();
        if (rootFiles != null) {
            for (File file : rootFiles) {
                if (file.isDirectory()) {
                    rootDirectories.add(file.getName());
                }
            }
        }
        System.out.println("Root Directory: " + rootDirectory.getAbsolutePath());
        System.out.println("Root Directories: " + rootDirectories);

        return rootDirectories;
    }
}