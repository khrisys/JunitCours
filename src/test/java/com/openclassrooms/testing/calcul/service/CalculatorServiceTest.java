package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Classe verifiant les calculs que fait la classe CalculatorServiceImpl , via son interface CalculatorService.
 */
@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {
    
    @Mock
    Calculator calculator = new Calculator();
    @Mock
    SolutionFormatted solutionFormatted;
    
    CalculatorService classUnderTest;
    
    // Initialisation de classUnderTest avant chaque methode de test grace à @BeforeEach
    // Apres ajout de l'interface visant à formaté les grands nbs, et COMME LE TEST VISE A SE FOCALISER SUR LA METHODE DE
    // CALCUL, on va utiliser l'interface de formatage des nbs avec un @Mock pour la simuler.
    @BeforeEach
    public void init() {
        classUnderTest = new CalculatorServiceImpl(calculator, solutionFormatted);
    }
    
    @Test
    public void add_returnsTheSum_ofTwoPositiveNumbers() {
        
        // GIVEN
        //Comme le calculator n'est plus reel, mais est un mock, je fais reellement l'action que la vraie classe aurait due faire
        when(calculator.add(1, 2)).thenReturn(3);
        
        // WHEN
        // Traitement en appelant la methode caluclate, comme si c'etait fait dans la methoide initiale
        int result = classUnderTest
                .calculate(new CalculationModel(CalculationType.ADDITION, 1, 2))
                .getSolution();
        
        // THEN
        // verification du calculator à 3, mais surtout, verification que le service a bien appelé le calculator avec les bons
        // arguments
        verify(calculator).add(1, 2);
        // assertion du resultat de la classe de Test, pas de celle du mock
        assertThat(result).isEqualTo(3);
        
    }
    
    /**
     * ici, je ne precise pas quels sont les nbs à ajouter (utilisation de Random(), mais je signifie que quels que soient les
     * args, le resultat sera toujours de 3
     */
    @Test
    public void addition_forAnyIntegerTest() {
        
        // GIVEN
        //Comme le calculator n'est plus reel, mais est un mock, je fais reellement l'action que la vraie classe aurait due faire.
        // Utilisation de nb aleatoire (deconseillé)
        final Random vRandom = new Random();
        when(calculator.add(any(Integer.class), any(Integer.class))).thenReturn(3);
        
        // WHEN
        // Traitement en appelant la methode caluclate, comme si c'etait fait dans la methoide initiale
        int result = classUnderTest
                .calculate(new CalculationModel(CalculationType.ADDITION, vRandom.nextInt(), vRandom.nextInt()))
                .getSolution();
        
        // THEN
        // verification du calculator à 3, mais surtout, verification que le service a bien appelé le calculator avec les bons
        // arguments.
        // Je precise que je ne veux appeler la methode (grace à times(1) qu'une seule, et une seule fois -> sinon erreur
        verify(calculator, times(1)).add(any(Integer.class), any(Integer.class));
        // verif qu'on ait pas appelé la methode de soustraction grace à times(0)
        verify(calculator, times(0)).sub(any(Integer.class), any(Integer.class));
        // assertion du resultat de la classe de Test, pas de celle du mock
        assertThat(result).isEqualTo(3);
        
    }
    
    @Test
    public void soustractionTest() {
        when(calculator.sub(3, 2)).thenReturn(1);
        
        final int result = classUnderTest
                .calculate(new CalculationModel(CalculationType.SUBTRACTION, 3, 2))
                .getSolution();
        //verif calculateur
        verify(calculator).sub(3, 2);
        // verif resultat
        assertThat(result).isEqualTo(1);
    }
    
    
    @Test
    public void divisionTest() {
        when(calculator.divide(8, 2)).thenReturn(4);
        
        final int result = classUnderTest
                .calculate(new CalculationModel(CalculationType.DIVISION, 8, 2))
                .getSolution();
        
        verify(calculator, times(1)).divide(8, 2);
        verify(calculator, times(0)).sub(8, 2);
        assertThat(result).isEqualTo(4);
    }
    
    
    //================================= Exceptions ==========================================
    
    /**
     * gestion d'un exception : division par 0
     * Ici, on convertit le type d'exception à renvoyer. Une exception de division par 0 renvoi une ArithmeticException, mais
     * je transforme ici cette ArithmeticException en IllegalArgumentException
     */
    @Test
    public void calculate_shouldThrowIllegalArgumentException_forADivisionBy0() {
        
        // GIVEN
        // Ici, on applique la methode "thenThrow"
        when(calculator.divide(1, 0)).thenThrow(new ArithmeticException());
        
        // WHEN
        // De là, on recupere thenThrows" pour passer une expression lambda (pour rappel, une expression lambda, c'est une
        // fonction qui passe une auytre fonction en parametre (au lieu d'un vrai param)
        assertThrows(IllegalArgumentException.class,
                () -> classUnderTest.calculate(new CalculationModel(CalculationType.DIVISION, 1, 0)));
        
        // THEN
        // verification du test, et qu'il ne passe qu'une seule fois, sinon erreur
        verify(calculator, times(1)).divide(1, 0);
    }
    
    
    //================================= Rajouter un formattage des grands nombres ==========================================
    
    /**
     * Methode permettant de seprarer les milleirs avec les centaines sur de grands nbs
     */
    @Test
    private void calculate_shouldFormatFormatSolution_forAnAddition() {
        // GIVEN
        // Je dois obligatiorement creer le test simulé
        when(calculator.add(10000, 3000)).thenReturn(13000);
        // apres insctanciation de solutionFormatter, quo'n a mocker, il faut le parametrer
        when(solutionFormatted.format(13000)).thenReturn("13 000");
        
        // WHEN
        //on code l'action : on recupere la solution formattée par la meme classe de données CalcultationMdoel
        final String formattedResult = classUnderTest
                .calculate(new CalculationModel(CalculationType.ADDITION, 10000, 3000))
                .getFormattedSolution();
        
        // THEN
        // Verification du formattage avec une assertion simple d'egalité
        assertThat(classUnderTest).isEqualTo("13000");
        assertThat(formattedResult).isEqualTo("13 000");
    }
    
    /**
     * Test de formattage d'une soustraction
     */
    @Test
    public void calculte_should_FormatSolution_forASubstraction() {
        
        // Je dois obligatiorement creer le test simulé
        when(calculator.sub(100000,30000)).thenReturn(70000);
        when(solutionFormatted.format(70000)).thenReturn("70 000");
        
        final String formattedResultat = classUnderTest
                .calculate(new CalculationModel(CalculationType.SUBTRACTION, 100000, 30000))
                .getFormattedSolution();
        
        assertThat(formattedResultat).isEqualTo("70 000");

    }
    
}
