package exe.boothsMultiplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import exe.GAIGSdatastr;
import exe.GAIGStext;
import exe.ShowFile;
import exe.boothsMultiplication.GAIGSmonospacedText;
/**
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public class GAIGSArithmetic implements GAIGSdatastr {
	private String name = "";
	private ArrayList<ArrayList<Character[]>> terms = new ArrayList<ArrayList<Character[]>>();
	private int firstTermIndex;
	private int lastTermIndex;
	private char op;
	private int maxLength = 0;
	private double x;
	private double y;
	
	
	//Consider Replacing that with a GAIGSdatasrt implementing ArrayList.
	//Its not static unless it says so, right?
	private GAIGScollection<GAIGSdatastr> draw = new GAIGScollection<GAIGSdatastr>();
	
	/**
	 * Construct a new Arithmetic object. It is capable of performing displaying
	 * math in any radix supported by the Character class.
	 *  
	 * @param op The operation to be performed; Valid are ("+", "-", "*", "/").
	 * @param term1 The first term.
	 * @param term2 The second term.
	 * @param radix The radix or base of the numbers.
	 * @param x The x coordinate of the upper right-hand corner of the drawing.
	 * @param y The y coordinate of upper right-hand corner of the drawing.
	 */
	public GAIGSArithmetic(char op, String term1, String term2, int radix, double x0, double y0){
		firstTermIndex = 0;
		lastTermIndex  = 1;
		
		x=x0;
		y=y0;
		
		maxLength = 0;
		int t1len = term1.length();
		int t2len = term2.length();
		if (t1len > t2len){
			maxLength = t1len;
			maxLength += (lastTermIndex-firstTermIndex);
			for (int i = maxLength - t2len; i > 0 ; i--){
				term2 = " " + term2;
			}
		} else{
			maxLength = t2len;
			maxLength += (lastTermIndex-firstTermIndex);
			for (int i = maxLength - t1len; i > 0 ; i--){
				term1 = " " + term1;
			}
		}
		
		switch (op){
		case '*':
			this.op='×'; break;
		case '/':
			this.op='÷'; break;
		default:
			this.op=op;
		}
		terms.add(term1.toCharArray());
		terms.add(term2.toCharArray());
		terms.add(emptyRow());

		
		
		
		double font_size = GAIGStext.DEFAULT_FONT_SIZE;
		double char_width = font_size*.9;
		
		x0=.65-.2;
		y0=.55;

		//draw.add(new GAIGSmonospacedText(x0,y0, GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term1, char_width));
		//draw.add(new GAIGSmonospacedText(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,op+" "+ term2, char_width));
		//double width = ((GAIGSmonospacedText)draw.get(draw.size()-1)).getWidth();
		//double height = ((GAIGSmonospacedText)draw.get(draw.size()-1)).getHeight();
		//draw.add(new GAIGSline(new double[] {x0+width,x0}, new double[] {y0-GAIGStext.DEFAULT_FONT_SIZE*1.6,y0-GAIGStext.DEFAULT_FONT_SIZE*1.6}, DEFAULT_COLOR, DEFAULT_COLOR, ""));
		//draw.add(new GAIGSmonospacedText(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE*2), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR, "100111", char_width));
		
		x0+=.4;
		
		//draw.add(new GAIGStext(x0,y0, GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term1));
		//draw.add(new GAIGStext(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,op+" "+ term2));
		//draw.add(new GAIGSline(new double[] {x0-.17,x0}, new double[] {y0-GAIGStext.DEFAULT_FONT_SIZE*1.6,y0-GAIGStext.DEFAULT_FONT_SIZE*1.6}, DEFAULT_COLOR, DEFAULT_COLOR, ""));
		//draw.add(new GAIGStext(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE*2), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR, "100111"));
	}
	
	private char[] emptyRow(){
		char[] ret = new char[maxLength];
		for (int i = 0; i < maxLength; i++){
			ret[i] = ' ';
		}
		return ret;
	}
	
	public void step(){
		switch (op){
		case '+':
			additionStep();
		}
	}
	
	private void additionStep() {
		// TODO Auto-generated method stub
		
	}


	@Override
	// TODO Align print based on first term.
	public String toXML() {
		String ret = "<!-- Start of GAIGSArithmetic -->\n";

		String print="";
		for (int i =0 ; i < terms.size(); i++){
//			print += new String((char[]) terms.get(0).toArray(new Character[])) + "\n";
//			System.out.println(terms.get(i).length);
		}
		
		ret+= (new GAIGSmonospacedText(x, y, print)).toXML();
		
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
		
		GAIGSArithmetic test = new GAIGSArithmetic('×', "01101", "11010", 10, .5, .5);
		
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Multiplication", test);
		show.close();
	}

}
