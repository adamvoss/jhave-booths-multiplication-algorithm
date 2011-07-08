package exe.boothsMultiplication;

import java.io.IOException;
import java.util.ArrayList;

import exe.boothsMultiplication.MutableGAIGSdatastr;
import exe.ShowFile;
import exe.boothsMultiplication.GAIGSmonospacedText;
/**
 * <code>GAIGSArithmetic</code> provides the ability to visualize arithmetic at various
 * stages of execution, in any based supported by the Character Class.
 * 
 * Currently only addition of two terms is fully implemented.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
//TODO add support for more operations
public class GAIGSArithmetic implements MutableGAIGSdatastr {
	protected static final boolean DEBUG = false;
	protected static final double LINE_TWEAK = 0.85;
	protected final static double FONT_SIZE =.05;
	protected static final String COLOR = "#000000";
					//Multiplier of Text height, because the class misrepresents its bounds/alignment
	
	
	protected String name = "";
	//	private ArrayList<ArrayList<Character>> terms = new ArrayList<ArrayList<Character>>();
	protected ArrayList<char[]> terms = new ArrayList<char[]>();
	protected ArrayList<String[]> colors = new ArrayList<String[]>();
	protected int firstTermIndex;
	protected int lastTermIndex;
	private String color;
	private double fontSize;
	private double digitWidth;
	protected char op;
	protected int currentDigit;
	protected int maxLength;
	private double xright;
	private double ytop;
	protected int radix;
	

	public GAIGSArithmetic(char op, String term1, String term2, int radix,
			double x0, double y0, double fontSize, double digitWidth,
			String color) {
		this.fontSize = fontSize;
		this.digitWidth = digitWidth;
		this.color = color;
		
		firstTermIndex = 0;
		this.radix = radix;

		xright=x0;
		ytop=y0;

		maxLength = 0;
		int t1len = term1.length();
		int t2len = term2.length();
		if (t1len > t2len){
			maxLength = t1len+1;
		} else{
			maxLength = t2len+1;
		}

		switch (op){
		case '*':
			this.op='x'; break;
		default:
			this.op=op;
		}
		
		terms.add(inplaceReverse(term1.toCharArray()));
		terms.add(inplaceReverse(term2.toCharArray()));
		lastTermIndex = terms.size()-1;
		terms.add(emptyRow());
		initializeColorArray();
		if (op == '+') addCarryRow();
	}
	
	public GAIGSArithmetic(char op, String term1, String term2, int radix,
			double x0, double y0, double fontSize, String color) {
		this(op, term1, term2, radix, x0, y0, fontSize, fontSize, color);
	}

	public GAIGSArithmetic(char op, String term1, String term2, int radix,
			double x0, double y0, double fontSize) {
		this(op, term1, term2, radix, x0, y0, fontSize, COLOR);
	}
	
	/**
	 * Construct a new Arithmetic object. It is capable of performing displaying
	 * math in any radix supported by the Character class.
	 *  
	 * @param op The operation to be performed; Valid are ("+", "-", "*", "/").
	 * @param term1 The first term.
	 * @param term2 The second term.
	 * @param radix The radix or base of the numbers.
	 * @param xright The x coordinate of the upper right-hand corner of the drawing.
	 * @param ytop The y coordinate of upper right-hand corner of the drawing.
	 */
	public GAIGSArithmetic(char op, String term1, String term2, int radix, double x0, double y0){
		this(op, term1, term2, radix, x0, y0, FONT_SIZE);
	}
	
	@SuppressWarnings("unchecked")
	public GAIGSArithmetic(GAIGSArithmetic source){
        this.name = source.name;
	    this.terms = (ArrayList<char[]>) source.terms.clone();
        this.colors = (ArrayList<String[]>) source.colors.clone();
        this.firstTermIndex = source.firstTermIndex;
        this.lastTermIndex = source.lastTermIndex;
        this.color = source.color;
        this.fontSize = source.fontSize;
        this.digitWidth = source.digitWidth;
        this.op = source.op;
        this.currentDigit = source.currentDigit;
        this.maxLength = source.maxLength;
        this.xright = source.xright;
        this.ytop = source.ytop;
        this.radix = source.radix;
	}
	
	/**
	 * step() function for addition
	 * @see step()
	 */
	private void additionStep() {
		int sum = 0;
		for (int i = 0 ; i <= lastTermIndex; i++){
			char[] nextLine = terms.get(i);

			if (nextLine.length > currentDigit){
				char nextChar = nextLine[currentDigit];
				int next = Character.digit(nextChar, radix);
				if (nextChar == ' ') next = 0;
				if (DEBUG) System.out.println("The digit is: " + next);		
				if (next == -1){
					System.err.println("Bad RADIX or TERM in GAIGSArithmetic");
				}
				sum += next; 	
			}
		}
		//Add carry
		if (sum >= radix){
			int carry = (sum / radix);
			if (terms.get(0)[currentDigit+1] != ' '){
				addCarryRow();
			}
			if (carry != 0) terms.get(0)[currentDigit+1] = Character.toUpperCase(Character.forDigit(carry, radix));
		}
		//Set result
		terms.get(lastTermIndex+1)[currentDigit]=Character.toUpperCase(Character.forDigit(sum % radix, radix));

		currentDigit++;
	}
	
	private void multiplicationStep(){
		int product;
		product = Integer.parseInt(new String(inplaceReverse(terms.get(firstTermIndex).clone())), radix) *
				Integer.parseInt(new String(inplaceReverse(terms.get(lastTermIndex).clone())), radix);
		terms.set(lastTermIndex+1, inplaceReverse(Integer.toString(product, radix).toCharArray()));
		colors.set(lastTermIndex+1, new String[terms.get(lastTermIndex+1).length]);
		
		currentDigit=maxLength;
	}
	
	/**
	 * Returns an empty row of maxLength filled with spaces characters.
	 * @return
	 */
	private char[] emptyRow(){
		char[] ret = new char[maxLength];
		for (int i = 0; i < maxLength; i++){
			ret[i] = ' ';
		}
		return ret;
	}
	
	private void initializeColorArray(){
		for (int i = 0; i < terms.size(); i++){
			colors.add(new String[terms.get(i).length]);
		}
	}
	
	/**
	 * Reverses the given array by modifying its entries.
	 * @param c A char array
	 * @return The array that was passed in
	 */
	private char[] inplaceReverse(char[] c){
		for(int i = 0, j = c.length - 1; i < j; i++, j--){
			char temp = c[j];
			c[j] = c[i];
			c[i] = temp;
		}
		return c;
	}

	/**
	 * Adds a new row for carry operations and adjusts Indexes accordingly.
	 */
	private void addCarryRow(){
		terms.add(0,emptyRow());
		colors.add(0, new String[maxLength]);
		firstTermIndex++;
		lastTermIndex++;
	}

	/**
	 * Returns true if the arithmetic has not yet completed.
	 * @return true if there are more steps, otherwise false.
	 */
	public boolean hasStep(){
		return (currentDigit < maxLength);
	}

	/**
	 * Execute one step of the arithmetic.
	 */
	public void step(){
		if (currentDigit < maxLength){
			switch (op){
			case '+':
				additionStep(); break;
			case 'x':
				multiplicationStep(); break;
			}
		}
	}

	/**
	 * Executes all remaining steps of the arithmetic.
	 */
	public void complete(){
		while (this.hasStep()){
			this.step();
		}
	}

	/**
	 * Provides GAIGS-compliant XML describing the state for the arithmetic.
	 */
	@Override
	// TODO Consider aligning print based on first term.
	public String toXML() {
		String ret = "<!-- Start of GAIGSArithmetic -->\n";

		String print = "";
		int i = 0;

		while (i < terms.size()) {
			char[] currentTerm = terms.get(i);
			String[] currentColors = colors.get(i);
			if (i == lastTermIndex) print += op+ " ";
			for (int n = currentTerm.length-1 ; n >= 0  ; n-- ){
				if (currentColors[n] != null){
					print += "\\"+currentColors[n]+currentTerm[n]+"\\"+color; 
				}
				else{
					print += currentTerm[n];
				}
			}
			print += "\n";
			i++;
		}

		ret += (new GAIGSmonospacedText(xright, ytop, 
				GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VTOP,
				this.fontSize, this.color, print, this.digitWidth)).toXML();
		
		ret += new GAIGSline(new double[] {
				xright - (maxLength + 1) * digitWidth,
				xright },
				new double[] {
				ytop - (lastTermIndex + 1) * fontSize * LINE_TWEAK,
				ytop - (lastTermIndex + 1) * fontSize * LINE_TWEAK})
		.toXML();

		return ret + "\n<!-- End of GAIGS Arithmetic -->\n";
	}

	/**
	 * The mandatory name for the GAIGSdatastr.  Note:  This will never appear on-screen.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the mandatory name for the GAIGSdatastr.  Note:  This will never appear on-screen.
	 * Defaults to an empty string.
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		GAIGSArithmetic test = new GAIGSArithmetic('+', Integer.toString(Integer.parseInt(args[1]), 2), Integer.toString(Integer.parseInt(args[2]), 2), 2, .5, .5);
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Addition", test);

		while (test.hasStep()){
			test.step();
			show.writeSnap("Addition", test);
		}

		GAIGSArithmetic test2 = new GAIGSArithmetic('*', args[1], args[2], 10, .5, .5);
		test2.complete();
		test2.setBounds(0, 0, 1, 1);
		
		show.writeSnap("Multiplication", test2);
		
		show.close();
	}

	@Override
	public double[] getBounds() {
		//Newlines should never occur within a term
		double[] ret = new double[4];
				
		ret[3]=ytop;
		ret[2]=xright;
		ret[1]=ytop-(terms.size()*fontSize);
		ret[0]=xright-Math.max(terms.get(lastTermIndex).length+2, maxLength)*digitWidth;
		
		return ret;
	}

	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		if (DEBUG) System.out.println("Setting Arithmetic Bounds:\n"+ "X0: " + x1 + " Y0: " + y1 + " X1: " + x2 + " Y1: " + y2);
		ytop = y2;
		xright = x2;
		
		int max = Math.max(terms.get(lastTermIndex).length+2, maxLength);
		
		digitWidth=(x2-x1)/max;
		
		fontSize = (y2-y1)/terms.size();
		
		if (DEBUG) {
			double[] bds=this.getBounds();
			System.out.println("Resulting Arithmetic Bounds:\n"+ "X0: " + bds[0] + " Y0: " + bds[1] + " X1: " + bds[2] + " Y1: " + bds[3]);
		}
	}

	@Override
	public double getFontSize() {
		return this.fontSize;
	}

	@Override
	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	
	public GAIGSArithmetic clone(){
		return new GAIGSArithmetic(this);
	}
}
