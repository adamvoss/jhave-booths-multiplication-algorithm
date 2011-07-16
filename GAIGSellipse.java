package exe.boothsMultiplication;

/**
 * <p>GAIGSellipse provides a convenient way to represent eclipses in GAIGS XML.
 * GAIGSellipse does not support drawing a filled ellipse.</p>
 *
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 */
public class GAIGSellipse extends AbstractPrimitive {
    /**
     * The lower right hand x coordinate of the ellipse bounds.
     */
    protected double x;

    /**
     * The lower right hand y coordinate of the ellipse bounds.
     */
    protected double y;

    /**
     * The starting angle of the ellipse in radians.
     */
    protected double stAngle;

    /**
     * The ending angle of the ellipse in radians.
     */
    protected double endAngle;

    /**
     * The radius value along the x axis.
     */
    protected double xR;

    /**
     * The radius value along the y axis.
     */
    protected double yR;

    /**
     * Creates an ellipse.
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
     * Creates an using the default font size and outline thickness.
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

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    public double[] getBounds(){
        double x1 = Double.POSITIVE_INFINITY;
        double y1 = Double.POSITIVE_INFINITY;
        double x2 = Double.NEGATIVE_INFINITY;
        double y2 = Double.NEGATIVE_INFINITY;

        x1 = (x1 < x ? x1 : x);
        x1 = (x1 < x + xR ? x1 : x + xR);
        x2 = (x2 > x ? x2 : x);
        x2 = (x2 > x + xR ? x2 : x + xR);
        y1 = (y1 < y ? y1 : y);
        y1 = (y1 < y + yR ? y1 : y + yR);
        y2 = (y2 > y ? y2 : y);
        y2 = (y2 > y + yR ? y2 : y + yR);

        return new double[] {x1, y1, x2, y2};
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.Primitive#toCollectionXML()
     */
    @Override
    protected String toCollectionXML() {
        return "\t<ellipse x=\"" + x + "\" y=\"" + y + "\" " +
        "sa=\"" + stAngle + "\" ea=\"" + endAngle + "\" rx=\"" + xR + "\" ry=\"" + yR + "\" color=\"" + ocolor + "\" " +
        "text=\"" + label + "\" lcolor=\"" + lcolor + "\" height=\"" + fontSize + "\" width=\"" + lineWidth + "\"/>\n";
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
    public AbstractPrimitive clone() {
        // TODO Auto-generated method stub
        return null;
    }
}
