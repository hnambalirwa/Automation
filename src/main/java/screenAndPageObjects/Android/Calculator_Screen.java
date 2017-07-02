package screenAndPageObjects.Android;

/**
 * Created by Harriet on 20/06/17.
 */
public class Calculator_Screen {

    //calculator buttons
    public static String dynamicCalculatorButtons()
    {
        return "com.htc.calculator:id/digit";
    }

    public String plusButtonId()
    {
        return "com.htc.calculator:id/plus";
    }

    public String equalsButtonName()
    {
        return "com.htc.calculator:id/equal";
    }

    public static String clearButtonId()
    {
        return "com.htc.calculator:id/del";
    }

    public static String backButtonId()
    {
        return "com.htc.calculator:id/back";
    }

    public static String displayTextAreaClassName()
    {
        return "android.widget.EditText";
    }

}
