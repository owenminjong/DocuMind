package com.documind.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class DocumentParserService {

    /**
     * PDF 파일에서 텍스트 추출
     */
    public String parsePdf(MultipartFile file) throws IOException {
        log.info("PDF 파싱 시작: {}", file.getOriginalFilename());

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            log.info("PDF 파싱 완료: {} 페이지, {} 글자",
                    document.getNumberOfPages(), text.length());

            return text;
        }
    }

    /**
     * Markdown 파일 텍스트 추출
     */
    public String parseMarkdown(MultipartFile file) throws IOException {
        log.info("Markdown 파싱 시작: {}", file.getOriginalFilename());

        String text = new String(file.getBytes());

        log.info("Markdown 파싱 완료: {} 글자", text.length());

        return text;
    }

    /**
     * 텍스트 파일 추출
     */
    public String parseText(MultipartFile file) throws IOException {
        log.info("Text 파싱 시작: {}", file.getOriginalFilename());

        String text = new String(file.getBytes());

        log.info("Text 파싱 완료: {} 글자", text.length());

        return text;
    }

    /**
     * 파일 타입에 따라 적절한 파서 선택
     */
    public String parse(MultipartFile file, String fileType) throws IOException {
        return switch (fileType.toUpperCase()) {
            case "PDF" -> parsePdf(file);
            case "MD", "MARKDOWN" -> parseMarkdown(file);
            case "TXT", "TEXT" -> parseText(file);
            default -> throw new IllegalArgumentException("지원하지 않는 파일 타입: " + fileType);
        };
    }
}