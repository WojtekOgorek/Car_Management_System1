package ogorek.wojciech.persistance.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistic<T> {
    private T max;
    private T min;
    private T avg;
}
