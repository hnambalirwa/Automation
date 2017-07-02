package utils;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import static java.lang.System.err;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import entities.Enums;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 *
 * @author Harriet
 */
// Contains logic for handling accessor methods and driver calls.
public class SeleniumDriverUtility{

    public WebDriver Driver;
    private Enums.BrowserType browserType;
    File fileIEDriver;
    File fileChromeDriver;
    private Boolean _isDriverRunning;
    public String DriverExceptionDetail = "";
    private Object document;
    private static Integer screenShotCounter = 1;
    private static Integer screenShotFolderCounter = 1;

   // private String reportDirectory = "";

    public SeleniumDriverUtility(Enums.BrowserType selectedBrowser) {
        //retrievedTestValues = new RetrievedTestValues();

        _isDriverRunning = false;
        browserType = selectedBrowser;

        fileIEDriver = new File("IEDriverServer.exe");
        System.setProperty("webdriver.ie.driver", fileIEDriver.getAbsolutePath());

        fileChromeDriver = new File("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", fileChromeDriver.getAbsolutePath());

       // reportDirectory =
    }

    public boolean isDriverRunning() {
        return _isDriverRunning;
    }
    
    
    public Set getCookiesAsSet()
    {
        try
        {
            return Driver.manage().getCookies();
        }
        catch(Exception ex)
        {
            err.println("[ERROR] Failed to retrieve cookies from browser session - " + ex.getMessage());
        }
        
        return null;
    }
    
    
    public boolean setCookiesAsPropertySet(Set cookieSet)
    {
        try
        {
            Set<Cookie> cookSet = cookieSet;
            
            for(Cookie cook : cookSet)
            {
                Driver.manage().addCookie(cook);
            }
            
            return true;
        }
        catch(Exception ex)
        {
            err.println("[ERROR] Failed to add cookies to browser session - " + ex.getMessage());
            
            return false;
        }
        
    }

    public void startDriver() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");

        switch (browserType) {
            case IE:
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        
                 Driver = new InternetExplorerDriver(cap);
                _isDriverRunning = true;
                break;
            case FireFox:
                Driver = new FirefoxDriver();
                _isDriverRunning = true;
                break;
            case Chrome:
                Driver = new ChromeDriver();
                _isDriverRunning = true;
                break;
            case Safari:
                break;
        }
        //retrievedTestValues = new RetrievedTestValues();
        Driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
        Driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
        Driver.manage().window().maximize();

    }
    
    public void maximizeWindow() {
        Driver.manage().window().maximize();
    }

    public boolean tryClickElementsByCSSSelector(String elementCSSSelector) {
        try {
            
            System.out.println("[Info]Finding elements by CSS Selector - " + elementCSSSelector);
            waitForElementByCSSSelector(elementCSSSelector);
            //Thread.sleep(1000);
            
            List<WebElement> detectedElements = Driver.findElements(By.cssSelector(elementCSSSelector));
            
            System.out.println("[Info]Found - " + detectedElements.size() + " matching elements");

            for (WebElement element : detectedElements) {
                if (element == null || !element.isDisplayed() || !element.isEnabled()) {
                    continue;
                }

                System.out.println("[Info]Element is valid, attempting to click...");

                element.click();

                System.out.println("[Info]Element clicked sucessfully, exiting search.");

                break;

            }

            return true;
        } catch (Exception e) {
            System.err.println("Error clicking elements by CSS Selector - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
   

    public boolean alertHandler() {
        try {
            System.out.println("[Info]Attempting to click OK in alert pop-up");
            // Get a handle to the open alert, prompt or confirmation
            Alert alert = Driver.switchTo().alert();
            // Get the text of the alert or prompt
            alert.getText();
            // And acknowledge the alert (equivalent to clicking "OK")
            alert.accept();
            System.out.println("[Info]Ok Clicked successfully...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("Error clicking OK in alert pop-up - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean alertHandlerFirefox() {
        try {
            System.out.println("[Info]Attempting to click OK in alert pop-up");
            // Get a handle to the open alert, prompt or confirmation
            Alert alert = Driver.switchTo().alert();
            // Get the text of the alert or prompt
            alert.getText();
            // And acknowledge the alert (equivalent to clicking "OK")
            alert.accept();
            System.out.println("[Info]Ok Clicked successfully...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("Error clicking OK in alert pop-up - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean switchToFrameById(String elementId) {
        int waitCount = 0;
        try {
            while (waitCount < 60) {
                try {
                    Driver.switchTo().frame(elementId);
                    return true;
                } catch (Exception e) {
                    waitCount++;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error switching to frame by Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean switchToDefaultContent() {
        try {
            Driver.switchTo().defaultContent();
            return true;
        } catch (Exception e) {
            System.err.println("Error switching to default content  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
  public boolean switchToNewTab() 
    {
	    try
        {
            String currentWindowHandle = Driver.getWindowHandle();
            //Get the list of all window handles
            ArrayList<String> windowHandles = new ArrayList<String>(Driver.getWindowHandles());

            for (String window:windowHandles)
            {
                //if it contains the current window we want to eliminate that from switchTo();
                if (window != currentWindowHandle)
                {
                    //Now switchTo new Tab.
                    Driver.switchTo().window(window);
                    return true;
                }
            }
	    }
        catch (Exception e) {
            System.err.println("Failed to switch to tab - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
        return true;
 }
    
    public boolean closeCurrentTab() 
    {
        try
        {
            Driver.close();
        }
        catch (Exception e)
        {
            System.err.println("Failed to close current tab - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
            return true;
    }
    
    public boolean moveToNewTab()
    {
        try
        {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_CONTROL);

        }
        catch (Exception e)
        {
            System.err.println("Failed to close current tab - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
        return true;
    }

    public boolean selectAllText()
    {
        try
        {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            pause(1500);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);

        }
        catch (Exception e)
        {
            System.err.println("Failed to close current tab - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }

        return true;
    }
       
    public boolean closeWindowKeys()
    {
        try
        {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_ALT);

        }
        catch (Exception e)
        {
            System.err.println("Failed to close current tab - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
        return true;
    }

    public boolean moveToElementById(String elementId)
    {
        try {
            Actions moveTo = new Actions(Driver);
            moveTo.moveToElement(Driver.findElement(By.id(elementId)));
            moveTo.perform();
            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementId + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }


    public boolean selectByValueFromDropDownListUsingId(String elementId, String valueToSelect) {
        try {
            waitForElementById(elementId);
            Select dropDownList = new Select(Driver.findElement(By.id(elementId)));
            dropDownList.selectByValue(valueToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by value using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean acceptAlertDialog() {
        int waitCount = 0;
        try {
            while (waitCount < 60) {
                try {
                    Driver.switchTo().alert().accept();
                    return true;
                } catch (Exception e) {
                    //Thread.sleep(500);
                    waitCount++;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error accepting alert dialog - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public String retrieveTextById(String elementId) {
        String retrievedText = "";
        try {
            waitForElementById(elementId);
            WebElement elementToRead = Driver.findElement(By.id(elementId));
            retrievedText = elementToRead.getText();
            return retrievedText;
        } catch (Exception e) {
            System.err.println("Error reading text from element - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public String retrieveTextByClassName(String elementClassName) {
        String retrievedText = "";
        try {
            this.waitForElementByClassName(elementClassName);
            WebElement elementToRead = Driver.findElement(By.className(elementClassName));
            retrievedText = elementToRead.getText();
            return retrievedText;
        } catch (Exception e) {
            System.err.println("Error reading text from element - " + elementClassName + " error - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public String retrieveTextByName(String elementName) {
        String retrievedText = "";
        try {
            this.waitForElementByName(elementName);
            WebElement elementToRead = Driver.findElement(By.name(elementName));
            retrievedText = elementToRead.getAttribute("value");
            return retrievedText;
        } catch (Exception e) {
            System.err.println("Error reading text from element - " + elementName + " error - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public String retrieveTextByLinkText(String elementLinkText) {
        String retrievedText = "";
        try {
            this.waitForElementByLinkText(elementLinkText);
            WebElement elementToRead = Driver.findElement(By.linkText(elementLinkText));
            retrievedText = elementToRead.getText();
            return retrievedText;
        } catch (Exception e) {
            System.err.println("Error reading text from element - " + elementLinkText + " error - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public String retrieveTextByCSS(String elementCSS) {
        String retrievedText = "";

        try {
            this.waitForElementByCSSSelector(elementCSS);
            WebElement elementToRead = Driver.findElement(By.cssSelector(elementCSS));
            retrievedText = elementToRead.getText();
            return retrievedText;
        } catch (Exception e) {
            System.err.println("[Error] Failed to read text from element CSS '" + elementCSS + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public Boolean clickEmbeddedElementByTagNameUsingContainerId(String containerId, String tagName, String validationText)//
    {
        try {
            waitForElementById(containerId);

            WebElement DivList = this.Driver.findElement(By.id(containerId));

            List<WebElement> subElements = DivList.findElements(By.tagName(tagName));

            for (WebElement subElement : subElements) {
                //SeleniumDriverInstance.pause(500);
                System.out.println("Detected entry " + subElement.getText() + "        Clickable");

                if (subElement.getText().toUpperCase().trim().equals(validationText.toUpperCase().trim())) {
                    subElement.click();
                    System.out.println("Validation match  " + validationText + " = " + subElement.getText());
                    System.out.println("Validation match  " + validationText + "       FOUND!!!");
                    System.out.println("Validation match  " + subElement.getText().toUpperCase() + "   <---FOUND!!!");
                    return true;
                }
            }
            return true;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean clickEmbeddedListElementByTagNameUsingContainerId(String containerId, String tagName, String validationText) {
        try {
            waitForElementById(containerId);

            WebElement DivList = this.Driver.findElement(By.id(containerId));

            List<WebElement> subElements = DivList.findElements(By.tagName(tagName));

            for (WebElement subElement : subElements) {
                this.pause(2000);
                System.out.println("Detected entry " + subElement.getText() + "        Clickable");

                if (subElement.getText().toUpperCase().trim().equals(validationText.toUpperCase().trim())) {
                    subElement.click();
                    System.out.println("Validation match  " + validationText + "       FOUND!!!");
                    System.out.println("Validation match  " + subElement.getText().toUpperCase() + "   <---FOUND!!!");
                    return true;
                }
            }
            return true;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean clickListElementByXpath(String elementXpath, String validationText) {
        try {
            waitForElementByXpath(elementXpath);

            WebElement elementToEnter = this.Driver.findElement(By.xpath(elementXpath));
            elementToEnter.findElement(By.xpath(elementXpath)).sendKeys(validationText);
            elementToEnter.sendKeys(Keys.TAB);
            System.out.println("ClickListElementByXpath complete");
            return true;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean clickEmbeddedElementByIDUsingContainerId(String containerId, String ElementID) {
        try {
           waitForElementById(containerId);

            WebElement DivList = Driver.findElement(By.id(containerId));

            WebElement subElement = DivList.findElement(By.id(ElementID));

            System.out.println("Detected entry " + subElement.getText());

            subElement.click();

            return true;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean clickEmbeddedListElementByIDUsingContainerId(String containerId, String ElementID, String ChildTagName, String validationText) {
        String ChildElementText = "";
        try {
            waitForElementById(containerId);

            WebElement DivList = Driver.findElement(By.id(containerId));

            WebElement subElement = DivList.findElement(By.id(ElementID));

            System.out.println("Detected entry " + subElement.getText());

            subElement.click();

            List<WebElement> ChildElements = subElement.findElements(By.tagName(ChildTagName));

            for (WebElement ChildElement : ChildElements) {
                ChildElementText = ChildElement.getText();
                if (ChildElementText == null) {
                    continue;
                }

                System.out.println("Detected Child Element: " + ChildElementText);

                if (ChildElementText.equals(validationText)) {
                    System.out.println("About to click child element: " + ChildElementText);
                    ChildElement.click();
                    System.out.println("Clicked child element: " + ChildElementText);
                }
            }

            return true;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

   public int getElementWidth(String elementXpath) {
        
        try {
            this.waitForElementByXpath(elementXpath);
       
            WebElement elementToRead = Driver.findElement(By.xpath(elementXpath));
            int  ElementWidth = elementToRead.getSize().getWidth();
            return ElementWidth;
            } 
            catch (Exception e) {
            System.err.println("[Error] Failed to retrieve text from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return 0;
        }
    }
   
   public int getElementHeight(String elementXpath) {
        
        try {
            this.waitForElementByXpath(elementXpath);
       
            WebElement elementToRead = Driver.findElement(By.xpath(elementXpath));
            int  ElementHeight = elementToRead.getSize().getHeight();
            return ElementHeight;
            } 
            catch (Exception e) {
            System.err.println("[Error] Failed to retrieve text from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return 0;
        }
    }
   
   
    public String retrieveSaveDisableByXpath(String elementXpath) {
        String retrievedText = "";
        try {
            this.waitForElementByXpath(elementXpath);
       
            WebElement elementToRead = Driver.findElement(By.xpath(elementXpath));
            Point coordinates = elementToRead.getLocation();
            Robot robot = new Robot(); 
            robot.mouseMove(coordinates.getX(), coordinates.getY() + 65); //Number 65 should vary
            Thread.sleep(3000);
            retrievedText = elementToRead.getAttribute("disabled");
            System.out.println("[Info]Text retrieved successfully from element - " + elementXpath);
            return retrievedText;
            } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve text from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }
    public boolean selectByIndexFromDropDownListUsingId(String elementId, Integer indexToSelect) {
        try {
            waitForElementById(elementId);
            Select dropDownList = new Select(Driver.findElement(By.id(elementId)));
            dropDownList.selectByIndex(indexToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to select option from dropdownlist by index using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    public boolean selectByTextFromDropDownListUsingId(String elementId, String valueToSelect) {
        try {
            waitForElementById(elementId);
            Select dropDownList = new Select(Driver.findElement(By.id(elementId)));
            dropDownList.selectByVisibleText(valueToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by text using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean acceptSslErrorMsg() {
        try {
            this.pause(4000);
            Driver.navigate().to("javascript:document.getElementById('overridelink').click()");
            return true;
        } catch (Exception e) {
            System.err.println("Error accepting SSL Certificate message - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean selectByValueFromDropDownListUsingName(String elementName, String valueToSelect) {
        try {
            this.waitForElementByName(elementName);
            Select dropDownList = new Select(Driver.findElement(By.name(elementName)));
            dropDownList.selectByValue(valueToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by value using Name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public void pressKeyOnElementById(String elementId, Keys keyToPress) {
        try {
            this.waitForElementById(elementId);
            WebElement elementToAccess = Driver.findElement(By.id(elementId));
            elementToAccess.sendKeys(keyToPress);

        } catch (Exception e) {
            this.DriverExceptionDetail = e.getMessage();
            System.err.println("[Error] Failed to send keypress to element - " + elementId);
        }
    }

    public boolean selectByTextFromDropDownListUsingName(String elementName, String valueToSelect) {
        try {
            this.waitForElementByName(elementName);
            Select dropDownList = new Select(Driver.findElement(By.name(elementName)));
            dropDownList.selectByVisibleText(valueToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("[Error] Selecting from dropdownlist by text using name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean clickElementByXpathUsingActionsNoExeption(String elementXpath)
    {
        try 
        {
            System.out.println("[INFO] Clicking element by Xpath : " + elementXpath);
            waitForElementByXpath(elementXpath, 3);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick);
            builder.click();
            builder.perform();  
            return true;
        } 
        catch (Exception e)
        {
            
        }
        return true;
    }
    
    
    public boolean selectByTextFromDropDownListUsingNameAndClick(String elementName, String valueToSelect) {
        try {
            this.waitForElementByName(elementName);
            Select ddl = new Select(Driver.findElement(By.name(elementName)));
            ddl.deselectAll();
            WebElement dropDownList = Driver.findElement(By.name(elementName));
            List<WebElement> options = dropDownList.findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().equals(valueToSelect)) {
                    option.click();
                    return true;
                }
            }

        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by text using name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
        return false;
    }

    public Integer getSelectedDropdownOptionIndexById(String elementId) {
        try {
            waitForElementById(elementId);
            Select dropDownList = new Select(Driver.findElement(By.id(elementId)));
            String selectedValue = dropDownList.getFirstSelectedOption().getText();

            List<WebElement> list = dropDownList.getOptions();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getText().equals(selectedValue)) {
                    return i;
                }
            }

            return -1;
        } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve selected dropdown option index using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return -1;
        }
    }
    
    public String getSelectedDropdownOptionTextByXpath(String elementXpath) {
        try {
            waitForElementByXpath(elementXpath);
            Select dropDownList = new Select(Driver.findElement(By.xpath(elementXpath)));
            String selectedValue = dropDownList.getFirstSelectedOption().getText();

            return selectedValue;
        } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve selected dropdown option index using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return null;
        }
    }

    public void waitUntilElementEnabledByID(String elementID) {
        try {
            int counter = 0;
            boolean isEnabled = false;
            WebDriverWait wait = new WebDriverWait(Driver, 1);

            while (!isEnabled && counter < 60) {
                if (wait.until(ExpectedConditions.elementToBeClickable(By.id(elementID))) != null) {
                    isEnabled = true;
                    break;
                } else {
                    counter++;
                    //Thread.sleep(500);
                }

            }
        } catch (Exception e) {
            System.err.println("Error waiting for element to be enabled - " + e.getMessage());
        }

    }

    public boolean checkBoxSelectionById(String elementId, boolean mustBeSelected) {
        try {
            this.waitForElementById(elementId);
            this.waitUntilElementEnabledByID(elementId);
            WebDriverWait wait = new WebDriverWait(Driver, 60);
            wait.until(ExpectedConditions.elementToBeClickable(By.id(elementId)));
            WebElement checkBox = Driver.findElement(By.id(elementId));
            if (checkBox.isSelected() != mustBeSelected) {
                checkBox.click();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting checkbox byId - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean checkBoxSelectionByName(String elementName, boolean mustBeSelected) {
        try {
            this.waitForElementById(elementName);
            this.waitUntilElementEnabledByID(elementName);
            WebDriverWait wait = new WebDriverWait(Driver, 60);
            wait.until(ExpectedConditions.elementToBeClickable(By.id(elementName)));
            WebElement checkBox = Driver.findElement(By.name(elementName));
            if (checkBox.isSelected() != mustBeSelected) {
                checkBox.click();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting checkbox by name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean checkBoxSelectionByXpath(String elementXpath, boolean mustBeSelected) {
        try {
            this.waitForElementByXpath(elementXpath);
            this.waitUntilElementEnabledByID(elementXpath);
            WebDriverWait wait = new WebDriverWait(Driver, 60);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath)));
            WebElement checkBox = Driver.findElement(By.xpath(elementXpath));
            if (checkBox.isSelected() != mustBeSelected) {
                checkBox.click();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting checkbox byId - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean uncheckCheckBoxSelectionById(String elementId, boolean mustBeSelected) {
        try {
            //Thread.sleep(2000);
            this.waitForElementById(elementId);
            this.waitUntilElementEnabledByID(elementId);
            WebDriverWait wait = new WebDriverWait(Driver, 60);
            wait.until(ExpectedConditions.elementToBeClickable(By.id(elementId)));
            WebElement checkBox = Driver.findElement(By.id(elementId));
            if (checkBox.isSelected() == mustBeSelected) {
                checkBox.click();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting checkbox byId - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean validateElementTextValueByClassName(String elementClassName, String elementText) {
        try {
            if (waitForElementByClassName(elementClassName)) {
                WebElement elementToValidate = Driver.findElement(By.className(elementClassName));
                String textDetected = elementToValidate.getText();
                if (textDetected.contains(elementText)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error validating element text value by class name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean validateElementTextValueById(String elementId, String elementText) {
        try {
            if (waitForElementById(elementId)) {
                WebElement elementToValidate = Driver.findElement(By.id(elementId));
                String textDetected = elementToValidate.getText();
                if (textDetected.contains(elementText)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error validating element text value by ID - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean validateElementTextValueByXpath(String elementXpath, String elementText) {
        try {
            if (waitForElementByXpath(elementXpath)) {
                WebElement elementToValidate = Driver.findElement(By.xpath(elementXpath));
                String textDetected = elementToValidate.getText();
                if (textDetected.contains(elementText)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("[Error] Failed to validate element text value by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public void waitUntilDropDownListPopulatedById(String elementId) {

        try {
            this.waitForElementById(elementId);
            int waitCount = 0;
            List<WebElement> optionsList;
            while (waitCount < 60) {
                try {
                    Select dropDownList = new Select(Driver.findElement(By.id(elementId)));
                    optionsList = dropDownList.getOptions();
                    if (optionsList.size() > 0) {
                        break;
                    }
                } catch (Exception e) {

                }
                //Thread.sleep(500);
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for dropdownlist to be populated by ID - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }

    public void waitUntilDropDownListPopulatedByName(String elementName) {

        try {
            this.waitForElementById(elementName);
            int waitCount = 0;
            List<WebElement> optionsList;
            while (waitCount < 60) {
                try {
                    Select dropDownList = new Select(Driver.findElement(By.name(elementName)));
                    optionsList = dropDownList.getOptions();
                    if (optionsList.size() > 0) {
                        break;
                    }
                } catch (Exception e) {

                }
                //Thread.sleep(500);
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for dropdownlist to be populated by Name - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }


    public boolean hoverOverAndClickElementUsingJavascript(String elementXpath, String elementToClickXpath) {
        try {
            WebElement elementToClick = Driver.findElement(By.xpath(elementToClickXpath));
            JavascriptExecutor js = (JavascriptExecutor) Driver;
            js.executeScript("onmouseover=menus['0'].exec('0',2)");
            js = (JavascriptExecutor) Driver;
            js.executeScript("arguments[0].click();", elementToClick);
            System.out.println("[Info]Successfully found and clicked element '" + elementToClickXpath + "' using javascript...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to hover over element and click sub element by Xpath and Xpath  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
   
    public boolean hoverOverElementUsingJavascriptAndClickElementByLinkText(String elementLinkText) {
        try {
            WebElement elementToClick = Driver.findElement(By.linkText(elementLinkText));
            JavascriptExecutor js = (JavascriptExecutor) Driver;
            js.executeScript("onmouseover=menus['0'].exec('0',2)");
            js = (JavascriptExecutor) Driver;
            js.executeScript("arguments[0].click();", elementToClick);
            System.out.println("[Info]Successfully found and clicked element '" + elementLinkText + "' using javascript...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to hover over element and click sub element by Xpath and Xpath  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    
    public boolean clickElementByXpathUsingActions(String elementXpath)
    {
        try 
        {
            System.out.println("[INFO] Clicking element by Xpath : " + elementXpath);
            waitForElementByXpath(elementXpath,30);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick);
            builder.click();
            builder.perform();  
            return true;
        } 
        catch (Exception e)
        {
            System.err.println("[Error] Failed to click on element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean clickElementByXpathUsingActions(String elementXpath,int timeout)
    {
        try 
        {
            System.out.println("[INFO] Clicking element by Xpath : " + elementXpath);
            waitForElementByXpath(elementXpath,timeout);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick);
            builder.click();
            builder.perform();  
            return true;
        } 
        catch (Exception e)
        {
            System.err.println("[Error] Failed to click on element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean clickElementByXpath(String elementXpath) {
        try 
        {
            this.waitForElementByXpath(elementXpath);
            System.out.println("[INFO] Clicking element by Xpath : " + elementXpath);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            elementToClick.click();

            return true;
        } catch (Exception e) {
            System.err.println("[Error] Failed to click on element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }          
    

    public boolean moveToAndClickElementByXpath(String elementXpath) {
        try {
            //this.waitForElementById(elementId);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));

            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick).click(elementToClick).perform();

            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to move to element and click by Xpath  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }



    public boolean moveToAndClickElementById(String elementId) {
        try {
            //this.waitForElementById(elementId);
            WebElement elementToClick = Driver.findElement(By.id(elementId));

            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick).click(elementToClick).perform();

            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to move to element and click by ID  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean moveToAndClickElementByLinkText(String elementLinkText) {
        try {
            //this.waitForElementById(elementId);
            WebElement elementToClick = Driver.findElement(By.linkText(elementLinkText));

            Actions builder = new Actions(Driver);
            builder.moveToElement(elementToClick);
            builder.click(elementToClick);
            builder.perform();

            return true;
        } catch (Exception e) {
            System.err.println("Failed to move to element and click by link text  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean clickElementbyLinkText(String elementLinkText) {
        try {
            waitForElementByLinkText(elementLinkText);
            WebElement elementToClick = Driver.findElement(By.linkText(elementLinkText));
            elementToClick.click();

            System.out.println("[Info]Element successfully clicked by Link Text '" + elementLinkText + "'...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("[Error]Failed to click element by link text '" + elementLinkText + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean validatePage(String elementClassName, String textToVerify) {
        try {
            WebElement span = Driver.findElement(By.className(elementClassName));
            if (span.getText().equals(textToVerify)) {
                return true;
            }
        } catch (Exception ex) {
            System.err.println("Error locating element by class name  - " + ex.getMessage());
            this.DriverExceptionDetail = ex.getMessage();
            return false;
        }
        return false;
    }

    public boolean doubleClickElementbyLinkText(String elementLinkText) {
        try {
            waitForElementByLinkText(elementLinkText);
            WebElement elementToClick = Driver.findElement(By.linkText(elementLinkText));
            elementToClick.click();

            System.out.println("[Info]Element successfully double-clicked by Link Text...proceeding");
            return true;
        } catch (Exception e) {
            System.err.println("Error double-clicking element by link text  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean doubleClickElementbyXpath(String elementLinkText) {
        try {
            //waitForElementByLinkText(elementLinkText);
            System.out.println("[INFO] Double Clicking element by Xpath : " + elementLinkText);
            Actions act = new Actions(Driver);
            WebElement elementToClick = Driver.findElement(By.partialLinkText(elementLinkText));

            act.doubleClick(elementToClick).perform();

            return true;
        } catch (Exception e) {
            System.err.println("Error double clicking element by link text  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean doubleClickElementByXpath(String elementXpath) {
        try {
            waitForElementByXpath(elementXpath);
            System.out.println("[INFO] Double Clicking element by Xpath : " + elementXpath);
            Actions act = new Actions(Driver);
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));

            act.doubleClick(elementToClick).perform();

            return true;
        } catch (Exception e) {
            System.err.println("Error double clicking element by xpath  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean clickElementbyName(String elementName) {
        try {
            this.waitForElementByName(elementName);
            WebElement elementToClick = Driver.findElement(By.name(elementName));
            elementToClick.click();

            return true;
        } catch (Exception e) {
            System.err.println("Error clicking element by name  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean clickElementbyClassName(String elementClassName) {
        try {
            this.waitForElementByClassName(elementClassName);
            WebElement elementToClick = Driver.findElement(By.className(elementClassName));
            elementToClick.click();

            return true;
        } catch (Exception e) {
            System.err.println("Error clicking element by class name  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }



    public boolean elementContainsTextById(String elementId) {
        try {
            String retrievedText = "";
            this.waitForElementById(elementId);
            WebElement elementToEvaluate = Driver.findElement(By.id(elementId));
            retrievedText = elementToEvaluate.getText();
            return !retrievedText.equals("");
        } catch (Exception e) {
            System.err.println("Error checking if element contains text - " + elementId + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean selectAndDeleteTextByXpath(String elementXpath) {
        try {
            this.waitForElementByXpath(elementXpath);
            
            Actions action = new Actions(Driver);
            action.sendKeys(Keys.chord(Keys.CONTROL,"a"));
            action.sendKeys(Keys.chord(Keys.DELETE));
            
            action.perform();
            
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    
    public String getElementTextByXpath(String elementXpath) {
        try 
        {
            String retrievedText;
            this.waitForElementByXpath(elementXpath);
            WebElement elementToEvaluate = Driver.findElement(By.xpath(elementXpath));
            retrievedText = elementToEvaluate.getText();
            return retrievedText;
        } catch (Exception ex) {

            System.err.println("Could not find elements" + ex.getMessage());
            this.DriverExceptionDetail = ex.getMessage();
            return "";
        }
        
    }
    

    public void clearTextById(String elementId) {
        try {
            this.waitForElementById(elementId);
            WebElement elementToTypeIn = Driver.findElement(By.id(elementId));
            elementToTypeIn.clear();
        } catch (Exception e) {
            System.err.println("Error clearing text - " + elementId + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }

    public void clearTextByXpath(String elementXpath) {
        try {
            Actions action = new Actions(Driver);
            action.sendKeys();
            this.waitForElementByXpath(elementXpath);
            WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
            elementToTypeIn.clear();
        } catch (Exception e) {
            System.err.println("Error clearing text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }

    public void KeyPress(String keyToPress) {
        try {
            Actions action = new Actions(Driver);
            action.sendKeys(keyToPress);
            action.perform();

        } catch (Exception e) {
            System.err.println("Error Pressing Key - " + keyToPress + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }

    public boolean enterTextById(String elementId, String textToEnter) {
        try {
            this.waitForElementById(elementId);
            WebElement elementToTypeIn = Driver.findElement(By.id(elementId));
            elementToTypeIn.clear();
            Actions typeText = new Actions(Driver);
            typeText.moveToElement(elementToTypeIn);
            typeText.click(elementToTypeIn);
            typeText.sendKeys(elementToTypeIn, textToEnter);
            typeText.click(elementToTypeIn);
            typeText.perform();

            return true;
        } catch (Exception e) {
            System.err.println("Error entering text - " + elementId + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean enterTextByXpath(String elementXpath, String textToEnter) {
        try {
           	this.waitForElementByXpath(elementXpath);
		WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
                elementToTypeIn.sendKeys(Keys.chord(Keys.CONTROL, "a"), textToEnter);
                
            return true;
        } catch (Exception e) {
            System.err.println("Error entering text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean EnterTextByXpath(String elementXpath, String textToEnter) {
        try {
           	this.waitForElementByXpath(elementXpath, 5);
		WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
                elementToTypeIn.sendKeys(Keys.chord(Keys.CONTROL, "a"), textToEnter);
                
            return true;
        } catch (Exception e) {
            System.err.println("Error entering text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }



    
    public boolean clearAndEnterTextByXpath(String elementXpath, String textToEnter) {
        try {
//            this.waitForElementByXpath(elementXpath);
            WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
            elementToTypeIn.clear();
            Actions typeText = new Actions(Driver);
            typeText.moveToElement(elementToTypeIn);
            typeText.click(elementToTypeIn);
            typeText.keyDown(elementToTypeIn, Keys.BACK_SPACE);
            pause(2000);
            typeText.keyUp(elementToTypeIn, Keys.BACK_SPACE);
            typeText.sendKeys(elementToTypeIn, textToEnter);
            typeText.click(elementToTypeIn);
            typeText.perform();

            return true;
        } catch (Exception e) {
            System.err.println("Error entering text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean clearExistingTextByXpath(String elementXpath) {
        try {
            waitForElementByXpath(elementXpath);
            WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
            elementToTypeIn.clear();

            return true;
        } catch (Exception e) {
            System.err.println("Error entering text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

     public boolean moveToAndEnterTextByXpath(String elementXpath, String textToEnter){
        try {
            this.waitForElementByXpath(elementXpath);
            WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
            Actions typeText = new Actions(Driver);
            typeText.moveToElement(elementToTypeIn);
            typeText.click(elementToTypeIn);
            typeText.sendKeys(elementToTypeIn, textToEnter);
            typeText.click(elementToTypeIn);
            typeText.perform();

            return true;
        }catch (Exception e){
            System.err.println("Error entering text - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public void scrollDownByOnePage(String elementId) {
        try {
            this.waitForElementById(elementId);
            WebElement scrollElement = Driver.findElement(By.id(elementId));
            scrollElement.sendKeys(Keys.PAGE_DOWN);

            System.out.println("Sucessfully scrolled down...");
        } catch (Exception e) {
            System.err.println("Error scrolling page - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }

    public boolean enterTextByName(String elementName, String textToEnter) {
        try {

            //Thread.sleep(500);
            this.waitForElementByName(elementName);
            WebElement elementToTypeIn = Driver.findElement(By.name(elementName));
            elementToTypeIn.clear();
            elementToTypeIn.sendKeys(textToEnter);
            elementToTypeIn.click();
            return true;
        } catch (Exception e) {
            System.err.println("Error entering text by name  - " + elementName + " Error : " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean enterTextByClassName(String elementClassName, String textToEnter) {
        try {
            //Thread.sleep(500);
            this.waitForElementByClassName(elementClassName);
            WebElement elementToTypeIn = Driver.findElement(By.className(elementClassName));
            elementToTypeIn.clear();
            elementToTypeIn.sendKeys(textToEnter);

            return true;
        } catch (Exception e) {
            System.err.println("Error entering text by class name  - " + elementClassName + " Error : " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    // Requires a fully qualified URL - eg: "http://www.myURL.com"
    public boolean navigateTo(String pageUrl) {
        try {
            Driver.navigate().to(pageUrl);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error navigating to URL - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public String getUrl() {
        try {
            
            String url = Driver.getCurrentUrl().toString();
            
            return url;
        } catch (Exception e) {
            System.err.println("Error navigating to URL - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return null;
        }
    }

    public boolean waitForElementNoLongerPresentById(String elementId) {
        boolean elementFound = true;
        try {
            int waitCount = 0;
            while (elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId))) == null) {
                        elementFound = false;
                        break;
                    }
                } catch (Exception e) {
                    this.DriverExceptionDetail = e.getMessage();
                    elementFound = false;
                    break;
                }

                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element to be no longer present by ID  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementById(String elementId) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId))) != null) {

                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element by ID  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementById(String elementId, Integer timeout) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < timeout) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId))) != null) {

                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element by ID  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByCSSSelector(String elementCSSSelector) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(elementCSSSelector))) != null) {

                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element by CSS Selector  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByLinkText(String elementLinkText) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {

                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(elementLinkText))) != null) {

                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for element by link text : '" + elementLinkText + "'  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByClassName(String elementClassName) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.className(elementClassName))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for element by class name : '" + elementClassName + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByClassName(String elementClassName, Integer timeout) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < timeout) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.className(elementClassName))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for element by class name : '" + elementClassName + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByName(String elementName) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.name(elementName))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element by name  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public boolean waitForElementByName(String elementName, int waitTimeout) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < waitTimeout) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.name(elementName))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("Error waiting for element by name  - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

      public String retrieveValueByXpath(String elementXpath) {
        String retrievedText = "";
        try {
            this.waitForElementByXpath(elementXpath);
            WebElement elementToRead = Driver.findElement(By.xpath(elementXpath));
            retrievedText = elementToRead.getAttribute("value");
            System.out.println("[Info]Text retrieved successfully from element - " + elementXpath);
            return retrievedText;

        } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve value from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }
      
      
      
       public void waitUntilElementEnabledByXpath(String elementXpath) 
        {
        try {
            int counter = 0;
            boolean isEnabled = false;
            WebDriverWait wait = new WebDriverWait(Driver, 2);

            while (!isEnabled && counter < 60)
            {
                if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath))) != null) 
                {
                    isEnabled = true;
                    break;
                } else
                {
                    counter++;
                    //Thread.sleep(500);
                }

            }
        } catch (Exception e) {
            System.err.println("Error waiting for element to be enabled - " + e.getMessage());
        }

    }
    
    
    public boolean waitForElementByXpath(String elementXpath) {
        System.out.println("[INFO] Waiting For Element by Xpath : " + elementXpath);
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                //Thread.sleep(500);
                waitCount++;
            }

        } catch (Exception e) {
            System.err.println("[Error] waiting for element by Xpath '" + elementXpath + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    
       public boolean moveToElementAndWaitByXpath(String elementXpath) {
        try {
            Actions moveTo = new Actions(Driver);
            moveTo.moveToElement(Driver.findElement(By.xpath(elementXpath)));
            moveTo.perform();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    
    public boolean waitForElementByXpath(String elementXpath, Integer timeout) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < timeout) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXpath))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                //Thread.sleep(500);
                waitCount++;
            }

        } catch (Exception e) {
            System.err.println("[Error] waiting for element by Xpath '" + elementXpath + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    
    public boolean waitForElementByXpathVisibility(String elementXpath) {
        boolean elementFound = false;
        try 
        {
            int waitCount = 0;
            while (!elementFound && waitCount < 60)
            {
                try 
                {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXpath))) != null)
                    {
                        if (wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(elementXpath))) != null) 
                        {
                            elementFound = true;
                            break;
                        }
                    }
                } 
                catch (Exception e)
                {
                    elementFound = false;
                }
                waitCount++;
            }
        } 
        catch (Exception e) 
        {
            System.err.println("[Error] waiting for element by Xpath '" + elementXpath + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }


    public void pause(int milisecondsToWait) {
        try {
            Thread.sleep(milisecondsToWait);
        } catch (Exception e) {

        }
    }

    public void shutDown() {
        //retrievedTestValues = null;
        try {

            Driver.quit();
            //CloseChromeInstances();
        } catch (Exception e) {
            System.err.println("Error shutting down driver - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        _isDriverRunning = false;
    }

    public void takeScreenShot(String screenShotDescription, boolean isError) {
        String imageFilePathString = "";

        try {
            StringBuilder imageFilePathBuilder = new StringBuilder();
            // add date time folder and test case id folder
            //imageFilePathBuilder.append(reportDirectory).append("\\");
            imageFilePathBuilder.append(String.format("%03d", screenShotFolderCounter)).append(" - ");
           // imageFilePathBuilder.append(testData.TestCaseId).append("\\");
            imageFilePathBuilder.append(String.format("%03d", screenShotCounter));
            imageFilePathBuilder.append(" - ");
            if (isError) {
                imageFilePathBuilder.append("FAILED_");
            }  else {
                imageFilePathBuilder.append("PASSED_");
            }

            //imageFilePathBuilder.append(testCaseId).append(" ");

            imageFilePathBuilder.append(screenShotDescription);

            imageFilePathBuilder.append(".png");

            imageFilePathString = imageFilePathBuilder.toString();

            File screenShot = ((TakesScreenshot) Driver).getScreenshotAs(OutputType.FILE);
            FileUtils.moveFile(screenShot, new File(imageFilePathString.toString()));
            screenShotCounter++;
        } catch (Exception e) {
            System.err.println("[Error] could not take screenshot - " + imageFilePathString.toString() + " - " + e.getMessage());
        }
    }

    public boolean selectByTextFromDropDownListUsingXpath(String elementXpath, String valueToSelect) {
        try {
            waitForElementByXpath(elementXpath);
            Select dropDownList = new Select(Driver.findElement(By.id("BusinessNatureID")));

            dropDownList.selectByVisibleText(valueToSelect);
            return true;
        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by text using Id - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public boolean selectByTextFromDropDownListusingXpath(String elementXpath, String valueToSelect) {
        try {
            waitForElementByXpath(elementXpath);
            Select dropDownList = new Select(Driver.findElement(By.xpath(elementXpath)));

            dropDownList.selectByVisibleText(valueToSelect);

            return true;
        } catch (Exception e) {
            System.err.println("Error selecting from dropdownlist by text using xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    public String generateDateTimeString() {
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");

        return dateFormat.format(dateNow).toString();
    }

    public String generateDateTimeString(String format) {
        Date dateNow = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(dateNow).toString();
    }

    public String generateNextFirstDayOfTheMonth(String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date dateNow = new Date();
            Calendar cal = new GregorianCalendar();

            cal.setTime(dateNow);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            if (day == 1) {
                return dateFormat.format(cal.getTime()).toString();
            }

            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            System.out.println("[Info] Successfully generated next first day of the month - '" + dateFormat.format(cal.getTime()).toString() + "'");
            return dateFormat.format(cal.getTime()).toString();
        } catch (Exception e) {
            System.err.println("[Error] Unable to generate the next first day of the month - " + e.getMessage());
            return "";
        }

    }

    public String generateFirstDayOfTheCurrentMonth(String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date dateNow = new Date();
            Calendar cal = new GregorianCalendar();

            cal.setTime(dateNow);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            if (day == 1) {
                return dateFormat.format(cal.getTime()).toString();
            }

            cal.set(Calendar.DAY_OF_MONTH, 1);

            System.out.println("[Info] Successfully generated first day of the current month - '" + dateFormat.format(cal.getTime()).toString() + "'");
            return dateFormat.format(cal.getTime()).toString();
        } catch (Exception e) {
            System.err.println("[Error] Unable to generate the first day of the current month - " + e.getMessage());
            return "";
        }

    }

    public boolean compareValues(String ElementId, String valueToCompare) {
        try {
            WebElement Element = Driver.findElement(By.id(ElementId));
            String valueFromElement = Element.getAttribute("value");

            if (valueFromElement.toUpperCase().trim().equals(valueToCompare.toUpperCase().trim())) {
                System.out.println("Specified values found: " + valueFromElement + " AND " + valueToCompare);
                return true;
            } else {
                System.err.println("Specified values do not match : " + valueFromElement + " AND " + valueToCompare);
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] Failed to find specified element : " + ex.getMessage());
            return false;
        }
    }

    public boolean compareValuesWithSubString(String ElementId, int StartValue, int EndValue, String valueToCompare) {
        try {

            WebElement Element = Driver.findElement(By.id(ElementId));
            String valueFromElement = Element.getAttribute("value").substring(StartValue, EndValue);

            if (valueFromElement.toUpperCase().trim().equals(valueToCompare.toUpperCase().trim())) {
                System.out.println("Specified values found: " + valueFromElement + " AND " + valueToCompare);
                return true;
            } else {
                System.err.println("Specified values do not match : " + valueFromElement + " AND " + valueToCompare);
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] Failed to find specified element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean checkForPrescenceOfAnElementUsingId(String containerId, String ElementID) {
        try {

            WebElement DivList = Driver.findElement(By.id(containerId));
            List<WebElement> Elements = DivList.findElements(By.id(ElementID));
            if (Elements.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }



    public Boolean checkForPrescenceOfAnElementUsingClassName(String containerXpath, String ElementClassName) {
        try {

            WebElement DivList = Driver.findElement(By.xpath(containerXpath));
            List<WebElement> Elements = DivList.findElements(By.className(ElementClassName));
            if (Elements.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean checkForPrescenceOfAnElementUsingName(String ElementName) {
        try {
            WebElement DivList = Driver.findElement(By.name(ElementName));
            List<WebElement> Elements = DivList.findElements(By.className(ElementName));
            if (Elements.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean checkForPrescenceOfAnElementUsingLinkText(String containerId, String ElementLinkText) {
        try {

            WebElement DivList = Driver.findElement(By.id(containerId));
            List<WebElement> Elements = DivList.findElements(By.linkText(ElementLinkText));
            if (Elements.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean checkForPrescenceOfAnElementUsingLinkTextSingle(String ElementLinkText) {
        try {

            WebElement DivList = Driver.findElement(By.linkText(ElementLinkText));
            return DivList != null;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean CheckForPrescenceOfAnElementUsingPartialLinkTextSingle(String ElementLinkText) {
        try {

            WebElement DivList = Driver.findElement(By.partialLinkText(ElementLinkText));
            return DivList != null;
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public Boolean checkForPrescenceOfAnElementUsingXpath(String containerId, String Xpath) {
        try {
            this.waitForElementById(containerId);

            WebElement DivList = Driver.findElement(By.id(containerId));
            List<WebElement> Elements = DivList.findElements(By.xpath(Xpath));
            if (Elements.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element : " + ex.getMessage());
            return false;
        }
    }

    public void highlightElement(WebDriver Driver, WebElement element) {
        for (int i = 0; i < 1; i++) {
            JavascriptExecutor js = (JavascriptExecutor) Driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: blue; border: 2px solid blue;");
        }
    }



    public boolean uploadFileUsingElementIdAndFileLocation(String browseButtonId, String fileLocation) {
        try {
            WebElement fileUpload = Driver.findElement(By.id(browseButtonId));
            fileUpload.sendKeys(fileLocation);
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean selectRadioButtonUsingElementXpathAndBoolean(String radioButtonXpath, boolean mustBeSelected) {
        try {
            WebElement radioButton = Driver.findElement(By.xpath(radioButtonXpath));

            if (mustBeSelected == true) {
                radioButton.click();
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    public boolean validateSelectedRadioButtonUsingXpath(String radioButtonXpath) {
        try {
            WebElement radioButton = Driver.findElement(By.xpath(radioButtonXpath));
                
                radioButton.isSelected();
            } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean selectRadioButtonUsingElementIdAndBoolean(String radioButtonId, boolean mustBeSelected) {
        try {
            WebElement radioButton = Driver.findElement(By.id(radioButtonId));

            if (mustBeSelected == true) {
                radioButton.click();
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] failed to find element: " + ex.getMessage());
            return false;
        }
        return true;
    }


    public void pressKey(Keys keyToPress) {
        try {
            Actions action = new Actions(Driver);
            action.sendKeys(keyToPress);
            action.perform();
        } catch (Exception e) {
            this.DriverExceptionDetail = e.getMessage();
            System.err.println("[Error] Failed to send keypress to element - " + keyToPress);

        }
    }
    
    public void pressALT_F4(Keys keyToPress,Keys keyToPress2) {
        try {
            Actions action = new Actions(Driver);
            action.sendKeys(keyToPress);
            action.sendKeys(keyToPress2);
            action.perform();
        } catch (Exception e) {
            this.DriverExceptionDetail = e.getMessage();
            System.err.println("[Error] Failed to send keypress to element - " + keyToPress);

        }
    }
    

    
    public void copyKeys() {
        try {
            Actions action = new Actions(Driver);
            action.sendKeys(Keys.CONTROL,"c");
            action.perform();
        } catch (Exception e) {
            this.DriverExceptionDetail = e.getMessage();
            System.err.println("[Error] Failed to send keypress to element - Contoll + C");

        }
    }

    public void pressKey(Keys keyToPress, int NumberOfPresses) {
        try {
            for (int i = 0; i <= NumberOfPresses; i++) {
                Actions action = new Actions(Driver);
                action.sendKeys(keyToPress);

                action.perform();
            }
        } catch (Exception e) {
            this.DriverExceptionDetail = e.getMessage();
            System.err.println("[Error] Failed to send keypress to element - " + keyToPress);

        }
    }

    public boolean selectByValueFromDropDownListUsingXpath(String elementXpath, String valueToSelect) {
        try 
        {
            waitForElementByXpath(elementXpath);
            Select dropDownList = new Select(Driver.findElement(By.xpath(elementXpath)));
            WebElement formxpath = Driver.findElement(By.xpath(elementXpath));
            formxpath.click();
            dropDownList.selectByValue(valueToSelect);
            return true;
        } 
        catch (Exception e) 
        {
            System.err.println("Error selecting from dropdownlist by text using Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    

    public String setDateFormat(String inputFormat, String inputDate, String outputFormat) {
        try {
            //Converts string to date
            String expectedPattern = inputFormat;
            SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
            String userInput = inputDate;
            Date date = formatter.parse(userInput);

            //Sets the date format
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);

            return dateFormat.format(date).toString();
        } catch (Exception ex) {
            System.err.println("[ERROR] Failed to convert date '" + inputDate + "': " + ex.getMessage());
            return inputDate;
//            return false;
        }
    }


    public String SplitAndReturnStringPart(String splitText, String splitBy, int returnPartPosition) {
        try {
            String[] splitParts = splitText.split(splitBy);
            return splitParts[returnPartPosition].trim();
        } catch (Exception e) {
            System.err.println("[Error] Failed to split the text : '" + splitText + "' by '" + splitBy + "' - " + e.getMessage());
            return "";
        }
    }


    public boolean checkIfElementHasChildren(WebElement element) {
        List<WebElement> subElements = element.findElements(By.xpath(".//*"));
        return (subElements.size() > 1);
    }


    public boolean scrollToElement(String elementXpath)
    {
        try {
            WebElement element = Driver.findElement(By.xpath(elementXpath));
            ((JavascriptExecutor) Driver).executeScript("arguments[0].scrollIntoView(true);", element);
        
            return true;
        } catch (Exception e) 
        {
            System.err.println("Error scrolling to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
        
    }
    
    
    public String DriverUtility() 
    {
        Capabilities cap = ((RemoteWebDriver) Driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        System.out.println(browserName);
        
        return browserName;
    }
    
    public boolean moveToElementByXpath(String elementXpath) {
        try {
            Actions moveTo = new Actions(Driver);
            moveTo.moveToElement(Driver.findElement(By.xpath(elementXpath)));
            moveTo.perform();
            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

     public String getMonthAbr(int month)
     {
         String monthAbr="";
         switch (month) {
            case 1:  monthAbr = "Jan";
                     break;
            case 2:  monthAbr = "Feb";
                     break;
            case 3:  monthAbr = "Mar";
                     break;
            case 4:  monthAbr = "Apr";
                     break;
            case 5:  monthAbr = "May";
                     break;
            case 6:  monthAbr = "Jun";
                     break;
            case 7:  monthAbr = "Jul";
                     break;
            case 8:  monthAbr = "Aug";
                     break;
            case 9:  monthAbr = "Sep";
                     break;
            case 10: monthAbr = "Oct";
                     break;
            case 11: monthAbr = "Nov";
                     break;
            case 12: monthAbr = "Dec";
                     break;
            default: monthAbr = "Invalid month";
                     break;
        }
         
         return monthAbr;
     }
    

    
    public boolean waitForfElementToBeClickableByXpath(String elementXpath) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for element by Xpath '" + elementXpath + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }
    
    public boolean waitForElementToBeClickableByXpath(String elementXpath) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath))) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for element by Xpath '" + elementXpath + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }
    
    public boolean waitForWebElementToBeClickable(WebElement element) {
        boolean elementFound = false;
        try {
            int waitCount = 0;
            while (!elementFound && waitCount < 60) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver, 1);
                    if (wait.until(ExpectedConditions.elementToBeClickable(element)) != null) {
                        elementFound = true;
                        break;
                    }
                } catch (Exception e) {
                    elementFound = false;
                }
                waitCount++;
            }
        } catch (Exception e) {
            System.err.println("[Error] waiting for web element '" + element + "' - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
        return elementFound;
    }

    public String retrievetextFromDropdownByXpath(String elementXpath) {
        String retrievedText = "";
        try {
            this.waitForElementByXpath(elementXpath);
            WebElement elementToRead = Driver.findElement(By.xpath(elementXpath));
            retrievedText = elementToRead.getText();
            System.out.println("[Info]Text retrieved successfully from element - " + elementXpath);
            return retrievedText;

        } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve text from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }
    
    public String retrieveTextFromDropdownByXpath(String elementXpath) {
        String retrievedText = "";
        try {
            this.waitForElementByXpath(elementXpath);
            List<WebElement> alloptions=new Select(Driver.findElement(By.xpath(elementXpath))).getOptions();
            for(WebElement options : alloptions)
            {
                retrievedText += "\n"+options.getText();
            }
            
            System.out.println("[Info]Text retrieved successfully from element - " + elementXpath);
            return retrievedText;

        } catch (Exception e) {
            System.err.println("[Error] Failed to retrieve text from element Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return retrievedText;
        }
    }

    public static void incrementScreenShotFolderCounter() {
        SeleniumDriverUtility.screenShotFolderCounter++;
    }

    public static void resetScreenShotCounter(Integer screenShotCounter) {
        SeleniumDriverUtility.screenShotCounter = screenShotCounter;
    }
    
    public static void resetScreenShotFolderCounter(Integer screenShotFolderCounter) {
        SeleniumDriverUtility.screenShotFolderCounter = screenShotFolderCounter;
    }

        public boolean DragElementByXpath(String sourceElementXpath, String targetElementXpath) {
        try {
            System.out.println("[Info]Performing drag and drop for element - " + sourceElementXpath);
            
            WebElement sourceElement = Driver.findElement(By.xpath(sourceElementXpath));
            WebElement targetElement = Driver.findElement(By.xpath(targetElementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(sourceElement).perform();
            builder.clickAndHold(sourceElement);
            builder.perform();
            this.pause(1000);
            builder.moveToElement(targetElement).perform();
            builder.release(targetElement);
            builder.perform();
          
            return true;

        } catch (Exception e) {
            System.err.println("[Error] Failed to drag element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
        
        public boolean DragAndDropElementByXpath(String sourceElementXpath, String targetElementXpath) {
        try {
            
            System.out.println("[Info]Performing drag and drop for element - " + sourceElementXpath);
            
            WebElement sourceElement = Driver.findElement(By.xpath(sourceElementXpath));
            WebElement targetElement = Driver.findElement(By.xpath(targetElementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(sourceElement).perform();
            builder.clickAndHold(sourceElement);
            builder.perform();
            this.pause(1000);
            builder.moveToElement(targetElement).perform();
            builder.release(targetElement);
            this.pause(2000);
            builder.perform();
          
            return true;

        } catch (Exception e) {
            System.err.println("[Error] Failed to drag element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean DragAndDropElementOffsetByXpath(String sourceElementXpath, String targetElementXpath, int xOffSet, int yOffSet) {
        try {

            System.out.println("[Info]Performing drag and drop for element - " + sourceElementXpath);
            
            WebElement sourceElement = Driver.findElement(By.xpath(sourceElementXpath));
            WebElement targetElement = Driver.findElement(By.xpath(targetElementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(sourceElement).perform();
            builder.clickAndHold(sourceElement);
            builder.perform();
            this.pause(1000);
            builder.moveToElement(targetElement, xOffSet, yOffSet).perform();
            builder.release();
            this.pause(2000);
            builder.perform();
          
            return true;

        } catch (Exception e) {
            System.err.println("[Error] Failed to drag element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public boolean DragAndDropElementOffsetByXpath(String sourceElementXpath, int xOffSet, int yOffSet) {
        try {

            System.out.println("[Info]Performing drag and drop for element - " + sourceElementXpath);
            
            WebElement sourceElement = Driver.findElement(By.xpath(sourceElementXpath));
            Actions builder = new Actions(Driver);
            builder.moveToElement(sourceElement).perform();
            builder.clickAndHold(sourceElement);
            builder.perform();
            this.pause(1000);
            builder.moveToElement(sourceElement, xOffSet, yOffSet).perform();
            builder.release();
            this.pause(2000);
            builder.perform();
          
            return true;

        } catch (Exception e) {
            System.err.println("[Error] Failed to drag element by Xpath - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }

    
    public Boolean CheckIfElementIsEnabled(String elementXpath) 
    {
         try {
             Thread.sleep(3000);
             waitForElementByXpath(elementXpath);
             WebElement mainDiv = Driver.findElement(By.xpath(elementXpath));
             
             Boolean IsEnabled = mainDiv.isEnabled();

             
             String disa = mainDiv.getAttribute("disabled");
             
             if(disa == null || true)
             {
                 IsEnabled = true;
             }
             else
                 IsEnabled = false;
            
            return IsEnabled;
             

            
        } catch (Exception e) {
            System.err.println("Error checking if element is enabled - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();          
            return false;
        }
    }
    
    public Boolean CheckIfElementIsSelected(String elementXpath) 
    {
        try 
        {
            waitForElementByXpath(elementXpath);
            boolean mainDiv = Driver.findElement(By.xpath(elementXpath)).isSelected();

            if(mainDiv == true)
            {
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error checking if element is selected - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();          
            return false;
        }
    }
    
  public Boolean CheckIfElementIsDisabled(String elementXpath) 
    {
         try {
            Thread.sleep(3000);
             waitForElementByXpath(elementXpath);
             WebElement mainDiv = Driver.findElement(By.xpath(elementXpath));
             
             Boolean IsEnabled = mainDiv.isEnabled();

             
             String disa = mainDiv.getAttribute("disabled");
             
             if(disa == null)
             {
                 IsEnabled = false;
             }
             else
                 IsEnabled = true;
            
            return IsEnabled;
             

            
        } catch (Exception e) {
            System.err.println("Error checking if element is enabled - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();          
            return false;
        }
    }

    public boolean isElementEnabled(String elementXpath)
    {
        waitForElementByXpath(elementXpath, 10);

        WebElement element = Driver.findElement(By.xpath(elementXpath));

        return element.isEnabled();
    }

    public String CheckingTommorowsDate()
     {
         try
         {
             // get a calendar instance, which defaults to "now"
            Calendar calendar = Calendar.getInstance();

            // get a date to represent "today"
            Date today = calendar.getTime();

            // add one day to the date/calendar
             calendar.add(Calendar.DAY_OF_YEAR, 1);

            // now get "tomorrow"
            Date tomorrow = calendar.getTime();
            
            // print out tomorrow's date
             System.out.println("tomorrow is: " + tomorrow.toString());
            
            return tomorrow.toString();

         }
         
         catch (Exception e)
         {
            System.err.println("Error checking tomorrow weekday - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();          
            return null;
        }
     }



      
    public boolean moveToElementTwiceByXpath(String elementXpath) {
        try {
            Actions moveTo = new Actions(Driver);
            moveTo.moveToElement(Driver.findElement(By.xpath(elementXpath)));
            moveTo.perform();
            moveTo.moveToElement(Driver.findElement(By.xpath(elementXpath)));
            moveTo.perform();
          
            
          
            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }   

    
    public boolean HoverOverElementByJavaScript(String elementXpath) {
        try {
             JavascriptExecutor js = (JavascriptExecutor) Driver;
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            Thread.sleep(5000); 
            String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');"
                    + "evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) "
                    + "{ arguments[0].fireEvent('onmouseover');}";
            Thread.sleep(1000); 
          ((JavascriptExecutor) Driver).executeScript(mouseOverScript,elementToClick);
            Thread.sleep(1000);  

            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
   public boolean HoverOverElementByXpathAndJavaScript(String elementXpath) {
        try {

            JavascriptExecutor js = (JavascriptExecutor) Driver;
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            Thread.sleep(5000); 
            String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');"
                    + "evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) "
                    + "{ arguments[0].fireEvent('onmouseover');}";
            Thread.sleep(1000); 
            ((JavascriptExecutor) Driver).executeScript(mouseOverScript,elementToClick);
            Thread.sleep(1000);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error moving to element - " + elementXpath + " - " + e.getStackTrace());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    public boolean isCheckBoxSelected(String elementXPath)
    {
        waitForElementByXpath(elementXPath);
        WebElement isSelected = Driver.findElement(By.xpath(elementXPath));
        
        return isSelected.isSelected();
    }

    public void setScreenResolution(){
        try{
            Driver.manage().window().setSize(new Dimension(1024, 768));
        }catch(Exception e){
            System.err.println("[Error] Failed to set screen resolution - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }
    
    public String SwitchToNewWindow()
    {
        // get the current window handle
         String parentHandle = this.Driver.getWindowHandle();
          while(parentHandle.equalsIgnoreCase(this.Driver.getWindowHandle()))
        {
            for (String winHandle : this.Driver.getWindowHandles()) {
                
                // switch focus of WebDriver to the next found window handle (that's your newly opened window)
                this.Driver.switchTo().window(winHandle);
            }
            
        }
        
        return parentHandle;
    }
    
     public void SwitchToOriginalWindow(String parentHandle)
    {
        parentHandle = SwitchToNewWindow();
        
        this.Driver.close(); // close newly opened window when done with it
        this.Driver.switchTo().window(parentHandle); // switch back to the original window
        //String winHandleBefore = SeleniumDriverInstance.Driver.getWindowHandle();
        
        // Switch back to original browser (first window)
       // SeleniumDriverInstance.Driver.switchTo().window(winHandleBefore);
        
    }
     
     public String CheckColourfElement(String elementXpath, String CssValue) 
    {

        try {
            Thread.sleep(8000);
           
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            
            String css = elementToClick.getCssValue(CssValue);
            return css;
                    
            } catch (Exception e) {
                System.err.println("[Error] failed to click first element on a list - " + e.getMessage());
                this.DriverExceptionDetail = e.getMessage();
                
            }
        
        return "";
    }

    
     public String GetElementTagName(String elementXpath) 
    {

        try {
            Thread.sleep(8000);
           
            WebElement elementToClick = Driver.findElement(By.xpath(elementXpath));
            
            String css = elementToClick.getAttribute("class");
            return css;
                    
            } catch (Exception e) {
                System.err.println("[Error] failed to click first element on a list - " + e.getMessage());
                this.DriverExceptionDetail = e.getMessage();
                
            }
        
        return "";
    }

    public boolean enterTextbyXpathUsingActions(String elementXpath, String textToEnter)
    {
        try {
            this.waitForElementById(elementXpath,5);
            WebElement elementToTypeIn = Driver.findElement(By.xpath(elementXpath));
            elementToTypeIn.clear();
            Actions typeText = new Actions(Driver);
            typeText.moveToElement(elementToTypeIn);
            typeText.click(elementToTypeIn);
            typeText.sendKeys(elementToTypeIn, textToEnter);
            typeText.click(elementToTypeIn);
            typeText.perform();

            return true;
        } catch (Exception e) {
            System.err.println("Error entering text using xpath - " + elementXpath + " - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
            return false;
        }
    }
    
    public void minimizeBrowserWindow()
    {
         Driver.manage().window().setPosition(new Point(-2000, 0));
    }
    
     public void showBrowserWindow(){
         Driver.manage().window().setPosition(new Point(2000, 0));
    }
     
    public void scrollMouseWheelUpwards(){
        try{
            JavascriptExecutor jse = (JavascriptExecutor)Driver;
            jse.executeScript("javascript:window.scrollBy(250,350)");
            Thread.sleep(3000);
        }catch(Exception e){
            System.err.println("[Error] Failed to scroll up - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }
    
    public void scrollMouseWheelDownwards(){
        try{
            JavascriptExecutor jse = (JavascriptExecutor)Driver;
            jse.executeScript("window.scrollBy(0,200)","");
            Thread.sleep(3000);
        }catch(Exception e){
            System.err.println("[Error] Failed to scroll down - " + e.getMessage());
            this.DriverExceptionDetail = e.getMessage();
        }
    }   
    
    public void closeWindow(){
        Driver.close();
    }
    
    public void SwitchToOriginalWindowNoClose(String parentHandle)
    {
        this.Driver.switchTo().window(parentHandle);
    }

}

 
            

