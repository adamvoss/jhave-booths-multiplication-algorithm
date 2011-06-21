package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import exe.question.*;

public class BoothsMultiplication {
    private static PseudoCodeDisplay pseudo;
    private static URI docURI;
    private static QuestionGenerator quest;

    //Definitions
    private static final boolean DEBUG = false;

    private static final double FONT_SIZE = 0.07;
    public static final String WHITE   = "#FFFFFF";
    public static final String BLACK   = "#000000";
    public static final String GREY    = "#888888";
    public static final String RED     = "#FF0000";
    public static final String GREEN   = "#00FF00";
    public static final String BLUE    = "#0000FF";
    public static final String YELLOW  = "#FFFF00";
    public static final String DEFAULT_COLOR = WHITE;
    
    public static final double WINDOW_HEIGHT = 1.0;
    public static final double WINDOW_WIDTH = 1.0;

    public static final double LEFT_MARGIN   =  0.0;
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
        //Trace finally defined, can now make the QuestionGenerator
    	quest = new QuestionGenerator(show, trace);

    	show.writeSnap("Hello Dave", docURI.toASCIIString(), easyPseudo(0), new GAIGSmonospacedText(.5, .5, "It looks like you've improved great deal"));
    	
        //Reg M
        GAIGSregister RegM= new GAIGSarrayRegister(regSize, "", DEFAULT_COLOR, mypoints[0], FONT_SIZE);
        RegM.setLabel("M:    ");
        RegM.set(multiplicand);
        trace.add("RegM", RegM);
        show.writeSnap("M is the Multiplicand", docURI.toASCIIString(), easyPseudo(2), trace);

        //Reg A
        GAIGSregister RegA= new GAIGSarrayRegister(regSize, "", DEFAULT_COLOR, mypoints[1], FONT_SIZE);
        RegA.set("0");
        RegA.setLabel("A:    ");
        trace.add("RegA", RegA);
        show.writeSnap("A is initialized to Zero", docURI.toASCIIString(), easyPseudo(3), trace);

        //Reg Q
        GAIGSregister RegQ= new GAIGSarrayRegister(regSize, "", DEFAULT_COLOR, mypoints[2], FONT_SIZE);
        RegQ.set(multiplier);
        RegQ.setLabel("Q:    ");
        trace.add("RegQ", RegQ);
        show.writeSnap("Q is the Multiplier\nThe final product will span A and Q",
            docURI.toASCIIString(), easyPseudo(4), trace);

        //Bit Q_1
        GAIGSregister Q_1 = new GAIGSarrayRegister(1,       "", DEFAULT_COLOR, mypoints[3], FONT_SIZE);
        Q_1.set("0");
        Q_1.setLabel( "Q(-1):");
        trace.add("Q_1" , Q_1);
        show.writeSnap("Q_₁ is initialized to 0", docURI.toASCIIString(), easyPseudo(5), trace);

        //Count
/*        GAIGSregister count = new GAIGSarrayRegister(1,     "", DEFAULT_COLOR, mypoints[4], FONT_SIZE);
        count.set("" + RegQ.getSize() );
        count.setLabel("Count:");
        trace.add("Count", count);
        show.writeSnap("Count is initialized to the number of bits in a register.", docURI.toASCIIString(), easyPseudo(6), trace);
*/

        //Count, take 2
        CountBox count = new CountBox(RegQ.getSize(), DEFAULT_COLOR, mypoints[4], FONT_SIZE);
        count.setLabel("Count");
        trace.add("Count", count);
        show.writeSnap("Count is initialized to the number of bits in a register.", docURI.toASCIIString(), easyPseudo(6), trace);

        mypoints = getPositions(1, numRows);
        RegM= RegM.copyTo(mypoints[0]);
        RegA= RegA.copyTo(mypoints[1]);
        RegQ= RegQ.copyTo(mypoints[2]);
        Q_1 = Q_1.copyTo(mypoints[3]);
        count=count.copyTo(mypoints[4]); //don't be fooled, not the same method.

        boothsAlgorithm(RegM, RegA, RegQ, Q_1, count, trace, 0, numRows, show);

        //Hack to show we are done.
        ((GAIGSregister)trace.get("RegA")).setAllToColor(YELLOW);
        ((GAIGSregister)trace.get("RegQ")).setAllToColor(YELLOW);
        show.writeSnap("Check the result.", docURI.toASCIIString(), easyPseudo(24), trace);

        show.close();
    }

    // TODO All the color logic goes in here.
    /**
    * A recursive method which steps through Booth's Multiplication Algorithm, making the
    * appropriate calls to show's writeSnap through each iteration.
    * 
    * At each iteration, Q(0) and Q(-1) are evaluated. If there is an add/subtract, curLine will be incremented
    * by 2, or else it will be incremented by 1. The function exits when curLine is no longer less than or equal
    * to numLines.
    *
    * @param trace The GAIGStrace object to keep the run history of the algorithm.
    * @param curLine The active "line" in the visualization.
    * @param numLines The total number of lines that will be displayed during the run of the visualization.
    * @param show The ShowFile object where the xml of the GAIGSregisters will be written.
    */
    public static void boothsAlgorithm(GAIGSregister M, GAIGSregister A, GAIGSregister Q,
        GAIGSregister Q_1, CountBox count, GAIGStrace trace, int curLine, int numLines, ShowFile show)
            throws IOException {

        CountBox OldCount=(CountBox)trace.get("Count");

        OldCount.setColor(YELLOW);
        show.writeSnap("Check the Value of Count", docURI.toASCIIString(), easyPseudo(8), trace);
        OldCount.setColor(DEFAULT_COLOR);

        if (curLine >= numLines) return;

        //Difference of the comparison bits
        int partCalc = Q.getBit(Q.getSize()-1) - Q_1.getBit(0);
        // 00 => 0 // 01 => -1 // 10 => 1 //

        //Why are we passing registers as parameters when you just get past ones off the trace
        //This seems messy
        GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
        GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");

        OldQ.setColor(OldQ.getSize()-1, BLUE);
        OldQ_1.setColor(0, BLUE);

        show.writeSnap("Comparison", docURI.toASCIIString(),
        		easyPseudo(new int[] {10, 14}), quest.getQuestion(1), trace);

        //Reset Color
        OldQ.setColor(OldQ.getSize()-1, DEFAULT_COLOR);
        OldQ_1.setColor(0, DEFAULT_COLOR);

        //This does more comparisons than necessary and does not support pseudocode.
        if (partCalc == 1 || partCalc == -1) {
            String title = "";
            int line = 0;
            if (partCalc == 1) {addIntoReg(A, negateValue(M) ); title="Added -M to A"; line = 11;}
            else               {addIntoReg(A, M)              ; title="Added  M to A"; line = 15;}
            
            A.setAllToColor(GREEN);
            show.writeSnap(title, docURI.toASCIIString(), easyPseudo(line), trace, M, A, Q, Q_1, count);
            A.setAllToColor(DEFAULT_COLOR);

            ++curLine;
            GAIGSdatastr[] ret = addToTraceAndGenerateNext(M, A, Q, Q_1, count, trace, curLine, numLines);

            //This is turning into variable soup, note the function seems to be straddling
            //Two different states of the registers
            M    = (GAIGSregister)ret[0];
            A    = (GAIGSregister)ret[1];
            Q    = (GAIGSregister)ret[2];
            Q_1  = (GAIGSregister)ret[3];
            count= (CountBox)ret[4];

        }
        //Gosh this function is getting long
        rightShift(A, Q, Q_1);

        //Shift color logic
        Q_1.setColor(0, BLUE);
        ((GAIGSregister)trace.get("RegQ")).setAllToColor(BLUE);
        Q.setAllToColor(BLUE);
        ((GAIGSregister)trace.get("RegA")).setAllToColor(GREEN);
        A.setAllToColor(GREEN);
        Q.setColor(0, GREEN);

        //This frame excludes count.
        show.writeSnap("Right Sign Preserving Shift", docURI.toASCIIString(), easyPseudo(20),
            trace, M, A, Q, Q_1);

        //Reset colors
        ((GAIGSregister)trace.get("RegA")).setAllToColor(DEFAULT_COLOR);
        ((GAIGSregister)trace.get("RegQ")).setAllToColor(DEFAULT_COLOR);
        A.setAllToColor(DEFAULT_COLOR);
        Q.setAllToColor(DEFAULT_COLOR);
        Q_1.setColor(0, DEFAULT_COLOR);

        ++curLine;

        //TODO: Ugly hack is ugly
        //count.set("" + (count.getBit(0) - 1) );
        count.decrement();
        show.writeSnap("Decrement count", docURI.toASCIIString(), easyPseudo(22), trace, M, A, Q, Q_1, count);

        GAIGSdatastr[] ret = addToTraceAndGenerateNext(M, A, Q, Q_1, count, trace, curLine, numLines);

        M    = (GAIGSregister)ret[0];
        A    = (GAIGSregister)ret[1];
        Q    = (GAIGSregister)ret[2];
        Q_1  = (GAIGSregister)ret[3];
        count= (CountBox)ret[4];

        //Finally Recurse
        boothsAlgorithm(M, A, Q, Q_1, count, trace, curLine, numLines, show);
    }

    /**
    * Helper function to reduce copy pasta
    * Adds the GAIGSregisters to the GAIGStrace and generate the next 4.
    * TODO @param @return
    */
    public static GAIGSdatastr[] addToTraceAndGenerateNext(GAIGSregister M, GAIGSregister A,
                        GAIGSregister Q, GAIGSregister Q_1, CountBox count, GAIGStrace trace, int curLine, int numLines) {
        GAIGSdatastr[] ret = new GAIGSdatastr[5];

        trace.newLine();
        trace.add("RegM", M);
        trace.add("RegA", A);
        trace.add("RegQ", Q);
        trace.add("Q_1" , Q_1);
        trace.add("Count", count);

        Bounds[] mypoints = getPositions(curLine+1, numLines);

        ret[0] = M.copyTo(mypoints[0]);
        ret[1] = A.copyTo(mypoints[1]);
        ret[2] = Q.copyTo(mypoints[2]);
        ret[3] = Q_1.copyTo(mypoints[3]);
        ret[4] = count.copyTo(mypoints[4]);

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

    /**
     * Adds two registers, storing the result in the first register (a la Intel Syntax).
     * @param A	Destination Register and addend.
     * @param toAdd other addend, not modified by function.
     */
    public static void addIntoReg(GAIGSregister A, GAIGSregister toAdd) {
        int carry = 0;
        int sum = 0;
        for (int i = A.getSize()-1; i >= 0; --i) {
            sum = carry + A.getBit(i) + toAdd.getBit(i);
            A.setBit(sum % 2, i);
            carry = sum / 2;
        }
    }
//TODO add method which does addition logic on Strings.

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
    * Calculate the appropriate positions of the current line, based on iteration
    * number and passed values, defaults.
    * 
    * @return ret Array of bounds
    */
    private static Bounds[] getPositions(int curLine, int numLines) {
        Bounds[] ret = new Bounds[5];
        double frac = WINDOW_HEIGHT / numLines;

        for (int i = 0; i<ret.length; ++i)
            ret[i] = new Bounds(
            		LEFT_MARGIN+(i*(REG_WIDTH+X_PAD)),
            		WINDOW_HEIGHT-(curLine+1)*frac,
            		LEFT_MARGIN+((i+1)*REG_WIDTH)+(i*X_PAD),
            		(WINDOW_HEIGHT-curLine*frac)+REG_HEIGHT);
        return ret;
    }

    /**
    * Converts an int to its shortest-length two's complement binary representative
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
