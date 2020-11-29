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
            if (node.getPos() == 0) {
                word = new ArrayList<>(Collections.singletonList(alphabetMap.get(node.getNext())));
            } else {
                word = new ArrayList<>(dict.get(node.getPos() - 1));
                word.add(alphabetMap.get(node.getNext()));
            }
            dict.add(word);
            decoded.addAll(word);
        }
        return decoded;
    }
}
