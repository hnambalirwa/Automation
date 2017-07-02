package utils;

/**
 * Created by Harriet Nambalirwa on 10/05/17.
 */

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverFactory{

    DesiredCapabilities capabilities;

    public static ExtentReports extent;
    public static ExtentTest test;
    public static WebDriver driver = null;
    public static WebDriverWait waitVariable = null;


    public DriverFactory(){
        capabilities = new DesiredCapabilities();
    }


    //Create the driver instance
    public void createDriver() throws MalformedURLException, InterruptedException {

        try {

            startDeviceSession();

            if(driver == null) {
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            }
            waitVariable = new WebDriverWait(driver, 1);


        }catch(Exception e){
            System.out.println("Error : " + e.getMessage());
        }
    }

    public  boolean startDeviceSession(){
        try{

            //Device set up
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "HT43MWM00020");
            capabilities.setCapability("platformVersion", "5.0.1");
            capabilities.setCapability("openDeviceTimeout", 5);

            //set up to Automate an android built in app in this case its the calculator
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.htc.calculator");
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.htc.calculator.Calculator");


        }catch(Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
        return  true;
    }

    public  boolean startSimulatorSession(){
        try{
            //Emulator set up
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "Android Emulator");
            capabilities.setCapability("platformVersion", "7.1");

        }catch(Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
        return true;
    }

    public  boolean executeAPKFile(){

        try {
            // set up appium / locate apk
            final File classpathRoot = new File(System.getProperty("user.dir"));
            final File appDir = new File(classpathRoot, "src/test/resources/apps");
            final File app = new File(appDir, "fr.xebia.xebicon_2016-11-08.apk");
            //final File app = new File(appDir, "com.xebia.eventsapp_2.1.apk");
            capabilities.setCapability("app", app.getAbsolutePath());

        }catch (Exception e){
            Log.error("Error : " + e.getMessage());
            return false;
        }

        return  true;
    }

    //End the driver instance
    public boolean teardown ()throws MalformedURLException, InterruptedException{
        try {
            //close the Driver(app)
            driver.quit();
            return true;
        }
        catch(Exception e){
            Log.error("Driver Error : " + e.getMessage());
            return false;
        }
    }
    //find elements methods
    public boolean waitForElementById(String elementID) {
        try {
            By element = By.id(elementID);
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(element));
            return true;

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
    }

    public boolean waitForElementByName(String elementName) {
        try {
            By element = By.name(elementName);
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(element));
            return true;

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
    }

    public boolean waitForElementByXpath(String elementXpath) {
        try {
            By element = By.xpath(elementXpath);
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(element));
            return true;

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
    }

    public boolean waitForElementByClassName(String elementClassName) {
        try {
            By element = By.className(elementClassName);
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(element));
            return true;

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());
            return false;

        }
    }

    public String getElementTextByClassName(String elementClassName) {
        String retrievedText = "";
        try {
            WebElement element = driver.findElement(By.className(elementClassName));
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(By.className(elementClassName)));
            retrievedText = element.getText();

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());

        }
       return retrievedText;
    }

    public String getElementTextById(String elementId) {
        String retrievedText = "";
        try {
            WebElement element = driver.findElement(By.id(elementId));
            waitVariable.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
            retrievedText = element.getText();

        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());

        }
        return retrievedText;
    }

    //Click element Methods
    public boolean clickElementByIName(String elementName){

        return true;
    }

    public boolean clickElementById(String elementId) {
        try {
            waitForElementById(elementId);
            waitVariable.until(ExpectedConditions.elementToBeClickable(By.id(elementId))).click();
            return true;

        } catch (Exception e) {
            return false;

        }
    }

    public boolean clearElementTextById(String elementId) {
        try {
            waitForElementById(elementId);
            waitVariable.until(ExpectedConditions.elementToBeClickable(By.id(elementId))).clear();
            return true;

        } catch (Exception e) {
            return false;

        }
    }

    public boolean clearElementTextByName(String elementName) {
        try {
            waitForElementByName(elementName);
            waitVariable.until(ExpectedConditions.elementToBeClickable(By.name(elementName))).clear();
            return true;

        } catch (Exception e) {
            return false;

        }
    }

    public boolean clickElementByXPAth(String elementXpath){
        return true;
    }

    public boolean clickElementByCSSSelector(String elementCSS){
        return true;
    }


    //Click from dropdown Methods
    public boolean selectFromDropDownById(String elementID){
        return true;
    }

    public boolean selectFromDropDownByXpath(String elementXpath){
        return true;
    }

    public boolean selectFromDropDownByCSSSelector(String elementCSSSelector){
        return true;
    }


    //enter text in the textbox  Methods
    public boolean enterTextInTextboxById(String elementID){
        return true;
    }

    public boolean enterTextInTextboxByXPath(String elementXPath){
        return true;
    }

    public boolean enterTextInTextboxByCSSSelector(String elementCSS){
        return true;
    }

    //Clear text from texbox  Methods
    public boolean clearTextFromTextboxById(String elementId){
        return true;
    }

    public boolean clearTextFromTextboxByXPath(String elementXPath){
        return true;
    }

    public boolean clearTextFromTextboxByCSSSelector(String elementId){
        return true;
    }


    //Validate text available
    public void validateTextAvailableById(String elementId){

    }

    public boolean validateTextAvailableByXPath(String elementXPath){
        return true;
    }

    public boolean validateTextAvailableByCSSSelector(String elementCSSSelector){
        return true;
    }

    //validate element is available
    public boolean validateElementAvailableById(String elementId){
        return false;

    }

    public boolean validateElementAvailableByXPath(String elementXPath){
        return true;
    }

    public boolean validateElementAvailableByCSS(String elementCSS){
        return true;
    }


    //Validate values are equal
    public boolean validateValuesAreEqualById(String elementId){
        return true;
    }

    public Boolean validateValuesAreEqualByXPath(String elementXPath){
        return true;
    }

    public Boolean validateValuesAreEqualByCSS(String elementCSS){
        return true;
    }
}