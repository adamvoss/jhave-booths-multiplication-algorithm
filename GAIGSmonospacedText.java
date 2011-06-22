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
		double X = getX();
		double Y = getY();
		int Halign = getHalign();
		int Valign = getValign();
		double fontsize = getFontsize();
		String color = getColor();
		char[] text = getText().toCharArray();

		if (Halign == HCENTER){
			X=X-(((text.length/2) - 0.5) * charWidth);
		}
		else if (Halign == HRIGHT){
			X=X-((text.length-1)*charWidth);
		}
		
		for (char chr : text){
			if (colorBuffer.equals("")){ 
				//TODO Check for Color Escapes, new lines, new line needs supers' color
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
    	return ret;
	}
}
