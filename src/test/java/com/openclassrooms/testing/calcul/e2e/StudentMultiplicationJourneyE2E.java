package com.openclassrooms.testing.calcul.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Classe de test de bout en bout pour tester la multiplication d'un calculateur
 *
 * @ExtendWith(SpringExtension.class) permet de simuler une appli bien configurée, comme si on la mettait en prod
 * @SpringBootTest utile pour les tests d'acceptation. Le param ajouté est utile pour dire qu'on lance l'appli comme un
 * environnement de serveur web. Le port reseau est choisi au hasard, ce qui pemet d'eviter tout conflit avec un ou pluseirus
 * autres serveurs web qui tourneraient sur la machine
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentMultiplicationJourneyE2E {
    
    // POrt reseau de connexion
    @LocalServerPort
    private Integer port;
    private WebDriver webDriver = null; // environnement de test basé sur selenium
    private String baseUrl;
    
    /**
     * chaine de caractere de l'adresse d'accueil de l'appli web.
     * Avec le webdriver, la 1ere chçose  àfaire avec le lancement de tous le stests est de s'assurer que ma machine possede
     * bien un driver permettant de communiquer avec un navigateur. Car le test va ouvrir un VRAI  navigateur! Et il me faut ce
     * logicile permettant d'effectuer cette communication
     */
    
    @BeforeAll
    public static void setUpFirefoxDriver() {
        WebDriverManager
                .firefoxdriver()
                .setup();
    }
    
    /**
     * Avant le lancement de chaque test, initialisation du driver du navigateur. C'est l'etape qui fait ouvrir un VRAI
     * navigateur web
     */
    @BeforeEach
    public void setUpWebDriver() {
        webDriver = new FirefoxDriver();
        baseUrl = "http://localhost:" + port + "/calculator";
        
    }
    
    /**
     * Fermeture du navigateur à chaque fin de test
     */
    @AfterEach
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
    
    /**
     * Etape GIVEN
     * On demande au navigateur d'aller directement sur l'url de base du calculateur. On recupere alors sur la page obtenue les
     * elements web obtenus correspondant aux champs saisis des operande et du typoe d'operation + au bouton submit du form.
     * Ensuite,n recupere ces elements  grace à la methode static de selenium "By.id(...)". Il s'agit des ids Html. Il faut pour
     * que ca fonctionne aller dans la page de template pour ajouter correctement les ids. (template dans
     * resources/templates/calculator.html
     *
     * Etape WHEN
     * On remplit les elements comme si un user remplissait les champs au clavier (methode 'sendKeys()') et on simule le submit
     * (avec la methode 'click()').
     *
     * Etape THEN
     * On crée un objet qui va attendre que la page de retour se recharge. Pour ca, on indique au webDriver qui'l y ait sur la
     * page un element d'id 'solution'. Puis , on extrait le contenu de l'element 'solution' et on verifie avec une assertion que
     * cet element contient bien le nb 'solution' de la multiplication
     */
    @Test
    public void aStudentUsesTheCalculatorToMultiplyTwoBySixteen() {
        // GIVEN
        webDriver.get(baseUrl);
        final WebElement leftField = webDriver.findElement(By.id("left"));
        final WebElement typeDropdown = webDriver.findElement(By.id("type"));
        final WebElement rightField = webDriver.findElement(By.id("right"));
        final WebElement submitButton = webDriver.findElement(By.id("submit"));
        
        // WHEN
        leftField.sendKeys("2");
        typeDropdown.sendKeys("x");
        rightField.sendKeys("16");
        submitButton.click();
        
        // THEN
        final WebDriverWait waiter = new WebDriverWait(webDriver, 5);
        // Stockage de l'element 'solution' dans une var
        final WebElement solutionElement = waiter.until(ExpectedConditions.presenceOfElementLocated(By.id("solution")));
        final String solution = solutionElement.getText();
        assertThat(solution).isEqualTo("32"); // 2 x 16
    }
    
}
