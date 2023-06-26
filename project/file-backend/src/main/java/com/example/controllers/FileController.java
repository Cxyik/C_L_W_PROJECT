package com.example.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

    @GetMapping("/file-list")
    public Map<String, Object> getFileList(@RequestParam("path") String path) {
        List<String> fileList = new ArrayList<>();
        Path dir = Paths.get(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    fileList.add(file.getName(file.getNameCount() - 1).toString()); // 注意修改了这一行
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.err.println(e);
        }
        System.out.println("File list: " + fileList);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("fileList", fileList);
        return resultMap;
    }

    @GetMapping("/files")
    public Map<String, List<String>> getFiles(@RequestParam(name = "path") String path) {
        // 处理path参数，获取文件列表
        List<String> files = getFilesList(path);
        // 将文件列表放入Map中返回
        Map<String, List<String>> result = new HashMap<>();
        result.put("fileList", files);
        return result;
    }

    private List<String> getFilesList(String path) {
        // 获取文件列表
        List<String> result = new ArrayList<>();
        File folder = new File(path);
        if (!folder.isDirectory()) {
            return result;
        }

        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && (file.getName().endsWith(".doc") || file.getName().endsWith(".docx") ||
                    file.getName().endsWith(".txt") || file.getName().endsWith(".pdf"))) {
                result.add(file.getName());
            }
        }

        return result;
    }
}
