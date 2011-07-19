package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.GAIGSdatastr;
import exe.question;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class BoothMultiplication {
    private static PseudoCodeDisplay pseudo;
    private static URI docURI;
    private static QuestionGenerator quest;
    private static GAIGSregister RegM;
    private static GAIGSregister RegA;
    private static GAIGSregister RegQ;
    private static GAIGSregister Q_1;
    private static CountBox Count;
    private static GAIGSpane<GAIGSpane<?>> main; //Is this the same as GAIGSpane<GAIGSpane<? extends MutableGAIGSdatastr>>?
    private static GAIGSpane<MutableGAIGSdatastr> header;
    private static GAIGSpane<MutableGAIGSdatastr> math;
    private static GAIGSpane<GAIGSpane<?>> trace;
    private static GAIGSpane<MutableGAIGSdatastr> currentRow;
    private static GAIGSmonospacedText title;
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

    public static final String WHITE     = "#FFFFFF";
    public static final String BLACK     = "#000000";
    public static final String GREY      = "#DDDDDD";
    public static final String DARK_GREY = "#666666";
    public static final String RED       = "#FF9999";
    public static final String GREEN     = "#55FF55";
    public static final String BLUE      = "#CCCBFF";
    public static final String YELLOW    = "#FFFF00";
    public static final String GOLD      = "#CDAD00";
    public static final String PURPLE    = "#880088";
    public static final String FONT_COLOR      = BLACK;
    public static final String DEFAULT_COLOR   = WHITE;
    public static final String INACTIVE_TEXT   = DARK_GREY;
    public static final String INACTIVE_OUTLINE= GREY;
    public static final String INACTIVE_FILL   = WHITE;
    public static final String OUTLINE_COLOR   = FONT_COLOR;

    private static final double WINDOW_WIDTH   = 1+GAIGSpane.JHAVE_X_MARGIN*2;
    private static final double WINDOW_HEIGHT  = 1+GAIGSpane.JHAVE_Y_MARGIN*2;

    private static       double MATH_LABEL_SPACE;
    private static final double FONT_SIZE         = 0.04;//was 0.05
    private static final double REG_WIDTH_PER_BIT = FONT_SIZE;
    private static final double REG_SPACE_CHUNK   = 0.35;
    private static final double REG_HEIGHT        = 0.06;
    private static       double REG_WIDTH;
    private static final double ROW_SPACE         = 0.03;
    private static       double COL_SPACE         = 0.02;

    private static final double LEFT_MARGIN       = 0.0;
    private static final double RIGHT_MARGIN      = FONT_SIZE;
    private static final double TOP_MARGIN        = 0.0;

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

        REG_SIZE = multiplicand.length();

        main = new GAIGSpane<GAIGSpane<?>>(0-GAIGSpane.JHAVE_X_MARGIN,
                0-GAIGSpane.JHAVE_Y_MARGIN,
                1+GAIGSpane.JHAVE_X_MARGIN,
                1+GAIGSpane.JHAVE_Y_MARGIN,
                WINDOW_WIDTH,
                WINDOW_HEIGHT);
        main.setName("Main");
        header = new GAIGSpane<MutableGAIGSdatastr>(0, WINDOW_HEIGHT*(3/4.0),
                WINDOW_WIDTH, WINDOW_HEIGHT, null, 1.0); //Top 1/4 of screen
        header.setName("Header");

        title=new GAIGSmonospacedText(header.getWidth()/2, header.getHeight(), GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VTOP, .25, FONT_COLOR, "", .1);
        header.add(title);

        GAIGSArithmetic binary = new TCMultBooth(multiplicand, multiplier, header.getWidth(), header.getHeight()-FONT_SIZE*1.5, 
                header.getHeight()/6, header.getHeight()/13, FONT_COLOR, PURPLE);
        ColoredResultArithmetic decimal = new ColoredResultArithmetic('*', toDecimal(args[1]), toDecimal(args[2]), 10, 10*FONT_SIZE, header.getHeight()-FONT_SIZE*1.5, 
                header.getHeight()/6, header.getHeight()/13, FONT_COLOR, PURPLE);

        MATH_LABEL_SPACE  = header.getWidth()/20;

        header.add(binary);
        header.add(decimal);
        
        math = new GAIGSpane<MutableGAIGSdatastr>(WINDOW_WIDTH*(3/4.0), 0, WINDOW_WIDTH, WINDOW_HEIGHT*(3/4.0), 1.0, 1.0);
        math.setName("Math");

        math.add(new GAIGSline(new double[] {0,0},
                new double[] {math.getHeight()+FONT_SIZE,
                    math.getHeight() - (numLines(multiplier)+1) * (REG_HEIGHT + ROW_SPACE) + ROW_SPACE/2 }));
        math.add(new GAIGSmonospacedText(math.getWidth()/2, math.getHeight(), GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, "Math/ALU"));

        trace = new GAIGSpane<GAIGSpane<?>>(0, 0, WINDOW_WIDTH*(3/4.0), WINDOW_HEIGHT*(3/4.0), null, 1.0);
        trace.setName("Trace");

        main.add(header);
        main.add(trace);
        main.add(math);

        GAIGSpane<GAIGSmonospacedText> trace_labels = new GAIGSpane<GAIGSmonospacedText>();
        //        math.add(new GAIGSpolygon(4, new double[] {0, math.getWidth(), math.getWidth(), 0}, 
        //            new double[] {0, 0, math.getHeight(), math.getHeight()}, DEFAULT_COLOR, RED, BLACK, "Work Here", FONT_SIZE, 2));

        trace.add(trace_labels);

        currentRow = new GAIGSpane<MutableGAIGSdatastr>();
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

        //**** Initialization Frames ****
        
        //----Register M Initialization Frame----
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                FONT_SIZE, FONT_COLOR, "M", FONT_SIZE/2));
        RegM= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegM.set(multiplicand);

        currentRow.add(RegM);
        
        
        //Let's draw arrows
        GAIGSarrow leftArrow;
        GAIGSarrow rightArrow;
        {
        title.setText("M is the multiplicand"); //Must do this here so we get the correct bounds
        double[] titlebounds = title.getBounds();
        double[] decbounds = decimal.getBounds();
        double[] binbounds = binary.getBounds();
        
        leftArrow = new GAIGSarrow(new double[]{titlebounds[0], decbounds[2]},
                new double[]{titlebounds[3], decbounds[3]},
                FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
        rightArrow = new GAIGSarrow(new double[]{titlebounds[2], (binbounds[0]+binbounds[2])/2},
                new double[]{titlebounds[3], binbounds[3]},
                FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
        }
        header.add(leftArrow);
        header.add(rightArrow);
        //We are done drawing arrows
        
        easySnap(null, easyPseudo(2), null); //A null title indicates to keep the last one.
        header.remove(rightArrow);
        header.remove(leftArrow);

        REG_SIZE = RegM.getSize();

        //----Register A Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+REG_WIDTH;
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                FONT_SIZE, FONT_COLOR, "A", FONT_SIZE/2));
        RegA= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegA.set("0");
        currentRow.add(RegA);
        easySnap("A is initialized to Zero", easyPseudo(3), null);

        //----Register Q Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+REG_WIDTH;
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                FONT_SIZE, FONT_COLOR, "Q", FONT_SIZE/2));
        RegQ= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        RegQ.set(multiplier);
        currentRow.add(RegQ);
        
        //Let's draw arrows
        {
        title.setText("Q is the Multiplier\nThe final product will span A and Q"); //Must do this here so we get the correct bounds
        double[] titlebounds = title.getBounds();
        double[] decbounds = decimal.getBounds();
        double[] binbounds = binary.getBounds();
        
        leftArrow = new GAIGSarrow(new double[]{titlebounds[0], decbounds[2]},
                new double[]{titlebounds[3], decbounds[3]-decimal.getFontSize()},
                FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
        rightArrow = new GAIGSarrow(new double[]{titlebounds[2], (binbounds[0]+binbounds[2])/2},
                new double[]{titlebounds[3], binbounds[3]-binary.getFontSize()},
                FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
        }
        header.add(leftArrow);
        header.add(rightArrow);
        //We are done drawing arrows
        
        easySnap(null, easyPseudo(4), null);
        header.remove(rightArrow);
        header.remove(leftArrow);

        //----Bit \u03B2 Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+FONT_SIZE;
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                FONT_SIZE, FONT_COLOR, "\u03B2", FONT_SIZE/2));
        Q_1 = new GAIGSregister(1,       "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        Q_1.set("0");
        currentRow.add(Q_1);
        
        easySnap("\u03B2 is initialized to Zero", easyPseudo(5), null);

        //----Count Initialization Frame----
        init[0] = trace.getWidth() - FONT_SIZE - RIGHT_MARGIN;
        init[2] = trace.getWidth() - RIGHT_MARGIN;
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                FONT_SIZE, FONT_COLOR, "Count", FONT_SIZE/2));
        Count = new CountBox(REG_SIZE, DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, FONT_SIZE);
        currentRow.add(Count);
        easySnap("Count is initialized to\nthe number of bits in a register.", easyPseudo(6), null);
        double[] last =  currentRow.get(0).getBounds();
        
        currentRow.add(new GAIGSline(new double[] {last[0], trace.getWidth()}, new double[] {last[1]-ROW_SPACE/2, last[1]-ROW_SPACE/2}));
        
        //Maybe this should be a function of GAIGSpane
        double[] unitLengths = main.getRealCoordinates(trace.getRealCoordinates(currentRow.getRealCoordinates(new double[]{0,0,1,1})));
        double unitLengthX = unitLengths[2]-unitLengths[0];
        
        currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, "Initialization", FONT_SIZE*0.5));
        
        boothsMultiplication();

        //----Finished Frame----
        //setRowTextColor(INACTIVE_TEXT);
        //setRowOutlineColor(INACTIVE_OUTLINE);
        decimal.complete();
        binary.complete();
        RegA.setFillOutlineColor(PURPLE);
//        RegA.setTextColor(FONT_COLOR);
        RegQ.setFillOutlineColor(PURPLE);
//        RegQ.setTextColor(FONT_COLOR);
        easySnap("The result is " + RegA + RegQ + "\nwhich is "
                + ((Integer.parseInt(toDecimal(args[1])))*(Integer.parseInt(toDecimal(args[2])))) + " in decimal", easyPseudo(25), null);


        show.close();
    }

    public static void boothsMultiplication(){
        //Maybe this should be a function of GAIGSpane
        double[] unitLengths = main.getRealCoordinates(trace.getRealCoordinates(currentRow.getRealCoordinates(new double[]{0,0,1,1})));
        double unitLengthX = unitLengths[2]-unitLengths[0];
        double[] last;
        
        while (Count.getCount() >= 0){
            //----Count Frame----
            Count.setFillColor(YELLOW);
            easySnap("Check the value of Count", easyPseudo(8, YELLOW, BLACK), null);
            //Change color back
            Count.setFillColor(DEFAULT_COLOR);

            if (Count.getBit(0) == 0) break; //Thats so we get the final check

            //----Start of Comparison and Addition/Subtraction Frame Logic----

            /* Note: This logic for drawing these frames is dictated by the QuestionGenerator,
             * not Booth's Multiplication Algorithm.  Previous revisions were cleaner. 
             */
            int cmpVal = RegQ.getBit(0) - Q_1.getBit(0);

            if (cmpVal == 1 || cmpVal == -1) {                
                positionAdditionRow();//clones all registers
                addRow();//now we have enough information for question type 3, calculations pending
                DiscardOverflowAddition sum;
                GAIGSmonospacedText sumLabel;
                GAIGSmonospacedText discardLabel;

                //Subtraction case   
                if (cmpVal == 1) {
                    sum = new DiscardOverflowAddition('+', RegA.toString(), negateValue(RegM).toString(), 2,
                            math.getWidth()/1.4, math.getHeight()/1.5, FONT_SIZE+.005, FONT_SIZE+.01, FONT_COLOR, GREEN);
                    sumLabel = new GAIGSmonospacedText(sum.getBounds()[2]+MATH_LABEL_SPACE/2, sum.getBounds()[3]-sum.getFontSize(), 
                            GAIGStext.HCENTER, GAIGStext.VTOP, sum.getFontSize(), FONT_COLOR, "(A)\n(-M)", sum.getFontSize()*1.5);
                    discardLabel = new GAIGSmonospacedText(sum.getBounds()[0], sum.getBounds()[3]+ sum.getFontSize()/2,
                            GAIGSmonospacedText.HLEFT, GAIGSmonospacedText.VBOTTOM, sumLabel.getFontSize(), FONT_COLOR, "Discard any\nOverflow", sumLabel.getFontSize()*1.25);
                    
                    addIntoReg(negateValue(RegM), RegA);
                }
                //Addition case
                else {
                    sum = new DiscardOverflowAddition('+', RegA.toString(), RegM.toString(), 2,
                            math.getWidth()/1.4, math.getHeight()/1.5, FONT_SIZE+.005, FONT_SIZE+.01, FONT_COLOR, GREEN);
                    sumLabel = new GAIGSmonospacedText(sum.getBounds()[2]+MATH_LABEL_SPACE/2, sum.getBounds()[3]-sum.getFontSize(), 
                            GAIGStext.HCENTER, GAIGStext.VTOP, sum.getFontSize(), FONT_COLOR, "(A)\n(M)", sum.getFontSize()*1.5);
                    discardLabel = new GAIGSmonospacedText(sum.getBounds()[0], sum.getBounds()[3]+ sum.getFontSize()/2,
                            GAIGSmonospacedText.HLEFT, GAIGSmonospacedText.VBOTTOM, sumLabel.getFontSize(), FONT_COLOR, "Discard any\nOverflow", sumLabel.getFontSize()*1.25);
                    addIntoReg(RegM, RegA);
                }

                //----Comparison Frame----
                getRegisterFromRow(trace.size()-2, REGQ).setFillOutlineColor(0, BLUE);
                getRegisterFromRow(trace.size()-2, Q1).setFillOutlineColor(0, BLUE);

                question que = quest.getComparisonQuestion();
                GAIGSpane<?> temp = trace.remove(trace.size()-1);
                easySnap("Determine the operation", easyPseudo(10, BLUE, BLACK), que);
                trace.add(temp);

                //Reset/deactivate colors
                fadeRow(trace.size()-2);
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);


                //----Addition/Subtraction frame----
                RegA.setFillOutlineColor(GREEN);
                math.add(sum);
                math.add(sumLabel);
                math.add(discardLabel);
                sum.complete();
                
                last =  currentRow.get(0).getBounds();
                currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                        last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, (cmpVal == 1 ? "Subtraction" : "Addition"), FONT_SIZE*0.5));
                
                easySnap((cmpVal == 1 ? "Subtract M from " : "Add M to ") + "A", easyPseudo((cmpVal == 1 ? 11 : 14), GREEN, BLACK), quest.getAdditionQuestion() );
                //Remove Overflow Label
                math.remove(math.size()-1);
                //Remove Label
                math.remove(math.size()-1);
                //Remove Addition
                math.remove(math.size()-1);
                sumLabel.setText("");

            }
            else {
                //----Comparison Frame---- (yep, again)
                //Colors
                RegQ.setFillOutlineColor(0, BLUE);
                Q_1.setFillOutlineColor(0, BLUE);

                //Question pane-hopping
                trace.add(null);
                question que = quest.getQuestion(1);
                trace.remove(trace.size()-1);
                easySnap("Determine the operation", easyPseudo(10, BLUE, BLACK), que);

                //Reset colors
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);
            }
            //Deactivate text
            setRowTextColor(INACTIVE_TEXT);
            setRowOutlineColor(INACTIVE_OUTLINE);

            //----Shift Frame----
            positionMajorRow(); //Remember this clones
            setRowTextColor(FONT_COLOR);
            addRow();
            rightShift(RegA, RegQ, Q_1);

            //Question and write
            question que = quest.getShiftQuestion(); 

            //Colors
            getRegisterFromRow(trace.size()-2, REGA).setFillOutlineColor(GREEN);
            getRegisterFromRow(trace.size()-2, REGQ).setFillOutlineColor(BLUE);
            setRowOutlineColor(OUTLINE_COLOR);
            RegA.setFillOutlineColor(GREEN);
            RegQ.setFillOutlineColor(BLUE);
            RegQ.setFillOutlineColor(REG_SIZE-1, GREEN);
            Q_1.setFillOutlineColor(0, BLUE);
            currentRow.remove(COUNT); //Oops...We don't want Count
            
            last =  currentRow.get(0).getBounds();
            currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                    last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, "Shift", FONT_SIZE*0.5));
            
            easySnap("Sign-Preserving Right Shift", easyPseudo(19, BLUE, BLACK), que);
            //    		RegQ.setTextColor(FONT_COLOR);
            Q_1.setFillOutlineColor(DEFAULT_COLOR);

            //Clean Color of A and Q on the previous line
            getRegisterFromRow(trace.size()-2, REGA).setFillOutlineColor(INACTIVE_FILL);
            getRegisterFromRow(trace.size()-2, REGQ).setFillOutlineColor(INACTIVE_FILL);
            RegA.setFillOutlineColor(DEFAULT_COLOR);
            RegQ.setFillOutlineColor(DEFAULT_COLOR);

            //----Decrement Count Frame---
            Count.decrement();
            currentRow.add(COUNT, Count); //Now we do want Count
            Count.setFillOutlineColor(RED);
            last =  currentRow.get(0).getBounds();
            currentRow.add(new GAIGSline(new double[] {last[0], trace.getWidth()}, new double[] {last[1]-ROW_SPACE/2, last[1]-ROW_SPACE/2}));
            easySnap("Decrement Count", easyPseudo(21, RED, BLACK), null);
            Count.setFillOutlineColor(DEFAULT_COLOR);
            //Hey!  We're ready to loop!
        }
    }

    public static void rightShift(GAIGSregister A, GAIGSregister Q, GAIGSregister Q_1) {
        if (A.getSize() < 1) return;

        Q_1.setBit(0, Q.getBit(0));
        int shiftOverToQ = A.getBit(0);

        for (int i = 0; i < REG_SIZE - 1; i++) {
            A.setBit(i, A.getBit(i+1));
            Q.setBit(i, Q.getBit(i+1));
        }

        Q.setBit(REG_SIZE-1, shiftOverToQ);
    }

    /**
     * Adds two registers, storing the result in the second register (a la AT&T Syntax).
     * @param toAdd other addend, not modified by function.
     * @param A	Destination Register and addend.
     */
    public static void addIntoReg(GAIGSregister toAdd, GAIGSregister A) {
        int carry = 0;
        int sum = 0;
        for (int i = 0; i < REG_SIZE; i++) {
            sum = carry + A.getBit(i) + toAdd.getBit(i);
            A.setBit(i, sum % 2);
            carry = sum / 2;
        }
    }

    /**
     * Negates the value of a register, in two's complement.
     * @param M The register to negate
     * @return A new register with the negated value.
     */
    public static GAIGSregister negateValue(GAIGSregister M) {
        int carry = 1;
        GAIGSregister ret = new GAIGSregister(M);

        for (int i = 0; i < M.getSize(); i++) {
            int negPart = 0;

            if (M.getBit(i) == 0) negPart = 1;
            else negPart = 0;

            ret.setBit(i, (negPart + carry) % 2);
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

    private static void adjustRegister(GAIGSregister reg){
        double[] bds = reg.getBounds();
        bds[3] = bds[1]-(ROW_SPACE);
        bds[1] = bds[3]-REG_HEIGHT;
        reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
    }

    private static void minorAdjustRegister(GAIGSregister reg){
        double[] bds = reg.getBounds();
        bds[3] = bds[1]-(ROW_SPACE/2);
        bds[1] = bds[3]-REG_HEIGHT;
        reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
    }

    private static void setRowTextColor(String color) {
        RegM.setTextColor(color) ;
        RegA.setTextColor(color) ;
        RegQ.setTextColor(color) ;
        Q_1.setTextColor(color) ;
        Count.setTextColor(color);
    }

    private static void setRowOutlineColor(String color) {
        RegM.setOutlineColor(color) ;
        RegA.setOutlineColor(color) ;
        RegQ.setOutlineColor(color) ;
        Q_1.setOutlineColor(color) ;
        Count.setOutlineColor(color);
    }


    private static void fadeRow(int row){
        setRowTextColor(row, INACTIVE_TEXT);
        setRowOutlineColor(row, INACTIVE_OUTLINE);
        setRegRowFillColor(row, INACTIVE_FILL);
    }

    private static void setRowTextColor(int row, String color) {
        getRegisterFromRow(row, REGM).setTextColor(color);
        getRegisterFromRow(row, REGA).setTextColor(color);
        getRegisterFromRow(row, REGQ).setTextColor(color);
        getRegisterFromRow(row, Q1  ).setTextColor(color);
        getRegisterFromRow(row,COUNT).setTextColor(color);
    }

    private static void setRowOutlineColor(int row, String color) {
        getRegisterFromRow(row, REGM).setOutlineColor(color);
        getRegisterFromRow(row, REGA).setOutlineColor(color);
        getRegisterFromRow(row, REGQ).setOutlineColor(color);
        getRegisterFromRow(row, Q1  ).setOutlineColor(color);
        getRegisterFromRow(row,COUNT).setOutlineColor(color);
    }

    private static void setRegRowFillColor(int row, String color) {
        getRegisterFromRow(row, REGM).setFillColor(color);
        getRegisterFromRow(row, REGA).setFillColor(color);
        getRegisterFromRow(row, REGQ).setFillColor(color);
        getRegisterFromRow(row, Q1  ).setFillColor(color);
        getRegisterFromRow(row,COUNT).setFillColor(color);
    }

    private static void positionMajorRow(){
        RegM = RegM.clone();
        RegA = RegA.clone();
        RegQ = RegQ.clone();
        Q_1 = Q_1.clone();
        Count = Count.clone();

        adjustRegister(RegM);
        adjustRegister(RegA);
        adjustRegister(RegQ);
        adjustRegister(Q_1);
        adjustRegister(Count);
    }

    //TODO figure out when to use this, then make it actually do something
    private static void positionAdditionRow(){
        RegM  = RegM.clone();
        RegA  = RegA.clone();
        RegQ  = RegQ.clone();
        Q_1   = Q_1.clone();
        Count = Count.clone();

        adjustRegister(RegM);
        adjustRegister(RegA);
        adjustRegister(RegQ);
        adjustRegister(Q_1);
        adjustRegister(Count);
    }

    /*
     * Now we are restricted and must be very careful what goes on the trace and 
     * currentRow
     */
    private static GAIGSregister getRegisterFromRow(int row, int reg) {
        return (GAIGSregister) trace.get(row).get(reg);
    }

    private static void addRow(){
        currentRow = new GAIGSpane<MutableGAIGSdatastr>();
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

    private static void easySnap(String title, String info, String pseudo, question que, GAIGSdatastr... stuff){
        if (title != null) BoothMultiplication.title.setText(title);
        try {
            if (que == null)
                show.writeSnap(" ", info, pseudo, stuff);
            else
                show.writeSnap(" ", info, pseudo, que, stuff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void easySnap(String title, String pseudo, question que, GAIGSdatastr... stuff){
        easySnap(title, docURI.toASCIIString(), pseudo, que, stuff);
    }

    private static void easySnap(String title, String pseudo, question que){
        easySnap(title, pseudo, que, main);
    }

    private static String easyPseudo(int selected){
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(),
                    selected, "#000000", "#FFFFFF");
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }
    
    private static String easyPseudo(int selected, String highlight, String textColor){
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(),
                    selected, highlight, textColor);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private static String easyPseudo(int selected, int lineColor){
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(),
                    selected, lineColor);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }
}
