package ru.itis.structures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LZ78Node {
    private Integer pos; // Позиция в словаре
    private Integer next; // Следующий символ
}
