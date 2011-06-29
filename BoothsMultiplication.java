package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.GAIGSdatastr;
import exe.ShowFile;
import exe.question;
import exe.pseudocode.*;

public class BoothsMultiplication {
    private static PseudoCodeDisplay pseudo;
    private static URI docURI;
    private static QuestionGenerator quest;
    private static GAIGSprimitiveRegister RegM;
    private static GAIGSprimitiveRegister RegA;
    private static GAIGSprimitiveRegister RegQ;
    private static GAIGSprimitiveRegister Q_1;
    private static CountBox Count;
    private static GAIGSpane main;
    private static GAIGSpane header;
    private static GAIGSpane math;
    private static GAIGSpane trace;
    private static GAIGSpane currentRow;
    private static int rowNumber; //This is only used for comments in the XML
    private static ShowFile show;
    private static int REG_SIZE;
    
    private final static int REGM  = 0;
    private final static int REGA  = 1;
    private final static int REGQ  = 2;
    private final static int Q1    = 3;
    private final static int COUNT = 4;
    
    
    //Definitions
    private static final boolean DEBUG = false;

    private static final double FONT_SIZE = 0.05;
    public static final String WHITE   = "#FFFFFF";
    public static final String BLACK   = "#000000";
    public static final String GREY    = "#888888";
    public static final String RED     = "#FF0000";
    public static final String GREEN   = "#00FF00";
    public static final String BLUE    = "#0000FF";
    public static final String YELLOW  = "#FFFF00";
    public static final String FONT_COLOR = BLACK;
    public static final String DEFAULT_COLOR = WHITE;
    public static final String OUTLINE_COLOR = FONT_COLOR;
    
    private static final double WINDOW_WIDTH = 1+GAIGSpane.JHAVÉ_X_MARGIN*2;
    private static final double WINDOW_HEIGHT  = 1+GAIGSpane.JHAVÉ_Y_MARGIN*2;

    private static final double LEFT_MARGIN   = 0.0;
    private static final double TOP_MARGIN    = 0.0;
    private static final double REG_WIDTH     = 0.20;
    private static final double REG_HEIGHT    = 0.06;
    private static final double ROW_SPACE     = 0.03;
    private static final double COL_SPACE     = 0.015;

    public static void main(String args[]) throws IOException {
        //JHAVÉ Stuff
        show = new ShowFile(args[0]);

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

        GAIGStrace trace_object = new GAIGStrace();
        
        main = new GAIGSpane(0-GAIGSpane.JHAVÉ_X_MARGIN,
				 0-GAIGSpane.JHAVÉ_Y_MARGIN,
				 1+GAIGSpane.JHAVÉ_X_MARGIN,
				 1+GAIGSpane.JHAVÉ_Y_MARGIN,
				 WINDOW_WIDTH,
				 WINDOW_HEIGHT);
        main.setName("Main");
        header = new GAIGSpane(0, WINDOW_HEIGHT*(3/4.0),
        		main.getWidth(), WINDOW_HEIGHT, null, 1.0); //Top 1/4 of screen
        header.setName("Header");
        GAIGSArithmetic binary = new GAIGSArithmetic('*', multiplicand, multiplier, 2, header.getWidth(), header.getHeight(), header.getHeight()/4, header.getHeight()/9, FONT_COLOR);
        GAIGSArithmetic decimal = new GAIGSArithmetic('*', args[1], args[2], 10, .6, header.getHeight(), header.getHeight()/4, header.getHeight()/9, FONT_COLOR);
        header.add(binary);
        header.add(decimal);
        
        math = new GAIGSpane(WINDOW_WIDTH*(3/4.0), 0, WINDOW_WIDTH, WINDOW_HEIGHT*(3/4.0), 1.0, 1.0);
        math.setName("Math");
        
        trace = new GAIGSpane(0, 0, WINDOW_WIDTH*(3/4.0), WINDOW_HEIGHT*(3/4.0), null, 1.0);
        trace.setName("Trace");
        
        main.forceAdd(header);
        main.forceAdd(trace);
        main.forceAdd(math);

        main=trace;

        trace.add(new GAIGSpolygon(4, new double[] {0, trace.getWidth(), trace.getWidth(), 0}, new double[] {0, 0, trace.getHeight(), trace.getHeight()},
				DEFAULT_COLOR, RED, BLACK, "Work Here", FONT_SIZE, 2));
        
        currentRow = new GAIGSpane();
        currentRow.setName("Row " + rowNumber++);
        
        trace.add(currentRow);
        //Trace finally defined, can now make the QuestionGenerator
    	quest = new QuestionGenerator(show, trace_object);
    	
    	
    	//One could add Register Spacing/Sizing Logic Here
        int numRows = numLines(multiplier);

    	//Initialize Register Location
    	double[] init = new double[] {
    			LEFT_MARGIN,
    			trace.getHeight()-TOP_MARGIN-REG_HEIGHT,
    			LEFT_MARGIN+REG_WIDTH,
    			trace.getHeight()-TOP_MARGIN};
    	
    	//----Init Frame----
        //Reg M
        RegM= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegM.setLabel("M:    ");
        RegM.set(multiplicand);
        currentRow.add(RegM);
        easySnap("M is the multiplicand", easyPseudo(2), null);
        
        REG_SIZE = RegM.getSize();

        //Reg A
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+REG_WIDTH;
        RegA= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegA.set("0");
        RegA.setLabel("A:    ");
        currentRow.add(RegA);
        easySnap("A is initialized to Zero", easyPseudo(3), null);

        //Reg Q
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+REG_WIDTH;
        RegQ= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegQ.set(multiplier);
        RegQ.setLabel("Q:    ");
        currentRow.add(RegQ);
        easySnap("Q is the Multiplier\nThe final product will span A and Q", easyPseudo(4), null);

        //Bit Q_1
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+FONT_SIZE;
        Q_1 = new GAIGSprimitiveRegister(1,       "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        Q_1.set("0");
        Q_1.setLabel( "Q(-1):");
        currentRow.add(Q_1);
        easySnap("Q_₁ is initialized to 0", easyPseudo(5), null);

        //Count
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+FONT_SIZE;
        Count = new CountBox(REG_SIZE, DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        Count.setLabel("Count");
        currentRow.add(Count);
        easySnap("Count is initialized to the number of bits in a register.", easyPseudo(6), null);


        boothsMultiplication();

        //We are done.
        //----Finished Frame----
        RegA.setAllToColor(YELLOW);
        RegQ.setAllToColor(YELLOW);
        easySnap("Check the result.", easyPseudo(24), null);

        show.close();
    }

    public static void boothsMultiplication(){
    	while (Count.getBit(0) >= 0){
    		//----Count Frame----
    		Count.setColor(YELLOW);
    		easySnap("Check the value of Count", easyPseudo(8), null);
    		//Change count back
    		Count.setColor(DEFAULT_COLOR);
    		
    		if (Count.getBit(0) == 0) break; //Thats so we get the final check
    		
            //----Check Bits Frame----
            RegQ.setBitColor(REG_SIZE-1, BLUE);
            Q_1.setBitColor(0, BLUE);
    		
    		int cmpVal = RegQ.getBit(REG_SIZE-1) - Q_1.getBit(0);

    		easySnap("Determine the operation", easyPseudo(new int[] {10,14}), null);
    		
            RegQ.setBitColor(REG_SIZE-1, FONT_COLOR);
            Q_1.setBitColor(0, FONT_COLOR);
            
            //----Subtraction Frame----
    		if (cmpVal == 1){
    			positionAdditionRow(); //That Cloned All the Registers, fyi
    			addRow();
    			addIntoReg(negateValue(RegM), RegA);
                RegA.setAllToColor(GREEN);
                GAIGSArithmetic sum=new GAIGSArithmetic('+',
    					RegA.toString(), negateValue(RegM).toString(),
    					2, 1, math.getHeight()/1.5);
                math.add(sum);
                sum.complete();
    			easySnap("Added -M to A", easyPseudo(11), null);
    			math.clear();
    		}
    		
            //----Addition Frame----
    		else if (cmpVal == -1){
    			positionAdditionRow();
    			addRow();
    			addIntoReg(RegM, RegA);
                RegA.setAllToColor(GREEN);
                GAIGSArithmetic sum=new GAIGSArithmetic('+', RegA.toString(), RegM.toString(), 2, 1, math.getHeight()/1.5);
                math.add(sum);
                sum.complete();
    			easySnap("Added -M to A", easyPseudo(15), null);
    			math.clear();
    		}
    		
    		//----Shift Frame----
    		RegA.setAllToColor(GREEN);
    		RegQ.setAllToColor(BLUE);
    		positionMajorRow(); //Remember this clones
    		addRow();
    		rightShift(RegA, RegQ, Q_1);
            
    		currentRow.remove(COUNT); //Oops...We don't want Count
    		RegQ.setAllToColor(BLUE);
    		RegQ.setBitColor(0, GREEN);
    		Q_1.setBitColor(0, BLUE);
    		easySnap("Sign-Preserving Right Shift", easyPseudo(20), null);
    		RegQ.setAllToColor(FONT_COLOR);
    		Q_1.setAllToColor(FONT_COLOR);
    		
    				//Clean Color of A and Q on the previous line
    		((GAIGSprimitiveRegister)((GAIGSpane)trace.get(trace.size()-2)).get(REGA)).setAllToColor(FONT_COLOR);
    		((GAIGSprimitiveRegister)((GAIGSpane)trace.get(trace.size()-2)).get(REGQ)).setAllToColor(FONT_COLOR);
    		RegA.setAllToColor(FONT_COLOR);
    		RegQ.setAllToColor(FONT_COLOR);
    		
    		//----Decrement Count Frame---
    		Count.decrement();
    		//We don't want a new row
    		currentRow.add(Count); //Now we do want Count
    		Count.setAllToColor(RED);
    		easySnap("Decrement Count", easyPseudo(22), null);
    		Count.setAllToColor(FONT_COLOR);
    		//Hey!  We're ready to loop!
    	}
    }

    public static void rightShift(GAIGSprimitiveRegister A, GAIGSprimitiveRegister Q, GAIGSprimitiveRegister Q_1) {
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
     * Adds two registers, storing the result in the second register (a la AT&T Syntax).
     * @param toAdd other addend, not modified by function.
     * @param A	Destination Register and addend.
     */
    public static void addIntoReg(GAIGSprimitiveRegister toAdd, GAIGSprimitiveRegister A) {
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
    public static GAIGSprimitiveRegister negateValue(GAIGSprimitiveRegister M) {
        int carry = 1;
        GAIGSprimitiveRegister ret = new GAIGSprimitiveRegister((GAIGSprimitiveRegister) M);

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

    private static void adjustRegister(GAIGSprimitiveRegister reg){
    	double[] bds = reg.getBounds();
    	bds[3] = bds[1]-(ROW_SPACE);
    	bds[1] = bds[3]-REG_HEIGHT;
    	reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
	}
	
	private static void minorAdjustRegister(GAIGSprimitiveRegister reg){
    	double[] bds = reg.getBounds();
    	bds[3] = bds[1]-(ROW_SPACE/2);
    	bds[1] = bds[3]-REG_HEIGHT;
    	reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
	}
    
    private static void positionMajorRow(){
    	RegM = RegM.clone();
    	RegA = RegA.clone();
    	RegQ = RegQ.clone();
    	Q_1 = Q_1.clone();
    	Count = (CountBox) Count.clone();
    	
		adjustRegister(RegM);
		adjustRegister(RegA);
		adjustRegister(RegQ);
		adjustRegister(Q_1);
		adjustRegister(Count);
    }
    
    private static void positionAdditionRow(){
    	RegM = RegM.clone();
    	RegA = RegA.clone();
    	RegQ = RegQ.clone();
    	Q_1 = Q_1.clone();
    	Count = (CountBox) Count.clone();
    	
		minorAdjustRegister(RegM);
		minorAdjustRegister(RegA);
		minorAdjustRegister(RegQ);
		minorAdjustRegister(Q_1);
		minorAdjustRegister(Count);
    }
    
    private static void addRow(){
    	currentRow = new GAIGSpane();
    	currentRow.setName("Row " + rowNumber);
    	
    	trace.add(currentRow);
    	
    	currentRow.add(RegM);
    	currentRow.add(RegA);
    	currentRow.add(RegQ);
    	currentRow.add(Q_1);
    	currentRow.add(Count);
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

    private static void easySnap(String title, String info, String pseudo, question que, GAIGSdatastr... stuff){
        try {
            if (que == null)
                show.writeSnap(title, FONT_SIZE+.01, info, pseudo, stuff);
            else
                show.writeSnap(title, FONT_SIZE+.01, info, pseudo, que, stuff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void easySnap(String title, String pseudo, question que, GAIGSdatastr... stuff){
        try {
            if (que == null)
                show.writeSnap(title, FONT_SIZE+.01, docURI.toASCIIString(), pseudo, stuff);
            else
                show.writeSnap(title, FONT_SIZE+.01, docURI.toASCIIString(), pseudo, que, stuff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void easySnap(String title, String pseudo, question que){
        try {
            if (que == null)
                show.writeSnap(title, FONT_SIZE+.01, docURI.toASCIIString(), pseudo, main);
            else
                show.writeSnap(title, FONT_SIZE+.01, docURI.toASCIIString(), pseudo, que, main);
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
