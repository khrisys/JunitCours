package com.openclassrooms.testing.calcul.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * L'annotation @Spy et la methode spy()  vous permettent d’utiliser des instances réelles d’une classe, mais sur lesquelles vous
 * pouvez utiliser when() et verify(). Ce sont des mocks partiels. Cela se révèle nécessaire lorsque vos classes contiennent
 * des méthodes avec le modificateur final.
 */
@ExtendWith(MockitoExtension.class)
public class StatisticsCalculatorTest {
    
    @Spy
    IntSummaryStatistics summaryStatistics = new IntSummaryStatistics();
    
    StatisticsCalculator underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new StatisticsCalculator(summaryStatistics);
    }
    
    @Test
    public void average_shouldSample_allIntegersProvided() {
        final ArgumentCaptor<Integer> sampleCaptor = ArgumentCaptor.forClass(Integer.class);
        final List<Integer> samples = Arrays.asList(2, 8, 5, 3, 7);
        
        underTest.average(samples);
        verify(summaryStatistics, times(samples.size())).accept(sampleCaptor.capture());
        
        final List<Integer> capturedList = sampleCaptor.getAllValues();
        assertThat(capturedList).containsExactly(samples.toArray(new Integer[0]));
    }
    
    @Test
    public void average_shouldReturnTheMean_ofAListOfIntegers() {
        final List<Integer> samples = Arrays.asList(2, 8, 5, 3, 7);
        final Integer result = underTest.average(samples);
        
        assertThat(result).isEqualTo(5);
    }
}