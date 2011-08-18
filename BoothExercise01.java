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
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class BoothExercise01 {
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
    private static GAIGSpane<MutableGAIGSdatastr> userInput;
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
    private static       double USER_INPUT_LABEL_SPACE;

    private static final double FONT_SIZE         = 0.05;
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

    private static  PrintWriter writer;//debug stuff

    public static void main(String args[]) throws IOException {
        //JHAVE stuff
        show = new ShowFile(args[0]);

        if (DEBUG) {
            writer = new PrintWriter("/home/christopher/jhave2/server/src/exe/boothsMultiplication/ex01b.log");
            writer.println("Beginning Log");
        }

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
        int multiplicand = Integer.parseInt(args[args.length-2]);
        int multiplier   = Integer.parseInt(args[args.length-1]);

        if (DEBUG) writer.println("M\t" + multiplicand + "\nQ\t" + multiplier);

        String binMultiplicand = toBinary(multiplicand);
        String binMultiplier   = toBinary(multiplier);

        if (DEBUG) writer.println("M\t" + binMultiplicand + "\nQ\t" + binMultiplier);

        //logic to make M and Q the same size
        if (binMultiplicand.length() > binMultiplier.length() ) 
            binMultiplier = signExtend(binMultiplier, binMultiplicand.length() - binMultiplier.length() );
        else if (binMultiplier.length() > binMultiplicand.length() )
            binMultiplicand = signExtend(binMultiplicand, binMultiplier.length() - binMultiplicand.length() );

        if (isWellFormedBinary(args[1]) && isWellFormedBinary(args[3]) ) //if well formed
            if (args[1].length() == args[3].length() ) //and matching in length
                if (toDecimal(args[1]).equals("" + multiplicand) && toDecimal(args[3]).equals("" + multiplier)  ) //and correct
                    if (args[1].length() > binMultiplicand.length() && args[1].length() <= 8) {
                        //then, if it's a larger representation but not too large
                        binMultiplicand = signExtend(binMultiplicand, args[1].length() - binMultiplicand.length() );
                        binMultiplier   = signExtend(binMultiplier  , args[3].length() - binMultiplier.length()   );
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
        userInput = new GAIGSpane<MutableGAIGSdatastr>(WINDOW_WIDTH*(3/4.0), 0, WINDOW_WIDTH, WINDOW_HEIGHT*(8/10.0), 1.0, 1.0);
        userInput.setName("User Input");

        userInput.add(new GAIGSline(new double[] {0,0},
            new double[] {userInput.getHeight()+FONT_SIZE,
            userInput.getHeight() - ( (REG_SIZE+1) * (REG_HEIGHT+ ROW_SPACE) - ROW_SPACE/2 + 
            (numLines(binMultiplier)-REG_SIZE) * (ROW_SPACE/2 + REG_HEIGHT))}));

        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/2, userInput.getHeight(), GAIGSmonospacedText.HCENTER, 
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "User Input"));

        final double USER_INPUT_TEXT_HEIGHT = 5 * userInput.getHeight() / 6;

        //User input text for M, A, Q, Beta, Count
        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/3, USER_INPUT_TEXT_HEIGHT, GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "M:\t" + args[1]));

        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "A:\t" + args[2]));

        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*2), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "Q:\t" + args[3]));

        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*3), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "β:\t" + args[4]));

        userInput.add(new GAIGSmonospacedText(userInput.getWidth()/3, USER_INPUT_TEXT_HEIGHT-(COLBL_FONT_SIZE*4), GAIGSmonospacedText.HLEFT,
            GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "Count:\t" + args[5]));
        //END user input

        trace = new GAIGSpane<GAIGSpane<?>>(0, 0, WINDOW_WIDTH*(3/4.0), userInput.getBounds()[3], null, 1.0);
        trace.setName("Trace");

        main.add(header);
        main.add(trace);
        main.add(userInput);

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
        errorCheck(args[1], binMultiplicand, "M", (GAIGSmonospacedText) userInput.get(2), true);
        easySnap(null, easyPseudo(2), null);

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
        errorCheck(args[2], RegA.toString(), "A", (GAIGSmonospacedText) userInput.get(3), true);
        easySnap(null, easyPseudo(3), null);

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
        errorCheck(args[3], binMultiplier, "Q", (GAIGSmonospacedText) userInput.get(4), true);
        easySnap(null, easyPseudo(4), null);

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
        errorCheck(args[4], "0", "β", (GAIGSmonospacedText) userInput.get(5), true);
        easySnap(null, easyPseudo(5), null);

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
        errorCheck(args[5], "" + Count.getCount(), "Count", (GAIGSmonospacedText) userInput.get(6), false);
        easySnap(null, easyPseudo(6), null);

        //Maybe this should be a function of GAIGSpane
        double[] unitLengths = main.getRealCoordinates(trace.getRealCoordinates(currentRow.getRealCoordinates(new double[]{0,0,1,1})));
        double unitLengthX = unitLengths[2]-unitLengths[0];
        
        currentRow.add(new GAIGSmonospacedText(0-(GAIGSpane.narwhal_JHAVE_X_MARGIN-GAIGSpane.JHAVE_X_MARGIN)/unitLengthX,
                last[1], GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR, "Initialization", FONT_SIZE*0.5));

        if (DEBUG) writer.close();
        show.close();
    }

    private static void errorCheck(String checkMe, String checkAgainst, String name, GAIGSmonospacedText displayMe, boolean binaryNum) {
        String mesg = "";
        if (checkMe.equalsIgnoreCase("INVALID") ) {
            mesg = "An invalid value was given for " + name + 
                "\nYou may have left this field blank or gave an input with whitespace";
            displayMe.setColor(RUBY);
        }
        else if ( ((!isWellFormedBinary(checkMe)) && binaryNum) || ((!isWellFormedDecimal(checkMe)) && !binaryNum) ) {//multiplexor anyone?
            mesg = "'" + checkMe + "' is not a well-formed " + (binaryNum ? "binary " : "decimal " ) + "integer value";
            displayMe.setColor(RUBY);
        }
        else if (binaryNum && checkMe.length() > 8) {
            mesg = "Inputs for binary values may be at most 8 bits long for this exercise";
            displayMe.setColor(RUBY);
        }
        else if (!checkMe.equals(checkAgainst) ) {
            if (binaryNum) {
                if (toDecimal(checkMe).equals(toDecimal(checkAgainst)) ) {
                    mesg = "The correct binary value was given, but your answer\n" +  
                        "uses an incorrect/inconsistent number of bits to represent it";
                }
                else {
                    if (name.equals("A") )
                        mesg = name + " is always initialized to some binary value representing 0";
                    else if (name.equals("β") ) {
                        mesg = "Incorrectly initialized β";
                    }
                    else {
                        mesg = "Signed binary value " + checkMe + " is " + toDecimal(checkMe) + " in decimal;\n" + 
                            "however, " + toDecimal(checkAgainst) + " is what was asked for";
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
            mesg = "Correct!";
            displayMe.setColor(DARK_GREEN);
        }

        title.setText(mesg);
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
    private static String signExtend(String binStr, int i){
        String firstBit = String.valueOf(binStr.charAt(0));
        String extension = "";
        while (i>0){extension = extension.concat(firstBit); i--;}
        return extension.concat(binStr);
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

    private static boolean isWellFormedBinary(String input) {
        if (input.length() == 0) return false;

        boolean flag = true;

        for (int i = 0; i < input.length(); ++i)
            flag = flag && (input.charAt(i) == '0' || input.charAt(i) == '1');

        return flag;
    }

    private static boolean isWellFormedDecimal(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {return false;}
    }

    private static void easySnap(String title, String info, String pseudo, question que, GAIGSdatastr... stuff){
        if (title != null) BoothExercise01.title.setText(title);
        try {
            if (que == null) {
                show.writeSnap(" ", info, pseudo, stuff);
            }
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
