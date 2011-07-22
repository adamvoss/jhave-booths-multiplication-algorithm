package exe.boothsMultiplication;


/**
 * GAIGScircle provides a way to draw circles in GAIGS XML.
 *
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 */
//TODO finish implementation
public class GAIGScircle extends AbstractPrimitive {
    /**
     * x coordinate of the circle's center.
     */
    protected double x;

    /**
     * y coordinate of the circle's center. 
     */
    protected double y;

    /**
     * radius of the circle. 
     */
    protected double r;

    /**
     * Constructs a circle from the given parameters. 
     * @param cx The center x coordinate of the circle.
     * @param cy The center y coordinate of the circle.
     * @param r The radius of the circle.
     * @param fillColor The internal color of the circle (use an empty string for no fill color).
     * @param outlineColor The color of the circle outline.
     * @param labelColor The color of the text in the circle label.
     * @param labelText The text to be drawn in the center of the circle.
     * @param textHeight The Height of the text in the label.
     * @param lineWidth The thickness of the outline of the circle.
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
     * Constructs a circle using the default font size and outline thickness.
     * @param cx The center x coordinate of the circle.
     * @param cy The center y coordinate of the circle.
     * @param r The radius of the circle.
     * @param fillColor The internal color of the circle (use an empty string for no fill color).
     * @param outlineColor The color of the circle outline.
     * @param labelColor The color of the text in the circle label.
     * @param labelText The text to be drawn in the center of the circle.
     */
    public GAIGScircle(double cx, double cy, double r, String fillColor, String outlineColor,
            String labelColor, String labelText){
        this(cx, cy, r, fillColor, outlineColor, labelColor, labelText, TEXT_HEIGHT, LINE_WIDTH);
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    public double[] getBounds(){
        double x1 = Double.POSITIVE_INFINITY;
        double y1 = Double.POSITIVE_INFINITY;
        double x2 = Double.NEGATIVE_INFINITY;
        double y2 = Double.NEGATIVE_INFINITY;

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
    public void setBounds(double x0, double y0, double x1, double y1) {
        scaleFont(x0, y0, x1, y1);
        
        throw new java.lang.UnsupportedOperationException();
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
