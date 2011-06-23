package exe.boothsMultiplication;

import java.io.IOException;

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
	public GAIGSArithmetic(String op, String term1, String term2, int radix, double x0, double y0){
		
		double font_size = GAIGStext.DEFAULT_FONT_SIZE;
		double char_width = font_size*.9;
		
		x0=.65-.2;
		y0=.55;
		
		this.term1=term1.toCharArray();
		this.term2=term2.toCharArray();
		draw.add(new GAIGSmonospacedText(x0,y0, GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term1, char_width));
		draw.add(new GAIGSmonospacedText(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,op+" "+ term2, char_width));
		double width = ((GAIGSmonospacedText)draw.get(draw.size()-1)).getWidth();
		//double height = ((GAIGSmonospacedText)draw.get(draw.size()-1)).getHeight();
		draw.add(new GAIGSline(new double[] {x0+width,x0}, new double[] {y0-GAIGStext.DEFAULT_FONT_SIZE*1.6,y0-GAIGStext.DEFAULT_FONT_SIZE*1.6}, DEFAULT_COLOR, DEFAULT_COLOR, ""));
		draw.add(new GAIGSmonospacedText(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE*2), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR, "100111", char_width));
		
		x0+=.4;
		
		draw.add(new GAIGStext(x0,y0, GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,term1));
		draw.add(new GAIGStext(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR,op+" "+ term2));
		draw.add(new GAIGSline(new double[] {x0-.17,x0}, new double[] {y0-GAIGStext.DEFAULT_FONT_SIZE*1.6,y0-GAIGStext.DEFAULT_FONT_SIZE*1.6}, DEFAULT_COLOR, DEFAULT_COLOR, ""));
		draw.add(new GAIGStext(x0,(y0-GAIGStext.DEFAULT_FONT_SIZE*2), GAIGStext.HRIGHT, GAIGStext.VTOP, GAIGStext.DEFAULT_FONT_SIZE, GAIGStext.DEFAULT_COLOR, "100111"));
	}
	
	@Override
	// TODO Align print based on first term.
	public String toXML() {
		return "<!-- Start of GAIGSArithmetic -->\n" + this.draw.toXML()
				+ "\n<!-- End of GAIGS Arithmetic -->\n";
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
		
		GAIGSArithmetic test = new GAIGSArithmetic("×", "01101", "11010", 10, .5, .5);
		
		ShowFile show = new ShowFile("bar.sho");
		show.writeSnap("Multiplication", test);
		show.close();
	}

}
