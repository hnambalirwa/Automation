package screenAndPageObjects.Web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Log;

/**
 * Created by Harriet Nambalirwa on 30/06/17.
 */
public class LogIn_Page {
    private static WebElement element = null;

    public static WebElement txtbx_UserName(WebDriver driver){
        element = driver.findElement(By.id("log"));
        Log.info("Username text box found");

        return element;
    }

    public static WebElement txtbx_Password(WebDriver driver){
        element = driver.findElement(By.id("pwd"));
        Log.info("Password text box found");

        return element;
    }

   public static WebElement btn_LogIn(WebDriver driver){
       element = driver.findElement(By.id("login"));
       Log.info("Submit button found");

       return element;
   }
}
