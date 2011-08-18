package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 *
 * @param <E> Any implementer or sub-interface of MutableGAIGSdatastr (inclusive)
 */
public class GAIGSpane<E extends MutableGAIGSdatastr> extends GAIGScollection<E> implements MutableGAIGSdatastr {

    //These are from Adam Voss's Arch Linux install, probably atypical.
    public static final double arch_JHAVE_X_MARGIN = 0.203;
    public static final double arch_JHAVE_Y_MARGIN = 0.067;

    //These are from Ubuntu 11.04 Natty Narwhal, left and top margins
    public static final double narwhal_JHAVE_X_MARGIN = 0.2075;
    public static final double narwhal_JHAVE_Y_MARGIN = 0.0025;

    //These are from the UWOSH Windows 7 Image, left and top margins
    public static final double win7_JHAVE_X_MARGIN = 0.196;
    public static final double win7_JHAVE_Y_MARGIN = 0.0525;

    //These are from OS X 10.6 (UWOSH Image), left and top margins
    public static final double OSX10_6_JHAVE_X_MARGIN = 0.1875;
    public static final double OSX10_6_JHAVE_Y_MARGIN = 0.035;

    /**
     * This is the minimum x margin from those collected.  Use of this margin should provide a
     * reasonable guarantee that a visualization within these bounds will display without being
     * clipped.
     */
    public static final double JHAVE_X_MARGIN = 0.1875;

    /**
     * This is the minimum y margin from those collected.  Use of this margin should provide a
     * reasonable guarantee that a visualization within these bounds will display without being
     * clipped.
     */
    public static final double JHAVE_Y_MARGIN = 0.0025;

    /**
     * This is the aspect ratio of the JHAVE view port when using the margins specified
     */
    public static final double JHAVE_ASPECT_RATIO = (1+2*JHAVE_X_MARGIN)/(1+2*JHAVE_Y_MARGIN);

    /**
     *  This is the default aspect ratio of the coordinate system in a pane.
     *  It is square meaning an a data structure with the same width and height will visually
     *  have the same width and height.
     */
    public static final double DEFAULT_ASPECT_RATIO = 1.0;

    /**
     * The maximum value of the x coordinate system.
     */
    private double width;

    /**
     * The maximum value of the y coordinate system. 
     */
    private double height;

    /**
     * The location of the pane on screen. 
     */
    private double[] realBounds;

    /**
     * Creates a pane with its own coordinate system.
     * Pass null for (only) one of width or height to get a square aspect ratio.
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     * @param width the maximum x value in the internal coordinate system.
     * @param height the maximum y value in the internal coordinate system.
     */
    public GAIGSpane(double x0, double y0, double x1, double y1, Double width, Double height){
        super();

        if (width == null) width = ((x1-x0)/(y1-y0)/DEFAULT_ASPECT_RATIO)*height;
        else if (height == null) height = (DEFAULT_ASPECT_RATIO/((x1-x0)/(y1-y0)))*width;

        this.name="Unnamed";
        this.realBounds = new double[] {x0, y0, x1, y1};
        this.width=width;
        this.height = height;
    }

    /**
     * Creates a GAIGSpane that fills the whole viewport (on the margin constants) with
     * the specified internal coordinate system. 
     * @param width the maximum x value in the internal coordinate system.
     * @param height the maximum y value in the internal coordinate system.
     */
    public GAIGSpane(double width, double height){
        this(0-JHAVE_X_MARGIN, 0-JHAVE_Y_MARGIN, 1+JHAVE_X_MARGIN, 1+JHAVE_Y_MARGIN, width, height);
    }

    /**
     * Creates a pane that has no scaling or resizing effect.
     */
    public GAIGSpane(){
        this(0.0, 0.0, 1.0, 1.0, 1.0, 1.0);
    }

    /**
     * A deep copy constructor that has
     * a deep copy of structure in the pane.
     * 
     * @param source the GAIGSpane to be copied
     */
    @SuppressWarnings("unchecked")
    public GAIGSpane(GAIGSpane<E> source){
        super();
        this.width = source.width;
        this.height = source.height;
        this.realBounds = source.realBounds.clone();
        this.name = source.name;
        for (MutableGAIGSdatastr item : source.items){
            this.add((E) item.clone());
        }
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGScollection#toXML()
     */
    @Override
    public String toXML() {
        String xml = "<!-- Start GAIGSpane: "+ name +" -->\n";

        for (MutableGAIGSdatastr item : items){
            MutableGAIGSdatastr clone = item.clone();
            double[] newBds = getRealCoordinates(clone);
            clone.setBounds(newBds[0], newBds[1], newBds[2], newBds[3]);
            xml += clone.toXML();
        }
        xml += "<!-- End of GAIGSpane: "+ name +" -->\n";
        return xml;
    }

    /**
     * Returns the x value of the coordinate system that corresponds to the right edge of the pane.
     * @return the width of the coordinate system
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the x value of the coordinate system that will correspond to the right edge of the pane.
     * @param width the new width of the coordinate system
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the y value of the coordinate system that corresponds to the top edge of the pane.
     * @return the height of the coordinate system
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the y value of the coordinate system that will correspond to the top edge of the pane.
     * @param height the new height of the coordinate system
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Returns the aspect ratio of the coordinate system in the pane.
     * @return the aspect ratio
     */
    public double getAspectRatio(){
        return (width/(realBounds[2]-realBounds[0]))/(height/(realBounds[3]-realBounds[1]));
    }

    /**
     * Gets the coordinates the given coordinates would map to
     * if a data structure with the given coordinates were drawn
     * in this pane.
     * 
     * @param srcBounds four doubles representing the bounds of an object.
     * @return an array with the coordinates
     */
    public double[] getRealCoordinates(double[] srcBounds){
        double scaleX = (realBounds[2]-realBounds[0])/width;
        double scaleY = (realBounds[3]-realBounds[1])/height;
        return new double[] {srcBounds[0]*scaleX + realBounds[0],
                srcBounds[1]*scaleY + realBounds[1],
                srcBounds[2]*scaleX + realBounds[0],
                srcBounds[3]*scaleY + realBounds[1]};
    }

    /**
     * Gets the coordinates the the given data structure would have have
     * when being displayed in this pane.
     *  
     * @param data The data structure, it does not need to be in the pane
     * @return an array with the coordinates
     */
    public double[] getRealCoordinates(MutableGAIGSdatastr data){
        return getRealCoordinates(data.getBounds());
    }
    
    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return realBounds;
    }

    /**
     * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x1, double y1, double x2, double y2) {
        this.realBounds = new double[] {x1, y1, x2, y2};
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getFontSize()
     */
    @Override
    public double getFontSize() {
        double sum = 0;
        for (MutableGAIGSdatastr item : items){
            sum += item.getFontSize();
        }
        return sum/items.size();
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#setFontSize(double)
     */
    @Override
    public void setFontSize(double fontSize) {
        for (MutableGAIGSdatastr item : items){
            item.setFontSize(fontSize);
        }
    }

    /**
     * Returns a deep copy of of the pane that has a deep copy of everything it contains.
     * 
     * @return A deep copy.
     */
    public GAIGSpane<E> clone(){
        return new GAIGSpane<E>(this);
    }
}
