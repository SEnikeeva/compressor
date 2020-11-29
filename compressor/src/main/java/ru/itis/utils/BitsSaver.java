package ru.itis.utils;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BitsSaver {
    public void lz78Save(List<Integer> bitsArray, String filepath) throws IOException {
        DataOutputStream os = new DataOutputStream(new FileOutputStream(filepath));
        writeText(os, bitsArray);
    }


    private void writeText(DataOutputStream os, List<Integer> encodedWordsByBits) throws IOException {
        for (int i = 0; i < encodedWordsByBits.size() % 8; i++) { // Так как минимальной единицей, которую можно записать в файл
            // является байт, дополняем двоичное представление нулями до количества, кратного 8
            encodedWordsByBits.add(0);
        }
        for (int i = 0; i < encodedWordsByBits.size(); i += 8) { // Записываем двоичное представление в виде байтов в файл
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                str.append(encodedWordsByBits.get(i + j));
            }
            os.write(Integer.parseInt(str.toString(), 2));
        }
    }
}
