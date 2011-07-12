package exe.boothsMultiplication;

import java.io.IOException;

import exe.ShowFile;

/**
 * Behaves the same as <code>GAIGStext</code> except providing
 * monospaced output.
 * 
 * @see exe.GAIGStext
 * @author Adam Voss <vossad01@luther.edu>
 *
 */

public class GAIGSmonospacedText extends GAIGStext implements MutableGAIGSdatastr {
	private static final boolean DEBUG = false;
	private double charWidth;
	private int charHalign;

	/**
	 * Default constructor that assigns default values to all fields.
	 */
	public GAIGSmonospacedText() {
		super();
		charWidth = this.fontsize;
		charHalign = this.halign;
	}

	/**
	 * Constructor that only sets the location of the text.
	 */
	public GAIGSmonospacedText(double x, double y) {
		super(x, y);
		charWidth = this.fontsize;
		charHalign = this.halign;
	}

	/**
	 * Constructor that sets the text and its location.
	 */
	public GAIGSmonospacedText(double x, double y, String text) {
		super(x, y, text);
		charWidth = this.fontsize;
		charHalign = this.halign;
	}

	/**
	 * Constructor that sets the text, location, and alignment.
	 */
	public GAIGSmonospacedText(double x, double y, String text, int halign, int valign) {
		super(x, y, halign, valign, DEFAULT_FONT_SIZE, DEFAULT_COLOR, text);
		charWidth = this.fontsize;
		charHalign = this.halign;
	}
	
	/**
	 * Constructor that provides parity with
	 * the all-fields constructor of GAIGStext.
	 */
	public GAIGSmonospacedText(double x, double y, int halign, int valign,
			double fontsize, String color, String text) {
		super(x, y, halign, valign, fontsize, color, text);
		charWidth = this.fontsize;
		charHalign = this.halign;
	}
	
	/**
	 * Constructor that also allows setting of the character width.
	 * 
	 * @param charWidth Width of each character,
	 * shares the same scale as fontSize.
	 */
	public GAIGSmonospacedText(double x, double y, int halign, int valign,
			double fontsize, String color, String text, double charWidth) {
		super(x, y, halign, valign, fontsize, color, text);
		this.charWidth = charWidth;
		this.charHalign = this.getHalign();
	}


	public GAIGSmonospacedText(GAIGSmonospacedText source) {
		this.charWidth = source.charWidth;
		this.charHalign = source.charHalign;
		
		//Text Stuff
		this.x = source.x;
		this.y = source.y;
		this.halign = source.halign;
		this.valign = source.valign;
		this.fontsize = source.fontsize;
		this.color = source.color;
		this.text = source.text;
	}

	/**
	 * Returns the horizontal alignment of each character within its
	 * "monospace box".
	 * Uses same constants as the inherited Halign.
	 * 
	 * @return the charHalign
	 */
	public int getCharHalign() {
		return charHalign;
	}

	/**
	 * Set the horizontal alignment of each character
	 * Default is HCENTER. Uses same constants as parent's Halign.
	 * 
	 * @param charHalign the charHalign to set
	 */
	public void setCharHalign(int charHalign) {
		this.charHalign = charHalign;
	}

	/**
	 * Sets the width of all characters.
	 * Defaults to the fontsize
	 * 
	 * @param width the desired character_width
	 */
	public void setCharacterWidth(double width){
		this.charWidth = width;
	}

	/**
	 * Returns the width of all characters.
	 *  
	 * @return the character_width
	 */
	public double getCharacterWidth() {
		return charWidth;
	}

	public double getWidth(){
		double width = 0;
		
		//for each line
		for (String line : getText().split("\n")){
			//count characters after removing color escapes
			int length = line.replaceAll("\\\\#[0-9A-Fa-f]{6}", "").length();
			if (length > width){
				width = length;
			}
		}
		return width*charWidth;
	}
	

	public double getHeight(){
		return this.fontsize*getLineCount();
	}

	//This method is inefficient beyond the fact it computes every time
	public int getLineCount(){
		return this.text.split("\n").length;
	}
	
	/**
	 * This outputs ever character as an equally spaced GAIGS text element.
	 * This is the lazy way to accomplish this, but it is 
	 * less efficient (because processing has to be redone every draw).
	 * The alternative would be to calculate all this information ahead
	 * of time, so that multiple draws would be quicker.
	 * 
	 * @return ret The XML string.
	 */
	@Override
	public String toXML(){
		String ret = "";
		String colorBuffer = "";
		//Now that parent's fields are protected, some of these lines may be culled 
		double X; //Initialized later
		double Y = this.y;
		int Halign = this.halign;
		int Valign = this.valign;
		double fontsize = this.fontsize;
		String color = this.color;
		String[] lines = this.text.split("\n");
		
		char[][] texts = new char[lines.length][];
		for (int i = 0 ; i < lines.length; i++){
			texts[i] = lines[i].toCharArray();
		}

		//New lines assumed to be rare thus the extra cost of this compare
		if (lines.length > 1){
			switch (Valign){ //We want the flow through behavior
			case VBOTTOM:
				Y+=(((lines.length-1)/2.0) * fontsize);
				if (DEBUG) System.out.println("in VBOTTOM case");
			case VCENTER:
				Y+=(((lines.length-1)/2.0) * fontsize);
				if (DEBUG) System.out.println("in VCENTER case");
			}
		}
		for (int line = 0; line < texts.length ; line++){
			char[] text = texts[line];
			X=getX();
			
			//Annoyingly we also have to strip out the Color Escapes
			String cleanedText = lines[line].replaceAll("\\\\#[0-9A-Fa-f]{6}", "");
			switch (Halign){ //We want the flow through behavior
			case HRIGHT:
				X=X-(((cleanedText.length()-1)/2.0) * charWidth);
			case HCENTER:
				X=X-(((cleanedText.length()-1)/2.0) * charWidth);
			}
			
			for (int i = 0 ; i < text.length ; i++){
				char chr = text[i];
				if (colorBuffer.equals("")){
					if (chr == '\\'){
						//Avoid Null Pointer
						if (text.length > (i+1)){
							//We are starting a color escape
							if (text[i+1] == '#'){
								colorBuffer += String.valueOf(chr);
								continue;
							}
						}
					}
					ret +=  "<text x=\"" + X + "\" y=\"" + Y + "\" halign=\"" + charHalign +
					"\" valign=\"" + Valign + "\" fontsize=\"" + fontsize + 
					"\" color=\"" + color + "\">" + String.valueOf(chr) + "</text>\n";
					X += charWidth;
				}
				else{
					//We don't have the full color string
					if (colorBuffer.length() <= 6){
						colorBuffer = colorBuffer + String.valueOf(chr);
					}
					//We have the full color string
					else{
						colorBuffer = colorBuffer + String.valueOf(chr);
						//User-Error checking
						if (!colorBuffer.matches("\\\\#[0-9A-Fa-f]{6}")){
							System.err.println("Invalid Color Escape in " +
									"monospaced Text; read: "+ colorBuffer);
							
							//Continue anyway, something upstream may thrown an exception
							//We aren't going to try to fix the user's mistake.
						}
						color = colorBuffer.substring(1);
						colorBuffer = "";
					}
				}
			}
			Y -= fontsize;
			color = getColor();
		}
		return ret;
	}

	@Override
	public double[] getBounds() {
		double width = getWidth();
		double height = getHeight();
		
		double x = getX();
		double y = getY();
		
		double[] ret = new double[4];
		
		switch (getHalign()){
		case HRIGHT:
			ret[0]=x-width;
			ret[2]=x;
			break;
		case HLEFT:
			ret[0]=x;
			ret[2]=x+width;
			break;
		case HCENTER:
			ret[0]=x-width/2;
			ret[2]=x+width/2;
			break;
		}
		
		switch (getValign()){
		case VTOP:
			ret[1]=y-height;
			ret[3]=y;
			break;
		case VBOTTOM:
			ret[1]=y;
			ret[3]=y+height;
			break;
		case VCENTER:
			ret[1]=y-height/2;
			ret[3]=y+height/2;
			break;
		}	
		return ret;
	}

	@Override
	public void setBounds(double x0, double y0, double x1, double y1) {
		if (DEBUG) System.out.println("Setting Monspaced Text Bounds:\n"+ "X0: " + x0 + " Y0: " + y0 + " X1: " + x1 + " Y1: " + y1);
		//Move
		switch (getHalign()){
		case HRIGHT:
			this.x = x1;
			break;
		case HLEFT:
			this.x = x0;
			break;
		case HCENTER:
			this.x = (x1+x0)/2;
			break;
		}
		
		switch (getValign()){
		case VTOP:
			this.y = y1;
			break;
		case HLEFT:
			this.y = y0;
			break;
		case HCENTER:
			this.y = (y1+y0)/2;
			break;
		}
		
		//Then Scale
		this.fontsize = (y1-y0)/getHeight() * this.fontsize;
		this.charWidth = (x1-x0)/getWidth() * this.charWidth;
		
		if (DEBUG) {
			double[] bds=this.getBounds();
			System.out.println("Resulting Monspaced Text Bounds:\n"+ "X0: " + bds[0] + " Y0: " + bds[1] + " X1: " + bds[2] + " Y1: " + bds[3]);
			
			//System.out.println(this.toXML());
		}
	}

	@Override
	public double getFontSize() {
		return this.fontsize;
	}
	
	@Override
	@Deprecated
	public double getFontsize() {
		return this.getFontSize();
	}


	@Override
	public void setFontSize(double fontSize) {
		this.fontsize = fontSize;
	}
	
	@Override
	@Deprecated
	public void setFontsize(double fontSize) {
		this.setFontSize(fontSize);
	}
	
	public GAIGSmonospacedText clone(){
		return new GAIGSmonospacedText(this);
	}
	
	public static void main(String[] args) throws IOException {
		ShowFile show = new ShowFile("mono.sho");
		
		/* Width Testing */
		GAIGSmonospacedText test = new GAIGSmonospacedText(.5, .5, HCENTER, HCENTER, DEFAULT_FONT_SIZE, DEFAULT_COLOR, "ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghilkmnopqrstuvwxyz");
		
		for (double i = 1.0; i >0 ;i-=.01){
			test.setCharacterWidth(test.getFontSize()*i);
			show.writeSnap(String.valueOf(test.getCharacterWidth()/test.getFontSize()), test);
		}
		
		//GAIGSmonospacedText text1 = new GAIGSmonospacedText(.5, .5);
		//text1.setText("Default");
		//show.writeSnap("Easy", text1);
		
		
		/* Alignment Testing */
		/*
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VCENTER, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HC VC", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HC VB", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VTOP, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HC VT", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HLEFT, GAIGSmonospacedText.VCENTER, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HL VC", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HLEFT, GAIGSmonospacedText.VBOTTOM, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HL VB", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HLEFT, GAIGSmonospacedText.VTOP, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HL VT", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VCENTER, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HR VC", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VBOTTOM, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HR VB", text1);
		
		text1 = new GAIGSmonospacedText(.5, .5, GAIGSmonospacedText.HRIGHT, GAIGSmonospacedText.VTOP, GAIGSmonospacedText.DEFAULT_FONT_SIZE, GAIGSmonospacedText.DEFAULT_COLOR, "Defa\nult");
		show.writeSnap("HR VT", text1);
		*/
		
		show.close();
	}
}