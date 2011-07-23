package exe.boothsMultiplication;

/**
 * <p>GAIGSline provides a convenient way to represent a line in GAIGS XML.
 * A line's color is determined by its outline color.
 * </p>
 *
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-23
 */
//TODO consider overriding color methods to be more intuitive.
public class GAIGSline extends AbstractPrimitive implements MutableGAIGSdatastr {
    /**
     * The x coordinates of the line.
     */
    protected double x[] = new double[2];
    /**
     * The y coordinates of the line.
     */
    protected double y[] = new double[2];

    /**
     * Creates a line from the specified parameters.
     * @param x Array of 2 containing the x coordinates for the start point and end point
     * @param y Array of 2 containing the y coordinates for the start point and end point
     * @param color The color of the line
     * @param labelColor The color of the text in the label
     * @param label The text to printed near the line
     * @param textHeight The height of the text in the label
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
     * Creates a line with the default font size and thickness. 
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
     * Creates a line between two points using default parameters.
     * @param x Array of 2 containing the x coordinates for the start point and end point
     * @param y Array of 2 containing the y coordinates for the start point and end point
     */
    public GAIGSline(double x[], double y[]){
        this(x, y, DEFAULT_COLOR, DEFAULT_COLOR, "", TEXT_HEIGHT, LINE_WIDTH);
    }

    /**
     * A deep copy constructor.
     * @param source The GAIGSline to be copied
     */
    public GAIGSline(GAIGSline source) {
        this.x = source.x.clone();
        this.y = source.y.clone();
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
    public double[] getBounds(){
        double x1 = Double.POSITIVE_INFINITY;
        double y1 = Double.POSITIVE_INFINITY;
        double x2 = Double.NEGATIVE_INFINITY;
        double y2 = Double.NEGATIVE_INFINITY;

        for(int j = 0; j < 2; ++j) {
            x1 = (x1 < x[j] ? x1 : x[j]);
            y1 = (y1 < y[j] ? y1 : y[j]);
            x2 = (x2 > x[j] ? x2 : x[j]);
            y2 = (y2 > y[j] ? y2 : y[j]);
        }

        return new double[] {x1, y1, x2, y2};
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

    /**
     * The line's orientation changed by changing the order parameters are passed. 
     * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x0, double y0, double x1, double y1) {   
        scaleFont(x0, y0, x1, y1);
        
        //Handle X
        
        if (x0 < x1){
            //User doesn't want to change orientation
            if (x[0] < x[1]){
                //We point right
                x[0] = x0;
                x[1] = x1;
                //We still point right
            }
            else{
                //We point left
                x[0] = x1;
                x[1] = x0;
                //We still point left
            }

        }
        else{
            //User indicated a flip
            if (x[0] < x[1]){
                //We point right
                x[0] = x1;
                x[1] = x0;
                //Now we point left
            }
            else{
                //We point left
                x[0] = x0;
                x[1] = x1;
                //Now we point right
            }
        }
        
        //Handle Y
        
        if (y0 < y1){
            //User doesn't want to change orientation
            if (y[0] < y[1]){
                //We point up
                y[0] = y0;
                y[1] = y1;
                //We still up
            }
            else{
                //We point down
                y[0] = y1;
                y[1] = y0;
                //We still point down
            }

        }
        else{
            //User indicated a flip
            if (y[0] < y[1]){
                //We point up
                y[0] = y1;
                y[1] = y0;
                //Now we down
            }
            else{
                //We point down
                y[0] = y0;
                y[1] = y1;
                //Now we point up
            }
        }
        
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.Primitive#clone()
     */
    @Override
    public GAIGSline clone() {
        return new GAIGSline(this);
    }

}
