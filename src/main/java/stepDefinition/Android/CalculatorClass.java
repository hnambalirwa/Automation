package stepDefinition.Android;
import screenAndPageObjects.Android.Calculator_Screen;
import Reporting.ExtentManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import utils.DriverFactory;



/**
 * Created by Harriet on 13/05/17.
 */
public class CalculatorClass extends DriverFactory {

    Calculator_Screen calculatorScreen = new Calculator_Screen();
    boolean testPassed = true;


    // All the behavior of Agenda page will be defined here in functions
    @Given("^The user has opened up the calculator App, user gets an a screen showing number buttons$")
    public void verifyCalculatorButtons() {

        test = extent.createTest("Calculator buttons", "Verify buttons functionality");

        if (!clickElementById(Calculator_Screen.clearButtonId())) {
            ExtentManager.displayScreenShotOnReport("ClearButtonFailure");
            test.fail("Could not clear clicked text");
            testPassed = false;
        }

        for (int count = 0; count < 9; count++) {
            //buttons
            String elementID = Calculator_Screen.dynamicCalculatorButtons() + count;
            if (!clickElementById(elementID)) {
                test.fail("Could not clicked button");
                testPassed = false;
                count = 9;
            }

            if (!clickElementById(Calculator_Screen.backButtonId())) {
                test.fail("Could not clicked delete button");
                testPassed = false;
                count = 9;
            }
        }
    }

    @When("^user clicks on any number button$")
    public void clickACulculatorButton() {
        //clear text first
        Assert.assertTrue(clickElementById(Calculator_Screen.clearButtonId()));

        //button 7
        String elementId = calculatorScreen.dynamicCalculatorButtons() + 7;
        Assert.assertTrue(clickElementById(elementId));
        test.pass("user clicks on any number button : Successful ");

    }

    @Then("^user sees the clicked number in the top section of the app$")
    public void verifyTheClickedNumberIsDisplayed() {
        //clear text first
        Assert.assertTrue(clickElementById(Calculator_Screen.clearButtonId()));

        String elementId = Calculator_Screen.dynamicCalculatorButtons() + 7;

        //button 7
        Assert.assertTrue(clickElementById(elementId));
        test.pass("Number Clicked Successfully");

        elementId = Calculator_Screen.displayTextAreaClassName();
        //TO DO
        //verify 7 is displaying in the clicked component text box
        if (getElementTextByClassName(elementId).contains("7"))
        {
            test.pass("The Clicked Number is displaying");
        }

        //pluss button
        Assert.assertTrue(clickElementById(calculatorScreen.plusButtonId()));
        test.pass("Plus Button Clicked Successfully");

        //TO DO
        //verify + is displaying in the clicked component text box
        if(!getElementTextById(elementId).contains("+"))
        {
            test.fail("Plus Button not displaying in the clicked items text box");
            testPassed = false;
        }

        //button 9
        elementId = calculatorScreen.dynamicCalculatorButtons() + 9;
        Assert.assertTrue(clickElementById(elementId));
        test.pass("Number Clicked Successfully");


        //TO DO
        //verify 9 is displaying in the clicked component text box
        if(!getElementTextById(elementId).contains("9"))
        {
            test.fail("The second clicked Number not displaying in the clicked items text box");
            testPassed = false;
        }

        ExtentManager.displayScreenShotOnReport("Two numbers added together are displaying successfully.");

        //button =
        Assert.assertTrue(clickElementById(calculatorScreen.equalsButtonName()));
        test.pass("Equals Button Clicked Successfully");

        //TO DO
        //verify = is displaying in the clicked component text box
        if(getElementTextById(elementId).equals(""))
        {
            test.fail("Numbers added up successfully");
            testPassed = false;
        }

        if(testPassed) {
            ExtentManager.displayScreenShotOnReport("buttonsFunctionalitySuccessful");
            test.pass("Buttons Functionality works successfully");
        }
    }
}
