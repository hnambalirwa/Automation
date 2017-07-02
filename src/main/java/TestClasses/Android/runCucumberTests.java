package TestClasses.Android;

/**
 * Created by Harriet on 10/05/17.
 */

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions
        (format = {"pretty", "html:target/cucumber"},
        features = "/Users/wamalalawrence/Desktop/Harriet/CalculatorDemo/src/main/java/features/Android/CalculatorApp.feature",
        glue={"stepDefinition"})
public class runCucumberTests {

}
