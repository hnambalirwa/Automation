package stepDefinition.Android;

import Reporting.ExtentManager;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.DriverFactory;
import utils.Log;

import java.net.MalformedURLException;

/**
 * Created by Harriet on 10/05/17.
 */
public class Hooks extends DriverFactory{

    @Before
    public void beforeHookfunction() throws MalformedURLException, InterruptedException
    {
        extent = ExtentManager.GetExtent();
        createDriver();

    }

    @After
    public void afterHookfunction() {
        try {

            extent.flush();
            extent.close();
            teardown();
        } catch (Exception e) {
            Log.error("Error : " + e.getMessage());

        }
    }
}
