package com.example.controllers;

import com.example.mapper.UserMapperRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SearchController {
    private final UserMapperRepository userMapperRepository;

    @Autowired
    public SearchController(UserMapperRepository userMapperRepository) {
        this.userMapperRepository = userMapperRepository;
    }

    @GetMapping("/search")
    public List<SearchResult> searchFiles(@RequestParam String keyword) {
        List<String> list_path = userMapperRepository.findAllUsers();
        List<SearchResult> results = new ArrayList<>();
        if (!StringUtils.hasText(keyword) || list_path.isEmpty()) {
            return results;
        }
        for (String filePath : list_path) {
            File file = new File(filePath);
            if (file.isFile() && isSupportedFileType(file)) {
                SearchResult result = searchInFile(file, keyword);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        return results;
    }

    private boolean isSupportedFileType(File file) {
        String fileName = file.getName();
        return fileName.endsWith(".txt") || fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }

    @SneakyThrows
    private SearchResult searchInFile(File file, String keyword) {
        SearchResult result = null;
        String fileName = file.getName();
        // 处理文本文件
        if (fileName.endsWith(".txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyword)) {
                    if (result == null) {
                        result = new SearchResult(file.getAbsolutePath());
                    }
                    result.addMatch(new Match(lineNumber, line));
                }
                lineNumber++;
            }
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            FileInputStream fis = new FileInputStream(file);
            int lineWidth = 19; // 设置行宽，例如80个字符
            // 对旧版 Word 文件（.doc）进行处理
            if (fileName.endsWith(".doc")) {
                HWPFDocument document = new HWPFDocument(fis);
                Range range = document.getRange();
                int lineNumber = 1;
                String text = range.text();
                StringBuilder paragraphTextBuilder = new StringBuilder();
                for (char c : text.toCharArray()) {
                    paragraphTextBuilder.append(c);
                    if (paragraphTextBuilder.length() % lineWidth == 0 && c != '\n') {
                        paragraphTextBuilder.append('\n'); // 在行宽处插入换行符
                    }
                }
                String[] paragraphs = paragraphTextBuilder.toString().split("\\r\\n|\\r|\\n");
                for (String paragraph : paragraphs) {
                    if (paragraph.contains(keyword)) {
                        System.out.println(paragraph);
                        if (result == null) {
                            result = new SearchResult(file.getAbsolutePath());
                        }
                        result.addMatch(new Match(lineNumber, paragraph));
                    }
                    lineNumber++;
                }
            }
            // 对新版 Word 文件（.docx）进行处理
            else if (fileName.endsWith(".docx")) {
                int lineNumber = 1;
                XWPFDocument document = new XWPFDocument(fis);
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder paragraphTextBuilder = new StringBuilder();
                for (XWPFParagraph paragraph : paragraphs) {
                    stringBuilder.append(paragraph.getText());
                    stringBuilder.append("\n");
                }
                for (int i = 0; i < stringBuilder.length(); i++) {
                    char c = stringBuilder.charAt(i);
                    paragraphTextBuilder.append(c);
                    if (paragraphTextBuilder.length() % lineWidth == 0 && c != '\n') {
                        paragraphTextBuilder.append('\n'); // 在行宽处插入换行符
                    }
                }
                String[] lines = paragraphTextBuilder.toString().split("\\r\\n|\\r|\\n");
                for (String line : lines) {
                    if (line.contains(keyword)) {
                        if (result == null) {
                            result = new SearchResult(file.getAbsolutePath());
                        }
                        result.addMatch(new Match(lineNumber, line));
                    }
                    lineNumber++;
                }
            }
        }
        // 处理 PDF 文件
        else if (fileName.endsWith(".pdf")) {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\n");
            int lineNumber = 1;
            for (String line : lines) {
                if (line.contains(keyword)) {
                    if (result == null) {
                        result = new SearchResult(file.getAbsolutePath());
                    }
                    result.addMatch(new Match(lineNumber, line));
                }
                lineNumber++;
            }
        }
        return result;
    }
    @Data
    public static class SearchResult {
        private final String filePath;
        private final List<Match> matches;

        public SearchResult(String filePath) {
            this.filePath = filePath;
            this.matches = new ArrayList<>();
        }

        public void addMatch(Match match) {
            matches.add(match);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Match {
        private final int lineNumber;
        private final String lineContent;
    }
}