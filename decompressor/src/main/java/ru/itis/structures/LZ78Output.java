package ru.itis.structures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LZ78Output {
    private List<LZ78Node> nodes;
    private Integer alphabetSize;
    private Integer dictSize;
    private Map<Integer, Integer> alphabetMap;
}
