package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    
    /**
     * Vérifier les appels au service de CalculatorService
     * À partir d'un fichier ou d'une liste de chaînes de caractères contenant des opérations, obtenir la liste des résultats des
     * calculs.
     *
     * La responsabilité de ce nouveau service BatchCalculatorService est de :
     * -  transformer un flux de chaînes de caractères en modèles de calcul ;
     * -  appeler la classe CalculatorService pour chaque modèle de calcul.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
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
        batchCalculatorService.batchCalculate(operations);
        
        //THEN
        // Lors de l'étape d'assertion, nous allons rapatrier tous les modèles de calcul utilisés. On en profite pour vérifier
        // que la classe  CalculatorService  a bien été utilisée autant de fois que de calculs dans le flux de données
        verify(calculatorService, times(4)).calculate(vCalculationModelArgumentCaptor.capture());
        final List<CalculationModel> calculationModels = vCalculationModelArgumentCaptor.getAllValues();
        
        assertThat(calculationModels)
                .extracting(CalculationModel::getType, CalculationModel::getLeftArgument, CalculationModel::getRightArgument)
                .containsExactly(tuple(CalculationType.ADDITION, 2, 2), tuple(CalculationType.SUBTRACTION, 5, 4),
                        tuple(CalculationType.MULTIPLICATION, 6, 8), tuple(CalculationType.DIVISION, 9, 3));
    }
    
    
    /**
     * Utiliser when() avec des reponses complexes
     * Vérifier si la méthode batchCalculate de BatchCalculatorService renvoie bien la liste des résultats envoyés par le
     * service de calcul CaclculatorService (et pas seulement les modeles de calcul utilisés)
     *
     * Notre mock est capable de traiter n'importe quelle opération. Il donnera alors la solution 4 si c'est une addition, 1 si
     * c'est une soustraction, 48 si c'est une multiplication et 3 si c'est une division.
     *
     * Le test va ensuite vérifier que le service a bien été appelé 4 fois et que les résultats sont conformes,
     */
    @Test
    public void reponseComplexe() {
        
        //GIVEN
        final Stream<String> operations = Arrays
                .asList("2 + 2", "5 - 4", "6 x 8", "9 / " + "3")
                .stream();
        // Le test va ensuite vérifier que le service a bien été appelé 4 fois et que les résultats sont conformes. when()
        // avec then() sert donner une réponse en fonction des arguments appelés, et non pas juste une réponse simple par
        // rapport à des arguments simples
        //     -   when()  pour qu'il traite n'importe quel type d'arguments avec  any()  ;
        //     -  puis  then()  pour construire la réponse sous forme de lambda.
        when(calculatorService.calculate(any(CalculationModel.class))).then(invocation -> {
            final CalculationModel model = invocation.getArgument(0, CalculationModel.class);
            switch (model.getType()) {
                case ADDITION:
                    model.setSolution(4);
                    break;
                case SUBTRACTION:
                    model.setSolution(1);
                    break;
                case MULTIPLICATION:
                    model.setSolution(48);
                    break;
                case DIVISION:
                    model.setSolution(3);
                    break;
                default:
            }
            return model;
        });
        
        // Ici, pas besoin d'ArgumentCaptor
        
        
        //WHEN
        final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);
        
        //THEN
        //
        verify(calculatorService, times(4)).calculate(any(CalculationModel.class));
        assertThat(results)
                .extracting("solution")
                .containsExactly(4, 1, 48, 3);
    }
    
    
    /**
     * Avec  when(), vous pouvez donner une réponse précise ou une fonction de réponse, mais vous pouvez aussi donner plusieurs
     * réponses. Le mock va alors utiliser ces réponses dans l'ordre des appels effectués : au premier appel du mock, ce
     * dernier renvoie la première réponse, puis au deuxième appel, la deuxième réponse fournie, et ainsi de suite. Au bout de
     * la dernière réponse, le mock revient à la première réponse fournie.
     *
     * Dans notre exemple de calculateur par lots, nous pouvons utiliser ce moyen, car nous maîtrisons bien les données de test
     * utilisées : le flux d'opérations sous forme de chaînes de caractères.
     */
    @Test
    public void reponsesMultiples() {
        
        //GIVEN
        final Stream<String> operations = Arrays
                .asList("2 + 2", "5 - 4", "6 x 8", "9 / 3")
                .stream();
        when(calculatorService.calculate(any(CalculationModel.class)))
                .thenReturn(new CalculationModel(CalculationType.ADDITION, 2, 2, 4))
                .thenReturn(new CalculationModel(CalculationType.SUBTRACTION, 5, 4, 1))
                .thenReturn(new CalculationModel(CalculationType.MULTIPLICATION, 6, 8, 48))
                .thenReturn(new CalculationModel(CalculationType.DIVISION, 9, 3, 3));
        
        //WHEN
        final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);
        
        //THEN
        verify(calculatorService, times(4)).calculate(any(CalculationModel.class));
        assertThat(results)
                .extracting("solution")
                .containsExactly(4, 1, 48, 3);
        
    }
}
