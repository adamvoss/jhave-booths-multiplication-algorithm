package exe.boothsMultiplication;


import java.io.*;
import java.util.*;
import exe.XMLParameterParser;
import org.jdom.JDOMException;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class boothsMultiplication{

    public static void main(String[] args)throws IOException, JDOMException
    {
        @SuppressWarnings("unchecked")
        Hashtable<String, String> param = XMLParameterParser.parseToHash(args[2]);

        if (param.containsKey("Booth's Multiplication Input") ) {
            String[] toPass = new String[3];
            toPass[0] = args[0] + ".sho";
            String[] temp = param.get("Booth's Multiplication Input").split(" ");
            toPass[1] = temp[0];
            toPass[2] = temp[1];

            BoothMultiplication.main(toPass);
        }
        else if (param.containsKey("Exercise 1 for Booth's Multiplication") ) {
            System.out.println("Made it here");
        }
        else if (param.containsKey("Exercise 2 for Booth's Multiplication") ) {
        }
        else if (param.containsKey("Exercise 3 for Booth's Multiplication") ) {
        }
        else {
        }

/*      String[] toPass = new String[3];
        toPass[0] = args[0] + ".sho";
        String[] temp = param.get("Booth's Multiplication Input").split(" ");
        toPass[1] = temp[0];
        toPass[2] = temp[1];

        BoothMultiplication.main(toPass);
*/
//      BoothsMultiplication.main(new String[] {args[0] + ".sho",
//                                             param.get("Multiplicand"),
//                                             param.get("Multiplier")});
     }
}
