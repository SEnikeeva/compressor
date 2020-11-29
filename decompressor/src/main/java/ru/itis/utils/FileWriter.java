package ru.itis.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileWriter {

    public void writeDecompressed(List<Integer> words, String filename) throws IOException {
        StringBuilder stringWords = new StringBuilder();
        for (Integer i : words) {
            if (i != null) {
                stringWords.append((char) i.intValue());
            }
        }
        try (java.io.FileWriter fw = new java.io.FileWriter(new File(filename))){
            fw.write(stringWords.toString());
        }
    }





}
