package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe testant que les methodes de CalculatorService repondent bien aux tests
 */
public class BatchCalculatorServiceNoMockTest {
    
    @Mock
    CalculatorService calculatorService;
    
    BatchCalculatorService batchCalculatorServiceNoMock;
    
    
    @BeforeEach
    public void init() {
        
        batchCalculatorServiceNoMock = new BatchCalculatorServiceImpl(new CalculatorServiceImpl(new Calculator(),
                new SolutionFormattedImpl()));
    }
    
    @Test
    public void givenOperationList_whenbatchCalcultate_thenReturnAnswerList() throws IOException, URISyntaxException {
        //GIVEN
        final Stream<String> operations = Arrays
                .asList("2 + 2", "5 - 4", "6 x 8", "9 / " + "3")
                .stream();
        
        // ArgumentCaptor est d'enregistrer les args utilisés lors d'un appel de mock. Ici, on veut capturer les modèles de
        // calcul utilisés pour appeler le service de calcul.
        final ArgumentCaptor<CalculationModel> vCalculationModelArgumentCaptor = ArgumentCaptor.forClass(CalculationModel.class);
        
        
        //WHEN
        final List<CalculationModel> resultsNoMock = batchCalculatorServiceNoMock.batchCalculate(operations);
        
        //THEN
        assertThat(resultsNoMock).extracting(CalculationModel::getSolution) //extractiong d'assertj transforme la liste de
                // results en liste de solutions, en appelant pour chaque item de la liste, la méthode getSolution()
                .containsExactly(4, 1, 48, 3);
    }
    
}
