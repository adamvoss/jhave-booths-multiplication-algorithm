package exe.boothsMultiplication;

/**
 * <p><code>GAIGSprimitiveCollection</code> provides the ability to draw 2D graphics for use in visualizations.
 * This class supports a variety of 2D graphics primitives, including lines, polygons, circles, ellipises, etc.
 * Creation of the primitives adheres to the other GAIGS classes and colors are specified with the standard color string.
 * </p>
 *
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>, Seperated out protected classes
 * @version 6/22/2010
 */
public class GAIGSline extends Primitive {
	public double x[] = new double[2];
	public double y[] = new double[2];

	/**
	 * Creates a line.
	 * @param x Array of 2 containing the x coordinates for the start point and end point
	 * @param y Array of 2 containing the y coordinates for the start point and end point
	 * @param color The color of the line
	 * @param labelColor The color of the text in the label
	 * @param label The text to printed near the line
	 * @param textHeight The Height of the text in the label
	 * @param lineWidth The thickness of the line
	 */
	public GAIGSline(double x[], double y[], String color, String labelColor,
			String label, double textHeight, int lineWidth)
	{
		for(int i=0;i<2;++i)
		{
			this.x[i]=x[i];
			this.y[i]=y[i];
		}
		this.ocolor = color;
		this.lcolor = labelColor;
		this.label = label;
		this.fontSize = textHeight;
		this.lineWidth = lineWidth;
	}

	/**
	 * Adds a line to the primitive collection
	 * @param x Array of 2 containing the x coordinates for the start point and end point
	 * @param y Array of 2 containing the y coordinates for the start point and end point
	 * @param color The color of the line
	 * @param lcolor The color of the text in the label
	 * @param label The text to printed near the line
	 */
	public GAIGSline(double x[], double y[], String color, String lcolor, String label){
		this(x, y, color,lcolor, label, TEXT_HEIGHT, LINE_WIDTH);
	}
	
	/**
	 * Creates a line between two points
	 * @param x Array of 2 containing the x coordinates for the start point and end point
	 * @param y Array of 2 containing the y coordinates for the start point and end point
	 */
	public GAIGSline(double x[], double y[]){
		this(x, y, DEFAULT_COLOR, DEFAULT_COLOR, "", TEXT_HEIGHT, LINE_WIDTH);
	}

	protected String computeBounds() {
		double x1 = Double.MAX_VALUE;
		double y1 = Double.MAX_VALUE;
		double x2 = Double.MIN_VALUE;
		double y2 = Double.MIN_VALUE;

		for(int j = 0; j < 2; ++j) {
			x1 = (x1 < x[j] ? x1 : x[j]);
			y1 = (y1 < y[j] ? y1 : y[j]);
			x2 = (x2 > x[j] ? x1 : x[j]);
			y2 = (y2 > y[j] ? y1 : y[j]);
		}

		return "<bounds x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\"/>";
	}
	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		String xml = "<polygon nSides=\"" + 2;
		for(int j=0; j<2 ; ++j) {
			xml += "\" ptsX"+ j + "=\"" + x[j] + "\" ptsY"+ j + "=\"" + y[j];
		}
		xml += "\" fcolor=\"" + ocolor + "\" " +
		"ocolor=\"" + ocolor + "\" text=\"" + label + "\" lcolor=\"" + lcolor +
		"\" height=\"" + fontSize + "\" width=\"" + lineWidth + "\"/>\n";
		return xml;
	}

}
