package com.example.controllers;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DiskController {
    @GetMapping("/disks")
    @SneakyThrows
    public String[] getDisks() {
        List<String> disks = new ArrayList<>();
        FileSystem fs = FileSystems.getDefault();
        for (FileStore store : fs.getFileStores()) {
            disks.add(store.toString());
        }
        return disks.toArray(new String[0]);
    }

}