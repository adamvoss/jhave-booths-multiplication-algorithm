package exe.boothsMultiplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import exe.GAIGSdatastr;
import exe.GAIGStext;
import exe.ShowFile;
import exe.boothsMultiplication.GAIGSmonospacedText;

public class GAIGSArithmetic implements GAIGSdatastr {
	private String name;
	private char[] term1;
	private char[] term2;
	private int term1_index =0;
	
	//Consider Replacing that with a GAIGSdatasrt implementing ArrayList. 
	private ArrayList<GAIGSdatastr> draw;
	
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
	public GAIGSArithmetic(String op, String term1, String term2, int radix, double x0, double y0){
		draw = new ArrayList<GAIGSdatastr>();
		
		double font_size = 0.07;
		double char_width = font_size*1.0;
		
		this.term1=term1.toCharArray();
		this.term2=term2.toCharArray();
		draw.add(new GAIGSmonospacedText(.5,.5, GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term1));
		draw.add(new GAIGSmonospacedText(.5,(.5-.07), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term2));
	}
	
	@Override
	//TODO Align print based on first term. 
	public String toXML() {
		String ret = "<!-- Start of GAIGSArithmetic -->\n";
		
		Iterator<GAIGSdatastr> iter = draw.iterator();
		while (iter.hasNext()){
			ret += iter.next().toXML();
		}
		ret += "<!-- End of GAIGS Arithmetic -->\n";
		return ret;
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
		GAIGSArithmetic test = new GAIGSArithmetic("+", "502", "713", 10, .5, .5);
		
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Addition", test);
		show.close();
	}

}
