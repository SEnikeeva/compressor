package ru.itis.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileWriter {
    public void write(Map<List<Integer>, List<Integer>> encodings, List<Integer> encodedWordsByBits) throws IOException {
        DataOutputStream os = new DataOutputStream(new FileOutputStream("output.dat"));
        for (int i = 0; i < encodedWordsByBits.size() % 8; i++) {
            encodedWordsByBits.add(0);
        }
        os.write(stringEncodings(encodings).getBytes());
        writeText(encodedWordsByBits, os);

    }

    public void writeFreq(Map<List<Integer>, Integer> frequencies, List<Integer> encodedWordsByBits) throws IOException {
        DataOutputStream os = new DataOutputStream(new FileOutputStream("output.dat"));
        for (int i = 0; i < encodedWordsByBits.size() % 8; i++) {
            encodedWordsByBits.add(0);
        }
        os.write((String.valueOf(encodedWordsByBits.size() % 8) + '\n').getBytes(StandardCharsets.UTF_8));
        os.write(stringFrequencies(frequencies).getBytes(StandardCharsets.UTF_8));
        writeText(encodedWordsByBits, os);

    }

    private void writeText(List<Integer> encodedWordsByBits, DataOutputStream os) throws IOException {
        for (int i = 0; i < encodedWordsByBits.size(); i += 8) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                str.append(encodedWordsByBits.get(i + j));
            }
            os.write(Integer.parseInt(str.toString(), 2));
        }
    }

    private String stringEncodings(Map<List<Integer>, List<Integer>> encodings) {
         StringBuilder stringEncodings = new StringBuilder();
        for (List<Integer> intWord : encodings.keySet()) {
            StringBuilder word = new StringBuilder();
            StringBuilder code = new StringBuilder();
            for (Integer i : intWord) {
                word.append((char) i.intValue());
            }
            for (Integer i : encodings.get(intWord)) {
                code.append(i.intValue());
            }
            stringEncodings.append(word.append("@").append(code).append(" "));
        }

        return stringEncodings.toString();
    }

    private String stringFrequencies(Map<List<Integer>, Integer> frequencies) {
        StringBuilder stringEncodings = new StringBuilder();
        stringEncodings.append(frequencies.size()).append('\n');
        for (List<Integer> intWord : frequencies.keySet()) {
            StringBuilder word = new StringBuilder();
            for (Integer i : intWord) {
                word.append((char) i.intValue());
            }
            stringEncodings.append(word.append("@").append(frequencies.get(intWord)).append("\n"));
        }

        return stringEncodings.toString();
    }

/*    public void writeDecompressed(List<List<Integer>> words) throws IOException {
        StringBuilder stringWords = new StringBuilder();
        for (List<Integer> intWord : words) {
            for (Integer i : intWord) {
                stringWords.append((char) i.intValue());
            }
        }
        try (java.io.FileWriter fw = new java.io.FileWriter(new File("outputDecompressed.txt"))){
            fw.write(stringWords.toString());
        }
    }*/

    public void writeDecompressed(List<Integer> words) throws IOException {
        StringBuilder stringWords = new StringBuilder();
        for (Integer i : words) {
            if (i != null) {
                stringWords.append((char) i.intValue());
            }
        }
        try (java.io.FileWriter fw = new java.io.FileWriter(new File("outputDecompressed.txt"))){
            fw.write(stringWords.toString());
        }
    }


    private byte[] intFrequencies(Map<List<Integer>, Integer> frequencies) {
        List<Integer> intFrequencies = new ArrayList<>();
        for (List<Integer> intWord : frequencies.keySet()) {
            intFrequencies.addAll(intWord);
            intFrequencies.add((int) ':');
            intFrequencies.add(frequencies.get(intWord));
        }
        byte[] bytes = new byte[intFrequencies.size()];
        int j = 0;
        for (Integer i : intFrequencies) {
            bytes[j] = (byte)i.intValue();
        }
        return bytes;

    }


}
