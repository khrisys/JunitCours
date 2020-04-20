package com.openclassrooms.testing.calcul.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentMultiplicationJourneyE2E {
    
    @LocalServerPort
    private Integer port;
    private WebDriver webDriver = null;
    private String baseUrl;
    
    @BeforeAll
    public static void setUpFirefoxDriver() {
        WebDriverManager
                .firefoxdriver()
                .setup();
    }
    
    @BeforeEach
    public void setUpWebDriver() {
        webDriver = new FirefoxDriver();
        baseUrl = "http://localhost:" + port + "/calculator";
    }
    
    @AfterEach
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
    
    @Test
    public void aStudentUsesTheCalculatorToMultiplyTwoBySixteen() {

        // SANS PAGE OBJET REPRENSENANT 'calculator.html'
/*		// GIVEN
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
		final WebElement solutionElement = waiter.until(
				ExpectedConditions.presenceOfElementLocated(By.id("solution")));
		final String solution = solutionElement.getText();
		assertThat(solution).isEqualTo("32"); // 2 x 16*/

        
        //AVEC PAGE OBJET 'CalculatorPage' (de ce meme package) REPRESENTANT 'calculator.html'
        // GIVEN
        webDriver.get(baseUrl);
        final CalculatorPage calculatorPage = new CalculatorPage(webDriver); // init de page
        
        // WHEN
        final String solution = calculatorPage.multiply("2", "16"); // calcul
        
        // THEN
        assertThat(solution).isEqualTo("32");
//        assertThat(solution, is(equalTo("32"))); // Ca, ca fonctionne aussi!!!
    }

    @Test
    public void aStudentUsesTheCalculatorToAddTwoToSixteen() throws InterruptedException {
        // GIVEN
        webDriver.get(baseUrl);
        final CalculatorPage calculatorPage = new CalculatorPage(webDriver);
        
        // les etapes WHEN et THEN sont traitées par CalculatorPage (du meme package)
        // WHEN
        final String solution = calculatorPage.add("2", "16");
        
        // THEN
        assertThat(solution).isEqualTo("18"); // 2 + 16
    }

}
