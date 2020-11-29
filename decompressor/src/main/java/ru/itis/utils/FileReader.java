package ru.itis.utils;

import ru.itis.structures.LZ78Node;
import ru.itis.structures.LZ78Output;

import java.io.*;
import java.util.*;

public class FileReader {

    public LZ78Output read7Z78(String filepath) throws IOException {
        int fileSize;
        int abcSize;
        int dSize;
        int alphabetB;
        int dictB;
        List<Integer> bits = new ArrayList<>();
        try (DataInputStream is = new DataInputStream(new FileInputStream(new File(filepath)))) {
            fileSize = is.readInt(); // Считываем размер файла
            abcSize = is.readInt(); // размер алфавита
            dSize = is.readInt(); // размер словаря
            alphabetB = (int) (Math.log(abcSize) / Math.log(2)) + 1; // Вычисляем количество бит, которыми закодированы символы алфавита
            dictB = (int) (Math.log(dSize) / Math.log(2)) + 1; // Вычисляем количество бит, которыми закодированы позиции в словаре
            int symbol;
            while ((symbol = is.read()) != -1) { // Преобразуем файл в двоичный код
                for (char sym:
                        toBinary(symbol).toCharArray() ) {
                    bits.add(Integer.parseInt(String.valueOf(sym)));
                }
            }
        }

        Map<Integer, Integer> alphabetMap = new HashMap<>();
        for (int i = 0; i < (alphabetB + 32) * abcSize; i += (alphabetB + 32)) { // Считываем словарь кодов алфавита
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
        for (int i = (alphabetB + 32) * abcSize; i < fileSize; i += (alphabetB + dictB)) { // Считываем ноды
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
