package exe.boothsMultiplication;


import java.io.*;
import java.lang.*;
import java.util.*;
import exe.XMLParameterParser;
import org.jdom.JDOMException;

public class boothsMultiplication{

    public static void main(String[] args)throws IOException, JDOMException
    {
        Hashtable param = XMLParameterParser.parseToHash(args[2]);

        boolean userDefined = false;
        String inputData = "";
        String showFileName = args[0] + ".sho";

        BoothsMultiplication.main(new String[] {args[0] + ".sho",
                                               (String)param.get("Multiplicand"),
                                               (String)param.get("Multiplier")});
     }

}
