package com.example.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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

    @GetMapping("/search")
    public List<SearchResult> searchFiles(@RequestParam String keyword, @RequestParam String folderPath) {
        List<SearchResult> results = new ArrayList<>();

        if (!StringUtils.hasText(keyword) || !StringUtils.hasText(folderPath)) {
            return results;
        }

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return results;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return results;
        }

        for (File file : files) {
            if (file.isFile() && isSupportedFileType(file)) {
                SearchResult result = searchInFile(file, keyword);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        for(SearchResult s:results){
            s.show();
        }
        return results;
    }

    private boolean isSupportedFileType(File file) {
        String fileName = file.getName();
        return fileName.endsWith(".txt") || fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }

    private SearchResult searchInFile(File file, String keyword) {
        SearchResult result = null;
        String fileName = file.getName();

        if (fileName.endsWith(".txt")) {
            // 处理文本文件
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if (line.contains(keyword)) {
                        if (result == null) {
                            result = new SearchResult(file.getAbsolutePath());
                        }
                        result.addMatch(new Match(lineNumber, line));
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (fileName.endsWith(".doc")||fileName.endsWith(".docx")) {
            try (FileInputStream fis = new FileInputStream(file)) {
                // 对旧版 Word 文件（.doc）进行处理
                if (fileName.endsWith(".doc")) {
                    HWPFDocument document = new HWPFDocument(fis);
                    Range range = document.getRange();
                    int lineNumber = 1;
                    for (int i = 0; i < range.numParagraphs(); i++) {
                        Paragraph paragraph = range.getParagraph(i);
                        String paragraphText = paragraph.text();
                        String[] lines = paragraphText.split("\\r\\n|\\r|\\n");
                        for (String line : lines) {
                            if (line.contains(keyword)) {
                                System.out.println(line);
                                if (result == null) {
                                    result = new SearchResult(file.getAbsolutePath());
                                }
                                result.addMatch(new Match(lineNumber, line));
                            }
                            lineNumber++;
                        }
                    }
                }
                // 对新版 Word 文件（.docx）进行处理
                else if (fileName.endsWith(".docx")) {
                    XWPFDocument document = new XWPFDocument(fis);
                    List<XWPFParagraph> paragraphs = document.getParagraphs();
                    int lineNumber = 1;
                    for (XWPFParagraph paragraph : paragraphs) {
                        String paragraphText = paragraph.getText();
                        String[] lines = paragraphText.split("\\r\\n|\\r|\\n");
                        for (String line : lines) {
                            if (line.contains(keyword)) {
                                System.out.println(line);
                                if (result == null) {
                                    result = new SearchResult(file.getAbsolutePath());
                                }
                                result.addMatch(new Match(lineNumber, line));
                            }
                        }
                        lineNumber++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (fileName.endsWith(".pdf")) {
            // 处理 PDF 文件
            try (PDDocument document = PDDocument.load(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\n");
                int lineNumber = 1;
                for (String line : lines) {
                    System.out.println(line);
                    if (line.contains(keyword)) {
                        if (result == null) {
                            result = new SearchResult(file.getAbsolutePath());
                        }
                        result.addMatch(new Match(lineNumber, line));
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }



    public static class SearchResult {
        private String filePath;
        private List<Match> matches;

        public SearchResult(String filePath) {
            this.filePath = filePath;
            this.matches = new ArrayList<>();
        }

        public String getFilePath() {
            return filePath;
        }

        public List<Match> getMatches() {
            return matches;
        }

        public void addMatch(Match match) {
            matches.add(match);
        }
        public void show(){
            for(Match m:matches){
                System.out.println(filePath+m.getString());
            }
        }
    }

    public static class Match {
        private int lineNumber;
        private String lineContent;

        public Match(int lineNumber, String lineContent) {
            this.lineNumber = lineNumber;
            this.lineContent = lineContent;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getLineContent() {
            return lineContent;
        }

        public String getString(){
            return "结果：第"+lineNumber+"行，关键词:"+lineContent;
        }
    }
}