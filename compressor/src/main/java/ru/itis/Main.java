package ru.itis;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.itis.structures.LZ78Output;
import ru.itis.utils.BitsSaver;
import ru.itis.utils.FileReader;
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

        FileReader fileReader = new FileReader();
        BitsSaver bitsSaver = new BitsSaver();
        List<Integer> fileInts = fileReader.getIntArrayOfFile(mainArg.input); // Считываем посимвольно исходный файл

        LZ78 lz78 = new LZ78();

        LZ78Output result = lz78.encode(fileInts); // Кодируем с помощью LZ78



        List<Integer> bitsArray = lz78.toBitsArray(result); // Результат переводим в двоичный код

        bitsSaver.lz78Save(bitsArray, mainArg.output); // Сохраняем в файл

    }

}