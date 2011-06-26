package exe.boothsMultiplication;

import exe.GAIGStext;
import exe.MutableGAIGSdatastr;

/**
 * Behaves the same as <code>GAIGStext</code> except providing
 * monospaced output.
 * 
 * @see exe.GAIGStext
 * @author Adam Voss <vossad01@luther.edu>
 *
 */

public class GAIGSmonospacedText extends GAIGStext implements MutableGAIGSdatastr {
	private double charWidth;
	private int charHalign;

	/**
	 * Default constructor that assigns default values to all fields.
	 */
	public GAIGSmonospacedText() {
		super();
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	/**
	 * Constructor that only sets the location of the text.
	 */
	public GAIGSmonospacedText(double x, double y) {
		super(x, y);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	/**
	 * Constructor that sets the text and its location.
	 */
	public GAIGSmonospacedText(double x, double y, String text) {
		super(x, y, text);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	/**
	 * Constructor that sets the text, location, and alignment.
	 */
	public GAIGSmonospacedText(double x, double y, String text, int halign, int valign) {
		super(x, y, halign, valign, DEFAULT_FONT_SIZE, DEFAULT_COLOR, text);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}
	
	/**
	 * Constructor that provides parity with
	 * the all-fields constructor of GAIGStext.
	 */
	public GAIGSmonospacedText(double x, double y, int halign, int valign,
			double fontsize, String color, String text) {
		super(x, y, halign, valign, fontsize, color, text);
		charWidth = getFontsize();
		charHalign = this.getHalign();
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
		this.setX(source.getX());
		this.setY(source.getY());
		this.setHalign(source.getHalign());
		this.setValign(source.getValign());
		this.setFontsize(source.getFontsize());
		this.setColor(source.getColor());
		this.setText(source.getText());
		
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
		return getFontsize()*getLineCount();
	}

	//This method is inefficient beyond the fact it computes every time
	public int getLineCount(){
		return getText().split("\n").length;
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
		//It seems like the parent should just have protected fields 
		double X; //Initialized later
		double Y = getY();
		int Halign = getHalign();
		int Valign = getValign();
		double fontsize = getFontsize();
		String color = getColor();
		String[] lines = getText().split("\n");
		
		char[][] texts = new char[lines.length][];
		for (int i = 0 ; i < lines.length; i++){
			texts[i] = lines[i].toCharArray();
		}

		//New lines assumed to be rare thus the extra cost of this compare
		if (lines.length > 1){
			switch (Halign){ //We want the flow through behavior
			case VBOTTOM:
				Y+=(((lines.length-1)/2.0) * fontsize);
			case VCENTER:
				Y+=(((lines.length-1)/2.0) * fontsize);
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
			ret[1]=y+height/2;
			ret[3]=y+height/2;
			break;
		}	
		return ret;
	}

	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		//Move
		switch (getHalign()){
		case HRIGHT:
			setX(x2);
			break;
		case HLEFT:
			setX(x1);
			break;
		case HCENTER:
			setX((x2-x1)/2);
			break;
		}
		
		switch (getValign()){
		case VTOP:
			setX(y2);
			break;
		case HLEFT:
			setX(y1);
			break;
		case HCENTER:
			setX((y2-y1)/2);
			break;
		}
		
		//Then Scale
		
		//This may not be accurate because the viewport's height >1
		//setFontSize( (y2-y1) /getLineCount());
		
		//Use this instead
		setFontSize( (y2-y1)/getHeight() *getFontSize());
		setCharacterWidth( (x2-x1)/getWidth() *getCharacterWidth());
	}

	@Override
	public double getFontSize() {
		return super.getFontsize();
	}


	@Override
	public void setFontSize(double fontSize) {
		super.setFontsize(fontSize);
	}
	
	public GAIGSmonospacedText clone(){
		return new GAIGSmonospacedText(this);
	}
}