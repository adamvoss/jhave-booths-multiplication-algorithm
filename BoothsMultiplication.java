package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

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
    public static final String TEXT_COLOR = BLACK;
    
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

    	GAIGSPane main = new GAIGSPane(100, 100/GAIGSPane.JHAVÉ_ASPECT_RATIO);
    	GAIGSArithmetic math = new GAIGSArithmetic('+', multiplicand, multiplier, 2, 75, 75, 10.0, 3, TEXT_COLOR);
    	math.complete();
    	main.add(math);
    	show.writeSnap("You can see this", main);
    	
        GAIGSpolygon hugePoly = new GAIGSpolygon(4, new double[] {0, 50, 50 ,0}, new double[] {0,0,50,50}, GREEN, BLUE, WHITE, "My Area is 2500");
    	main.add(hugePoly);
    	show.writeSnap("You can still see this", main);

    	GAIGSPane nest = new GAIGSPane(50,0,100,main.getHeight(),1,1);
    	nest.setName("Inner Frame");
    	main.add(nest);
    	nest.add(new GAIGSpolygon(4, new double[] {0, 1, 1,0}, new double[] {0,0,1,1}, RED, YELLOW, WHITE, "My Area is 1! (Yes, that is a factorial)"));
    	show.writeSnap("Psst!  A secret:     You can't se this", main);
    	
    	
        //Reg M
        GAIGSregister RegM= new GAIGSprimitiveRegister(regSize, "", TEXT_COLOR, mypoints[0], FONT_SIZE);
        RegM.setLabel("M:    ");
        RegM.set(multiplicand);
        trace.add("RegM", RegM);
        show.writeSnap("M is the Multiplicand", docURI.toASCIIString(), easyPseudo(2), trace);

        //Reg A
        GAIGSregister RegA= new GAIGSprimitiveRegister(regSize, "", TEXT_COLOR, mypoints[1], FONT_SIZE);
        RegA.set("0");
        RegA.setLabel("A:    ");
        trace.add("RegA", RegA);
        show.writeSnap("A is initialized to Zero", docURI.toASCIIString(), easyPseudo(3), trace);

        //Reg Q
        GAIGSregister RegQ= new GAIGSprimitiveRegister(regSize, "", TEXT_COLOR, mypoints[2], FONT_SIZE);
        RegQ.set(multiplier);
        RegQ.setLabel("Q:    ");
        trace.add("RegQ", RegQ);
        show.writeSnap("Q is the Multiplier\nThe final product will span A and Q",
            docURI.toASCIIString(), easyPseudo(4), trace);

        //Bit Q_1
        GAIGSregister Q_1 = new GAIGSprimitiveRegister(1,       "", TEXT_COLOR, mypoints[3], FONT_SIZE);
        Q_1.set("0");
        Q_1.setLabel( "Q(-1):");
        trace.add("Q_1" , Q_1);
        show.writeSnap("Q_₁ is initialized to 0", docURI.toASCIIString(), easyPseudo(5), trace);

        //Count
        CountBox count = new CountBox(RegQ.getSize(), TEXT_COLOR, mypoints[4], FONT_SIZE);
        count.setLabel("Count");
        trace.add("Count", count);
        show.writeSnap("Count is initialized to the number of bits in a register.", docURI.toASCIIString(), easyPseudo(6), trace);

        //Create new line
        mypoints = getPositions(1, numRows);
        trace.newLine();
        trace.add("RegM" , RegM.copyTo( mypoints[0]) );
        trace.add("RegA" , RegA.copyTo( mypoints[1]) );
        trace.add("RegQ" , RegQ.copyTo( mypoints[2]) );
        trace.add("Q_1"  , Q_1.copyTo(  mypoints[3]) );
        trace.add("Count", count.copyTo(mypoints[4]) ); //don't be fooled, not the same method as others.

        boothsAlgorithmIter(trace, numRows, show);

        //Hack to show we are done.
        ((GAIGSregister)trace.get("RegA")).setAllToColor(YELLOW);
        ((GAIGSregister)trace.get("RegQ")).setAllToColor(YELLOW);
        show.writeSnap("Check the result.", docURI.toASCIIString(), easyPseudo(24), trace);

        show.close();
    }

    /**
    * An iterative method which steps through Booth's Multiplication Algorithm, making the
    * appropriate calls to show's writeSnap through each iteration.
    * 
    * At each iteration, Q(0) and Q(-1) are evaluated. If there is an add/subtract, curLine will be incremented
    * by 2, or else it will be incremented by 1. The function exits when curLine is no longer less than or equal
    * to numLines.
    *
    * @param trace The GAIGStrace object to keep the run history of the algorithm.
    * @param numLines The total number of lines that will be displayed during the run of the visualization.
    * @param show The ShowFile object where the xml of the GAIGSregisters will be written.
    */
    public static void boothsAlgorithmIter(GAIGStrace trace, int numLines, ShowFile show) throws IOException {
        
        int cycles  = ((CountBox)(trace.get(0, "Count")) ).getCount();
        int curLine = 0;
        int size    = ((GAIGSregister)trace.get("RegQ") ).getSize();
        HashMap<String, MutableGAIGSdatastr> tempLine;
        tempLine = trace.popLine();

        //Assuming 2 lines of history
        for (int i = 0; i < cycles; ++i) {
            //Check count
            //Colors
            CountBox OldCount = (CountBox)trace.get("Count");
            OldCount.setColor(YELLOW);

            show.writeSnap("Check the value of Count", docURI.toASCIIString(), easyPseudo(8), trace); 

            //Reset count color
            OldCount.setColor(TEXT_COLOR);

            //Calculate case
            //00, 11 => 0 // 01 => -1 // 10 => 1
            int partCalc = ((GAIGSregister)trace.get("RegQ")).getBit(size-1) - 
                ((GAIGSregister)trace.get("Q_1")).getBit(0);

            //Set comparison color
            GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
            OldQ.setColor(size-1, BLUE);
            GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");
            OldQ_1.setColor(0,    BLUE);

            //Compare for add frames
            if (partCalc == 1 || partCalc == -1) { //addition or subtraction
                String title = "";
                int psline = 0;

                //Active registers
                GAIGSregister M = (GAIGSregister)tempLine.get("RegM");
                GAIGSregister A = (GAIGSregister)tempLine.get("RegA");

                if (partCalc == 1) {addIntoReg(A, negateValue(M) ); title = "Added -M to A"; psline = 11;}
                else               {addIntoReg(A, M)              ; title = "Added  M to A"; psline = 15;}

                //addToTrace logic here
                //all values in tempLine have effectively been 'pushed' onto the trace.
                ++curLine;
                MutableGAIGSdatastr[] ret = addToTraceAndGenerateNext(M, A, (GAIGSregister)tempLine.get("RegQ"), (GAIGSregister)tempLine.get("Q_1"), 
                    (CountBox)tempLine.get("Count"), trace, curLine, numLines);

                A.setAllToColor(GREEN);

                //Needs to be done with the extra line on first
                question que = quest.getComparisonQuestion();
                
                //BEGIN hop
                tempLine = trace.popLine();

                //Finishes comparison thought.
                easyWriteQuestion(title, new int[] {10, 14}, que, show, trace);

                //END hop
                trace.pushLine(tempLine);

                //Reset Colors
                OldQ.setColor  (size-1, TEXT_COLOR);
                OldQ_1.setColor(0, TEXT_COLOR);

                //Finishes addition thought
                que = quest.getAdditionQuestion();
                easyWriteQuestion(title, new int[] {psline}, que, show, trace);

                //Add returned results to the leading line
                tempLine = assignToLeadingLine(ret);

            } 
            else { 
                //No addition occured, so finish comparison with different quesiton 
                //Assumes it's looking 2 back, so add dummy.
                trace.newLine();
                show.writeSnap("Comparison", docURI.toASCIIString(),
                    easyPseudo(new int[] {10, 14}), quest.getQuestion(1), trace);
                trace.popLine();

                //Reset Color
                OldQ.setColor(size-1, TEXT_COLOR);
                OldQ_1.setColor(0, TEXT_COLOR);

                //tempLine is still the leading line.
            }
            //Get individual structures of the current line
            GAIGSregister M   = (GAIGSregister)tempLine.get("RegM") ;
            GAIGSregister A   = (GAIGSregister)tempLine.get("RegA") ;
            GAIGSregister Q   = (GAIGSregister)tempLine.get("RegQ") ;
            GAIGSregister Q_1 = (GAIGSregister)tempLine.get("Q_1")  ;
            CountBox count    = (CountBox)     tempLine.get("Count");

            rightShift(A, Q, Q_1);

            //Begin count logic
            count.decrement();

            //addToTrace logic
            //everything from tempLine has been effectively 'pushed' onto trace
            ++curLine;
            MutableGAIGSdatastr[] ret = addToTraceAndGenerateNext(M, A, Q, Q_1, count, trace, curLine, numLines);

            //Shift color logic
            Q_1.setColor(0, BLUE);
            ((GAIGSregister)trace.get(trace.size()-2, "RegQ")).setAllToColor(BLUE);
            Q.setAllToColor(BLUE);
            ((GAIGSregister)trace.get(trace.size()-2, "RegA")).setAllToColor(GREEN);
            A.setAllToColor(GREEN);
            Q.setColor(0, GREEN);

            //HOP
            question que = quest.getShiftQuestion();
            tempLine = trace.popLine();

            //This frame excludes count
            easyWriteQuestion("Right Sign Preserving Shift", new int[] {20}, que, show, trace, M, A, Q, Q_1);

            //Reset colors
            ((GAIGSregister)trace.get("RegA")).setAllToColor(TEXT_COLOR);
            A.setAllToColor(TEXT_COLOR);
            ((GAIGSregister)trace.get("RegQ")).setAllToColor(TEXT_COLOR);
            Q.setAllToColor(TEXT_COLOR);
            Q_1.setColor(0, TEXT_COLOR);

            //END hop
            //This gets all the instance variables added.
            trace.pushLine(tempLine);

            //Add returned results to the leading line
            tempLine = assignToLeadingLine(ret);
            
            show.writeSnap("Decrement count", docURI.toASCIIString(), easyPseudo(22), trace);
        }

        //Last check of count
        CountBox OldCount = (CountBox)trace.get("Count");
        OldCount.setColor("YELLOW");
        show.writeSnap("Check the value of Count", docURI.toASCIIString(), easyPseudo(8), trace);
        OldCount.setColor(TEXT_COLOR);
    }
    
    /*
    * Helper function to reduce copy pasta
    * Assigns appropriate values to leading line
    * TODO @param @return
    */
    private static HashMap<String, MutableGAIGSdatastr> assignToLeadingLine(MutableGAIGSdatastr[] vals) {
        HashMap<String, MutableGAIGSdatastr> ret = new HashMap<String, MutableGAIGSdatastr>();
        ret.put("RegM" , vals[0]);
        ret.put("RegA" , vals[1]);
        ret.put("RegQ" , vals[2]);
        ret.put("Q_1"  , vals[3]);
        ret.put("Count", vals[4]);

        return ret;
    }

    /*
    * Helper function to reduce copy pasta
    * Adds the GAIGSregisters to the GAIGStrace and generate the next 4.
    * TODO @param @return
    */
    private static MutableGAIGSdatastr[] addToTraceAndGenerateNext(GAIGSregister M, GAIGSregister A,
                        GAIGSregister Q, GAIGSregister Q_1, CountBox count, GAIGStrace trace, int curLine, int numLines) {
        MutableGAIGSdatastr[] ret = new MutableGAIGSdatastr[5];

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

    /**
     * Negates the value of a register, in two's complement.
     * @param M The register to negate
     * @return A new register with the negated value.
     */
    //TODO Clean up this method so cast's aren't necessary
    public static GAIGSregister negateValue(GAIGSregister M) {
        int carry = 1;
        GAIGSregister ret = new GAIGSprimitiveRegister((GAIGSprimitiveRegister) M);

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

    private static void easyWriteQuestion(String title, int[] pslines, question que, ShowFile show, GAIGSdatastr... stuff) {
        try {
            if (que == null)
                show.writeSnap(title, docURI.toASCIIString(), easyPseudo(pslines), stuff);
            else
                show.writeSnap(title, docURI.toASCIIString(), easyPseudo(pslines), que, stuff);
        } catch (IOException e) {
            e.printStackTrace();
        }    
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
