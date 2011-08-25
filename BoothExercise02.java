package exe.boothsMultiplication;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.GAIGSArithmetic;
import exe.GAIGSarrow;
import exe.GAIGSdatastr;
import exe.GAIGSline;
import exe.GAIGSmonospacedText;
import exe.GAIGSpane;
import exe.GAIGSregister;
import exe.GAIGStext;
import exe.MutableGAIGSdatastr;
import exe.ShowFile;
import exe.question;
import exe.pseudocode.PseudoCodeDisplay;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class BoothExercise02 {
    private static PseudoCodeDisplay pseudo;
    private static URI docURI;
    private static GAIGSregister RegM;
    private static GAIGSregister RegA;
    private static GAIGSregister RegQ;
    private static GAIGSregister Q_1;
    private static CountBox Count;
    private static GAIGSpane<GAIGSpane<?>> main; //Is this the same as GAIGSpane<GAIGSpane<? extends MutableGAIGSdatastr>>?
    private static GAIGSpane<MutableGAIGSdatastr> header;
    private static GAIGSpane<MutableGAIGSdatastr> userInputPane;
    private static String[]                       userInputValues;
    private static HashMap<String, Boolean> correctInputs;
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
    public static final String LIGHT_GREY= "#DDDDDD";
    public static final String GREY      = "#BBBBBB";
    public static final String DARK_GREY = "#666666";
    public static final String RED       = "#FF9999";
    public static final String RUBY      = "#FF0000";
    public static final String GREEN     = "#55FF55";
    public static final String DARK_GREEN= "#008800";
    public static final String BLUE      = "#AAABFF";
    public static final String YELLOW    = "#FFFF00";
    public static final String GOLD      = "#CDAD00";
    public static final String PURPLE    = GREEN;
    public static final String FONT_COLOR      = BLACK;
    public static final String DEFAULT_COLOR   = WHITE;
    public static final String INACTIVE_TEXT   = DARK_GREY;
    public static final String INACTIVE_OUTLINE= LIGHT_GREY;
    public static final String INACTIVE_FILL   = WHITE;
    public static final String OUTLINE_COLOR   = FONT_COLOR;

    private static final double WINDOW_WIDTH   = 1+GAIGSpane.JHAVE_X_MARGIN*2;
    private static final double WINDOW_HEIGHT  = 1+GAIGSpane.JHAVE_Y_MARGIN*2;

    private static       double MATH_LABEL_SPACE;
    private static final double FONT_SIZE         = 0.05;//was 0.05
    private static final double COLBL_FONT_SIZE   = FONT_SIZE+0.01;
    private static final double REG_FONT_SIZE     = FONT_SIZE-0.01;
    
    private static final double REG_WIDTH_PER_BIT = REG_FONT_SIZE;
    private static final double COUNT_WIDTH       = REG_WIDTH_PER_BIT;
    
    
    private static final double REG_HEIGHT        = 0.06;
    private static       double REG_WIDTH;
    private static final double ROW_SPACE         = 0.03;
    private static       double COL_SPACE         = 0.00;

    private static final double LEFT_MARGIN       = 0.0;
    private static final double RIGHT_MARGIN      = COLBL_FONT_SIZE;
    private static final double TOP_MARGIN        = 0.0;
    private static final double COUNT_LEFT_MARGIN = RIGHT_MARGIN * 2;

    /**
    * The greek character beta, lower case
    */
    public static final String BETA = "β";

    private static PrintWriter writer;//debug stuff

    public static void main(String args[]) throws IOException {
        //JHAVÉ Stuff
        show = new ShowFile(args[0]);

        //Load the Pseudocode
        try{
            pseudo = new PseudoCodeDisplay("exe/boothsMultiplication/pseudocode.xml");
        } catch (JDOMException e){
            e.printStackTrace();
        }
 
        if (DEBUG) {
            writer = new PrintWriter("/home/christopher/jhave2/server/src/exe/boothsMultiplication/ex01b.log");
            writer.println("Beginning Log");
        }

        try{
            docURI = new URI("str", "<html>hi</html>", "");
        } catch (java.net.URISyntaxException e) {}

        //Our Stuff
        int multiplicand = Integer.parseInt(args[args.length-2]);
        int multiplier   = Integer.parseInt(args[args.length-1]);

        userInputValues = new String[args.length - 3];

        for (int i = 1; i < args.length-2; ++i)
            userInputValues[i-1] = args[i];

        if (DEBUG) writer.println("M\t" + multiplicand + "\nQ\t" + multiplier);

        String binMultiplicand = Utilities.toBinary(multiplicand);
        String binMultiplier   = Utilities.toBinary(multiplier);

        correctInputs = new HashMap<String, Boolean>();
        correctInputs.put("M",     true);
        correctInputs.put("A",     true);
        correctInputs.put("Q",     true);
        correctInputs.put("β",     true);
        correctInputs.put("Count", true);

        if (DEBUG) writer.println("M\t" + binMultiplicand + "\nQ\t" + binMultiplier);

        //logic to make M and Q the same size
        if (binMultiplicand.length() > binMultiplier.length() ) 
            binMultiplier = Utilities.signExtend(binMultiplier, binMultiplicand.length() - binMultiplier.length() );
        else if (binMultiplier.length() > binMultiplicand.length() )
            binMultiplicand = Utilities.signExtend(binMultiplicand, binMultiplier.length() - binMultiplicand.length() );

        if (Utilities.isWellFormedBinary(args[1]) && Utilities.isWellFormedBinary(args[3]) ) //if well formed
            if (args[1].length() == args[3].length() ) //and matching in length
                if (Utilities.toDecimal(args[1]).equals("" + multiplicand) && Utilities.toDecimal(args[3]).equals("" + multiplier)  ) //and correct
                    if (args[1].length() > binMultiplicand.length() && args[1].length() <= 8) {
                        //then, if it's a larger representation but not too large
                        binMultiplicand = Utilities.signExtend(binMultiplicand, args[1].length() - binMultiplicand.length() );
                        binMultiplier   = Utilities.signExtend(binMultiplier  , args[3].length() - binMultiplier.length()   );
                        //make it the size of the user input
                    }

        REG_SIZE = binMultiplicand.length();

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

        title=new GAIGSmonospacedText(header.getWidth()/2, header.getHeight()-FONT_SIZE*1.5, 
            GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VTOP, .25, FONT_COLOR, "", .1);
        header.add(title);

        MATH_LABEL_SPACE  = header.getWidth()/20;

        //User input (uses same space as math/alu pane standard viz
        userInputPane = new GAIGSpane<MutableGAIGSdatastr>(WINDOW_WIDTH*(3/4.0), 0, WINDOW_WIDTH, WINDOW_HEIGHT*(8/10.0), 1.0, 1.0);
        userInputPane.setName("User Input");

        userInputPane.add(new GAIGSline(new double[] {0,0},
            new double[] {userInputPane.getHeight()+FONT_SIZE,
            userInputPane.getHeight() - ( (REG_SIZE+1) * (REG_HEIGHT+ ROW_SPACE) - ROW_SPACE/2 + 
            (numLines(binMultiplier)-REG_SIZE) * (ROW_SPACE/2 + REG_HEIGHT))}));

        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/2, userInputPane.getHeight(), GAIGSmonospacedText.HCENTER, 
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "User Input"));

        final double USER_INPUT_TEXT_HEIGHT = 5 * userInputPane.getHeight() / 6;
       
        //User input text for M, A, Q, Beta, Count
        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/3, USER_INPUT_TEXT_HEIGHT, GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "M:\t" + args[1]));

        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "A:\t" + args[2]));

        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*2), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "Q:\t" + args[3]));

        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*3), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "β:\t" + args[4]));

        userInputPane.add(new GAIGSmonospacedText(userInputPane.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*4), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "Count:\t" + args[5]));
        //END user input

        trace = new GAIGSpane<GAIGSpane<?>>(0, 0, WINDOW_WIDTH*(3/4.0), userInputPane.getBounds()[3], null, 1.0);
        trace.setName("Trace");

        main.add(header);
        main.add(trace);
        main.add(userInputPane);

        GAIGSpane<GAIGSmonospacedText> trace_labels = new GAIGSpane<GAIGSmonospacedText>();

        trace.add(trace_labels);

        currentRow = new GAIGSpane<MutableGAIGSdatastr>();
        currentRow.setName("Row " + rowNumber++);

        trace.add(currentRow);

        REG_WIDTH = REG_WIDTH_PER_BIT * REG_SIZE;
        COL_SPACE = ((trace.getWidth()-LEFT_MARGIN-RIGHT_MARGIN-COUNT_WIDTH) - (3* REG_WIDTH) - REG_WIDTH_PER_BIT)/4;

        //We only want to use the Count Margin when it would otherwise be too small
        if (COL_SPACE < COUNT_LEFT_MARGIN){
            COL_SPACE = ((trace.getWidth()-LEFT_MARGIN-RIGHT_MARGIN-COUNT_WIDTH) - (3* REG_WIDTH) - REG_WIDTH_PER_BIT - COUNT_LEFT_MARGIN)/3;
        }

        //Initialize Register Location
        double[] init = new double[] {
            LEFT_MARGIN,
            trace.getHeight()-TOP_MARGIN-REG_HEIGHT,
            LEFT_MARGIN+REG_WIDTH,
            trace.getHeight()-TOP_MARGIN};

        //**** Initialization Frames ****//
        
        //----Register M Initialization Frame----//
        trace_labels.add(new GAIGSmonospacedText(
                (init[2]-init[0])/2.0+init[0], init[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                COLBL_FONT_SIZE, FONT_COLOR, "M", COLBL_FONT_SIZE/2));
        RegM= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, REG_FONT_SIZE);
        RegM.set(binMultiplicand);

        currentRow.add(RegM);
        
        title.setText("M was given as " + multiplicand + 
            "\nwhich is " + binMultiplicand + " as a " + binMultiplicand.length() 
            + "-digit binary value"); //Must do this here so we get the correct bounds
        
        easySnap(null, easyPseudo(2), null); //A null title indicates to keep the last one.

        REG_SIZE = RegM.getSize();
        //----Register M Error Check Frame----//
        errorCheck(args[1], binMultiplicand, "M", (GAIGSmonospacedText) userInputPane.get(2), true, easyPseudo(2) );
        //easySnap(null, easyPseudo(2), null);

        //----Register A Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+REG_WIDTH;
        trace_labels.add(new GAIGSmonospacedText(
            (init[2]-init[0])/2.0+init[0], init[3],
            GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
            COLBL_FONT_SIZE, FONT_COLOR, "A", COLBL_FONT_SIZE/2));
        RegA= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, REG_FONT_SIZE);
        RegA.set("0");
        currentRow.add(RegA);
        easySnap("A is initialized to Zero", easyPseudo(3), null);

        //----Register A Error Check Frame----//
        errorCheck(args[2], RegA.toString(), "A", (GAIGSmonospacedText) userInputPane.get(3), true, easyPseudo(3) );
//      easySnap(null, easyPseudo(3), null);

        //----Register Q Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+REG_WIDTH;
        trace_labels.add(new GAIGSmonospacedText(
            (init[2]-init[0])/2.0+init[0], init[3],
            GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
            COLBL_FONT_SIZE, FONT_COLOR, "Q", COLBL_FONT_SIZE/2));
        RegQ= new GAIGSregister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, REG_FONT_SIZE);
        RegQ.set(binMultiplier);
        currentRow.add(RegQ);

         title.setText("Q was given as " + multiplier + 
            "\nwhich is " + binMultiplier + " as a " + binMultiplier.length() 
            + "-digit binary value");

        easySnap(null, easyPseudo(4), null);

        //----Register Q Error Check Frame----//
        errorCheck(args[3], binMultiplier, "Q", (GAIGSmonospacedText) userInputPane.get(4), true, easyPseudo(4) );
//      easySnap(null, easyPseudo(4), null);

        //----Bit β Initialization Frame----
        init[0] = init[2]+(COL_SPACE);
        init[2] = init[0]+FONT_SIZE;
        trace_labels.add(new GAIGSmonospacedText(
            (init[2]-init[0])/2.0+init[0], init[3],
            GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
            COLBL_FONT_SIZE, FONT_COLOR, "β", COLBL_FONT_SIZE/2));
        Q_1 = new GAIGSregister(1,       "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, REG_FONT_SIZE);
        Q_1.set("0");
        currentRow.add(Q_1);
        
        easySnap("β is initialized to Zero", easyPseudo(5), null);

        //----Bit β Error Check Frame----//
        errorCheck(args[4], "0", "β", (GAIGSmonospacedText) userInputPane.get(5), true, easyPseudo(5) );
//      easySnap(null, easyPseudo(5), null);

        //----Count Initialization Frame----
        init[0] = trace.getWidth() - COUNT_WIDTH - RIGHT_MARGIN;
        init[2] = trace.getWidth() - RIGHT_MARGIN;
        trace_labels.add(new GAIGSmonospacedText(
            (init[2]-init[0])/2.0+init[0], init[3],
            GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
            COLBL_FONT_SIZE, FONT_COLOR, "Count", COLBL_FONT_SIZE/2));
        Count = new CountBox(REG_SIZE, DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, init, REG_FONT_SIZE);
        currentRow.add(Count);
        easySnap("Count is initialized to\nthe number of bits in a register.", easyPseudo(6), null);
        double[] last =  currentRow.get(0).getBounds();

        //----Count Error Check Frame----//
        errorCheck(args[5], "" + Count.getCount(), "Count", (GAIGSmonospacedText) userInputPane.get(6), false, easyPseudo(6) );
//      easySnap(null, easyPseudo(6), null);

        //Maybe this should be a function of GAIGSpane
        double[] unitLengths = main.getRealCoordinates(trace.getRealCoordinates(currentRow.getRealCoordinates(new double[]{0,0,1,1})));
        double unitLengthX = unitLengths[2]-unitLengths[0];
        
        currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, "Initialization", FONT_SIZE*0.5));

        boothsMultiplication();

        //----Finished Frame----
        RegA.setFillOutlineColor(GREEN);
//        RegA.setTextColor(FONT_COLOR);
        RegQ.setFillOutlineColor(GREEN);
//        RegQ.setTextColor(FONT_COLOR);
        easySnap("The result is " + RegA + RegQ + "\nwhich is "
                + (Integer.parseInt(args[args.length-1] ) * Integer.parseInt(args[args.length-2])) + 
                " in decimal", easyPseudo(-1), null);

        if (DEBUG) writer.close();
        show.close();
    }

    //writes a snap if a previously correct answer column is now incorrect, or a previously incorrect
    //answer column is now correct
    private static void errorCheck(String checkMe, String checkAgainst, String name, 
        GAIGSmonospacedText displayMe, boolean binaryNum, String pseudo) {
        String mesg = "";
        boolean answer = false;

        if (checkMe.equalsIgnoreCase("INVALID") ) {
            mesg = "An invalid value was given for " + name + 
                "\nYou may have left this field blank or gave an input with whitespace";
            displayMe.setColor(RUBY);
        }
        else if ( ((!Utilities.isWellFormedBinary(checkMe)) && binaryNum) || 
            ((!Utilities.isWellFormedDecimal(checkMe)) && !binaryNum) ) {//multiplexor anyone?

            mesg = "'" + checkMe + "' is not a well-formed " + (binaryNum ? "binary " : "decimal " ) + "integer value";
            displayMe.setColor(RUBY);
        }
        else if (binaryNum && checkMe.length() > 8) {
            mesg = "Inputs for binary values may be at most 8 bits long for this exercise";
            displayMe.setColor(RUBY);
        }
        else if (!checkMe.equals(checkAgainst) ) {
            if (binaryNum) {
                if (Utilities.toDecimal(checkMe).equals(Utilities.toDecimal(checkAgainst)) ) {
                    mesg = "You gave the correct binary value, but your answer\n" +  
                        "uses an incorrect/inconsistent number of bits to represent it";
                }
                else {
                    if (name.equals("β") ) {
                        mesg = "Incorrectl value given for β";
                    }
                    else {
                        mesg = "Signed binary value " + checkMe + " is " + Utilities.toDecimal(checkMe) + " in decimal;\n" + 
                            "however, " + checkAgainst + " (which is " + Utilities.toDecimal(checkAgainst) +" in decimal) is the correct answer";
                    }
                }

                displayMe.setColor(RUBY);
            }

            else {//decimal -> Count
                String tmp = displayMe.getText();
                mesg = "The number of bits in a register for this case is " + REG_SIZE + ", not " + tmp.substring(tmp.lastIndexOf("\t")  + 1);
                displayMe.setColor(RUBY);
            }
        }

        else {
            mesg = name + ": Correct!";
            answer = true;
            displayMe.setColor(DARK_GREEN);
        }

        if (correctInputs.get(name) || answer) //will show if you previously got it wrong and then corrected, or was correct and got it wrong
        {
            title.setText(mesg);
            easySnap(null, pseudo, null);
            show.flush();
        }

        correctInputs.put(name, answer);
    }

    private static void errorCheckAll(int userInputNum) {
        String[] names = new String[] {"M", "A", "Q", "β", "Count"};
        boolean[] binaryBools= new boolean[] {true, true, true, true, false};
        String[] colors      = new String[] {RUBY, RUBY, RUBY, RUBY, RUBY};
        String[] rightAnswers  = new String[] {RegM.toString(), RegA.toString(), RegQ.toString(), Q_1.toString(), Count.toString()};
        ArrayList<String> messages = new ArrayList<String>();
        messages.add("");

        for (int i = 0; i < 5; ++i) {
            String checkMe = userInputValues[userInputNum + i];
            String checkAgainst = rightAnswers[i];
            String name = names[i];
            GAIGSmonospacedText displayMe = (GAIGSmonospacedText) userInputPane.get(2 + i);
            boolean binaryNum = binaryBools[i];
            boolean answer    = false;
            String mesg = "";

            if (checkMe.equalsIgnoreCase("INVALID") ) {
                mesg = "An invalid value was given for " + name + 
                    "\nYou may have left this field blank or gave an input with whitespace";
//              displayMe.setColor(RUBY);
            }
            else if ( ((!Utilities.isWellFormedBinary(checkMe)) && binaryNum) || 
                ((!Utilities.isWellFormedDecimal(checkMe)) && !binaryNum) ) {//multiplexor anyone?

                mesg = "'" + checkMe + "' is not a well-formed " + (binaryNum ? "binary " : "decimal " ) + "integer value";
 //             displayMe.setColor(RUBY);
            }
            else if (binaryNum && checkMe.length() > 8) {
                mesg = "Inputs for binary values may be at most 8 bits long for this exercise";
//              displayMe.setColor(RUBY);
            }
            else if (!checkMe.equals(checkAgainst) ) {
                if (binaryNum) {
                    if (Utilities.toDecimal(checkMe).equals(Utilities.toDecimal(checkAgainst)) ) {
                        mesg = "You gave the correct binary value, but your answer\n" +  
                            "uses an incorrect/inconsistent number of bits to represent it";
                    }
                    else {
                        if (name.equals("β") ) {
                            mesg = "Incorrectl value given for β";
                        }
                        else {
                            mesg = "Signed binary value " + checkMe + " is " + Utilities.toDecimal(checkMe) + " in decimal;\n" + 
                                "however, " + checkAgainst + " (which is " + Utilities.toDecimal(checkAgainst) +" in decimal) is the correct answer";
                        }
                    }

//                  displayMe.setColor(RUBY);
                }

                else {//decimal -> Count
                    String tmp = displayMe.getText();
                    mesg = "The number of bits in a register for this case is " + REG_SIZE + ", not " + tmp.substring(tmp.lastIndexOf("\t")  + 1);
//                  displayMe.setColor(RUBY);
                }
            }

            else {
                mesg = (messages.get(0).length() == 0 ? name : ", " + name);
                answer = true;
                colors[i] = DARK_GREEN;
//              displayMe.setColor(DARK_GREEN);
            }

            if (correctInputs.get(name) || answer) //will show if you previously got it wrong and then corrected, or was correct and got it wrong
            {
                if (answer) {
                    messages.set(0, messages.get(0) + mesg);
                }
                else {
                    messages.add(mesg);
                }
//              title.setText(mesg);
//              easySnap(null, easyPseudo(22), null);

            }

            correctInputs.put(name, answer);
            displayMe.setColor(colors[i]);
        }

        if (messages.get(0).length() != 0)//if you didn't get all the answers for this round wrong
            messages.set(0, messages.get(0) + ": correct!" );
 
        for (int i = 0; i < messages.size(); ++i) {
            title.setText(messages.get(i) );
            easySnap(null, easyPseudo(22), null);
            show.flush();
        }

        for (int i = 0; i < 5; ++i)
            ( (GAIGSmonospacedText) (userInputPane.get(2 + i) ) ).setColor(FONT_COLOR);
    }

    private static void setUserInputValues(int userInputNum) {
        for (int i = 0; i < 5; ++i) {
            GAIGSmonospacedText temp = (GAIGSmonospacedText) userInputPane.get(2 + i);
            String tempTxt           = temp.getText();
            temp.setText(tempTxt.substring(0, tempTxt.indexOf("\t")+1) + userInputValues[userInputNum + i]);
            temp.setColor(FONT_COLOR);
        }
    }

    public static void boothsMultiplication(){
        //Maybe this should be a function of GAIGSpane
        double[] unitLengths = main.getRealCoordinates(trace.getRealCoordinates(currentRow.getRealCoordinates(new double[]{0,0,1,1})));
        double unitLengthX = unitLengths[2]-unitLengths[0];
        double[] last;
        boolean did_math = false;
        int userInputNum = 0;        

        while (Count.getCount() >= 0){
            //----Initialize inputs for answers---//
            userInputNum += 5;

            if (Count.getBit(0) != 0)
                setUserInputValues(userInputNum);//no user input on last check

            //----Count Frame----
            Count.setFillColor(YELLOW);
            easySnap("Check the value of Count", easyPseudo(8, YELLOW, BLACK), null);
            //Change color back
            Count.setFillColor(DEFAULT_COLOR);

            if (Count.getBit(0) == 0) break; //Thats so we get the final check

            //----Start of Comparison and Addition/Subtraction Frame Logic----
            int cmpVal = RegQ.getBit(0) - Q_1.getBit(0);

            if (cmpVal == 1 || cmpVal == -1) {
                did_math = true;
                positionMajorRow();//clones all registers
                addRow();
                DiscardOverflowAddition sum;
                GAIGSmonospacedText sumLabel;
                GAIGSmonospacedText discardLabel;

                //Subtraction case   
                if (cmpVal == 1) {
                    addIntoReg(negateValue(RegM), RegA);
                }
                //Addition case
                else {
                    addIntoReg(RegM, RegA);
                }

                //----Comparison Frame----
                getRegisterFromRow(trace.size()-2, REGQ).setFillOutlineColor(0, BLUE);
                getRegisterFromRow(trace.size()-2, Q1).setFillOutlineColor(0, BLUE);

                //Reset/deactivate colors
                fadeRow(trace.size()-2);
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);


                //----Addition/Subtraction frame----
                RegA.setFillOutlineColor(GREEN);
                
                last =  currentRow.get(0).getBounds();
                currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                        last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, (cmpVal == 1 ? "Subtraction" : "Addition"), FONT_SIZE*0.5));
                
                easySnap((cmpVal == 1 ? "Subtract M from " : "Add M to ") + "A", easyPseudo((cmpVal == 1 ? 11 : 14), GREEN, BLACK), null);

            }
            else {
                //----Comparison Frame---- (yep, again)
                //Colors
                RegQ.setFillOutlineColor(0, BLUE);
                Q_1.setFillOutlineColor(0, BLUE);

                easySnap("Determine the operation", easyPseudo(10, BLUE, BLACK), null);

                //Reset colors
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);
            }
            //Deactivate text
            setRowTextColor(INACTIVE_TEXT);
            setRowOutlineColor(INACTIVE_OUTLINE);

            //----Shift Frame----
            if (did_math){
                positionAdditionRow();
                did_math = false;
            } else positionMajorRow(); //Remember this clones
            
            setRowTextColor(FONT_COLOR);
            addRow();
            rightShift(RegA, RegQ, Q_1);

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
            
            easySnap("Sign-Preserving Right Shift", easyPseudo(19, BLUE, BLACK), null);
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
            //before we do that, check for errors!

            errorCheckAll(userInputNum);

/*          errorCheck(userInputValues[userInputNum  ], RegM.toString(), "M",     (GAIGSmonospacedText)userInputPane.get(2), true, easyPseudo(22) );
            errorCheck(userInputValues[userInputNum+1], RegA.toString(), "A",     (GAIGSmonospacedText)userInputPane.get(3), true, easyPseudo(22) );
            errorCheck(userInputValues[userInputNum+2], RegQ.toString(), "Q",     (GAIGSmonospacedText)userInputPane.get(4), true, easyPseudo(22) );
            errorCheck(userInputValues[userInputNum+3], Q_1.toString(),  "β",     (GAIGSmonospacedText)userInputPane.get(5), true, easyPseudo(22) );
            errorCheck(userInputValues[userInputNum+4], Count.toString(),"Count", (GAIGSmonospacedText)userInputPane.get(6),false, easyPseudo(22) );
*/
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

    private static void easySnap(String title, String info, String pseudo, question que, GAIGSdatastr... stuff){
        if (title != null) BoothExercise02.title.setText(title);
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
                    selected, GREY, BLACK);
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
