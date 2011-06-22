package exe.boothsMultiplication;

import exe.GAIGStext;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */

public class GAIGSmonospacedText extends GAIGStext {
	private double charWidth;
	private int charHalign;

	public GAIGSmonospacedText() {
		super();
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	public GAIGSmonospacedText(double x, double y) {
		super(x, y);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	public GAIGSmonospacedText(double x, double y, String text) {
		super(x, y, text);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	public GAIGSmonospacedText(double x, double y, int halign, int valign,
			double fontsize, String color, String text) {
		super(x, y, halign, valign, fontsize, color, text);
		charWidth = getFontsize();
		charHalign = this.getHalign();
	}

	/**
	 * Uses same constants as parent's Halign
	 * 
	 * @return the charHalign
	 */
	public int getCharHalign() {
		return charHalign;
	}

	/**
	 * @param charHalign the charHalign to set
	 */
	public void setCharHalign(int charHalign) {
		this.charHalign = charHalign;
	}

	/**
	 * @param width the desired character_width
	 */
	public void setCharacterWidth(double width){
		this.charWidth = width;
	}

	/**
	 * @return the character_width
	 */
	public double getCharacterWidth() {
		return charWidth;
	}

	/**
	 * This is the lazy method but it is less efficient
	 * (because processing has to be redone every draw).
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
		for (char[] text : texts){
			X=getX();
			switch (Halign){ //We want the flow through behavior
			case HRIGHT:
				X=X-(((text.length-1)/2.0) * charWidth);
			case HCENTER:
				X=X-(((text.length-1)/2.0) * charWidth);
			}
			
			for (char chr : text){
				if (colorBuffer.equals("")){
					//TODO Check for Color Escapes,
					ret +=  "<text x=\"" + X + "\" y=\"" + Y + "\" halign=\"" + charHalign +
					"\" valign=\"" + Valign + "\" fontsize=\"" + fontsize + 
					"\" color=\"" + color + "\">" + String.valueOf(chr) + "</text>\n";
					X += charWidth;
				}
				else{
					//We don't have the full color string
					if (colorBuffer.length() < 6){
						colorBuffer = colorBuffer + String.valueOf(chr);
					}
					//We have the full color string
					else{
						color = colorBuffer;
						colorBuffer = "";
					}
				}
			}
			Y -= fontsize;
			color = getColor();
		}
		return ret;
	}
}
