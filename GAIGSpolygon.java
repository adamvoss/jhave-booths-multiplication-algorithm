package exe.boothsMultiplication;

/**
 * <p>
 * <code>GAIGSprimitiveCollection</code> provides the ability to draw 2D
 * graphics for use in visualizations. This class supports a variety of 2D
 * graphics primitives, including lines, polygons, circles, ellipises, etc.
 * Creation of the primitives adheres to the other GAIGS classes and colors are
 * specified with the standard color string.
 * </p>
 * 
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>, Separated out protected classes
 * @version 6/22/2010
 */
public class GAIGSpolygon extends AbstractPrimitive {
	public int nSides;
	public double[] ptsX;
	public double[] ptsY;

	/**
	 * Creates a polygon.
	 * @param nSides The number of sides to the polygon
	 * @param ptsX Array containing the x coordinate values for the polygon
	 * @param ptsY Array containing the y coordinate values for the polygon
	 * @param fillColor The internal color of the polygon (use an empty string for no fill color)
	 * @param outlineColor The color of the circle polygon
	 * @param labelColor The color of the text in the circle label
	 * @param labelText The text to be drawn in the center of the circle
	 * @param textHeight The Height of the text in the label
	 * @param lineWidth The thickness of the outline of the polygon
	 */
	public GAIGSpolygon(int nSides, double ptsX[], double ptsY[],
			String fillColor, String outlineColor, String labelColor, String labelText,
			double textHeight, int lineWidth) {
		this.nSides = nSides;
		this.ptsX = new double[nSides];
		this.ptsY = new double[nSides];
		for (int i = 0; i < nSides; ++i) {
			this.ptsX[i] = ptsX[i];
			this.ptsY[i] = ptsY[i];
		}
		this.fcolor = fillColor;
		this.ocolor = outlineColor;
		this.lcolor = labelColor;
		this.label = labelText;
		this.fontSize = textHeight;
		this.lineWidth = lineWidth;
	}
	/**
	 * Creates a polygon
	 * @param nSides The number of sides to the polygon
	 * @param ptsX Array containing the x coordinate values for the polygon
	 * @param otsY Array containing the y coordinate values for the polygon
	 * @param fillColor The internal color of the polygon (use an empty string for no fill color)
	 * @param outlineColor The color of the circle polygon
	 * @param labelColor The color of the text in the circle label
	 * @param labelText The text to be drawn in the center of the circle
	 */
	public GAIGSpolygon(int nSides, double ptsX[], double ptsY[], String fillColor, String outlineColor,
			String labelColor, String labelText){
		this(nSides, ptsX, ptsY, fillColor, outlineColor, labelColor, labelText, TEXT_HEIGHT, LINE_WIDTH);
	}

	public GAIGSpolygon(GAIGSpolygon source) {
		this.nSides = source.nSides;
		this.ptsX = source.ptsX.clone();
		this.ptsY = source.ptsY.clone();
		this.fcolor = source.fcolor;
		this.ocolor = source.ocolor;
		this.lcolor = source.lcolor;
		this.label = source.label;
		this.fontSize = source.fontSize;
		this.lineWidth = source.lineWidth;
		this.name = source.name;
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

		for(int j = 0; j < nSides; ++j) {
			x1 = (x1 < ptsX[j] ? x1 : ptsX[j]);
			y1 = (y1 < ptsY[j] ? y1 : ptsY[j]);
			x2 = (x2 > ptsX[j] ? x2 : ptsX[j]);
			y2 = (y2 > ptsY[j] ? y2 : ptsY[j]);
		}

		return new double[] {x1, y1, x2, y2};
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		String xml = "<polygon nSides=\"" + nSides;
		for(int j=0; j<nSides ; ++j) {
			xml += "\" ptsX"+ j + "=\"" + ptsX[j] + "\" ptsY"+ j + "=\"" + ptsY[j];
		}
		xml += "\" fcolor=\"" + fcolor + "\" " +
		"ocolor=\"" + ocolor + "\" text=\"" + label + "\" lcolor=\"" + lcolor + "\" height=\"" +
		fontSize + "\" width=\"" + lineWidth + "\"/>\n";
		return xml;
	}
	
	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		double[] current = this.getBounds();
		double scaleX = (x2-x1)/(current[2]-(current[0]));
		double scaleY = (y2-y1)/(current[3]-(current[1]));
		double translateX = x1-(current[0]*scaleX);
		double translateY = y1-(current[1]*scaleY);
		
		for(int j = 0; j < nSides; ++j){
			ptsX[j] = ptsX[j] * scaleX + translateX;
			ptsY[j] = ptsY[j] * scaleY + translateY;
		}
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.Primitive#clone()
	 */
	@Override
	public GAIGSpolygon clone() {
		return new GAIGSpolygon(this);
	}
}