package ru.itis.utils;


import java.io.*;
import java.util.*;

public class FileReader {
    public List<Integer> getIntArrayOfFile(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filepath));
        int symbol;
        List<Integer> intList = new ArrayList<>();
        while ((symbol = reader.read()) != -1) {
            intList.add(symbol);
        }
        return intList;
    }
}
