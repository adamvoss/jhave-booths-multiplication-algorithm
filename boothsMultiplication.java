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
    static GAIGSarray items2;
    static GAIGSarray Q_1;
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
	items2= new GAIGSarray(10, false, "", "#999999", 0.1, 0.1, 0.8, 0.9, 0.07);
	Q_1   = new GAIGSarray(1, false, "", "#999999", 0.05, 0.1, 0.5, 0.9, 0.07);
	fillDummyArray(items, 10);
	fillDummyArray(items2, 10);
	fillDummyArray(Q_1, 1);

        try{
        docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {
        }

        try {
        pseudoURI = pseudo.pseudo_uri(new HashMap<String, String>(), new int[0], new int[0]);
    } catch (JDOMException e) {
        // TODO Auto-generated catch block
	// TODO delete line above
        e.printStackTrace();
    }



        show.writeSnap("Hi", docURI.toASCIIString(), pseudoURI, items, items2, Q_1);
	rightShift(items, items2, Q_1, 10);
	show.writeSnap("Bye", docURI.toASCIIString(), pseudoURI, items, items2, Q_1);
	addIntoRegA(items2, items, 10);
	show.writeSnap("ps", docURI.toASCIIString(), pseudoURI, items, items2, Q_1);
	

        show.close();
    }

    public static void rightShift(GAIGSarray A, GAIGSarray Q, GAIGSarray Q_1, int length) {
	if (length < 1) return;

	Q_1.set(Q.get(length-1), 0);
	Object shiftOverToQ = A.get(length-1);

	for (int i = length - 1; i >= 1; --i) {
	    A.set(A.get(i-1), i);
	    Q.set(Q.get(i-1), i);
	}

  	    Q.set(shiftOverToQ, 0);
    }

    public static void addIntoRegA(GAIGSarray A, GAIGSarray toAdd, int length) {
	int carry = 0;
	int sum = 0;
	for (int i = length-1; i >= 0; --i) {
	    sum = carry + ((Integer) A.get(i) ).intValue() + ((Integer) toAdd.get(i) ).intValue();
	    A.set(sum % 2, i);
	    carry = sum / 2;
	}
    }

    public static void fillDummyArray(GAIGSarray dummy, int length) {
	Random rand = new Random();
	for (int i = 0; i < length; ++i) dummy.set(Math.abs(rand.nextInt() ) % 2, i);
    }	 
}
