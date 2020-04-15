package com.openclassrooms.testing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    
    //    Variables
    public static Instant sInstant;
    private Calculator calculator;
    
    //  ===========================  Methodes ==================================
    /**
     * @BeforeAll permet d'executer ce test AVANT TOUS les tests de cette classe
     * Methode qui doit etre static. methode static = variable static, Or, Instant est une classe static
     */
    @BeforeAll
    public static void initStartTime() {
        System.out.println("test executé AVANT TOUS les tests.");
        sInstant = Instant.now();
    }

    /**
     * @AfterAll permet d'excuter ce test APRES TOUS les tests.
     * Methode doit etre static
     */
    @AfterAll
    public static void endTestsTime(){
        Instant vInstant = Instant.now();
        long duration = Duration.between(sInstant, vInstant).toMillis();
        System.out.println("test excuté APRES TOUS les tests. Temps de passage des tests : " + duration + " ms");
    }
    
    /*
     * @BeforeEach permet d'excuter un test AVANT CHAQUE action (@Test)
     * Ici,on instancie une var avant chaque @Test
     */
    @BeforeEach
    private void initCalculator() {
        calculator = new Calculator();
        System.out.println("init calculator. Executé avant chaque @Test");
    }

    /**
     * @AfterEach permetd 'executer une methode APRES CHAQUE action (@Test)
     * Ici, on supprimer la var apres chaque test effectué
     */
    @AfterEach
    private void deleteCalculator() {
        calculator = null;
        System.out.println("delete calculator. Executée apres chaque @Test");
    }
    
    /**
     * Test
     */
    @Test
    public void testAddTwoPositiveNumbers() {
        // Arrange
        int a = 2;
        int b = 3;
        
        // Act
        int somme = calculator.add(a, b);
        
        // Assert
        assertEquals(5, somme);
        System.out.println("addition");
    }
    
    @Test
    public void multiply_shouldReturnTheProduct_ofTwoIntegers() {
        // Arrange
        int a = 42;
        int b = 11;
        
        // Act
        int produit = calculator.multiply(a, b);
        
        // Assert
        assertEquals(462, produit);
        System.out.println("multiplication");
    }
    
    /**
     * @ParameterizedTest Permet d'utiliser une var instanciée en debut de classe  dans plusieurs methode de test. {0}
     * correspond à la valeur de chaque element du tableau de @ValueSource. Ici, il y aura donc 5 tests effectués.
     * @ValueSource permet l'insertion d'un tableau de nb entier à tester.
     *
     * @param pI
     */
    @ParameterizedTest(name = "{0} x0 doit toujours etre egal à 0")
    @ValueSource(ints = {1,5,684,342,49864})
    public void alwaysReturnZero(int pI){
        // ARRANGE : tout estfait
        
        //ACT : multiplier par zero
        int res = calculator.multiply(pI, 0);
        
        //ASSERT : verif que le result est toujours de 0, quelque soit le param passé
        assertEquals(0, res);
        
        System.out.println("0");
    }
    
    /**
     * @ParameterizedTest Permet d'utiliser une var instanciée en debut de classe dans plusieurs methode de test. {0} correspond
     * à la valeur du 1er attr du tableau de @CsvSource, {1} au 2e element du tab, etc.
     * @CsvSource permet 'linsertion d'un tableau de nb entier à tester
     *
     * @param pI
     */
    @ParameterizedTest(name = "{0} + {1} doit etre egal à {2}")
    @CsvSource({"1,2,3","5,10,15","100,568,668","51,291,342","49500,364,49864"})
    public void testSumOfIntegers(int pI, int pII, int pIII){
        // ARRANGE : tout estfait
        
        //ACT : multiplier par zero
        int res = calculator.add(pI, pII);
        
        //ASSERT : verif que le result est toujours de 0, quelque soit le param passé
        assertEquals(pIII, res);
        
        System.out.println(pIII);
    }
    
    /**
     * @Test indique que je fais un test. Avec @Timeout(), si je ne met pas @Test, @Timeout() n'aura aucun effet
     * @Timeout(1) Permet de verififer la durée que prend une methode. Ici, elle doit etre <= à 1 seconde.
     *
     * @throws InterruptedException
     */
    @Test
    @Timeout(2)
    public void testLongCalculation() throws InterruptedException {
        calculator.longCalculation();
    }
}
