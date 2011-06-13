package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

public class boothsMultiplication {
    static PseudoCodeDisplay pseudo;    // The pseudocode
    static GAIGSarray items;    // the array of items (stick display)
    static URI docURI;
    static String pseudoURI;

    public static void main(String args[]) throws IOException {
        ShowFile show = new ShowFile(args[0]);
        
        try{
        pseudo = new PseudoCodeDisplay("exe/boothsMultiplication/pseudocode.xml");
        } catch (JDOMException e){
            e.printStackTrace();
        }


        items = new GAIGSarray(10, false, "",
                               "#999999", 0.2, 0.1, 0.9, 0.9, 0.07);

        try{
        docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {
        }

        try {
        pseudoURI = pseudo.pseudo_uri(new HashMap<String, String>(), new int[0], new int[0]);
    } catch (JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }



        show.writeSnap("Hi", docURI.toASCIIString(), pseudoURI, items);

        show.close();
    }
}
