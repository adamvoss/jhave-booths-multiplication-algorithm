package exe.boothsMultiplication;


import java.io.*;
import java.util.*;
import exe.XMLParameterParser;
import org.jdom.JDOMException;

public class boothsMultiplication{

    public static void main(String[] args)throws IOException, JDOMException
    {
        @SuppressWarnings("unchecked")
		Hashtable<String, String> param = XMLParameterParser.parseToHash(args[2]);

        BoothsMultiplication.main(new String[] {args[0] + ".sho",
                                               param.get("Multiplicand"),
                                               param.get("Multiplier")});
     }
}
