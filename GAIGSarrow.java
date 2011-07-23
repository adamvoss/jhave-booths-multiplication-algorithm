package exe.boothsMultiplication;

/**
 * <p>GAIGSarrow provides the ability represent pointed arrow in GAIGS XML. 
 * This arrow is simply a line with a triangle on the end of it.</p>
 * 
 * <p>An arrow's color is determined by its outline color.</p>
 * 
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-23
 */
//TODO consider overriding color methods to be more intuitive.
public class GAIGSarrow extends AbstractPrimitive implements MutableGAIGSdatastr{
    protected double headSize;
    protected double[] x;
    protected double[] y;
    
    /**
     * Arrows heads can end up backwards in the primitiveCollection,
     * when true, this ensures that functionality.
     */
    public boolean legacy_primitiveCollection = false;

    /**
     * Creates an arrow from the specified parameters.
     * @param x Array of 2 containing the x coordinates for the start point and end point.
     * @param y Array of 2 containing the y coordinates for the start point and end point.
     * @param color The color of the line.
     * @param labelColor The color of the text in the label.
     * @param label The text to printed near the line.
     * @param headSize The size of the arrow head.
     * @param textHeight The Height of the text in the label.
     * @param lineWidth The thickness of the line.
     */
    public GAIGSarrow(double x[], double y[], String color, String labelColor,
            String label, double headSize, double textHeight, int lineWidth){

        this.headSize = headSize;

        this.fcolor = color;
        this.lcolor = labelColor;
        this.label = label;
        this.fontSize = textHeight;
        this.lineWidth = lineWidth;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Creates an arrow with the default font size and line width.
     * @param x Array of 2 containing the x coordinates for the start point and end point.
     * @param y Array of 2 containing the y coordinates for the start point and end point.
     * @param color The color of the line.
     * @param labelColor The color of the text in the label.
     * @param label The text to printed near the line.
     * @param headSize The size of the arrow head.
     */
    public GAIGSarrow(double x[], double y[], String color, String labelColor,
            String label, double headSize){
        this(x, y, color, labelColor, label, headSize, TEXT_HEIGHT, LINE_WIDTH);
    }
    
    /**
     * A deep copy constructor.
     * @param source the GAIGSarrow to be copied.
     */
    public GAIGSarrow(GAIGSarrow source){
        this.x = source.x;
        this.y = source.y;
        this.headSize = source.headSize;

        this.ocolor = source.ocolor;
        this.lcolor = source.lcolor;
        this.label = source.label;
        this.fontSize = source.fontSize;
        this.lineWidth = source.lineWidth;
        this.text_resize = source.text_resize;
    }
    
    /** 
     * Creates the shape for the head of the arrow.
     * @return GAIGSpolygon of a triangular Arrow Head.
     */
    //This could be cleaned a lot, but not touching because it works
    //TODO clean this code.
    private GAIGSpolygon makeArrowHead(){
        double headSize = this.headSize;
        
        if (!legacy_primitiveCollection && (x[0] > x[1])){
            headSize *= -1; 
        }
        
        double [] x0 = {this.x[0], 0};
        double [] y0 = {this.y[0], 0};
        double [] x1 = {this.x[1], 0};
        double [] y1 = {this.y[1], 0};

        double theta = Math.atan((y1[0] - y0[0])/(x1[0] - x0[0]));
        double end1 = theta + Math.toRadians(30);
        double end2 = theta - Math.toRadians(30);

        x0[1] = x1[0] - headSize * Math.cos(end1);
        x1[1] = x1[0] - headSize * Math.cos(end2);
        y0[1] = y1[0] - headSize * Math.sin(end1);
        y1[1] = y1[0] - headSize * Math.sin(end2);

        double [] xvals = {x1[0], x0[1], x1[1]};
        double [] yvals = {y1[0], y0[1], y1[1]};

        return new GAIGSpolygon(3, xvals, yvals,
                this.fcolor, this.fcolor, this.lcolor,
                this.label, this.fontSize, this.lineWidth);
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return new GAIGSline(x, y, fcolor, lcolor, "", fontSize, lineWidth).getBounds();
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x0, double y0, double x1, double y1) {
        scaleFont(x0, y0, x1, y1);
        
        GAIGSline ref = new GAIGSline(x, y, fcolor, lcolor, "", fontSize, lineWidth);
        
        double newLen = Math.sqrt(Math.pow(x1-x0, 2)+Math.pow(y1-y0,2));
        double[] oldBds = ref.getBounds();
        double oldLen = Math.sqrt(Math.pow(oldBds[2]-oldBds[0], 2)+Math.pow(oldBds[3]-oldBds[1],2));

        this.headSize*=(newLen/oldLen);
        
        ref.setBounds(x0, y0, x1, y1);
        this.x = ref.x;
        this.y = ref.y;
    }
    
    /* (non-Javadoc)
     * @see exe.boothsMultiplication.AbstractPrimitive#toCollectionXML()
     */
    @Override
    protected String toCollectionXML() {
        return new GAIGSline(x, y, fcolor, lcolor, "", fontSize, lineWidth).toCollectionXML() + makeArrowHead().toCollectionXML();
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.AbstractPrimitive#clone()
     */
    @Override
    public GAIGSarrow clone() {
        return new GAIGSarrow(this);
    }

}
