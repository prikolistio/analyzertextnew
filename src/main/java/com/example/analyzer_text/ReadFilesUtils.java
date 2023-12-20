package com.example.analyzer_text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.List;

/**
 * Класс объединяет методы, отвечающие за чтение файлов разных форматов: txt, doc и docx
 */
public class ReadFilesUtils {

    private static Logger log = LogManager.getLogger(ReadFilesUtils.class);

    /**
     * Метод для чтения файлов с расширением .txt
     *
     * @param path путь до файла
     * @param dictUtils объект класса DictUtils, с помощью которого проверяются вхождения ключевых слов
     */
    public static void readTxtFile(String path, DictUtils dictUtils) {
        log.debug("Reading txt file " + path);
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                String[] line = reader.readLine().replaceAll("[.,!?(){}\\[\\]\\-\\\\|/\"'_=$@<>:;*&%]", "")
                        .split("\\s+");
                for (String word : line) {
                    dictUtils.checkDict(word);
                }
            }
        } catch (IOException e) {
            log.error("Exception while reading txt file " + path);
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для чтения файлов с расширением .docx
     *
     * @param path путь до файла
     * @param dictUtils объект класса DictUtils, с помощью которого проверяются вхождения ключевых слов
     */
    public static void readDocxFile(String path, DictUtils dictUtils) {
        log.debug("Reading docx file " + path);
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(path))) {
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph para : paragraphs) {
                String[] line = para.getParagraphText().replaceAll("[.,!?(){}\\[\\]\\-\\\\|/\"'_=$@<>:;*&%]", "")
                        .split("\\s+");
                for (String word : line) {
                    dictUtils.checkDict(word);
                }
            }
        } catch (Exception e) {
            log.error("Exception while reading docx file " + path);
            e.printStackTrace();
        }
    }

    /**
     * Метод для чтения файлов с расширением .doc
     *
     * @param path путь до файла
     * @param dictUtils объект класса DictUtils, с помощью которого проверяются вхождения ключевых слов
     */
    public static void readDocFile(String path, DictUtils dictUtils)  {
        log.debug("Reading doc file " + path);
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(path))) {

            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);
            String[] paragraphs = extractor.getParagraphText();

            for (String paragraph : paragraphs) {
                String[] words = paragraph.replaceAll("[.,!?(){}\\[\\]\\-\\\\|/\"'_=$@<>:;*&%]", "")
                        .split("\\s+");
                for (String word : words) {
                    dictUtils.checkDict(word);
                }
            }
        } catch (Exception e) {
            log.error("Exception while reading doc file " + path);
            e.printStackTrace();
        }
    }
}
