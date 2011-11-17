package exe.boothsMultiplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import exe.XMLParameterParser;
import org.jdom.JDOMException;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class boothsMultiplication{

    private static final boolean DEBUG = false;

    private static PrintWriter writer;

    private static String BOOTH_STANDARD = "Booth's Multiplication Input"         ;
    private static String BOOTH_EX01     = "Exercise 1 for Booth's Multiplication";
    private static String BOOTH_EX02     = "Exercise 2 for Booth's Multiplication";
    private static String BOOTH_EX03     = "Exercise 3 for Booth's Multiplication";

    public static void main(String[] args)throws IOException, JDOMException
    {
        @SuppressWarnings("unchecked")
        Hashtable<String, String> param = XMLParameterParser.parseToHash(args[2]);

        try {
            if (DEBUG) writer = new PrintWriter("/home/christopher/jhave2-new/server/src/exe/boothsMultiplication/ex01.log");
        } catch (IOException e) {
            //Oh dear...
        }

        if (DEBUG) {
            writer.println("Beginning log");
            writer.flush();
        }

        if (param.containsKey(BOOTH_STANDARD) ) {
            String[] toPass = new String[3];
            toPass[0] = args[0] + ".sho";
            String[] temp = param.get(BOOTH_STANDARD).split(" ");
            toPass[1] = temp[0];
            toPass[2] = temp[1];

            BoothMultiplication booth = new BoothMultiplication();
            
            booth.execute(toPass);
        }
        else if (param.containsKey(BOOTH_EX01) ) {
            if (DEBUG) {
                writer.println("Successfully identified exercise 01");
                writer.flush();
            }

            ArrayList<String> toPass = new ArrayList<String>();
            toPass.add(args[0] + ".sho");
            String[] temp = param.get(BOOTH_EX01).split(" ");

            for (String str : temp)
                toPass.add(str);

            if (DEBUG) {
                writer.println("Length of argument list is " + toPass.size() + " and should be 8");

                for (String str : toPass)
                    writer.println("\t" + str);

                writer.flush();
            }

            Object[] tmp = toPass.toArray();
            String[] pass= new String[tmp.length];

            for (int i = 0; i < tmp.length; ++i)
                pass[i] = (String) tmp[i];

            BoothExercise01.main(pass);

            if (DEBUG) writer.println("Recovered from BoothExercise01.main");
            writer.flush();

        }
        else if (param.containsKey(BOOTH_EX02) ) {
            ArrayList<String> toPass = new ArrayList<String>();
            toPass.add(args[0] + ".sho");
            String[] temp = param.get(BOOTH_EX02).split(" ");

            for (String str : temp)
                toPass.add(str);

            if (DEBUG) {
                writer.println("Length of argument list is " + toPass.size() + " and should be 48");

                for (String str : toPass)
                    writer.println("\t" + str);

                writer.flush();
            }

            Object[] tmp = toPass.toArray();
            String[] pass= new String[tmp.length];

            for (int i = 0; i < tmp.length; ++i)
                pass[i] = (String) tmp[i];

            BoothExercise02.main(pass);

            if (DEBUG) writer.println("Recovered from BoothExercise01.main");
            writer.flush();


        }
        else if (param.containsKey(BOOTH_EX03) ) {
            String[] toPass = new String[3];
            toPass[0] = args[0] + ".sho";
            String[] temp = param.get(BOOTH_EX03).split(" ");
            toPass[1] = temp[0];
            toPass[2] = temp[1];

            BoothExercise03.main(toPass);
        }
        else {
        }

        if (DEBUG) {
            writer.println("Closing log file");
            writer.close();
        }
     }
}
