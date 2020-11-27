package ru.itis.utils;

import ru.itis.structures.LZ78Node;
import ru.itis.structures.LZ78Output;
import ru.itis.structures.Word;

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
//        intList.add(-1);
        return intList;
    }

    public List<List<Integer>> getIntsOfWords(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filepath));
        int symbol;
        List<List<Integer>> words = new ArrayList<>();
        List<Integer> word = new ArrayList<>();
        while ((symbol = reader.read()) != -1) {
            if ((symbol >= 1 && symbol <= 64) || (symbol >= 91 && symbol <= 96)) {
                if (word.size() > 0) {
                    words.add(word);
                }
                words.add(Collections.singletonList(symbol));
                word = new ArrayList<>();
            } else {
                word.add(symbol);
            }
        }
        return words;
    }

    public List<List<Integer>> getIntPacks(String filepath, int n) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filepath));
        int symbol;
        List<List<Integer>> words = new ArrayList<>();
        List<Integer> word;
        boolean flag = true;
        while (flag) {
            word = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                symbol = reader.read();
                if (symbol == -1) {
                    words.add(word);
                    flag = false;
                }
                word.add(symbol);
            }
            words.add(word);
        }
        return words;
    }

    public List<Word> getWords(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filepath));
        int symbol;
        List<Word> words = new ArrayList<>();
        List<Integer> word = new ArrayList<>();
        while ((symbol = reader.read()) != -1) {
            if (symbol >= 32 && symbol <= 64) {
                words.add(Word.builder().wordInts(word).build());
                words.add(Word.builder().wordInts(Collections.singletonList(symbol)).build());
                word = new ArrayList<>();
            }
        }
        return words;
    }

    public LZ78Output read7Z78(String filepath) throws IOException {
        int fileSize;
        int abcSize;
        int dSize;
        int alphabetB;
        int dictB;
        List<Integer> bits = new ArrayList<>();
        try (DataInputStream is = new DataInputStream(new FileInputStream(new File(filepath)))) {
            fileSize = is.readInt();
            abcSize = is.readInt();
            dSize = is.readInt();
            alphabetB = (int) (Math.log(abcSize) / Math.log(2)) + 1;
            dictB = (int) (Math.log(dSize) / Math.log(2)) + 1;
            int symbol;
            while ((symbol = is.read()) != -1) {
                for (char sym:
                        toBinary(symbol).toCharArray() ) {
                    bits.add(Integer.parseInt(String.valueOf(sym)));
                }
            }
        }

        Map<Integer, Integer> alphabetMap = new HashMap<>();
        for (int i = 0; i < (alphabetB + 32) * abcSize; i += (alphabetB + 32)) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 32; j++) {
                str.append(bits.get(i + j));
            }
            Integer sym = Integer.parseInt(str.toString(), 2);

            str = new StringBuilder();
            for (int j = 0; j < alphabetB; j++) {
                str.append(bits.get(i + 32 + j));
            }
            alphabetMap.put(Integer.parseInt(str.toString(), 2), sym);
        }

        List<LZ78Node> nodes = new ArrayList<>();
        for (int i = (alphabetB + 32) * abcSize; i < fileSize; i += (alphabetB + dictB)) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < dictB; j++) {
                str.append(bits.get(i + j));
            }
            Integer sym = Integer.parseInt(str.toString(), 2);

            str = new StringBuilder();
            for (int j = 0; j < alphabetB; j++) {
                str.append(bits.get(i + dictB + j));
            }
            nodes.add(LZ78Node.builder().pos(sym).next(Integer.parseInt(str.toString(), 2)).build());
        }
        return LZ78Output.builder().alphabetMap(alphabetMap).nodes(nodes).dictSize(dSize).alphabetSize(abcSize).build();
    }

    private String toBinary(int n) {
        StringBuilder binary = new StringBuilder();
        while (n > 0 ) {
            binary.append( n % 2 );
            n >>= 1;
        }
        int len = binary.length();
        for (int i = 0; i < 8 - len; i++) {
            binary.append(0);
        }
        return binary.reverse().toString();
    }
}
