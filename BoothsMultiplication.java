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
    
    public final static int REGM  = 0;
    public final static int REGA  = 1;
    public final static int REGQ  = 2;
    public final static int Q1    = 3;
    public final static int COUNT = 4;
    
    
    //Definitions
    private static final boolean DEBUG = false;

    private static final double FONT_SIZE = 0.05;
    public static final String WHITE     = "#FFFFFF";
    public static final String BLACK     = "#000000";
    public static final String GREY      = "#CCCCCC";
    public static final String DARK_GREY = "#666666";
    public static final String RED       = "#FF0000";
    public static final String GREEN     = "#00AA00";
    public static final String BLUE      = "#0000FF";
    public static final String YELLOW    = "#FFFF00";
    public static final String GOLD      = "#CDAD00";
    public static final String FONT_COLOR      = BLACK;
    public static final String DEFAULT_COLOR   = WHITE;
    public static final String INACTIVE_TEXT   = DARK_GREY;
    public static final String INACTIVE_OUTLINE= GREY;
    public static final String OUTLINE_COLOR   = FONT_COLOR;
    
    private static final double WINDOW_WIDTH   = 1+GAIGSpane.JHAVÉ_X_MARGIN*2;
    private static final double WINDOW_HEIGHT  = 1+GAIGSpane.JHAVÉ_Y_MARGIN*2;

    private static       double ARLABEL_SPACE;

    private static final double LEFT_MARGIN       = 0.0;
    private static final double RIGHT_MARGIN      = 1.0;
    private static final double TOP_MARGIN        = 0.0;
    private static       double REG_WIDTH         = 0.12;
    private static final double REG_WIDTH_PER_BIT = 0.035;
    private static final double REG_SPACE_CHUNK   = 0.32;
    private static final double REG_HEIGHT        = 0.06;
    private static final double ROW_SPACE         = 0.03;
    private static       double COL_SPACE         = 0.02;

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
        String multiplicand = args[1];
        String multiplier   = args[2];
        
        //shouldn't be necessary, but just in case
        final int regSize;
        if (multiplicand.length() > multiplier.length()){
            regSize=multiplicand.length();
            multiplier = signExtend(multiplier, regSize-multiplier.length() );
        }
        else {
            regSize=multiplier.length();
            multiplicand = signExtend(multiplicand, regSize-multiplicand.length() );
        }
        
        main = new GAIGSpane(0-GAIGSpane.JHAVÉ_X_MARGIN,
				 0-GAIGSpane.JHAVÉ_Y_MARGIN,
				 1+GAIGSpane.JHAVÉ_X_MARGIN,
				 1+GAIGSpane.JHAVÉ_Y_MARGIN,
				 WINDOW_WIDTH,
				 WINDOW_HEIGHT);
        main.setName("Main");
        header = new GAIGSpane(0, WINDOW_HEIGHT*(3/4.0),
        		WINDOW_WIDTH, WINDOW_HEIGHT, null, 1.0); //Top 1/4 of screen
        header.setName("Header");

        GAIGSArithmetic binary = new GAIGSArithmetic('*', multiplicand, multiplier, 2, header.getWidth(), header.getHeight(), 
            header.getHeight()/6, header.getHeight()/13, FONT_COLOR);
        GAIGSArithmetic decimal = new GAIGSArithmetic('*', toDecimal(args[1]), toDecimal(args[2]), 10, 0.06, header.getHeight(), 
            header.getHeight()/6, header.getHeight()/13, FONT_COLOR);
        
        ARLABEL_SPACE  = header.getWidth()/20;
        double[] binBds = binary.getBounds();
        
        /* You are aligning on the vertical middle which is why you need the adjust */
//        GAIGSmonospacedText binLabel = new GAIGSmonospacedText(
//        	binBds[0]-ARLABEL_SPACE, (binBds[3]+binBds[1])/2 + ARLABEL_ADJUST,
//            GAIGStext.HRIGHT, GAIGStext.VBOTTOM,
//            binary.getFontSize(), FONT_COLOR, "M\n ", header.getHeight()/13);
//        //interesting that I don't need to adjust decimal, but I do for binary
//        //and different alignment...?
//        double[] deciBds = decimal.getBounds();
//        GAIGSmonospacedText decLabel = new GAIGSmonospacedText(
//        	deciBds[2]+ARLABEL_SPACE, (deciBds[3]+deciBds[1])/2 + ARLABEL_ADJUST,
//        	GAIGStext.HRIGHT, GAIGStext.VBOTTOM,
//        	decimal.getFontSize(), FONT_COLOR,"M\n ", header.getHeight()/13);
        
        /* If you align them the same, you don't need the adjust on only one of them */
//        GAIGSmonospacedText binLabel = new GAIGSmonospacedText(
//        	binBds[0]-ARLABEL_SPACE, (binBds[3]+binBds[1])/2,
//            GAIGStext.HLEFT, GAIGStext.VTOP,
//            binary.getFontSize(), FONT_COLOR, "M\n ", header.getHeight()/13);
//        //interesting that I don't need to adjust decimal, but I do for binary
//        //and different alignment...?
//        double[] deciBds = decimal.getBounds();
//        GAIGSmonospacedText decLabel = new GAIGSmonospacedText(
//        	deciBds[2]+ARLABEL_SPACE, (deciBds[3]+deciBds[1])/2,
//        	GAIGStext.HLEFT, GAIGStext.VTOP,
//        	decimal.getFontSize(), FONT_COLOR,"M\n ", header.getHeight()/13);
        
        /* You may have noticed that the top bounds was actually one character higher
         * than expected.  This is because multiplication was still reserving space for
         * carries even though they are only relevant in addition/subtraction.  I have
         * changed that now so its not there, though before it was nice to have because
         * it kept spacing with the top nice.  If I didn't do that it would just have been
         * a matter of subtracting fontSize from the coordinate.  It also turned out there
         * was a bug in monospaced text that prevented the following way from working.
         */
        GAIGSmonospacedText binLabel = new GAIGSmonospacedText(
        	binBds[0]-ARLABEL_SPACE, binBds[3],
            GAIGStext.HCENTER, GAIGStext.VTOP,
            binary.getFontSize(), FONT_COLOR, "M\n ", header.getHeight()/13);
        //interesting that I don't need to adjust decimal, but I do for binary
        //and different alignment...?
        double[] deciBds = decimal.getBounds();
        GAIGSmonospacedText decLabel = new GAIGSmonospacedText(
        	deciBds[2]+ARLABEL_SPACE, deciBds[3],
        	GAIGStext.HCENTER, GAIGStext.VTOP,
        	decimal.getFontSize(), FONT_COLOR,"M\n ", header.getHeight()/13);

        header.add(binary);
        header.add(binLabel);
        header.add(decimal);
        header.add(decLabel);
        
        math = new GAIGSpane(WINDOW_WIDTH*(3/4.0), 0, WINDOW_WIDTH, WINDOW_HEIGHT*(3/4.0), 1.0, 1.0);
        math.setName("Math");
        
        trace = new GAIGSpane(0, 0, WINDOW_WIDTH*(3/4.0), WINDOW_HEIGHT*(3/4.0), null, 1.0);
        trace.setName("Trace");
        
        main.forceAdd(header);
        main.forceAdd(trace);
        main.forceAdd(math);

        GAIGSpane trace_labels = new GAIGSpane();
        trace_labels.add(new GAIGSpolygon(4, new double[] {0, trace.getWidth(), trace.getWidth(), 0}, 
            new double[] {0, 0, trace.getHeight(), trace.getHeight()}, DEFAULT_COLOR, RED, BLACK, "Work Here", FONT_SIZE, 2));

        trace.add(trace_labels);
        
        currentRow = new GAIGSpane();
        currentRow.setName("Row " + rowNumber++);
        
        trace.add(currentRow);
        //Trace finally defined, can now make the QuestionGenerator
    	quest = new QuestionGenerator(show, trace);
    	
    	
    	//One could add Register Spacing/Sizing Logic Here
        //int numRows = numLines(multiplier);
        REG_WIDTH = REG_WIDTH_PER_BIT * multiplier.length();
        COL_SPACE = REG_SPACE_CHUNK - REG_WIDTH;
        

    	//Initialize Register Location
    	double[] init = new double[] {
    			LEFT_MARGIN,
    			trace.getHeight()-TOP_MARGIN-REG_HEIGHT,
    			LEFT_MARGIN+REG_WIDTH,
    			trace.getHeight()-TOP_MARGIN};
    	
    	//----Init Frame----
        //Reg M
    	trace_labels.add(new GAIGSmonospacedText(
    						(init[2]-init[0])/2.0+init[0], init[3],
    						GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER,
    						FONT_SIZE, FONT_COLOR, "M:", FONT_SIZE/2));
        RegM= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegM.setLabel("M:    ");
        RegM.set(multiplicand);
        
        currentRow.add(RegM);
        easySnap("M is the multiplicand", easyPseudo(2), null);
        
        REG_SIZE = RegM.getSize();

        //Reg A
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+REG_WIDTH;
    	trace_labels.add(new GAIGSmonospacedText(
				(init[2]-init[0])/2.0+init[0], init[3],
				GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER,
				FONT_SIZE, FONT_COLOR, "A:", FONT_SIZE/2));
        RegA= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegA.set("0");
        RegA.setLabel("A:    ");
        currentRow.add(RegA);
        easySnap("A is initialized to Zero", easyPseudo(3), null);

        //Reg Q
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+REG_WIDTH;
    	trace_labels.add(new GAIGSmonospacedText(
				(init[2]-init[0])/2.0+init[0], init[3],
				GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER,
				FONT_SIZE, FONT_COLOR, "Q:", FONT_SIZE/2));
        RegQ= new GAIGSprimitiveRegister(regSize, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegQ.set(multiplier);
        RegQ.setLabel("Q:    ");
        currentRow.add(RegQ);
        //multiplier label appears here.
        binLabel.setText("M\nQ");
        decLabel.setText("M\nQ");
        easySnap("Q is the Multiplier\nThe final product will span A and Q", easyPseudo(4), null);

        //Bit β
    	init[0] = init[2]+(COL_SPACE);
    	init[2] = init[0]+FONT_SIZE;
    	trace_labels.add(new GAIGSmonospacedText(
				(init[2]-init[0])/2.0+init[0], init[3],
				GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER,
				FONT_SIZE, FONT_COLOR, "β", FONT_SIZE/2));
        Q_1 = new GAIGSprimitiveRegister(1,       "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        Q_1.set("0");
        Q_1.setLabel( "β");
        currentRow.add(Q_1);
        easySnap("β is initialized to 0", easyPseudo(5), null);

        //Count
//      I assume there's a good reason why these aren't equivalent?
        //Yes, get Bounds gives its coordinates on the screen.
        //Get Width gives the width of the coordinate system within the object
//      init[0] = trace.getBounds()[2] - FONT_SIZE;
//      init[2] = trace.getBounds()[2];
        init[0] = trace.getWidth() - FONT_SIZE;
        init[2] = trace.getWidth();
    	trace_labels.add(new GAIGSmonospacedText(
				(init[2]-init[0])/2.0+init[0], init[3],
				GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER,
				FONT_SIZE, FONT_COLOR, "Count:", FONT_SIZE/2));
        Count = new CountBox(REG_SIZE, DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        Count.setLabel("Count");
        currentRow.add(Count);
        easySnap("Count is initialized to\nthe number of bits in a register.", easyPseudo(6), null);


        boothsMultiplication();

        //We are done.
        //----Finished Frame----
        setAllRegBitsColor(INACTIVE_TEXT);
        decimal.complete();
        binary.complete();
        RegA.setColor(YELLOW);
        RegA.setAllToColor(FONT_COLOR);
        RegQ.setColor(YELLOW);
        RegQ.setAllToColor(FONT_COLOR);
        easySnap("Check the result.", easyPseudo(25), null);

        show.close();
    }

    public static void boothsMultiplication(){
    	while (Count.getBit(0) >= 0){
    		//----Count Frame----
    		Count.setColor(YELLOW);
    		easySnap("Check the value of Count", easyPseudo(8), null);
    		//Change color back
    		Count.setColor(DEFAULT_COLOR);
    		
    		if (Count.getBit(0) == 0) break; //Thats so we get the final check
    		
    		//----Start of Comparison and Addition/Subtraction Frame Logic----
    		
            /* Note: This logic for drawing these frames is dictated by the QuestionGenerator,
      	    * not Booth's Multiplication Algorithm.  Previous revisions were cleaner. 
    	    */
    		int cmpVal = RegQ.getBit(REG_SIZE-1) - Q_1.getBit(0);
    		
            if (cmpVal == 1 || cmpVal == -1) {                
                positionAdditionRow();//clones all registers
                addRow();//now we have enough information for question type 3, calculations pending
                GAIGSArithmetic sum;
                GAIGSmonospacedText sumLabel;
                
               	//Subtraction case   
                if (cmpVal == 1) {
                    sum = new GAIGSArithmetic('+', RegA.toString(), negateValue(RegM).toString(),
                        2, 1, math.getHeight()/1.5);
                    sumLabel = new GAIGSmonospacedText(sum.getBounds()[2]+ARLABEL_SPACE, sum.getBounds()[3]-sum.getFontSize(), 
                        GAIGStext.HCENTER, GAIGStext.VTOP, sum.getFontSize(), FONT_COLOR, "(A)\n(-M)");
                    addIntoReg(negateValue(RegM), RegA);
                }
               	//Addition case
                //TODO THIS ONE TOO //What about it?
                else {
                    sum = new GAIGSArithmetic('+', RegA.toString(), RegM.toString(), 
                        2, 1, math.getHeight()/1.5);
                    sumLabel = new GAIGSmonospacedText(sum.getBounds()[2]+ARLABEL_SPACE, sum.getBounds()[3]-sum.getFontSize(), 
                            GAIGStext.HCENTER, GAIGStext.VTOP, sum.getFontSize(), FONT_COLOR, "(A)\n(M)");
                    addIntoReg(RegM, RegA);
                }
                
                //----Comparison Frame----
                getRegisterFromRow(trace.size()-2, REGQ).setBitColor(REG_SIZE-1, BLUE);
                getRegisterFromRow(trace.size()-2, Q1).setBitColor(0         , BLUE);
                
                question que = quest.getComparisonQuestion();
                GAIGSpane temp = (GAIGSpane)trace.remove(trace.size()-1);
                easySnap("Determine the operation", easyPseudo(10), que);
                trace.add(temp);
                
                //Reset/deactivate colors
                setRowRegisterBitsColor(trace.size()-2,    INACTIVE_TEXT);
                setRowRegisterOutlineColor(trace.size()-2, INACTIVE_OUTLINE);
                RegQ.setBitColor(REG_SIZE-1, FONT_COLOR);
                Q_1.setBitColor(0, FONT_COLOR);

                
                //----Addition/Subtraction frame----
                RegA.setAllToColor(GREEN);
                math.add(sum);
                math.add(sumLabel);
                sum.complete();
                easySnap("Added " + (cmpVal == 1 ? "-M " : " M") + " to A", easyPseudo(11), quest.getAdditionQuestion() );
                math.clear();
                sumLabel.setText("");

            }
            else {
                //----Comparison Frame---- (yep, again)
                //Colors
                RegQ.setBitColor(REG_SIZE-1, BLUE);
                Q_1.setBitColor(0, BLUE);
                
                //Question pane-hopping//
                trace.add(null);
                question que = quest.getQuestion(1);
                trace.remove(trace.size()-1);
                easySnap("Determine the operation", easyPseudo(10), que);

                //Reset colors
                RegQ.setBitColor(REG_SIZE-1, FONT_COLOR);
                Q_1.setBitColor(0, FONT_COLOR);
            }
            //Deactivate text
            setAllRegBitsColor(INACTIVE_TEXT);
            setRowRegisterOutlineColor(INACTIVE_OUTLINE);
            
    		//----Shift Frame----
    		positionMajorRow(); //Remember this clones
            setAllRegBitsColor(FONT_COLOR);
    		addRow();
    		rightShift(RegA, RegQ, Q_1);
           
            //Question and write
            question que = quest.getShiftQuestion(); 

            //Colors
            getRegisterFromRow(trace.size()-2, REGA).setAllToColor(GREEN);
            getRegisterFromRow(trace.size()-2, REGQ).setAllToColor(BLUE);
            setRowRegisterOutlineColor(OUTLINE_COLOR);
    		RegA.setAllToColor(GREEN);
    		RegQ.setAllToColor(BLUE);
    		RegQ.setBitColor(0, GREEN);
    		Q_1.setBitColor(0, BLUE);
    		currentRow.remove(COUNT); //Oops...We don't want Count
    		easySnap("Sign-Preserving Right Shift", easyPseudo(21), que);
    		RegQ.setAllToColor(FONT_COLOR);
    		Q_1.setAllToColor(FONT_COLOR);
    		
            //Clean Color of A and Q on the previous line
            getRegisterFromRow(trace.size()-2, REGA).setAllToColor(INACTIVE_TEXT);
            getRegisterFromRow(trace.size()-2, REGQ).setAllToColor(INACTIVE_TEXT);
    		RegA.setAllToColor(FONT_COLOR);
    		RegQ.setAllToColor(FONT_COLOR);
    		
    		//----Decrement Count Frame---
    		Count.decrement();
    		currentRow.add(Count); //Now we do want Count
    		Count.setAllToColor(RED);
    		easySnap("Decrement Count", easyPseudo(23), null);
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
    
    private static void setAllRegBitsColor(String color) {
        RegM.setAllToColor(color) ;
        RegA.setAllToColor(color) ;
        RegQ.setAllToColor(color) ;
        Q_1.setAllToColor( color) ;
        Count.setAllToColor(color);
    }

    private static void setRowRegisterBitsColor(int row, String color) {
        getRegisterFromRow(row, REGM).setAllToColor(color);
        getRegisterFromRow(row, REGA).setAllToColor(color);
        getRegisterFromRow(row, REGQ).setAllToColor(color);
        getRegisterFromRow(row, Q1  ).setAllToColor(color);
        getRegisterFromRow(row,COUNT).setAllToColor(color);
    }

    private static void setRowRegisterOutlineColor(int row, String color) {
        getRegisterFromRow(row, REGM).setOutlineColor(color);
        getRegisterFromRow(row, REGA).setOutlineColor(color);
        getRegisterFromRow(row, REGQ).setOutlineColor(color);
        getRegisterFromRow(row, Q1  ).setOutlineColor(color);
        getRegisterFromRow(row,COUNT).setOutlineColor(color);
    }

    private static void setRowRegisterOutlineColor(String color) {
        setRowRegisterOutlineColor(trace.size()-1, color);
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
    	RegM  = RegM.clone();
    	RegA  = RegA.clone();
    	RegQ  = RegQ.clone();
    	Q_1   = Q_1.clone();
    	Count = (CountBox) Count.clone();
    	
		minorAdjustRegister(RegM);
		minorAdjustRegister(RegA);
		minorAdjustRegister(RegQ);
		minorAdjustRegister(Q_1);
		minorAdjustRegister(Count);
    }

    /*
     * Now we are restricted and must be very careful what goes on the trace and 
     * currentRow
     */
    private static GAIGSprimitiveRegister getRegisterFromRow(int row, int reg) {
        return (GAIGSprimitiveRegister) ((GAIGSpane) trace.get(row)).get(reg);
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

    private static String toDecimal(String binstr) {
        int sum    = 0;
        int maxPow = 1;

        for (int i = 0; i < binstr.length(); ++i) {
            sum = (2 * sum) + binstr.charAt(i) - '0';
            maxPow *= 2;
        }

        //Two's complement madness
        return ((sum < maxPow / 2) ? "" + sum : "" + (sum - maxPow) );

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
