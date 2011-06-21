package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import exe.question.*;

public class BoothsMultiplication {
    static PseudoCodeDisplay pseudo;
    static URI docURI;

    private static Random rand     = new Random();
    private static UniqueIDGen gen = new UniqueIDGen();

    //Definitions
    private static final boolean DEBUG = false;

    private static final double FONT_SIZE = 0.07;
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

        try{
            docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {}


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

        GAIGStrace trace = new GAIGStrace();

        //Reg M
        GAIGSregister RegM= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[0], FONT_SIZE);
        RegM.setLabel("M:    ");
        RegM.set(multiplicand);
        trace.add("RegM", RegM);
        show.writeSnap("M is the Multiplicand", docURI.toASCIIString(), easyPseudo(2), trace);

        //Reg A
        GAIGSregister RegA= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[1], FONT_SIZE);
        RegA.set("0");
        RegA.setLabel("A:    ");
        trace.add("RegA", RegA);
        show.writeSnap("A is initialized to Zero", docURI.toASCIIString(), easyPseudo(3), trace);

        //Reg Q
        GAIGSregister RegQ= new GAIGSarrayRegister(regSize, "", DEFAULT, mypoints[2], FONT_SIZE);
        RegQ.set(multiplier);
        RegQ.setLabel("Q:    ");
        trace.add("RegQ", RegQ);
        show.writeSnap("Q is the Multiplier\nThe final product will span A and Q",
            docURI.toASCIIString(), easyPseudo(4), trace);

        //Bit Q_1
        GAIGSregister Q_1 = new GAIGSarrayRegister(1,       "", DEFAULT, mypoints[3], FONT_SIZE);
        Q_1.set("0");
        Q_1.setLabel( "Q(-1):");
        trace.add("Q_1" , Q_1);
        mypoints = getPositions(1, numRows);
        show.writeSnap("Q_₁ is initialized to 0", docURI.toASCIIString(), easyPseudo(5), trace);
    
        RegM= RegM.copyTo(mypoints[0]);
        RegA= RegA.copyTo(mypoints[1]);
        RegQ= RegQ.copyTo(mypoints[2]);
        Q_1 = Q_1.copyTo(mypoints[3]);

        boothsAlgorithm(RegM, RegA, RegQ, Q_1, trace, 0, numLines(RegQ.toString() ), show);

        show.close();
    }

    // TODO All the color logic goes in here.
    /**
    * A recursive method which steps through Booth's Multiplication Algorithm, making the
    * appropriate calls to show's writeSnap through each iteration.
    * 
    * At each iteration, Q(0) and Q(-1) are evaluated. If there is an add/subtract, iter will be incremented
    * by 2, or else it will be incremented by 1. The function exits when iter is no longer less than or equal
    * to numLines.
    *
    * @param trace The GAIGStrace object to keep the run history of the algorithm.
    * @param iter The active "line" in the visualization.
    * @param numLines The total number of lines that will be displayed during the run of the visualization.
    * @param show The ShowFile object where the xml of the GAIGSregisters will be written.
    */
    public static void boothsAlgorithm(GAIGSregister M, GAIGSregister A, GAIGSregister Q,
        GAIGSregister Q_1, GAIGStrace trace, int iter, int numLines, ShowFile show)
            throws IOException {
        if (iter >= numLines) return;

        int partCalc = Q.getBit(Q.getSize()-1) - Q_1.getBit(0);

        GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
        GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");
        OldQ.setColor(OldQ.getSize()-1, BLUE);
        OldQ_1.setColor(0, BLUE);

        question quest1 = getType1Question(OldQ.getBit(OldQ.getSize()-1), OldQ_1.getBit(0), show); 

        String pseudoURI = easyPseudo(new int[0], new int[0]);

        show.writeSnap("Comparison", docURI.toASCIIString(), pseudoURI, quest1, trace);

        OldQ.setColor(OldQ.getSize()-1, DEFAULT);
        OldQ_1.setColor(0, DEFAULT);

        if (partCalc == 1 || partCalc == -1) {
            String title = "";
            if (partCalc == 1) {addIntoRegA(A, negateValue(M) ); title="Added -M to A";}
            else               {addIntoRegA(A, M)              ; title="Added  M to A";}
             
            pseudoURI = easyPseudo(new int[0], new int[0]);
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

        pseudoURI = easyPseudo(new int[0], new int[0]);

        show.writeSnap("Right Sign Preserving Shift", docURI.toASCIIString(), pseudoURI,
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
    * TODO @param @return
    */
    public static GAIGSregister[] addToTraceAndGenerateNext(GAIGSregister M, GAIGSregister A,
                        GAIGSregister Q, GAIGSregister Q_1, GAIGStrace trace, int iter, int numLines) {
        GAIGSregister[] ret = new GAIGSregister[4];

        trace.newLine();
        trace.add("RegM", M);
        trace.add("RegA", A);
        trace.add("RegQ", Q);
        trace.add("Q_1" , Q_1);

        Bounds[] mypoints = getPositions(iter+1, numLines);

        ret[0] = M.copyTo(mypoints[0]);
        ret[1] = A.copyTo(mypoints[1]);
        ret[2] = Q.copyTo(mypoints[2]);
        ret[3] = Q_1.copyTo(mypoints[3]);

        return ret;
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

    /**
    * Returns a random question of the "Which operation will occur next?" flavor.
    * Call at the beginnig of a loop iteration.
    *
    */
    public static question getType1Question(int Q0, int Q_1, ShowFile show) {
        int select = ((int)Math.abs(rand.nextInt() )) % 3;
        question ret = null;
        int pcalc = Q0 - Q_1;

        if (select == 0) {
            XMLmsQuestion ret1 = new XMLmsQuestion(show, gen.getID() );
            ret1.setQuestionText("Q(0) and Q(-1) are " + Q0 + " and " + Q_1 + 
                " respectively. Select all the operations that will occur on this iteration of the loop.");

            ret1.addChoice("Addition");
            ret1.addChoice("Subtraction");

            if (pcalc != 0) {
                ret1.setAnswer(pcalc == -1 ? 1 : 2);
            }

            ret1.addChoice("Arithmetic Right Shift");
            ret1.setAnswer(3);

            ret = ret1;
            
        }
        else if (select == 1) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, gen.getID() );
            ret1.setQuestionText("Which operation will occur on the next slide?");
            ret1.addChoice("Addition");
            ret1.addChoice("Subtraction");
            ret1.addChoice("Arithmetic Right Shift");
            ret1.addChoice("None of the above.");

            ret1.setAnswer(pcalc == 0 ? 3 : (pcalc == 1 ? 2 : 1) );

            ret = ret1;
        }
        else {
            XMLtfQuestion ret1 = new XMLtfQuestion(show, gen.getID() );
            ret1.setQuestionText("Both an addition (+M) and an arithmetic right shift will occur in the next iteration of the loop.");
            ret1.setAnswer(pcalc != 0 && pcalc != 1);

            ret = ret1;
        }

//      ret.shuffle();

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

    private static class UniqueIDGen {
        private int id;

        public UniqueIDGen() {
            id = 0;
        }

        public String getID() {return ("" + id++);}
    }

    private static String easyPseudo(int[] selected, int[] lineColors){
        try {
           return pseudo.pseudo_uri(new HashMap<String, String>(),
                                        selected, lineColors);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private static String easyPseudo(int[] selected){
        try {
           return pseudo.pseudo_uri(new HashMap<String, String>(),
                                        selected);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private static String easyPseudo(int selected){
        try {
           return pseudo.pseudo_uri(new HashMap<String, String>(),
                                        selected);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }
}
