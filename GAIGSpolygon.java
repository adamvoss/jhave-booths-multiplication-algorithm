package exe.boothsMultiplication;

/**
 * GAIGSpolygon provides a way to draw 2D polygons in GAIGS XML.
 * 
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-15
 */
public class GAIGSpolygon extends AbstractPrimitive {
    protected int nSides;
    protected double[] ptsX;
    protected double[] ptsY;

    /**
     * Creates a polygon.
     * @param nSides The number of sides to the polygon
     * @param ptsX Array containing the x coordinate values for the polygon
     * @param ptsY Array containing the y coordinate values for the polygon
     * @param fillColor The internal color of the polygon (use an empty string for no fill color)
     * @param outlineColor The outline color of the polygon
     * @param labelColor The color of the text in the polygon
     * @param labelText The text to be drawn in the center of the polygon
     * @param textHeight The height of the text in the label
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
     * Creates a polygon using the default font size and outline thickness
     * @param nSides The number of sides to the polygon
     * @param ptsX Array containing the x coordinate values for the polygon
     * @param otsY Array containing the y coordinate values for the polygon
     * @param fillColor The internal color of the polygon (use an empty string for no fill color)
     * @param outlineColor The outline color of the polygon
     * @param labelColor The color of the text in the polygon
     * @param labelText The text to be drawn in the center of the polygon
     */
    public GAIGSpolygon(int nSides, double ptsX[], double ptsY[], String fillColor, String outlineColor,
            String labelColor, String labelText){
        this(nSides, ptsX, ptsY, fillColor, outlineColor, labelColor, labelText, TEXT_HEIGHT, LINE_WIDTH);
    }

    /**
     * A deep-copy constructor.
     * @param The GAIGSpolygon to be copied
     */
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
        this.text_resize = source.text_resize;
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds(){
        double x1 = Double.POSITIVE_INFINITY;
        double y1 = Double.POSITIVE_INFINITY;
        double x2 = Double.NEGATIVE_INFINITY;
        double y2 = Double.NEGATIVE_INFINITY;

        for(int j = 0; j < nSides; ++j) {
            x1 = (x1 < ptsX[j] ? x1 : ptsX[j]);
            y1 = (y1 < ptsY[j] ? y1 : ptsY[j]);
            x2 = (x2 > ptsX[j] ? x2 : ptsX[j]);
            y2 = (y2 > ptsY[j] ? y2 : ptsY[j]);
        }

        return new double[] {x1, y1, x2, y2};
    }

    /* (non-Javadoc)
     * @see exe.AbstractPrimitive#toCollectionXML()
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
    public void setBounds(double x0, double y0, double x1, double y1) {
        scaleFont(x0, y0, x1, y1);
        
        double[] current = this.getBounds();

        if (x1-x0 == 0 | y1-y0==0){
            System.err.println("Polygon dimension collapse (Zero width or height)\n this is unsupported");
            //TODO add logic that allows such collapses.
        }

        double scaleX = (x1-x0)/(current[2]-(current[0]));
        double scaleY = (y1-y0)/(current[3]-(current[1]));

        double translateX = x0-(current[0]*scaleX);
        double translateY = y0-(current[1]*scaleY);

        for(int j = 0; j < nSides; ++j){
            ptsX[j] = ptsX[j] * scaleX + translateX;
            ptsY[j] = ptsY[j] * scaleY + translateY;
        }
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.AbstractPrimitive#clone()
     */
    @Override
    public GAIGSpolygon clone() {
        return new GAIGSpolygon(this);
    }
}