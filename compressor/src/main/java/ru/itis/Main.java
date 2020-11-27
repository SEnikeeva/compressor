package ru.itis;

import com.beust.jcommander.JCommander;
import ru.itis.structures.LZ78Output;
import ru.itis.utils.BitsSaver;
import ru.itis.utils.FileReader;
import ru.itis.utils.LZ78;

import java.io.IOException;
import java.util.List;

public class LZ78Test {
    public static void main(String[] args) throws IOException {
        Main mainArg = new Main();
        JCommander.newBuilder()
                .addObject(mainArg)
                .build()
                .parse(args);
        String filepath = "voina_i_mir.txt";

        FileReader fileReader = new FileReader();
        BitsSaver bitsSaver = new BitsSaver();
        List<Integer> fileInts = fileReader.getIntArrayOfFile(filepath);

        LZ78 lz78 = new LZ78();

        LZ78Output result = lz78.encode(fileInts);



        List<Integer> bitsArray = lz78.toBitsArray(result);

        bitsSaver.lz78Save(bitsArray, "lz78.dat");

    }

}