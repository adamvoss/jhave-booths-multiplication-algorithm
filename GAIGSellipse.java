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
public class GAIGSellipse extends Primitive {
	public double x;
	public double y;
	public double stAngle;
	public double endAngle;
	public double xR;
	public double yR;
	private GAIGSellipse e = this;


	/**
	 * Creates an ellipse.  Does not support a filled ellipse.
	 * @param x The lower right hand x coordinate of the ellipse bounds
	 * @param y The lower right hand y coordinate of the ellipse bounds
	 * @param stAngle The starting angle in radians of the ellipse
	 * @param endAngle The ending angle in radians of the ellipse
	 * @param xR The radius value along the x axis
	 * @param yR The radius value along the y axis
	 * @param color The color of the outline of the ellipse
	 * @param lcolor The color of the text in the label
	 * @param label The text for the label to appear in the center of the ellipse
	 * @param textHeight The Height of the text in the label
	 * @param lineWidth The width of the outline of the circle
	 */
	public GAIGSellipse(double x, double y, double stAngle, double endAngle, double xR,
			double yR, String color, String lcolor, String label, double textHeight, int lineWidth)
	{
		this.x = x;
		this.y = y;
		this.stAngle = stAngle;
		this.endAngle = endAngle;
		this.xR = xR;
		this.yR = yR;
		this.ocolor = color;
		this.lcolor = lcolor;
		this.label = label;
		this.fontSize = textHeight;
		this.lineWidth = lineWidth;
	}

	/**
	 * Creates an ellipse.  Does not support a filled ellipse.
	 * @param x The lower right hand x coordinate of the ellipse bounds
	 * @param y The lower right hand y coordinate of the ellipse bounds
	 * @param stAngle The starting angle in radians of the ellipse
	 * @param endAngle The ending angle in radians of the ellipse
	 * @param xR The radius value along the x axis
	 * @param yR The radius value along the y axis
	 * @param color The color of the outline of the ellipse
	 * @param lcolor The color of the text in the label
	 * @param label The text for the label to appear in the center of the ellipse
	 * @param textHeight The Height of the text in the label
	 */
	public GAIGSellipse(double x, double y, double stAngle, double endAngle, double xR,

			double yR, String color, String lcolor, String label){
		this(x,y,stAngle,endAngle,xR,yR,color,lcolor,label,TEXT_HEIGHT,LINE_WIDTH);
	}

	@Override
	public String toXML() {
		return "<primitivecollection>\n\t<name>" + name +
		"</name>\n\t"+ "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>" +"\n\t" + "\t<ellipse x=\"" + e.x + "\" y=\"" + e.y + "\" " +
		"sa=\"" + e.stAngle + "\" ea=\"" + e.endAngle + "\" rx=\"" + e.xR + "\" ry=\"" + e.yR + "\" color=\"" + e.ocolor + "\" " +
		"text=\"" + e.label + "\" lcolor=\"" + e.lcolor + "\" height=\"" + e.fontSize + "\" width=\"" + e.lineWidth + "\"/>\n" + "</primitivecollection>\n";
	}

	protected String computeBounds() {
		double x1 = Double.MAX_VALUE;
		double y1 = Double.MAX_VALUE;
		double x2 = Double.MIN_VALUE;
		double y2 = Double.MIN_VALUE;

		x1 = (x1 < e.x ? x1 : e.x);
		x1 = (x1 < e.x + e.xR ? x1 : e.x + e.xR);
		x2 = (x2 > e.x ? x2 : e.x);
		x2 = (x2 > e.x + e.xR ? x2 : e.x + e.xR);
		y1 = (y1 < e.y ? y1 : e.y);
		y1 = (y1 < e.y + e.yR ? y1 : e.y + e.yR);
		y2 = (y2 > e.y ? y2 : e.y);
		y2 = (y2 > e.y + e.yR ? y2 : e.y + e.yR);

		return "<bounds x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\"/>";
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		return "\t<ellipse x=\"" + e.x + "\" y=\"" + e.y + "\" " +
		"sa=\"" + e.stAngle + "\" ea=\"" + e.endAngle + "\" rx=\"" + e.xR + "\" ry=\"" + e.yR + "\" color=\"" + e.ocolor + "\" " +
		"text=\"" + e.label + "\" lcolor=\"" + e.lcolor + "\" height=\"" + e.fontSize + "\" width=\"" + e.lineWidth + "\"/>\n";
	}
}
