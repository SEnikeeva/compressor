package ru.itis;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import ru.itis.structures.LZ78Output;
import ru.itis.utils.FileReader;
import ru.itis.utils.FileWriter;
import ru.itis.utils.LZ78;


import java.io.IOException;
import java.util.List;


public class Main {
    @Parameter(names = "-i")
    private String input;

    @Parameter(names = "-o")
    private String output;
    public static void main(String[] args) throws IOException {
        Main mainArg = new Main();
        JCommander.newBuilder()
                .addObject(mainArg)
                .build()
                .parse(args);
        LZ78 lz78 = new LZ78();
        LZ78Output lz78Output = new FileReader().read7Z78(mainArg.input);
        List<Integer> decoded = lz78.decode(lz78Output);
        new FileWriter().writeDecompressed(decoded, mainArg.output);
    }

}