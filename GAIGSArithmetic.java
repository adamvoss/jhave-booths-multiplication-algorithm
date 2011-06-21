package exe.boothsMultiplication;

import java.util.ArrayList;

import exe.GAIGSdatastr;
import exe.GAIGStext;

public class GAIGSArithmetic implements GAIGSdatastr {
	private char[] term1;
	private char[] term2;
	private ArrayList<GAIGSdatastr> draw;
	
	/**
	 * Construct a new Arithmetic object. It is capable of performing displaying
	 * math in any radix supported by the Character class.
	 *  
	 * @param op The operation to be performed valid are ("+", "-", "*", "/").
	 * @param term1 The first term.
	 * @param term2 The second term.
	 * @param radix The radix or base of the numbers.
	 * @param x The x coordinate of the upper right-hand corner of the drawing.
	 * @param y The y coordinate of upper right-hand corner of the drawing.
	 */
	public GAIGSArithmetic(String op, String term1, String term2, int radix, double x0, double y0){
		double font_size = 0.07;
		double char_width = font_size*1.0;
		
		this.term1=term1.toCharArray();;
		this.term2=term2.toCharArray();
		System.out.println(Character.MAX_RADIX);
		//System.out.println(this.term2);
		draw.add(new GAIGStext());
	}
	
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GAIGSArithmetic("+", "502", "713", 2, .5, .5);
	}

}
