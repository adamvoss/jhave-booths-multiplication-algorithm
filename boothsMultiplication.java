package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import exe.boothsMultiplication.*;

public class boothsMultiplication {
    static PseudoCodeDisplay pseudo;    // The pseudocode
    static GAIGSregister RegA;    // the array of RegA (stick display)
    static GAIGSregister RegQ;
    static GAIGSregister Q_1;
    static URI docURI;
    static String pseudoURI;

    public static void main(String args[]) throws IOException {
        ShowFile show = new ShowFile(args[0]);
        
        try{
        pseudo = new PseudoCodeDisplay("exe/boothsMultiplication/pseudocode.xml");
        } catch (JDOMException e){
            e.printStackTrace();
        }


        RegA= new ProtoRegister(4, "", "#999999", 0.05, 0.1, 0.5, 0.9, 0.07);
	RegQ= new ProtoRegister(4, "", "#999999", 0.4, 0.1, 0.8, 0.9, 0.07);
	Q_1 = new ProtoRegister(1, "", "#999999", 0.6, 0.1, 0.95, 0.9, 0.07);
	fillDummyRegister(RegA, RegA.getSize() );
	fillDummyRegister(RegQ, RegQ.getSize() );
	fillDummyRegister(Q_1, Q_1.getSize() );
	//System.out.println("Size of registers: " + RegA.getSize() );
	//System.out.println(RegA.getBit(0));

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

        show.writeSnap("Hi", docURI.toASCIIString(), pseudoURI, RegA, RegQ, Q_1);
	rightShift(RegA, RegQ, Q_1);
	show.writeSnap("Bye", docURI.toASCIIString(), pseudoURI, RegA, RegQ, Q_1);
	addIntoRegA(RegA, RegQ);
	show.writeSnap("ps", docURI.toASCIIString(), pseudoURI, RegA, RegQ, Q_1);
	

        show.close();
    }

    public static void rightShift(GAIGSregister A, GAIGSregister Q, GAIGSregister Q_1) {
	if (A.getSize() < 1) return;

	Q_1.setBit(Q.getBit(Q.getSize()-1), 0);
	int shiftOverToQ = A.getBit(Q.getSize()-1);

	for (int i = A.getSize() - 1; i >= 1; --i) {
	    A.setBit(A.getBit(i-1), i);
	    Q.setBit(Q.getBit(i-1), i);
	}

  	    Q.setBit(shiftOverToQ, 0);
    }

    public static void addIntoRegA(GAIGSregister A, GAIGSregister toAdd) {
	int carry = 0;
	int sum = 0;
	for (int i = A.getSize()-1; i >= 0; --i) {
	    sum = carry + A.getBit(i) + toAdd.getBit(i);
	    A.setBit(sum % 2, i);
	    carry = sum / 2;
	}
    }

    public static void fillDummyRegister(GAIGSregister dummy, int length) {
	Random rand = new Random();
	for (int i = 0; i < dummy.getSize(); ++i) {
	    Integer val = new Integer(Math.abs(rand.nextInt() ) % 2);
	    //System.out.println("Added " + val + " in fillDummyArray");
	    dummy.setBit(val, i);
	}
    }	 
}

class ProtoRegister implements GAIGSregister{
    private GAIGSarray wrapped;
    private int size;

    public ProtoRegister(GAIGSarray w, int len) {
	super();
	wrapped = w;
	size = len;
    }

    public ProtoRegister(int length) {
	super();
	wrapped = new GAIGSarray(1, length);
	size = length;
    }

    public ProtoRegister(int length, String name, String color, double x1, double y1, double x2, double y2, double fontSize) {
	super();
	wrapped = new GAIGSarray(1, length, name, color, x1, y1, x2, y2, fontSize);
	size = length;
    }

    public String getName() {
	return wrapped.getName();
    }

    public void setName(String name) {
	wrapped.setName(name);
    }

    public String toXML() {
	return wrapped.toXML();
    }

    public int getSize() {return size;}

    public int getBit(int loc) {return ((Integer) wrapped.get(0, loc));}

    public void setBit(int value, int loc) {wrapped.set(new Integer(value), 0, loc);}

    public void setBit(int value, int loc, String cl) {wrapped.set(new Integer(value), 0, loc, cl);}

    public void setLabel(String label) {wrapped.setRowLabel(label, 0);}

    public String getLabel() {return wrapped.getRowLabel(0);}

    public void setColor(int loc, String cl) {wrapped.setColor(0, loc, cl);}
}
