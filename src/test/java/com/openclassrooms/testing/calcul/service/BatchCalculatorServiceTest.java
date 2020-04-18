package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.times;

/**
 * Classe testant que les methodes de CalculatorService repondent bien aux tests, et test de chacune des methodes addition,
 * sub, multiplication, et division avec assertion grace au fait que les resultats correpodent aux operations effectuées (par
 * ex 2 + 2 = 4, ou 9 / 3 = 3)
 */
@ExtendWith(MockitoExtension.class)
public class BatchCalculatorServiceTest {
    
    @Mock
    CalculatorService calculatorService;
    
    BatchCalculatorService batchCalculatorServiceNoMock;
    
    BatchCalculatorService batchCalculatorService;
    
    @BeforeEach
    public void init() {
        
        batchCalculatorServiceNoMock = new BatchCalculatorServiceImpl(new CalculatorServiceImpl(new Calculator(),
                new SolutionFormattedImpl()));
        
        batchCalculatorService = new BatchCalculatorServiceImpl(calculatorService);
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
        final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);
        
        //THEN
        // Lors de l'étape d'assertion, nous allons rapatrier tous les modèles de calcul utilisés. On en profite pour vérifier
        // que la classe  CalculatorService  a bien été utilisée autant de fois que de calculs dans le flux de données
        Mockito
                .verify(calculatorService, times(4))
                .calculate(vCalculationModelArgumentCaptor.capture());
        final List<CalculationModel> calculationModels = vCalculationModelArgumentCaptor.getAllValues();
        
        assertThat(calculationModels)
                .extracting(CalculationModel::getType, CalculationModel::getLeftArgument, CalculationModel::getRightArgument)
                .containsExactly(tuple(CalculationType.ADDITION, 2, 2), tuple(CalculationType.SUBTRACTION, 5, 4),
                        tuple(CalculationType.MULTIPLICATION, 6, 8), tuple(CalculationType.DIVISION, 9, 3));
    }
}
