package exe.boothsMultiplication;


/**
 * <p><code>GAIGSprimitiveCollection</code> provides the ability to draw 2D graphics for use in visualizations.
 * This class supports a variety of 2D graphics primitives, including lines, polygons, circles, ellipises, etc.
 * Creation of the primitives adheres to the other GAIGS classes and colors are specified with the standard color string.
 * </p>
 *
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>, Separated out protected classes
 * @version 6/22/2010
 */
public class GAIGScircle extends Primitive {
	public double x;
	public double y;
	public double r;

	/**
	 * Adds a circle to the primitive collection
	 * @param cx The center x coordinate of the circle
	 * @param cy The center y coordinate of the circle
	 * @param r The radius of the circle
	 * @param fillColor The internal color of the circle (use an empty string for no fill color)
	 * @param outlineColor The color of the circle outline
	 * @param labelColor The color of the text in the circle label
	 * @param labelText The text to be drawn in the center of the circle
	 * @param textHeight The Height of the text in the label
	 * @param lineWidth The thickness of the outline of the circle
	 */
	public GAIGScircle(double cx, double cy, double r, String fillColor,
			String outlineColor, String labelColor, String labelText, double textHeight, int lineWidth) {
		this.x = cx;
		this.y = cy;
		this.r = r;
		this.fcolor = fillColor;
		this.ocolor = outlineColor;
		this.lcolor = labelColor;
		this.label = labelText;
		this.fontSize = textHeight;
		this.lineWidth = lineWidth;
	}

	/**
	 * Adds a circle to the primitive collection
	 * @param cx The center x coordinate of the circle
	 * @param cy The center y coordinate of the circle
	 * @param r The radius of the circle
	 * @param fillColor The internal color of the circle (use an empty string for no fill color)
	 * @param outlineColor The color of the circle outline
	 * @param labelColor The color of the text in the circle label
	 * @param labelText The text to be drawn in the center of the circle
	 */
	public GAIGScircle(double cx, double cy, double r, String fillColor, String outlineColor,
			String labelColor, String labelText){
		this(cx, cy, r, fillColor, outlineColor, labelColor, labelText, TEXT_HEIGHT, LINE_WIDTH);
	}


	/**
	 * Returns the bounds of the <code>GAIGS</code> Structure.
	 * @return      Array of coordinates.
	 */
	public double[] getBounds(){
		double x1 = Double.MAX_VALUE;
		double y1 = Double.MAX_VALUE;
		double x2 = Double.MIN_VALUE;
		double y2 = Double.MIN_VALUE;

		x1 = (x1 < (x - r) ? x1 : x - r);
		y1 = (y1 < (y - r) ? y1 : y - r);
		x2 = (x2 > (x + r) ? x2 : x + r);
		y2 = (y2 > (y + r) ? y2 : y + r);

		return new double[] {x1, y1, x2, y2};
	}
	
	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		return "\t<circle x=\"" + x + "\" y=\"" + y + "\" " +
		"r=\"" + r + "\" fcolor=\"" + fcolor + "\" " +
		"ocolor=\"" + ocolor + "\" text=\"" + label + "\" lcolor=\"" + lcolor + "\" height=\"" +
		fontSize + "\" width=\""+ lineWidth +"\"/>\n";
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#clone()
	 */
	@Override
	public Primitive clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
