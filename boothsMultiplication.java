package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import java.lang.Integer;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import exe.boothsMultiplication.*;

public class boothsMultiplication {
    static PseudoCodeDisplay pseudo; 
    static GAIGSregister RegM;
    static GAIGSregister RegA;
    static GAIGSregister RegQ;
    static GAIGSregister Q_1;
    static URI docURI;
    static String pseudoURI;

    public static void main(String args[]) throws IOException {
        //JHAVÉ Stuff
        ShowFile show = new ShowFile(args[0]);

        try{
        pseudo = new PseudoCodeDisplay("exe/boothsMultiplication/pseudocode.xml");
        } catch (JDOMException e){
            e.printStackTrace();
        }


        //Our Stuff
        String multiplicand = toBinary(Integer.parseInt(args[1]));
        String multiplier   = toBinary(Integer.parseInt(args[2]));

        final int regSize;
        if (multiplicand.length() > multiplier.length()){
        //    regSize=multiplicand.length();
        }
        else {
        //    regSize=multiplier.length();
        }
        regSize=4; //Because Chris likes it Random

        RegM= new ProtoRegister(regSize, "", "#999999", 0.05, 0.1, 0.3, 0.5, 0.07);
        RegA= new ProtoRegister(regSize, "", "#999999", 0.3,  0.1, 0.55,  0.5, 0.07);
        RegQ= new ProtoRegister(regSize, "", "#999999", 0.5, 0.1, 0.75, 0.5, 0.07);
        Q_1 = new ProtoRegister(1, "", "#999999", 0.7,  0.1, 0.9,  0.5, 0.07);

        RegM.set(multiplicand);
        RegA.set("0");
        RegQ.set(multiplier);
        Q_1.set("0");

        //Give Chris back his random behavior
        Random rand = new Random();
        RegM.set(toBinary(rand.nextInt()));
        RegA.set(toBinary(rand.nextInt()));
        RegQ.set(toBinary(rand.nextInt()));
        Q_1.set(toBinary(rand.nextInt()));
        

        //System.out.println(numLines("0111010") );

        RegM.setLabel("M:   ");
        RegA.setLabel("A:   ");
        RegQ.setLabel("Q:   ");
        Q_1.setLabel("Q(-1):");

        try{
        docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {
        }

        try {
        pseudoURI = pseudo.pseudo_uri(new HashMap<String, String>(), new int[0], new int[0]);
        } catch (JDOMException e) {
            e.printStackTrace();
        }


        show.writeSnap("Hi", docURI.toASCIIString(), pseudoURI, RegM, RegA, RegQ, Q_1);
        rightShift(RegA, RegQ, Q_1);
        show.writeSnap("Bye", docURI.toASCIIString(), pseudoURI, RegM, RegA, RegQ, Q_1);
        addIntoRegA(RegA, RegQ);
        show.writeSnap("ps", docURI.toASCIIString(), pseudoURI, RegM, RegA, RegQ, Q_1);
        boothsAlgorithmStep(RegM, RegA, RegQ, Q_1);
        show.writeSnap("curtail", docURI.toASCIIString(), pseudoURI, RegM, RegA, RegQ, Q_1);

        GAIGStrace trace = new GAIGStrace();
        trace.add("RegM", RegM);
        trace.add("RegA", RegA);
        trace.add("RegQ", RegQ);
        trace.add("Q_1" , Q_1);

        show.writeSnap("GAIGS me with a spoon", docURI.toASCIIString(), pseudoURI, trace); 

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

    public static GAIGSregister negateValue(GAIGSregister M) {
        GAIGSregister ret = new ProtoRegister(M.getSize() );
        int carry = 1;

        for (int i = M.getSize()-1; i >= 0; --i) {
            int negPart = 0;

            if (M.getBit(i) == 0) negPart = 1;
            else negPart = 0;

            ret.setBit((negPart + carry) % 2, i);
            carry = (negPart + carry) / 2;
        }

        return ret;
    }

    public static void boothsAlgorithmStep(GAIGSregister M, GAIGSregister A, GAIGSregister Q, GAIGSregister Q_1) {
        int partCalc = Q.getBit(Q.getSize()-1) - Q_1.getBit(0);

        if      (partCalc == 1)  addIntoRegA(A, negateValue(M) );
        else if (partCalc == -1) addIntoRegA(A, M);

        rightShift(A, Q, Q_1);
    }

    /**
    * What is ths supposed to be used for?
    */
    public static int numLines(String binNum) {
        int sum = 0;
        char prev = '0';

        for (int i = binNum.length()-1; i >= 0; --i) {
            if (binNum.charAt(i) == '0') sum += (prev == '0' ? 1 : 2);
            else sum += (prev == '1' ? 1 : 2);

            prev = binNum.charAt(i); 
        }

        return sum;
    }
 
    /**
    * Converts an int to its shortest-length two's complement binary represntative
    */
    public static String toBinary(int a){
        if (a<0){
            return Integer.toBinaryString(a).replaceFirst("11*", "1");
        }
        //positive numbers are already shortest length 
       return "0"+Integer.toBinaryString(a);
    }

    /**
    * Sign extends binStr by i bits
    */
    public static String signExtend(String binStr, int i){
        String firstBit = String.valueOf(binStr.charAt(0));
        String extension = "";
        while (i>0){extension = extension.concat(firstBit); i--;}
        return extension.concat(binStr);
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

    public void set(String binStr){
        //Empty String == 0
        if (binStr.isEmpty()){binStr="0";}
        
        //Expand string to register size
        if (binStr.length() < this.getSize()){
            binStr = boothsMultiplication.signExtend(binStr,this.getSize()-binStr.length());
        }

        //If string too big, cut off most significant bits
        binStr = binStr.substring(binStr.length()-this.getSize());
        for (int count = (this.getSize()-1); count >= 0; count--){
            this.setBit(Character.getNumericValue(binStr.charAt(count)), count);
        }

    }
}
