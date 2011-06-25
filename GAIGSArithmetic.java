package exe.boothsMultiplication;

import java.io.IOException;
import java.util.ArrayList;

import exe.GAIGSdatastr;
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
public class GAIGSArithmetic implements GAIGSdatastr {
	private static final boolean DEBUG = false;
	private static final double LINE_TWEAK = 0.85;
					//Multiplier of Text height, because the class misrepresents its bounds/alignment
	private String name = "";
	//	private ArrayList<ArrayList<Character>> terms = new ArrayList<ArrayList<Character>>();
	private ArrayList<char[]> terms = new ArrayList<char[]>();
	private ArrayList<String[]> colors = new ArrayList<String[]>();
	private int firstTermIndex;
	private int lastTermIndex;
	private String color = "#000000";
	private double fontSize = .03;
	private double charWidth = fontSize;
	private char op;
	private int currentDigit;
	private int maxLength;
	private double xright;
	private double ytop;
	private int radix;
	

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
			this.op='×'; break;
		case '/':
			this.op='÷'; break;
		default:
			this.op=op;
		}
		terms.add(inplaceReverse(term1.toCharArray()));
		terms.add(inplaceReverse(term2.toCharArray()));
		lastTermIndex = terms.size()-1;
		terms.add(emptyRow());
		initializeColorArray();
		addCarryRow();
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
	public void addCarryRow(){
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
				additionStep();
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
				this.fontSize, this.color, print, this.charWidth)).toXML();
		
		ret += new GAIGSline(new double[] {
				xright - (maxLength + 1) * fontSize,
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

		GAIGSArithmetic test = new GAIGSArithmetic('+', "110101", "1011011", 2, .5, .5);
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Addition", test);

		while (test.hasStep()){
			test.step();
			show.writeSnap("Addition", test);
		}

		show.close();
	}
}
