package com.example.controllers;

import com.example.mapper.UserMapperRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {
    private final UserMapperRepository userMapperRepository;

    @Autowired
    public FileController(UserMapperRepository userMapperRepository) {
        this.userMapperRepository = userMapperRepository;
    }

    @GetMapping("/file-list")
    @SneakyThrows
    public Map<String, Object> getFileList(@RequestParam("path") String path) {
        List<String> fileList = new ArrayList<>();
        Path dir = Paths.get(path);
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
        for (Path file : stream) {
            if (Files.isDirectory(file)) {
                fileList.add(file.getName(file.getNameCount() - 1).toString()); // 注意修改了这一行
            }
        }
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
        userMapperRepository.deleteUser();
        for (String key : result.keySet()) {
            // 获取当前键对应的值
            List<String> values = result.get(key);
            for(String str:values){
                userMapperRepository.createUser(path+"/"+str);
            }
        }
        return result;
    }

    private List<String> getFilesList(String path) {
        // 获取文件列表
        List<String> result = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (!folder.isDirectory()) {
            return result;
        }
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".doc") || file.getName().endsWith(".docx") ||
                        file.getName().endsWith(".txt") || file.getName().endsWith(".pdf"))) {
                    result.add(file.getName());
                }
            }
        }
        return result;
    }
}
