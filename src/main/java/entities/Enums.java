package entities;

/**
 * Created with IntelliJ IDEA.
 * User: Harriet
 * Date: 2017/05/26
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Enums
{
    public enum BrowserType {
        IE, FireFox, Chrome, Safari
    }
    
    public enum ResultStatus
    {
        PASS, FAIL, WARNING, UNCERTAIN
    }
    
    public enum RelativePosition
    {
        Above, Below, Right, Left
    }
    
    public enum Environment
    {
        // Add environment urls here, parameter order is defined by the constructor (Environment) definition below
        // Please note that adding an addtional environment type will require you to comma-seperate them.
        // Visit http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html to learn more about Java Enum declarations. 
       
        // Here we are declaring the Dev Environment type, and passing the following two properties, a url and a connection string, 
        // which are defined below as both being string literals:
        
        // DEV[FirstPageURL,FirstDatabaseConnectionString]
        
        INT("https://integration.mixtelematics.com"),
        UAT("https://beta.mixtelematics.com")
        ;
        
        
        // For each system (website1, database1, website2 etc.) within the defined environment (Dev, QA, Prod etc.)
        // you will have to declare the appropriate string to store its properties (URL or connection string etc.).
        public final String PageUrl;

        
        // This constructor defines and instantiates the parameters declared above. Parameter order is specified here and will 
        // define the order in which the enum types' properties are specified. 
        Environment(String pageUrl)
        {
            this.PageUrl = pageUrl;
            
        }
                
    }
    
    public static Environment resolveTestEnvironment(String environment)
        {
            switch (environment.toUpperCase())
            {
                case "INTEGRATION":
                    return Environment.INT;
                case "UAT":
                    return Environment.UAT;
                default:
                    return Environment.INT;
            }

        }
}
