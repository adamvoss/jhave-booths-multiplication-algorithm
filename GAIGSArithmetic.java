package exe.boothsMultiplication;

import java.io.IOException;
import java.util.ArrayList;

import exe.GAIGSdatastr;
import exe.ShowFile;
import exe.boothsMultiplication.GAIGSmonospacedText;
/**
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public class GAIGSArithmetic implements GAIGSdatastr {
	private static final boolean DEBUG = false;
	private String name = "";
//	private ArrayList<ArrayList<Character>> terms = new ArrayList<ArrayList<Character>>();
	private ArrayList<char[]> terms = new ArrayList<char[]>();
	private int firstTermIndex;
	private int lastTermIndex;
	private char op;
	private int currentDigit = 0;
	private int maxLength = 0;
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
	//TODO add support for more 
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
		addCarryRow();
	}
	
	private char[] emptyRow(){
		char[] ret = new char[maxLength];
		for (int i = 0; i < maxLength; i++){
			ret[i] = ' ';
		}
		return ret;
	}

	private char[] inplaceReverse(char[] c){
		for(int i = 0, j = c.length - 1; i < j; i++, j--){
			char temp = c[j];
			c[j] = c[i];
			c[i] = temp;
		}
		return c;
	}

	//TODO make this more efficient (start with an empty array rather than clone)
	private char[] reverse(char[] c){
		return inplaceReverse(c.clone());
	}
	
	public void addCarryRow(){
		terms.add(0,emptyRow());
		firstTermIndex++;
		lastTermIndex++;
	}
	
	public boolean hasStep(){
		return (currentDigit < maxLength);
	}
	
	public void step(){
		if (currentDigit < maxLength){
			switch (op){
			case '+':
				additionStep();
			}
		}
	}
	
	public void complete(){
		while (this.hasStep()){
			this.step();
		}
	}
	
	private void additionStep() {
		int sum = 0;
		for (int i = 0 ; i <= lastTermIndex; i++){
			char[] nextLine = terms.get(i);
			
			if (nextLine.length > currentDigit){
				char nextChar = nextLine[currentDigit];
				int next = Character.digit(nextChar, radix);
				if (nextChar == ' ') next = 0;
				if (DEBUG) System.out.println("The digit is: " + next);		
				if (next == -1) System.err.println("BAD RADIX or TERM in GAIGSArithmetic");
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


	@Override
	// TODO Consider aligning print based on first term.
	public String toXML() {
		String ret = "<!-- Start of GAIGSArithmetic -->\n";

		String print="";
		int i = 0;
		
		//Print Carrys
		while (i < firstTermIndex){
			print += new String(reverse(terms.get(i++))) + "\n";
		}
		
		//All but last term
		while (i < lastTermIndex){
			print += new String(reverse(terms.get(i++))) + "\n";
		}
		
		//Print operator with last term
		print += this.op + " " + new String(reverse(terms.get(i++))) + "\n";
		
		//Print the result
		print += new String(reverse(terms.get(i++))) + "\n";
		
		//Create the Text Object
		ret+= (new GAIGSmonospacedText(xright, ytop, print, GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VTOP)).toXML();
		
		ret += new GAIGSline(new double[] {xright-(maxLength+1)*GAIGSmonospacedText.DEFAULT_FONT_SIZE, xright}, new double[] {ytop-(lastTermIndex+1)*GAIGSmonospacedText.DEFAULT_FONT_SIZE, ytop-(lastTermIndex+1)*GAIGSmonospacedText.DEFAULT_FONT_SIZE}).toXML();
		
		return  ret	+ "\n<!-- End of GAIGS Arithmetic -->\n";
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		GAIGSArithmetic test = new GAIGSArithmetic('+', "ZBCTY0Z", "1808051", 36, .5, .5);
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Addition", test);
		
		while (test.hasStep()){
			test.step();
			show.writeSnap("Addition", test);
		}
		
		show.close();
	}

}
