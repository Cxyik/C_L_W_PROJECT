package com.example.controllers;



<<<<<<< HEAD
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
=======
import com.example.JDBCClass.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
>>>>>>> 226dcf06765855d23075194c79e0fe6c18aad6b8
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
<<<<<<< HEAD
import org.apache.poi.xwpf.usermodel.XWPFRun;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 226dcf06765855d23075194c79e0fe6c18aad6b8
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
<<<<<<< HEAD
=======

    UserRepository userRepository;

    @Autowired
    public SearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

>>>>>>> 226dcf06765855d23075194c79e0fe6c18aad6b8
    @GetMapping("/search")
    public List<SearchResult> searchFiles(@RequestParam String keyword) {
        List<String> list_path = userRepository.findAllUsers();
        List<SearchResult> results = new ArrayList<>();
<<<<<<< HEAD
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
=======
        if (!StringUtils.hasText(keyword) || list_path.isEmpty()) {
            return results;
        }
        for (String filePath : list_path) {
            File file = new File(filePath);
>>>>>>> 226dcf06765855d23075194c79e0fe6c18aad6b8
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
                    XWPFDocument document = new XWPFDocument(fis);
                    List<XWPFParagraph> paragraphs = document.getParagraphs();
                    StringBuilder sb = new StringBuilder();
                    StringBuilder paragraphTextBuilder = new StringBuilder();
                    int lineNumber = 1;
                    for (XWPFParagraph paragraph : paragraphs) {
                        sb.append(paragraph.getText());
                        sb.append("\n");
                    }
                    for (int i = 0; i < sb.length(); i++) {
                        char c = sb.charAt(i);
                        paragraphTextBuilder.append(c);
                        if (paragraphTextBuilder.length() % lineWidth == 0 && c != '\n') {
                            paragraphTextBuilder.append('\n'); // 在行宽处插入换行符
                        }
                    }
                    String[] lines = paragraphTextBuilder.toString().split("\\r\\n|\\r|\\n");
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

        public void show() {
            for (Match m : matches) {
                System.out.println(filePath + m.getString());
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
<<<<<<< HEAD
        public String getString(){
            return "结果：第"+lineNumber+"行，关键词:"+lineContent;
=======

        public String getString() {
            return "结果：第" + lineNumber + "行，关键词:" + lineContent;
>>>>>>> 226dcf06765855d23075194c79e0fe6c18aad6b8
        }
    }
}