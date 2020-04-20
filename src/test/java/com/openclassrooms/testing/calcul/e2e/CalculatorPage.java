package com.openclassrooms.testing.calcul.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Creation de l page objet representant 'calculator.html'. En cas de modif quelconque sur cette page, tous les tests de bout
 * en bout echoueront, mais on n'aura qu'à changer uniquement la page qui a changé dans cet ensemble de tests pour que tout
 * refonctionne. Et non pas réadapter tous les tests de toutes les pages du test de bout en bout.
 *
 * Les modèles PageObject vous permettent d’englober les sélecteurs d’une page et de fournir des méthodes nommées de façon
 * sémantique pour effectuer des actions sur cette page web.
 *
 * Étant donné que vous disposez de toute votre connaissance du comportement d’une page web à un seul endroit, cela vous protège
 * aussi de la dispersion de dépendances sur la structure de votre page web à travers de nombreux tests. Ex : si l'id 'type'
 * de la page 'aclculator.html' change par par ex 'typeOperation', alors , ici, on n'a qu'à changer le nom de l'id à la ligne 42!
 *
 * Chaque attribut est une instance de class WebElement, et l'annotation  @FindBy  permettra au WebDriver d'associer cet élément
 * avec l'élément HTML ayant l'id indiqué dans la page 'calculator.html'
 */
public class CalculatorPage {
    
    public static final String ADDITION_SYMBOL = "+";
    public static final String SUBTRACTION_SYMBOL = "-";
    public static final String DIVISION_SYMBOL = "/";
    public static final String MULTIPLICATION_SYMBOL = "x";
    
    // A declarer dans le constructeur
    private final WebDriver webDriver;
    
    // methode de recuup des ids sur la page html
    @FindBy(id = "submit")
    private WebElement submitButton;
    
    @FindBy(id = "left")
    private WebElement leftArgument;
    
    @FindBy(id = "right")
    private WebElement rightArgument;
    
    @FindBy(id = "type")
    private WebElement calculationType;
    
    @FindBy(id = "solution")
    private WebElement solution;
    
    /**
     * Constructeur
     *
     * PageFactory est une classe de selenium qui pemret  que les attr annotés @FindBy soient intialisés. C'est importnat!
     */
    public CalculatorPage(WebDriver pWebDriver) {
        webDriver = pWebDriver;
        PageFactory.initElements(webDriver, this);
    }
    
    // 4 Methodes de calcul
    public String add(String leftValue, String rightValue) {
        return calculate(ADDITION_SYMBOL, leftValue, rightValue);
    }
    
    /**
     * Permet de soumettre un calcul depuis la page html
     *
     * @param calculationTypeValue
     * @param leftValue
     * @param rightValue
     * @return
     */
    private String calculate(String calculationTypeValue, String leftValue, String rightValue) {
        leftArgument.sendKeys(leftValue);
        calculationType.sendKeys(calculationTypeValue);
        rightArgument.sendKeys(rightValue);
        submitButton.click();
        
        final WebDriverWait waiter = new WebDriverWait(webDriver, 5);
        waiter.until(ExpectedConditions.visibilityOf(solution));
        
        return solution.getText();
    }
    
    public String subtract(String leftValue, String rightValue) {
        return calculate(SUBTRACTION_SYMBOL, leftValue, rightValue);
    }
    
    public String divide(String leftValue, String rightValue) {
        return calculate(DIVISION_SYMBOL, leftValue, rightValue);
    }
    
    public String multiply(String leftValue, String rightValue) {
        return calculate(MULTIPLICATION_SYMBOL, leftValue, rightValue);
    }
    
}
