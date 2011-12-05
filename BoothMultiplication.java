package exe.boothsMultiplication;

import java.io.IOException;
import java.util.HashMap;
import java.net.URI;
import java.net.URISyntaxException;

import org.jdom.JDOMException;

import exe.GAIGSArithmetic;
import exe.GAIGSarrow;
import exe.GAIGSdatastr;
import exe.GAIGSline;
import exe.GAIGSmonospacedText;
import exe.GAIGSpane;
import exe.boothsMultiplication.GAIGSbigEdianRegister;
import exe.GAIGStext;
import exe.MutableGAIGSdatastr;
import exe.ShowFile;
import exe.question;
import exe.pseudocode.PseudoCodeDisplay;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class BoothMultiplication {
    public class RegisterRowHistoryTrace implements MutableGAIGSdatastr {
        public GAIGSpane<MutableGAIGSdatastr> trace = new GAIGSpane<MutableGAIGSdatastr>();
        
        private GAIGSpane<GAIGSmonospacedText> labels = new GAIGSpane<GAIGSmonospacedText>();
        
        public RegisterRowHistoryTrace() {
        }

        public RegisterRowHistoryTrace(double[] bounds) {
            trace = new GAIGSpane<MutableGAIGSdatastr>(
                    bounds[0], bounds[1],
                    bounds[2], bounds[3],
                    null, 1.0);
            trace.add(labels);
            setName("Trace");
        }
        
        public void add(GAIGSpane<?> data){
            trace.add(data);
        }
        
        public void addRow(RegisterRow registerRow){
            trace.add(registerRow);            
        }
        
        public double getWidth(){
            return trace.getWidth();
        }
        
        public double getHeight(){
            return trace.getHeight();
        }
        
        public int size(){
            return trace.size();
        }
        
        
        // TODO: When I can stop worrying about XML being identical,
        //    change the type of trace to eliminate this cast.
        public RegisterRow removeLast(){
            return (RegisterRow) trace.remove(trace.size() - 1);
        }
               
        public RegisterRow getRow(int rowNumber){
            return (RegisterRow) trace.get(rowNumber);
        }
        
        // There is a bug here because labels will be null in the clone.
        // Doesn't currently affect anything.
        public RegisterRowHistoryTrace clone(){
            RegisterRowHistoryTrace ret = new RegisterRowHistoryTrace(this.getBounds());
            ret.trace = this.trace.clone();
            
            return ret;
        }
        
        public void addLabel(double[] coords, String displayText) {
            labels.add(new GAIGSmonospacedText(
                    (coords[2]-coords[0])/2.0+coords[0], coords[3],
                    GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                    COLBL_FONT_SIZE, FONT_COLOR, displayText, COLBL_FONT_SIZE/2));
        }
        
        public void fadeLastRow(){
            fadeRow(this.size() - 2);
        }
        
        private void fadeRow(int row) {
            getRow(row).setTextColor(INACTIVE_TEXT);
            getRow(row).setOutlineColor(INACTIVE_OUTLINE);
            getRow(row).setFillColor(INACTIVE_FILL);
        }

        /* (non-Javadoc)
         * @see exe.GAIGSdatastr#toXML()
         */
        @Override
        public String toXML() {
            return trace.toXML();
        }

        /* (non-Javadoc)
         * @see exe.GAIGSdatastr#getName()
         */
        @Override
        public String getName() {
            return trace.getName();
        }

        /* (non-Javadoc)
         * @see exe.GAIGSdatastr#setName(java.lang.String)
         */
        @Override
        public void setName(String name) {
            trace.setName(name);
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#getBounds()
         */
        @Override
        public double[] getBounds() {
            return trace.getBounds();
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
         */
        @Override
        public void setBounds(double x0, double y0, double x1, double y1) {
            trace.setBounds(x0, y0, x1, y1);
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#getFontSize()
         */
        @Override
        public double getFontSize() {
            return trace.getFontSize();
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#setFontSize(double)
         */
        @Override
        public void setFontSize(double fontSize) {
            trace.setFontSize(fontSize);
        }
    }

    public class RegisterRow implements MutableGAIGSdatastr{

        public GAIGSpane<MutableGAIGSdatastr> currentRow;

        public RegisterRow() {
            currentRow = new GAIGSpane<MutableGAIGSdatastr>();
        }
        
        public RegisterRow(String name){
            this();
            setName(name);
        }
        
        public void setName(String name){
            currentRow.setName(name);
        }
        
        public void add(MutableGAIGSdatastr register){
            currentRow.add(register);
        }
        
        public void addCount(MutableGAIGSdatastr register){
            currentRow.add(COUNT, register);
        }
        
        public void removeCount(){
            currentRow.remove(COUNT);
        }
        
        public GAIGSbigEdianRegister getRegister(int register){
            return (GAIGSbigEdianRegister) currentRow.get(register);
        }
        
        public double[] getFirstRegiterPosition(){
            return currentRow.get(0).getBounds();
        }
        
        public void addUnderline(double traceWidth) {
            double[] firstRegPosition = this.getFirstRegiterPosition();
            currentRow.add(
                    new GAIGSline(
                            new double[] { firstRegPosition[0], traceWidth },
                            new double[] { firstRegPosition[1] - ROW_SPACE / 2,
                            firstRegPosition[1] - ROW_SPACE / 2 }));
        }
        
        public RegisterRow clone(){
            RegisterRow ret = new RegisterRow();
            ret.currentRow = this.currentRow.clone();
            
            return ret;
            
        }

        /* (non-Javadoc)
         * @see exe.GAIGSdatastr#toXML()
         */
        @Override
        public String toXML() {
            return currentRow.toXML();
        }

        /* (non-Javadoc)
         * @see exe.GAIGSdatastr#getName()
         */
        @Override
        public String getName() {
            return currentRow.getName();
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#getBounds()
         */
        @Override
        public double[] getBounds() {
            return currentRow.getBounds();
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
         */
        @Override
        public void setBounds(double x0, double y0, double x1, double y1) {
            currentRow.setBounds(x0, y0, x1, y1);
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#getFontSize()
         */
        @Override
        public double getFontSize() {
            return currentRow.getFontSize();
        }

        /* (non-Javadoc)
         * @see exe.MutableGAIGSdatastr#setFontSize(double)
         */
        @Override
        public void setFontSize(double fontSize) {
            currentRow.setFontSize(fontSize);
        }

        /**
         * @param color
         */
        public void setFillColor(String color) {
            getRegister(REGM).setFillColor(color);
            getRegister(REGA).setFillColor(color);
            getRegister(REGQ).setFillColor(color);
            getRegister(Q1).setFillColor(color);
            getRegister(COUNT).setFillColor(color);
        }

        /**
         * @param color
         */
        public void setOutlineColor(String color) {
            getRegister(REGM).setOutlineColor(color);
            getRegister(REGA).setOutlineColor(color);
            getRegister(REGQ).setOutlineColor(color);
            getRegister(Q1).setOutlineColor(color);
            getRegister(COUNT).setOutlineColor(color);
        }

        /**
         * @param color
         */
        public void setTextColor(String color) {
            getRegister(REGM).setTextColor(color);
            getRegister(REGA).setTextColor(color);
            getRegister(REGQ).setTextColor(color);
            getRegister(Q1).setTextColor(color);
            getRegister(COUNT).setTextColor(color);
        }
    }

    public final static int REGM = 0;
    public final static int REGA = 1;
    public final static int REGQ = 2;
    public final static int Q1 = 3;
    public final static int COUNT = 4;

    // Definitions
    private static final boolean DEBUG = false;

    private static final String WHITE     = "#FFFFFF";
    private static final String BLACK     = "#000000";
    private static final String LIGHT_GREY= "#DDDDDD";
    private static final String GREY      = "#BBBBBB";
    private static final String DARK_GREY = "#666666";
    private static final String RED       = "#FF9999";
    private static final String GREEN     = "#55FF55";
    private static final String DARK_GREEN= "#008800";
    private static final String BLUE      = "#AAABFF";
    private static final String YELLOW    = "#FFFF00";
    
    //Configuration
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
    
    private PseudoCodeDisplay pseudo;
    private QuestionGenerator quest;
    private GAIGSbigEdianRegister RegM;
    private GAIGSbigEdianRegister RegA;
    private GAIGSbigEdianRegister RegQ;
    private GAIGSbigEdianRegister Q_1;
    private CountBox Count;
    // Is the next varriable's type the same as
    // GAIGSpane<GAIGSpane<? extends MutableGAIGSdatastr>>  ?
    private GAIGSpane<MutableGAIGSdatastr> main;
    private GAIGSpane<MutableGAIGSdatastr> header;
    private GAIGSpane<MutableGAIGSdatastr> math;
    private RegisterRowHistoryTrace trace = new RegisterRowHistoryTrace();
    private RegisterRow currentRow = new RegisterRow();
    private GAIGSmonospacedText title;
    private int rowNumber; // This is only used for comments in the XML
    private ShowFile show;
    private int REG_SIZE;
    
    private static final String FONT_COLOR      = BLACK;
    private final String DEFAULT_COLOR   = WHITE;
    private final String INACTIVE_TEXT   = DARK_GREY;
    private final String INACTIVE_OUTLINE= LIGHT_GREY;
    private final String INACTIVE_FILL   = WHITE;
    private final String OUTLINE_COLOR   = FONT_COLOR;
    private String filename;
    private String multiplicand;
    private String multiplier;

    public static void main(String args[]) throws IOException{
        BoothMultiplication booth = new BoothMultiplication(args[0], args[1], args[2]);
        booth.execute();
    }
    
    public BoothMultiplication(String filename, String multiplicand, String multiplier){
        this.filename = filename;
        this.multiplicand = multiplicand;
        this.multiplier = multiplier;
        
    }
    
    public void execute() throws IOException {
        initalizeJhaveComponents(filename);

        // Our Stuff
        REG_SIZE = multiplicand.length();

        main = createMainPane();
        header = createHeaderPane();

        title=createTitle();
        header.add(title);

        GAIGSArithmetic binary = createBinaryMultiplicationDisplay(multiplicand, multiplier);
        header.add(binary);
        
        ColoredResultArithmetic decimal = createDecimalMultiplicationDisplay(multiplicand, multiplier);
        header.add(decimal);

        MATH_LABEL_SPACE = header.getWidth() / 20;

        math = createMathPane();
        populateMathPane(multiplier);

        trace = createAlgorithmTrace();

        populateMainPane();

        currentRow = new RegisterRow("Row " + rowNumber++);

        trace.addRow(currentRow);
        //Trace finally defined, can now make the QuestionGenerator
        quest = new QuestionGenerator(show, trace.trace);


        //One could add Register Spacing/Sizing Logic Here
        //int numRows = numLines(multiplier);
        REG_WIDTH = REG_WIDTH_PER_BIT * REG_SIZE;
        
        
        COL_SPACE = ((trace.getWidth()-LEFT_MARGIN-RIGHT_MARGIN-COUNT_WIDTH) - (3* REG_WIDTH) - REG_WIDTH_PER_BIT)/4;
        //We only want to use the Count Margin when it would otherwise be too small
        if (COL_SPACE < COUNT_LEFT_MARGIN){
            COL_SPACE = ((trace.getWidth()-LEFT_MARGIN-RIGHT_MARGIN-COUNT_WIDTH) - (3* REG_WIDTH) - REG_WIDTH_PER_BIT - COUNT_LEFT_MARGIN)/3;
        }

        //Initialize Register Locations
        double[] init = locationOfFirstRegister();

        //**** Initialization Frames ****
        setupAndShowInitialization(multiplicand, multiplier, binary, decimal,
                trace, init);
        
        
        double[] last = currentRow.getFirstRegiterPosition();

        currentRow.addUnderline(trace.getWidth());

        // Maybe this should be a function of GAIGSpane
        double[] unitLengths = getUnitLength();
        double unitLengthX = unitLengths[2] - unitLengths[0];

        String labelText="Initialization";
        currentRow.add(new GAIGSmonospacedText(0
                - (GAIGSpane.narwhal_JHAVE_X_MARGIN - GAIGSpane.JHAVE_X_MARGIN)
                / unitLengthX, last[1], GAIGSmonospacedText.HRIGHT,
                GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR,
                labelText, FONT_SIZE * 0.5));

        boolean did_math = false;
        
        while (Count.getCount() >= 0) {
            // ----Count Frame----
            Count.setFillColor(YELLOW);
        
            easySnap("Check the value of Count", infoCheckCount(),
                    easyPseudo(8, YELLOW, BLACK), null);
            // Change color back
            Count.setFillColor(DEFAULT_COLOR);
        
            if (Count.getBit(0) == 0)
                break; // Thats so we get the final check
        
            // ----Start of Comparison and Addition/Subtraction Frame Logic----
        
            int cmpVal = RegQ.getBit(0) - Q_1.getBit(0);
        
            if (cmpVal == 1 || cmpVal == -1) {
                did_math = true;
                positionMajorRow();// clones all registers
                trace.addRow(createRow());// now we have enough information for question type 3,
                         // calculations pending
                DiscardOverflowAddition sum;
                GAIGSmonospacedText sumLabel;
                GAIGSmonospacedText discardLabel;
        
                // Subtraction case
                GAIGSbigEdianRegister multiplicandRegister = negateValue(RegM);
                if (cmpVal == 1) {
                    sum = createALUArithmetic(negateValue(RegM));
                    sumLabel = createArithmeticLabels(sum, "A", "-M");
                    discardLabel = createDiscardOverflowLabel(sum, sumLabel);
                    RegA.add(multiplicandRegister);
                }
                // Addition case
                else {
                    sum = createALUArithmetic(RegM);
                    sumLabel = createArithmeticLabels(sum, "A", "M");
                    discardLabel = createDiscardOverflowLabel(sum, sumLabel);
                    RegA.add(multiplicandRegister);
                }
        
                // ----Comparison Frame----
                trace.getRow(trace.size() - 2).getRegister(REGQ).setFillOutlineColor(0, BLUE);
                trace.getRow(trace.size() - 2).getRegister(Q1).setFillOutlineColor(0, BLUE);
        
                question que = quest.getComparisonQuestion();
                RegisterRow temp = trace.removeLast();
        
                easySnap("Determine the operation", infoDetermineOp(),
                        easyPseudo(10, BLUE, BLACK), que);
                trace.addRow(temp);
        
                // Reset/deactivate colors
                trace.fadeLastRow();
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);
        
                // ----Addition/Subtraction frame----
                RegA.setFillOutlineColor(GREEN);
                math.add(sum);
                math.add(sumLabel);
                math.add(discardLabel);
                sum.complete();
        
                last = currentRow.getFirstRegiterPosition();
                currentRow.add(new GAIGSmonospacedText(
                                0
                                        - (GAIGSpane.narwhal_JHAVE_X_MARGIN - GAIGSpane.JHAVE_X_MARGIN)
                                        / unitLengthX, last[1],
                                GAIGSmonospacedText.HRIGHT,
                                GAIGSmonospacedText.VBOTTOM, FONT_SIZE,
                                FONT_COLOR, (cmpVal == 1 ? "Subtraction"
                                        : "Addition"), FONT_SIZE * 0.5));
        
                easySnap(
                        (cmpVal == 1 ? "Subtract M from " : "Add M to ") + "A",
                        (cmpVal == 1 ? infoSubtraction() : infoAddition()),
                        easyPseudo((cmpVal == 1 ? 11 : 14), GREEN, BLACK),
                        quest.getAdditionQuestion());
                // Remove Overflow Label
                math.remove(math.size() - 1);
                // Remove Label
                math.remove(math.size() - 1);
                // Remove Addition
                math.remove(math.size() - 1);
                sumLabel.setText("");
        
            } else {
                // ----Comparison Frame---- (yep, again)
                // Colors
                RegQ.setFillOutlineColor(0, BLUE);
                Q_1.setFillOutlineColor(0, BLUE);
        
                // Question pane-hopping
                trace.add(null);
                question que = quest.getQuestion(1);
                trace.removeLast();
        
                easySnap("Determine the operation", infoDetermineNoMath(),
                        easyPseudo(10, BLUE, BLACK), que);
        
                // Reset colors
                RegQ.setFillOutlineColor(0, DEFAULT_COLOR);
                Q_1.setFillOutlineColor(0, DEFAULT_COLOR);
            }
            // Deactivate text
            setRowTextColor(INACTIVE_TEXT);
            setRowOutlineColor(INACTIVE_OUTLINE);
        
            // ----Shift Frame----
            if (did_math) {
                positionAdditionRow();
                did_math = false;
            } else
                positionMajorRow(); // Remember this clones
        
            setRowTextColor(FONT_COLOR);
            trace.addRow(createRow());
            rightShift(RegA, RegQ, Q_1);
        
            // Question and write
            question que = quest.getShiftQuestion();
        
            // Colors
            trace.getRow(trace.size() - 2).getRegister(REGA).setFillOutlineColor(
                    GREEN);
            trace.getRow(trace.size() - 2).getRegister(REGQ)
                    .setFillOutlineColor(BLUE);
            setRowOutlineColor(OUTLINE_COLOR);
            RegA.setFillOutlineColor(GREEN);
            RegQ.setFillOutlineColor(BLUE);
            RegQ.setFillOutlineColor(REG_SIZE - 1, GREEN);
            Q_1.setFillOutlineColor(0, BLUE);
            currentRow.removeCount(); // Oops...We don't want Count
        
            last = currentRow.getFirstRegiterPosition();
            currentRow.add(new GAIGSmonospacedText(
                            0
                                    - (GAIGSpane.narwhal_JHAVE_X_MARGIN - GAIGSpane.JHAVE_X_MARGIN)
                                    / unitLengthX, last[1],
                            GAIGSmonospacedText.HRIGHT,
                            GAIGSmonospacedText.VBOTTOM, FONT_SIZE, FONT_COLOR,
                            "Shift", FONT_SIZE * 0.5));
        
            easySnap("Sign-Preserving Right Shift", infoShift(),
                    easyPseudo(19, BLUE, BLACK), que);
            // RegQ.setTextColor(FONT_COLOR);
            Q_1.setFillOutlineColor(DEFAULT_COLOR);
        
            // Clean Color of A and Q on the previous line
            trace.getRow(trace.size() - 2).getRegister(REGA).setFillOutlineColor(
                    INACTIVE_FILL);
            trace.getRow(trace.size() - 2).getRegister(REGQ).setFillOutlineColor(
                    INACTIVE_FILL);
            RegA.setFillOutlineColor(DEFAULT_COLOR);
            RegQ.setFillOutlineColor(DEFAULT_COLOR);
        
            // ----Decrement Count Frame---
            Count.decrement();
            currentRow.addCount(Count); // Now we do want Count
            Count.setFillOutlineColor(RED);
            
            currentRow.addUnderline(trace.getWidth());
        
            easySnap("Decrement Count", infoDecrement(),
                    easyPseudo(21, RED, BLACK), null);
            Count.setFillOutlineColor(DEFAULT_COLOR);
            // Hey! We're ready to loop!
        }

        // ----Finished Frame----
        showFinishedFrame(multiplicand, multiplier, binary, decimal);

        show.close();
    }

    private void initalizeJhaveComponents(String outputFileName)
            throws IOException {
        // JHAVÉ Stuff
        show = new ShowFile(outputFileName);

        // Load the Pseudocode
        pseudo = loadPseudocode();
    }

    //TODO: Make this a varargs function of GAIGSpanes and make part of GAIGSpane class
    private double[] getUnitLength() {
        return main.getRealCoordinates(trace.trace
                .getRealCoordinates(currentRow.currentRow.getRealCoordinates(createStandardUnitLength())));
    }

    private static double[] createStandardUnitLength() {
        return new double[] {
                0, 0, 1, 1 };
    }

    private GAIGSmonospacedText createArithmeticLabels(
            DiscardOverflowAddition sum, String top, String bottom) {
        return new GAIGSmonospacedText(sum.getBounds()[2]
                + MATH_LABEL_SPACE / 2, sum.getBounds()[3]
                - sum.getFontSize(), GAIGStext.HCENTER,
                GAIGStext.VTOP, sum.getFontSize(), FONT_COLOR,
                String.format("(%s)\n(%s)", top, bottom), sum.getFontSize() * 1.5);
    }

    private GAIGSmonospacedText createDiscardOverflowLabel(
            DiscardOverflowAddition sum, GAIGSmonospacedText sumLabel) {
        return new GAIGSmonospacedText(sum.getBounds()[0],
                sum.getBounds()[3] + sum.getFontSize() / 2,
                GAIGSmonospacedText.HLEFT,
                GAIGSmonospacedText.VBOTTOM,
                sumLabel.getFontSize(), FONT_COLOR,
                "Discard any\nOverflow",
                sumLabel.getFontSize() * 1.25);
    }

    private DiscardOverflowAddition createALUArithmetic(
            GAIGSbigEdianRegister multiplicandRegister) {
        return new DiscardOverflowAddition('+', RegA.toString(),
                multiplicandRegister.toString(), 2,
                math.getWidth() / 1.4, math.getHeight() / 1.5,
                FONT_SIZE + .005, FONT_SIZE + .01, FONT_COLOR,
                DARK_GREEN);
    }
    
    private void setupAndShowInitialization(String multiplicand,
            String multiplier, GAIGSArithmetic binary,
            ColoredResultArithmetic decimal,
            RegisterRowHistoryTrace historyTrace, double[] init) {
        //----Register M Initialization Frame----
        setupAndShowRegisterMInitialization(multiplicand, binary, decimal, historyTrace, init);

        REG_SIZE = RegM.getSize();

        //----Register A Initialization Frame----
        setupAndShowRegisterAInitialization(historyTrace, init);

        //----Register Q Initialization Frame----
        setupAndShowRegisterQInitialization(multiplier, binary, decimal, historyTrace, init);

        //----Bit β Initialization Frame----
        setupAndShowBitBetaInitialization(historyTrace, init);

        // ----Count Initialization Frame----
        setupAndShowCountInitialization(historyTrace, init);
    }

    private void setupAndShowCountInitialization(
            RegisterRowHistoryTrace historyTrace, double[] positions) {
        positions[0] = trace.getWidth() - COUNT_WIDTH - RIGHT_MARGIN;
        positions[2] = trace.getWidth() - RIGHT_MARGIN;
        historyTrace.addLabel(positions, "Count");
        Count = createCountBox(positions);
        currentRow.add(Count);

        easySnap("Count is initialized to\nthe number of bits in a register.",
                infoCount(), easyPseudo(6), null);
    }

    private void setupAndShowBitBetaInitialization(
            RegisterRowHistoryTrace historyTrace, double[] position) {
        setStartOfNextRegister(position);
        setEndOfNextBit(position);
        historyTrace.addLabel(position, "β");
        Q_1 = createBitBeta(position);
        currentRow.add(Q_1);

        easySnap("β is initialized to Zero", infoBeta(), easyPseudo(5), null);
    }

    private void setupAndShowRegisterQInitialization(String registerValue,
            GAIGSArithmetic leftCornerObject,
            ColoredResultArithmetic rightCornerObject,
            RegisterRowHistoryTrace historyTrace, double[] position) {
        setStartOfNextRegister(position);
        setEndOfNextRegister(position);
        historyTrace.addLabel(position, "Q");
        RegQ= createRegister(position, registerValue);
        currentRow.add(RegQ);
        
        showRegisterQInit(leftCornerObject, rightCornerObject);
    }

    private void setupAndShowRegisterAInitialization(
            RegisterRowHistoryTrace historyTrace, double[] position) {
        setStartOfNextRegister(position);
        setEndOfNextRegister(position);
        historyTrace.addLabel(position, "A");
        RegA= createRegister(position, "0");
        currentRow.add(RegA);
        easySnap("A is initialized to Zero", infoRegisterA(), easyPseudo(3),
                null);
    }

    private void setupAndShowRegisterMInitialization(String displayValue,
            GAIGSArithmetic leftCornerObject,
            ColoredResultArithmetic RightCornerObject,
            RegisterRowHistoryTrace historyTrace, double[] position) {
        historyTrace.addLabel(position, "M");
        RegM= createRegister(position, displayValue);

        currentRow.add(RegM);
        
        showRegisterMInit(leftCornerObject, RightCornerObject);
    }

    private PseudoCodeDisplay loadPseudocode() throws IOException {
        try {
            return new PseudoCodeDisplay(
                    "exe/boothsMultiplication/pseudocode.xml");
        } catch (JDOMException e) {
            e.printStackTrace();
            throw new InternalError("IOException");
        }
    }

    private CountBox createCountBox(double[] position) {
        return new CountBox(REG_SIZE, DEFAULT_COLOR, FONT_COLOR,
                OUTLINE_COLOR, position, REG_FONT_SIZE);
    }

    private GAIGSbigEdianRegister createBitBeta(double[] position) {
        GAIGSbigEdianRegister beta = new GAIGSbigEdianRegister(1, "", DEFAULT_COLOR, FONT_COLOR,
                OUTLINE_COLOR, position, REG_FONT_SIZE);
        beta.set("0");
        return beta;
    }

    private GAIGSbigEdianRegister createRegister(double[] position, String initialValue) {
        GAIGSbigEdianRegister ret = new GAIGSbigEdianRegister(REG_SIZE, "", DEFAULT_COLOR, FONT_COLOR, OUTLINE_COLOR, position, REG_FONT_SIZE);
        ret.set(initialValue);
        return ret;
    }

    private GAIGSmonospacedText createTitle() {
        return new GAIGSmonospacedText(header.getWidth()/2, header.getHeight()-FONT_SIZE*1.5, GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VTOP, .25, FONT_COLOR, "", .1);
    }

    private void showFinishedFrame(String multiplicand,
            String multiplier, GAIGSArithmetic binary,
            ColoredResultArithmetic decimal) {
        decimal.complete();
        binary.complete();
        RegA.setFillOutlineColor(GREEN);
        // RegA.setTextColor(FONT_COLOR);
        RegQ.setFillOutlineColor(GREEN);
        // RegQ.setTextColor(FONT_COLOR);

        easySnap(
                "The result is "
                        + RegA
                        + RegQ
                        + "\nwhich is "
                        + ((Integer.parseInt(Utilities.toDecimal(multiplicand))) * (Integer.parseInt(Utilities
                                .toDecimal(multiplier)))) + " in decimal",
                infoFinished(), easyPseudo(-1), null);
    }

    private void showRegisterQInit(GAIGSArithmetic binary,
            ColoredResultArithmetic decimal) {
        GAIGSarrow leftArrow;
        GAIGSarrow rightArrow;
        //Let's draw arrows
        title.setText("Q is the Multiplier\nThe final product will span A and Q"); //Must do this here so we get the correct bounds
        
        leftArrow = createLeftArrow(title, decimal, 0, 0, 0, -1*decimal.getFontSize());
        rightArrow = createRightArrow(title, binary, 0, 0, 0, -1*binary.getFontSize());
        header.add(leftArrow);
        header.add(rightArrow);
        // We are done drawing arrows

        easySnap(null, infoRegisterQ(), easyPseudo(4), null);
        header.remove(rightArrow);
        header.remove(leftArrow);
    }

    private void showRegisterMInit(GAIGSArithmetic binary,
            ColoredResultArithmetic decimal) {
        //Let's draw arrows
        title.setText("M is the multiplicand"); //Must do this here so we get the correct bounds
        GAIGSarrow leftArrow = createLeftArrow(title, decimal);
        GAIGSarrow rightArrow = createRightArrow(title, binary);
        header.add(leftArrow);
        header.add(rightArrow);
        // We are done drawing arrows

        easySnap(null, infoRegisterM(), easyPseudo(2), null); // A null title
                                                              // indicates to
                                                              // keep
                                                              // the last one.
        header.remove(rightArrow);
        header.remove(leftArrow);
    }

    private void populateMathPane(String multiplier) {
        math.add(new GAIGSline(new double[] {0,0},
                new double[] {math.getHeight()+FONT_SIZE,
                    math.getHeight() - ( (REG_SIZE+1) * (REG_HEIGHT+ ROW_SPACE) - ROW_SPACE/2 + (numLines(multiplier)-REG_SIZE) * (ROW_SPACE/2 + REG_HEIGHT))}));
        math.add(new GAIGSmonospacedText(math.getWidth()/2, math.getHeight(), GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM, COLBL_FONT_SIZE, FONT_COLOR, "Math/ALU"));
    }

    private double[] locationOfFirstRegister() {
        return new double[] {
                LEFT_MARGIN,
                trace.getHeight()-TOP_MARGIN-REG_HEIGHT,
                LEFT_MARGIN+REG_WIDTH,
                trace.getHeight()-TOP_MARGIN};
    }

    private static void setEndOfNextBit(double[] init) {
        init[2] = init[0] + FONT_SIZE;
    }

    private static void setEndOfNextRegister(double[] init) {
        init[2] = init[0]+REG_WIDTH;
    }

    private static void setStartOfNextRegister(double[] init) {
        init[0] = init[2]+(COL_SPACE);
    }

    private ColoredResultArithmetic createDecimalMultiplicationDisplay(
            String multiplicand, String multiplier) {
        return new ColoredResultArithmetic('*', Utilities.toDecimal(multiplicand), Utilities.toDecimal(multiplier), 10, 10*FONT_SIZE, header.getHeight()-FONT_SIZE*1.5, 
                header.getHeight()/6, header.getHeight()/13, FONT_COLOR, DARK_GREEN);
    }

    private TCMultBooth createBinaryMultiplicationDisplay(
            String multiplicand, String multiplier) {
        return new TCMultBooth(multiplicand, multiplier, header.getWidth(), header.getHeight()-FONT_SIZE*1.5, 
                header.getHeight()/6, header.getHeight()/13, FONT_COLOR, DARK_GREEN);
    }

    private void populateMainPane() {
        main.add(header);
        main.add(trace);
        main.add(math);
    }

    private RegisterRowHistoryTrace createAlgorithmTrace() {
        return new RegisterRowHistoryTrace(
                new double[]{0, 0,
                             WINDOW_WIDTH * (3 / 4.0), math.getBounds()[3]});
    }

    private static GAIGSpane<MutableGAIGSdatastr> createMathPane() {
        GAIGSpane<MutableGAIGSdatastr> mathPane = new GAIGSpane<MutableGAIGSdatastr>(WINDOW_WIDTH * (3 / 4.0), 0,
                WINDOW_WIDTH, WINDOW_HEIGHT * (8 / 10.0), 1.0, 1.0);
        mathPane.setName("Math");
        return mathPane;
    }

    private static GAIGSpane<MutableGAIGSdatastr> createHeaderPane() {
        GAIGSpane<MutableGAIGSdatastr> headerPane = new GAIGSpane<MutableGAIGSdatastr>(0, WINDOW_HEIGHT*(3/4.0),
                WINDOW_WIDTH, WINDOW_HEIGHT, null, 1.0); //Top 1/4 of screen
        headerPane.setName("Header");
        return headerPane;
    }

    private static GAIGSpane<MutableGAIGSdatastr> createMainPane() {
        GAIGSpane<MutableGAIGSdatastr> mainPane = new GAIGSpane<MutableGAIGSdatastr>(0-GAIGSpane.JHAVE_X_MARGIN,
                0-GAIGSpane.JHAVE_Y_MARGIN,
                1+GAIGSpane.JHAVE_X_MARGIN,
                1+GAIGSpane.JHAVE_Y_MARGIN,
                WINDOW_WIDTH,
                WINDOW_HEIGHT);
        mainPane.setName("Main");
        return mainPane;
    }

    /**
     * @param rightItem
     * @param leftItem
     * @return
     */
    private GAIGSarrow createLeftArrow(MutableGAIGSdatastr rightItem,
            MutableGAIGSdatastr leftItem) {
        return createLeftArrow(rightItem, leftItem, 0, 0, 0, 0);
    }
    
    private GAIGSarrow createRightArrow(MutableGAIGSdatastr leftItem,
            MutableGAIGSdatastr rightItem) {
        return createRightArrow(leftItem, rightItem, 0, 0, 0, 0);
    }

    private GAIGSarrow createRightArrow(MutableGAIGSdatastr leftItem,
            MutableGAIGSdatastr rightItem,
            double sourceXOffset, double sourceYOffset,
            double destinationXOffset, double destinationYOffset) {
        double[] titlebounds = leftItem.getBounds();
        double[] binbounds = rightItem.getBounds();
        return new GAIGSarrow(new double[] {
                        titlebounds[2]+sourceXOffset,
                        (binbounds[0] + binbounds[2]) / 2 + sourceYOffset},
                    new double[] {
                        titlebounds[3] + destinationXOffset,
                        binbounds[3] + destinationYOffset },
                    FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
    }

    private GAIGSarrow createLeftArrow(MutableGAIGSdatastr rightItem,
            MutableGAIGSdatastr leftItem, double sourceXOffset, double sourceYOffset,
            double destinationXOffset, double destinationYOffset) {
        double[] titlebounds = rightItem.getBounds();
        double[] decbounds =leftItem.getBounds();
        return new GAIGSarrow(new double[]{
                        titlebounds[0] + sourceXOffset,
                        decbounds[2] + sourceYOffset},
                    new double[]{
                        titlebounds[3] + destinationXOffset,
                        decbounds[3] + destinationYOffset},
                    FONT_COLOR, FONT_COLOR, "", FONT_SIZE);
    }

    public void rightShift(GAIGSbigEdianRegister A, GAIGSbigEdianRegister Q,
            GAIGSbigEdianRegister Q_1) {
        int qDiscard = Q.arithmeticRightShift();
		Q_1.setBit(0, qDiscard);
        int aDiscard = A.arithmeticRightShift();  
        Q.setBit(Q.getSize() - 1, aDiscard);
		
    }

    /**
     * Negates the value of a register, in two's complement.
     * 
     * @param M
     *            The register to negate
     * @return A new register with the negated value.
     */
    public static GAIGSbigEdianRegister negateValue(GAIGSbigEdianRegister M) {
        GAIGSbigEdianRegister ret = new GAIGSbigEdianRegister(M);
        ret.twosComplementNegate();
        return ret;
    }

    /**
     * Calculates the number of lines the final display will occupy
     */
    public static int numLines(String binNum) {
        if (DEBUG) {
            System.out.println("BinNum is: ");
        }
        int sum = binNum.length();
        char prev = '0';

        for (int i = binNum.length() - 1; i >= 0; --i) {
            if (binNum.charAt(i) == '0')
                sum += (prev == '0' ? 0 : 1);
            else
                sum += (prev == '1' ? 0 : 1);

            prev = binNum.charAt(i);
        }

        return sum;
    }

    private static void adjustRegister(GAIGSbigEdianRegister reg) {
        double[] bds = reg.getBounds();
        bds[3] = bds[1] - (ROW_SPACE);
        bds[1] = bds[3] - REG_HEIGHT;
        reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
    }

    private static void minorAdjustRegister(GAIGSbigEdianRegister reg) {
        double[] bds = reg.getBounds();
        bds[3] = bds[1] - (ROW_SPACE / 2);
        bds[1] = bds[3] - REG_HEIGHT;
        reg.setBounds(bds[0], bds[1], bds[2], bds[3]);
    }

    private void setRowTextColor(String color) {
        RegM.setTextColor(color);
        RegA.setTextColor(color);
        RegQ.setTextColor(color);
        Q_1.setTextColor(color);
        Count.setTextColor(color);
    }

    private void setRowOutlineColor(String color) {
        RegM.setOutlineColor(color);
        RegA.setOutlineColor(color);
        RegQ.setOutlineColor(color);
        Q_1.setOutlineColor(color);
        Count.setOutlineColor(color);
    }

    private void positionMajorRow() {
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

    private void positionAdditionRow() {
        RegM = RegM.clone();
        RegA = RegA.clone();
        RegQ = RegQ.clone();
        Q_1 = Q_1.clone();
        Count = Count.clone();

        minorAdjustRegister(RegM);
        minorAdjustRegister(RegA);
        minorAdjustRegister(RegQ);
        minorAdjustRegister(Q_1);
        minorAdjustRegister(Count);
    }

    private RegisterRow createRow() {
        currentRow = new RegisterRow( "Row " + rowNumber);
        
        currentRow.add(RegM);
        currentRow.add(RegA);
        currentRow.add(RegQ);
        currentRow.add(Q_1);
        currentRow.add(Count);

        return currentRow;
    }

    private void easySnap(String title, String info, String pseudo,
            question que, GAIGSdatastr... stuff) {
        if (title != null)
            this.title.setText(title);

        URI infoURI = null;
        try {
            infoURI = new URI("str", info, "");
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        try {
            if (que == null)
                show.writeSnap(" ", infoURI.toASCIIString(), pseudo, stuff);
            else
                show.writeSnap(" ", infoURI.toASCIIString(), pseudo, que, stuff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void easySnap(String title, String info, String pseudo,
            question que) {
        easySnap(title, info, pseudo, que, main);
    }

    private void easySnap(String title, String pseudo, question que,
            GAIGSdatastr... stuff) {
        easySnap(title, "<html>hi</html>", pseudo, que, stuff);
    }

    private void easySnap(String title, String pseudo, question que) {
        easySnap(title, pseudo, que, main);
    }

    private String easyPseudo(int selected) {
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(), selected,
                    GREY, BLACK);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private String easyPseudo(int selected, String highlight,
            String textColor) {
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(), selected,
                    highlight, textColor);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private String easyPseudo(int selected, int lineColor) {
        try {
            return pseudo.pseudo_uri(new HashMap<String, String>(), selected,
                    lineColor);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    private static String infoRegisterM() {
        return "<html><h3>Welcome to the Booth's Multiplication Algorithm Visualization</h2>" + "<br/>" +
                "Booth's Multiplication Algorithm provides a relatively simple way to " +
                "multiply binary numbers stored in two's complement notation.  " +
                "As such, it is of academic interest in computer architecture.  " + "<br/>" +
                "Due to the computer architecture connection, machine registers are depicted " +
                "in this visualization for storage of data.  The exact details of these registers will " +
                "vary between machine architectures.  " + "<br/><br/>" +
                "To begin with we will store the multiplicand in register <i>M</i>.  " +
//                "This register will not be modified over the course of the algorithm." +
//                This is something that the question asks them, so we shouldn't give it away!
                "</html>";
    }

    private static String infoRegisterA() {
        return "<html>The second register, <i>A</i>, is initialized to zero." + "<br/><br/>" +
                        "<i>A</i> acts as a running sum and its value will be used in " +
                        "determining the resulting product." +
                        "</html>";
    }

    private static String infoRegisterQ() {
        return "<html>Register <i>Q</i> is initialized to the value of the multiplier.  " +
                        "However, <i>Q</i> is also be used to store the product.  As such, the multiplier will be " +
                        "destructively modified during the execution of the algorithm." + "<br/><br/>" +
                        "The use of an additional register for the product is necessary because when " +
                        "multiplying two n-bit numbers, the result can be as large as 2*n-bits." +
                        "</html>";
    }

    private static String infoBeta() {
        return "<html><i>β</i> is also initialized to zero.  " +
                        "Since the multiplier is not stored over the course of the algorithm, " +
                        "a single bit of information from it needs to be kept at each iteration of the algorithm. " + "<br/><br/>" +
                        "Specific hardware approaches for storing this will vary, so here we simply denote it as a Bit." +
                        "</html>";
        //Talk about what this bit tells us.
    }

    private static String infoCount() {
        return "<html><i>Count</i>  is used to keep track of the number of times to execute the " +
                        "loop of the algorithm.  It is initialized to the number of bits " +
                        "used to represent the multiplier: in this case the size of the registers.  " +
                        "<br/><br/>" + "Shown in decimal form for convenience, <i>Count</i> would" +
                        "also need a register or some other storage."/* +  " One possibility would be " +
//While thought provoking, this is extraneous to the algorithm as it is given.
                        "to store it and <i>β</i> in a single full-length register."*/ +
                        "</html>";
    }

    private static String infoCheckCount() {
        return "<html><i>Count</i>  was initialized to the number of times to execute the loop.  " +
                        "Thus, it needs to be checked against zero to determine whether to enter the loop body." +
                        "</html>";
    }
    
    public static String infoDetermineOpPreamble(){
        return "The least-significant bit of <i>Q</i> and <i>β</i> determine how " +
                "the algorithm behaves on each iteration of the loop." + "</br></br>";
    }

    //TODO This section needs more math explanation
    private static String infoDetermineOp() {
        return "<html>" + infoDetermineOpPreamble() +
                "Since the bits differ then an addition or subtraction needs to occur." +
                "</html>";
    }
    
    //TODO More Math
    private static String infoDetermineNoMath() {
        return "<html>" + infoDetermineOpPreamble() +
                "Since the bits are the same (both zero or both one) then the algorithm is in a block of zeros or ones.  " +
                "There is thus no addition or subtraction in this iteration.  Only a shift operation will occur." +
                "</html>";
    }

    //TODO More Math
    private static String infoAddition() {
        return "<html>An addition or subtraction of the multiplicand, <i>M</i>, executed on <i>A</i>.  " +
                        "Since the value in <i>A</i> gets shifted in this algorithm this every time an addition" +
                        "or subtraction executes on <i>A</i>, it is affecting more-significant bits of the result.</html>";
    }

    private static String infoSubtraction() {//seriously? :-) ^_^ Yep, thanks for the chuckle
        return infoAddition();
    }

    private static String infoShift() {
        return "<html>A sign-preserving right shift shift occurred across <i>A</i>, <i>Q</i>, and <i>β</i>.  " +
                        "That is, every bit is moved right one position, with the most-significant bit of <i>Q</i> " +
                        "replaced becoming the previous least-significant bit of <i>A</i>, and similarly with <i>β</i> " +
                        "The previous value of <i>β</i> is discarded.  The most significant bit of <i>A</i> stays at its " +
                        "previous value.  " + "</br></br>" +
                        "This is effectively multiplication by 2<sup>-1</sup> of the number " +
                        "that would be found in A though the second-least-significant bit of <i>M</i>.  " +
                        "The bit that is discarded was part of the multiplier and was never part of <i>A</i> " +
                        "(where all bits of the result originate)." +
                        "</html>";
    }

    private static String infoDecrement() {
        return "<html>An iteration of the algorithm's loop has completed.  Thus <i>Count</i>  " +
                        "is decremented so the proper number of iterations occur.</html>";
    }

    private static String infoFinished() {
        return "<html>Now that the execution of the algorithm has complete, " +
                        "the result is the number that spans <i>A</i>,<i>M</i>.</html>";
    }
}
