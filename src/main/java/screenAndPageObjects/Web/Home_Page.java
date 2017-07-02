package screenAndPageObjects.Web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Log;

/**
 * Created by Harriet Nambalirwa on 30/06/17.
 */
public class Home_Page {

    private static WebElement element = null;

    public static WebElement lnk_MyAccount(WebDriver driver){
        element = driver.findElement(By.id("account"));
        Log.info("My Account link element found");

        return element;
    }

    public static WebElement lnk_LogOut(WebDriver driver){
        element = driver.findElement(By.id("account_logout"));
        Log.info("Log Out link element found");
        return element;
    }
}
