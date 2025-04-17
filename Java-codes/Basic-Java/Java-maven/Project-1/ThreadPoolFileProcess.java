package com.javacode.fileprocessor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.odftoolkit.simple.TextDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ThreadPoolFileProcess {

    public static void main(String[] args) throws Exception {
        File folder = new File("/home/acro0nix/Programs/Java-maven/src/main/resources/");
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String text = readFileContent(file);
                if (text != null) {
                    saveTextToFile(file.getName(), text);
                }
            }
        }
    }

    private static String readFileContent(File file) {
        try {
            String name = file.getName().toLowerCase();
            if (name.endsWith(".pdf")) {
                return readPDF(file);
            } else if (name.endsWith(".odt")) {
                return readODT(file);
            } else if (name.endsWith(".csv")) {
                return readCSV(file);
            } else if (name.endsWith(".html") || name.endsWith(".htm")) {
                return readHTML(file);
            } else {
                System.out.println("Unsupported file: " + file.getName());
            }
        } catch (Exception e) {
            System.out.println("Error reading " + file.getName() + ": " + e.getMessage());
        }
        return null;
    }

    private static String readPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String readODT(File file) throws Exception {
        TextDocument odt = TextDocument.loadDocument(file);
        return odt.getContentRoot().getTextContent();
    }

    private static String readCSV(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath())); // Keep commas, newlines
    }

    private static String readHTML(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        return doc.text(); // Extracts readable text, ignores tags
    }

    private static void saveTextToFile(String originalFileName, String text) throws IOException {
        String outputName = originalFileName.replaceAll("\\s+", "_") + ".txt"; // replace spaces
        Files.write(Paths.get("/home/acro0nix/Programs/Java-maven/src/main/resources/target/", outputName), text.getBytes());
    }
}

