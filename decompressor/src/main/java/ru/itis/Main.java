package ru.itis;

import ru.itis.structures.LZ78Node;
import ru.itis.structures.LZ78Output;
import ru.itis.utils.BitsSaver;
import ru.itis.utils.FileReader;
import ru.itis.utils.FileWriter;
import ru.itis.utils.HuffmanEncoder;
import ru.itis.utils.LZ78;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LZ78Test {
    public static void main(String[] args) throws IOException {
        String filepath = "voina_i_mir.txt";
//        String filepath = "lz78.dat";

        FileReader fileReader = new FileReader();
        BitsSaver bitsSaver = new BitsSaver();
        List<Integer> fileInts = fileReader.getIntArrayOfFile(filepath);

        LZ78 lz78 = new LZ78();

        LZ78Output result = lz78.encode(fileInts);


        System.out.println("symbols in raw: " + fileInts.size());
        System.out.println("nodes after lz78: " + result.getNodes().size());

//        System.out.println(Math.log(result.getDictSize()) / Math.log(2));
//        System.out.println(Math.log(result.getAlphabetSize()) / Math.log(2));
//        System.out.println(25 * result.getNodes().size());

        Set<Integer> posSet = new HashSet<>();
        Set<Integer> nextSet = new HashSet<>();
        for (LZ78Node node : result.getNodes()) {
            posSet.add(node.getPos());
            nextSet.add(node.getNext());
        }
        System.out.println("poses: " + posSet.size());
        System.out.println("nexts: " + nextSet.size());

        List<Integer> bitsArray = lz78.toBitsArray(result);

        bitsSaver.lz78Save(bitsArray, "lz78.dat");

        LZ78Output lz78Output = new FileReader().read7Z78("lz78.dat");
        System.out.println(result.getNodes().stream().map(LZ78Node::getPos).collect(Collectors.toList())
                .equals(lz78Output.getNodes().stream().map(LZ78Node::getPos).collect(Collectors.toList())));
//        System.out.println(result.getNodes().equals(lz78Output.getNodes()));
        List<Integer> decoded = lz78.decode(lz78Output);
        new FileWriter().writeDecompressed(decoded);
    }

}