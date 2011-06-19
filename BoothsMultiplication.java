package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

public class BoothsMultiplication {
    static PseudoCodeDisplay pseudo;
    static GAIGSregister RegM;
    static GAIGSregister RegA;
    static GAIGSregister RegQ;
    static GAIGSregister Q_1;
    static URI docURI;
    static String pseudoURI;

    //Definitions
    private static final boolean DEBUG = false;

    public static final String DEFAULT = "#FFFFFF";
    public static final String WHITE   = "#FFFFFF";
    public static final String BLACK   = "#000000";
    public static final String GREY    = "#888888";
    public static final String RED     = "#FF0000";
    public static final String GREEN   = "#00FF00";
    public static final String BLUE    = "#0000FF";

    public static final double LEFT_MARGIN   =  0.1;
    public static final double REG_WIDTH  =  0.25;
    public static final double X_PAD  = -0.05;
    public static final double REG_HEIGHT  =  0.1;

    public static void main(String args[]) throws IOException {
        //JHAVÉ Stuff
        ShowFile show = new ShowFile(args[0]);

        //Load the Pseudocode
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
            regSize=multiplicand.length();
            multiplier = signExtend(multiplier, regSize-multiplier.length() );
        }
        else {
            regSize=multiplier.length();
            multiplicand = signExtend(multiplicand, regSize-multiplicand.length() );
        }

        int numRows = numLines(multiplier);
        Bounds[] mypoints = getPositions(0, numRows);

        RegM= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[0].x1, mypoints[0].y1,
            mypoints[0].x2, mypoints[0].y2, 0.07);
        RegA= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[1].x1, mypoints[1].y1,
            mypoints[1].x2, mypoints[1].y2, 0.07);
        RegQ= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[2].x1, mypoints[2].y1,
            mypoints[2].x2, mypoints[2].y2, 0.07);
        Q_1 = new GAIGSarrayRegister(1,       "", DEFAULT, mypoints[3].x1, mypoints[3].y1,
            mypoints[3].x2, mypoints[3].y2, 0.07);

        RegM.set(multiplicand);
        RegA.set("0");
        RegQ.set(multiplier);
        Q_1.set("0");

        mypoints = getPositions(1, numRows);

        GAIGSregister RegM2= RegM.copyTo(mypoints[0].x1, mypoints[0].y1,
            mypoints[0].x2, mypoints[0].y2, 0.07);
        GAIGSregister RegA2= RegA.copyTo(mypoints[1].x1, mypoints[1].y1,
            mypoints[1].x2, mypoints[1].y2, 0.07);
        GAIGSregister RegQ2= RegQ.copyTo(mypoints[2].x1, mypoints[2].y1,
            mypoints[2].x2, mypoints[2].y2, 0.07);
        GAIGSregister Q_12 = Q_1.copyTo(mypoints[3].x1, mypoints[3].y1,
            mypoints[3].x2, mypoints[3].y2, 0.07);

        RegM.setLabel("M:    ");
        RegA.setLabel("A:    ");
        RegQ.setLabel("Q:    ");
        Q_1.setLabel( "Q(-1):");

        try{
            docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {}

        try {
        pseudoURI = pseudo.pseudo_uri(new HashMap<String, String>(), new int[0], new int[0]);
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        GAIGStrace trace = new GAIGStrace();
        trace.add("RegM", RegM);
        trace.add("RegA", RegA);
        trace.add("RegQ", RegQ);
        trace.add("Q_1" , Q_1);

        boothsAlgorithm(RegM2, RegA2, RegQ2, Q_12, trace, 0, numLines(RegQ.toString() ), show);

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
        int carry = 1;
        GAIGSregister ret = new GAIGSarrayRegister(M.getSize() );

        for (int i = M.getSize()-1; i >= 0; --i) {
            int negPart = 0;

            if (M.getBit(i) == 0) negPart = 1;
            else negPart = 0;

            ret.setBit((negPart + carry) % 2, i);
            carry = (negPart + carry) / 2;
        }

        return ret;
    }

    public static void boothsAlgorithm(GAIGSregister M, GAIGSregister A, GAIGSregister Q,
        GAIGSregister Q_1, GAIGStrace trace, int iter, int numLines, ShowFile show)
            throws IOException {
        if (iter >= numLines) return;

        int partCalc = Q.getBit(Q.getSize()-1) - Q_1.getBit(0);

        GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
        GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");
        OldQ.setColor(OldQ.getSize()-1, BLUE);
        OldQ_1.setColor(0, BLUE);
        show.writeSnap("Comparison", docURI.toASCIIString(), pseudoURI, trace);

        OldQ.setColor(OldQ.getSize()-1, DEFAULT);
        OldQ_1.setColor(0, DEFAULT);

        if (partCalc == 1 || partCalc == -1) {
            String title = "";
            if (partCalc == 1) {addIntoRegA(A, negateValue(M) ); title="Added -M to A";}
            else               {addIntoRegA(A, M)              ; title="Added  M to A";}

            show.writeSnap(title, docURI.toASCIIString(), pseudoURI, trace, M, A, Q, Q_1);

            ++iter;
            GAIGSregister[] ret = addToTraceAndGenerateNext(M, A, Q, Q_1, trace, iter, numLines);

            OldQ   = Q;
            OldQ_1 = Q_1;
            M = ret[0];
            A = ret[1];
            Q = ret[2];
            Q_1=ret[3];

        }

        rightShift(A, Q, Q_1);
        show.writeSnap("Sign Preserving Shift", docURI.toASCIIString(), pseudoURI,
            trace, M, A, Q, Q_1);

        ++iter;
        GAIGSregister[] ret = addToTraceAndGenerateNext(M, A, Q, Q_1, trace, iter, numLines);

        OldQ   = Q;
        OldQ_1 = Q_1;
        M = ret[0];
        A = ret[1];
        Q = ret[2];
        Q_1=ret[3];

        boothsAlgorithm(M, A, Q, Q_1, trace, iter, numLines, show);
    }

    /**
    * Helper function to reduce copy pasta
    * Adds the GAIGSregisters to the GAIGStrace and generate the next 4.
    */
    public static GAIGSregister[] addToTraceAndGenerateNext(GAIGSregister M, GAIGSregister A, GAIGSregister Q, GAIGSregister Q_1, GAIGStrace trace,
        int iter, int numLines) {
        GAIGSregister[] ret = new GAIGSregister[4];

        trace.newLine();
        trace.add("RegM", M);
        trace.add("RegA", A);
        trace.add("RegQ", Q);
        trace.add("Q_1" , Q_1);

        Bounds[] mypoints = getPositions(iter+1, numLines);

        ret[0] = M.copyTo(mypoints[0].x1, mypoints[0].y1,
            mypoints[0].x2, mypoints[0].y2, 0.07);
        ret[1] = A.copyTo(mypoints[1].x1, mypoints[1].y1,
            mypoints[1].x2, mypoints[1].y2, 0.07);
        ret[2] = Q.copyTo(mypoints[2].x1, mypoints[2].y1,
            mypoints[2].x2, mypoints[2].y2, 0.07);
        ret[3] = Q_1.copyTo(mypoints[3].x1, mypoints[3].y1,
            mypoints[3].x2, mypoints[3].y2, 0.07);

        return ret;
    }

    /**
    * Calculates the number of lines the final display will occupy
    */
    public static int numLines(String binNum) {
    if (DEBUG){System.out.println("BinNum is: ");}
        int sum = binNum.length();
        char prev = '0';

        for (int i = binNum.length()-1; i >= 0; --i) {
            if (binNum.charAt(i) == '0') sum += (prev == '0' ? 0 : 1);
            else sum += (prev == '1' ? 0 : 1);

            prev = binNum.charAt(i);
        }

        return sum;
    }

    /**
    * Calculate the appropriate positions of the current line, based on interation
    * number and passed values, defaults.
    */
    public static Bounds[] getPositions(int iter, int numLines) {
        Bounds[] ret = new Bounds[4];
        double frac = 1.0 / numLines;

        for (int i = 0; i<4; ++i)
            ret[i] = new Bounds(LEFT_MARGIN+(i*(REG_WIDTH+X_PAD)), 1.0-(iter+1)*frac,
                 LEFT_MARGIN+((i+1)*REG_WIDTH)+(i*X_PAD),  (1.0-iter*frac)+REG_HEIGHT);

        return ret;
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
