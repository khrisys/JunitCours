package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BatchCalculatorServiceTest {
    
    BatchCalculatorService batchCalculatorService;
    
    @BeforeEach
    public void init() {
        batchCalculatorService = new BatchCalculatorServiceImpl(new CalculatorServiceImpl(new Calculator(),
                new SolutionFormattedImpl()));
    }
    
    @Test
    public void givenOperationList_whenbatchCalcultate_thenReturnAnswerList() {
        //GIVEN
        final Stream<String> operations = Arrays
                .asList("2 + 2", "5 - 4", "6 x 8", "9 / " + "3")
                .stream();
        
        //WHEN
        final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);
        
        //THEN
        Assertions
                .assertThat(results)
                .extracting(CalculationModel::getSolution)
                .containsExactly(4, 1, 48, 3);
    }
}
