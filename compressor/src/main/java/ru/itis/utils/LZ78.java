package ru.itis.utils;

import ru.itis.structures.LZ78Node;
import ru.itis.structures.LZ78Output;

import java.util.*;

import static java.util.Arrays.asList;

public class LZ78 {
    public LZ78Output encode(List<Integer> fileInts) {
        Set<Integer> alphabet = new HashSet<>(); // Алфавит символов, которые встречаются в файле
        List<Integer> buffer = new ArrayList<>(); // Буффер, в котором "наращиваем" слова
        Map<List<Integer>, Integer> dict = new HashMap<>(); // Словарь слов, которые встретились до этого
        List<LZ78Node> result = new ArrayList<>(); // Результат - список нод и дополнительные сведения о файле
        for (Integer fileInt : fileInts) { // Проходимся по символам файла
            List<Integer> tmp = new ArrayList<>(buffer);
            tmp.add(fileInt);
            if (dict.containsKey(tmp)) { // Проверяем есть ли слово в словаре
                buffer.add(fileInt); // Если есть, просто наращиваем слово
            } else {
                result.add(LZ78Node.builder() // Иначе, создаем ноду и добавляем в результат и словарь
                        .pos(dict.getOrDefault(buffer, 0))
                        .next(fileInt)
                        .build());
                alphabet.add(fileInt);
                dict.put(tmp, dict.size() + 1);
                buffer = new ArrayList<>(); // Очищаем буффер
            }
        }
        if (buffer.size() > 0) { // Если в конце буффер остался непустым
            // мы создаем ноду с ссылкой на позицию в словаре (потому что он там точно есть),
            // предварительно убрав последний символ, который записываем как следующий символ
            Integer lastSymbol = buffer.remove(buffer.size() - 1);
                result.add(LZ78Node.builder()
                    .pos(dict.getOrDefault(buffer, 0))
                    .next(lastSymbol)
                    .build());
        }

        return LZ78Output.builder()
                .alphabetSize(alphabet.size())
                .dictSize(dict.keySet().size())
                .nodes(result)
                .alphabetMap(encodeAlphabet(result, alphabet))
                .build();
    }

    private Map<Integer, Integer> encodeAlphabet(List<LZ78Node> nodes, Set<Integer> alphabet) { // Даем символам из алфавита новые укороченные коды
        Map<Integer, Integer> encodingMap = new HashMap<>();
        int code = 0;
        for (Integer next : alphabet) {
            encodingMap.put(next, code);
            code++;
        }
        return encodingMap;
    }

    public List<Integer> toBitsArray(LZ78Output lz78Output) { // Результат метода encode преобразуем в двоичный код, который далее будем записывать в файл
        int dictB = (int) (Math.log(lz78Output.getDictSize()) / Math.log(2)) + 1; // Вычисляем количество битов, необходимое для кодирование позиции в словаре
        int alphabetB = (int) (Math.log(lz78Output.getAlphabetSize()) / Math.log(2)) + 1; // Вычисляем количество битов, необходимое для кодирование символа алфавита
        List<Integer> mapBitsArray = mapToBitsArray(lz78Output.getAlphabetMap(), alphabetB); // Преобразуем словарь (код символа алфавита -> укороченный код) в двоичный код
        List<Integer> textBitsArray = toBitsArray(lz78Output.getNodes(), dictB, alphabetB, lz78Output.getAlphabetMap()); // Преобразуем сам закодированный текст в двоичный код
        List<Integer> sizesBitsArray = intsToBitsArray(asList(mapBitsArray.size() + textBitsArray.size(), // Преобразуем информацию об общем количестве бит в файле и
                lz78Output.getAlphabetSize(), lz78Output.getDictSize())); // о количестве бит для кодирования позиции и символа в двоичный код
        List<Integer> bitsArray = new ArrayList<>(sizesBitsArray); // Объединяем все двоичные представления в один
        bitsArray.addAll(mapBitsArray);
        bitsArray.addAll(textBitsArray);
        return bitsArray;
    }

    private List<Integer> mapToBitsArray(Map<Integer, Integer> alphabetMap, int alphabetB) { // Метод преобразования словаря кодов алфавита в двоичный код
        List<Integer> bits = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : alphabetMap.entrySet()) {
            bits.addAll(intToBitsArray(entry.getKey()));
            bits.addAll(intToBitsArray(entry.getValue(), alphabetB));
        }
        return bits;
    }

    private List<Integer> intToBitsArray(int int_) { // Метод перевода int в двоичный код
        List<Integer> bits = new ArrayList<>();
        int tmp = int_;
        for (int i = 0; i < 32; i++) {
            int div = powOfTwo(31 - i);
            bits.add(tmp / div);
            tmp = tmp % div;
        }

        return bits;
    }

    private List<Integer> intToBitsArray(int int_, int size) { // Метод перевода int в двоичный код с обрезанием количества бит до size
        List<Integer> bits = new ArrayList<>();
        int tmp = int_;
        for (int i = 0; i < size; i++) {
            int div = powOfTwo(size - 1 - i);
            bits.add(tmp / div);
            tmp = tmp % div;
        }

        return bits;
    }

    private List<Integer> intsToBitsArray(List<Integer> ints) { // Метод перевода массива Integer в массив двоичных представлений
        List<Integer> bits = new ArrayList<>();
        for (Integer int_ : ints) {
            int tmp = int_;
            for (int i = 0; i < 32; i++) {
                int div = powOfTwo(31 - i);
                bits.add(tmp / div);
                tmp = tmp % div;
            }
        }
        return bits;
    }

    private List<Integer> toBitsArray(List<LZ78Node> nodes, int dictB, int alphabetB, Map<Integer, Integer> alphabetMap) { // Метод перевода массива нод в двоичное представление
        List<Integer> bitsArray = new ArrayList<>();
        for (LZ78Node node : nodes) {
            int d = node.getPos();
            for (int i = 0; i < dictB; i++) {
                int div = powOfTwo(dictB - i - 1);
                bitsArray.add(d / div);
                d = d % div;
            }
            if (node.getNext() != null) {
                int a = alphabetMap.get(node.getNext());
                for (int i = 0; i < alphabetB; i++) {
                    int div = powOfTwo(alphabetB - i - 1);
                    bitsArray.add(a / div);
                    a = a % div;
                }
            }
        }
        return bitsArray;
    }

    private int powOfTwo(int pow) { // Степени двойки
        int p = 1;
        for (int i = 0; i < pow; i++) {
            p = p * 2;
        }
        return p;
    }

}
