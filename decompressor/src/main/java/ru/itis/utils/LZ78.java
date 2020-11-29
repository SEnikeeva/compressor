package ru.itis.utils;

import ru.itis.structures.LZ78Node;
import ru.itis.structures.LZ78Output;

import java.util.*;


public class LZ78 {

    public List<Integer> decode(LZ78Output lz78Output) {
        List<Integer> decoded = new ArrayList<>();
        List<List<Integer>> dict = new ArrayList<>();
        List<Integer> word = new ArrayList<>();
        Map<Integer, Integer> alphabetMap = lz78Output.getAlphabetMap();
        for (LZ78Node node : lz78Output.getNodes()) {
            if (node.getPos() == 0) { // Проверяем есть ли слово в словаре
                word = new ArrayList<>(Collections.singletonList(alphabetMap.get(node.getNext()))); // Если нет, очищаем лист и добавляем символ
            } else {
                word = new ArrayList<>(dict.get(node.getPos() - 1));  // Если есть, берем слово из словаря и добавляем следующий символ
                word.add(alphabetMap.get(node.getNext()));
            }
            dict.add(word); // Добавляем в словарь
            decoded.addAll(word); // Добавляем в результат
        }
        return decoded;
    }
}
