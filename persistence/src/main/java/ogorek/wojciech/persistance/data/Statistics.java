package ogorek.wojciech.persistance.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistics {
    private Statistic<Double> mileageStatistics;
    private Statistic<BigDecimal> priceStatistics;
}
