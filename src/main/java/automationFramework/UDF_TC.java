package automationFramework;

import appModules.Web.SignIn_Action;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import screenAndPageObjects.Web.Home_Page;
import utils.Constant;
import utils.ExcelUtils;
import utils.Log;


/**
 * Created by Harriet Nambalirwa on 01/07/17.
 */
public class UDF_TC
{
    public WebDriver driver;
    private String sTestCaseName;
    private int iTestCaseRow;

    @Before
    public void beforeMethod() throws Exception {
        DOMConfigurator.configure("log4j.xml");
        //sTestCaseName = this.toString();

        sTestCaseName = utils.Utils.getTestCaseName(this.toString());
        Log.startTestCase(sTestCaseName);

        ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData, "Sheet 1");
        iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName,Constant.Col_TestCaseName);

        driver = utils.Utils.openBrowser(iTestCaseRow);

    }

    @Test
    public void main() throws Exception {
        SignIn_Action.Execute(driver);
        Log.info("Login Successfully, now it is the time to Log Off buddy.");

        Home_Page.lnk_LogOut(driver).click();
        Log.info("Click action is performed on Log Out link");

    }

    @After
    public void afterMethod() {
        driver.quit();

    }
}
