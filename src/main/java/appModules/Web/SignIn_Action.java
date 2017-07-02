package appModules.Web;

import org.openqa.selenium.WebDriver;
import screenAndPageObjects.Web.Home_Page;
import screenAndPageObjects.Web.LogIn_Page;
import utils.Constant;
import utils.ExcelUtils;
import utils.Log;

/**
 * Created by Harriet Nambalirwa on 30/06/17.
 */
public class SignIn_Action {

    public static void Execute(WebDriver driver) throws Exception {

        //This is to get the values from Excel sheet, passing parameters (Row num &amp; Col num)to getCellData method


        String sUserName = ExcelUtils.getCellData(2, 1);
        Log.info("Username picked from Excel is " + sUserName);

        String sPassword = ExcelUtils.getCellData(2, 2);
        Log.info("Password picked from Excel is "+ sPassword );


        Home_Page.lnk_MyAccount(driver).click();
        Log.info("Click action performed on My Account link");


        LogIn_Page.txtbx_UserName(driver).sendKeys(sUserName);
        Log.info("Username entered in the Username text box");

        LogIn_Page.txtbx_Password(driver).sendKeys(sPassword);
        Log.info("Password entered in the Password text box");

        LogIn_Page.btn_LogIn(driver).click();
        Log.info("Click action performed on Submit button");

    }
}
