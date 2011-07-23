package exe.boothsMultiplication;

import exe.GAIGSdatastr;
import exe.boothsMultiplication.MutableGAIGSdatastr;

/** 
 * This is the abstract base class for primitives intended for use in the
 * GAIGSprimitiveCollection.  All primitives should be supported client side
 * by having its XML contained within &lt;primitivecollection&gt; tags.
 * <br/>
 * <br/>
 * GAIGSprimitiveCollection was originally developed for the Sutherland-Hodgman 
 * clipping algorithm visualization by Shawn Recker and Alejandro Carrasquilla.
 * The primitives have since been refactored for more general use.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-23
 */
public abstract class AbstractPrimitive implements GAIGSdatastr  {
    
    /**
     * Enum definition of valid font resizing options. Note: while this functionality
     * exists, it is hard to use with MutableGAIGSdatastr methods, because you would
     * need to vastly inflate the initial font size to get the expected outcome.  
     * 
     * Someone may want to implement more advanced (useful) scale options in the future.
     * Maybe scaling according to change in area.  Also adding a way to specify font size
     * such that it automatically is inflated (it would adjust to be a percentage of the
     * Primitives bounds) would be appreciated.
     */
    private enum TextResizeMode{NONE, SCALEX, SCALEY}

    /**
     * Enum to be used with setTextResizeMode to indicate that no resizing should be done
     */
    public static final TextResizeMode NONE = TextResizeMode.NONE;
    
    /**
     * Enum to be used with setTextResizeMode to indicate that font size should
     * scale according the change in height.
     */
    public static final TextResizeMode SCALEX = TextResizeMode.SCALEX;
    
    /**
     * Enum to be used with setTextResizeMode to indicate that font size should
     * scale according the change in width. This is how GAIGStext behaves.
     */
    public static final TextResizeMode SCALEY = TextResizeMode.SCALEY;

    /**
     * The text String that will be drawn with this primitive
     */
    protected String label;

    /**
     * The default fontSize used by primitives
     */
    final public static double TEXT_HEIGHT = .03;

    /**
     * The default lineWidth or Outline Thickness used by primitives
     */
    final public static int LINE_WIDTH = 12;

    protected String fcolor;
    protected String ocolor;
    protected String lcolor;

    /**
     * This is the name field that exists for compliance with the GAIGSdatastr.
     * It will appear in the XML, but never on-screen.  For on-screen with primitives
     * use their label. 
     */
    protected String name = "";


    protected double fontSize;
    protected int lineWidth;
    protected TextResizeMode text_resize = NONE;


    /**
     * Returns the XML that is to be used within &lt;primitivecollection&gt; tags
     * for use in GAIGSprimitiveCollection.
     * @return String representation of XML elements.
     */
    abstract protected String toCollectionXML();


    /** 
     * Returns a deep-copy of this object.
     */
    abstract public AbstractPrimitive clone();
    
    /**
     * @see exe.MutableGAIGSdatastr#getBounds()
     */
    abstract double[] getBounds();

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#toXML()
     * 
     * This is final to discourage subclasses of existing primitives from
     * straying far from their roots in GAIGSprimitveCollection.  If you have
     * a good reason to extend then remove the final.  Otherwise the recommendation
     * it so use the generic GAIGScollection in favor of GAIGSprimitiveCollection.
     * 
     * Inheritors of Abstract Primitives must provide the
     * toCollection XML for use within a collection.
     * This allows them to be drawn singly by using a collection
     * with only one element. 
     */
    @Override
    final public String toXML() {
        return "<primitivecollection>\n\t<name>" + name +
        "</name>\n\t"+ this.computeBounds() + "\n\t" + 
        this.toCollectionXML() +"</primitivecollection>\n";
    }

    /**
     * This returns a string containing the &lt;bounds /&gt; XML element  
     * @return String representing bounds XML element.
     */
    /* Due to a client side bug/feature the coordinates of cannot be the coordinates of the bounds.
     * Instead the coordinates given are drawn relative to the bounds given, wherein a unit square
     * primitive perfectly fills bounds on screen.
     */
    //	protected String computeBounds() {
    //	double[] bounds = this.getBounds();
    //	return "<bounds x1=\"" + bounds[0] + "\" y1=\"" + bounds[1] + "\" x2=\"" + bounds[2] + "\" y2=\"" + bounds[3] + "\"/>";
    //}
    //This is final for the same reason as toXML()
    final protected String computeBounds() {
        return "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>";
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }
    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Fill Color
     * @return GAIGS-formated hex color string.
     */
    public String getColor() {
        return fcolor;
    }

    /**
     * Sets the fill color of the primitive to the given String.
     * null results in Black; however, the empty string "" will result in no fill or transparency.
     * @param fcolor GAIGS-formated hex color string.
     */
    public void setColor(String fcolor) {
        this.fcolor = fcolor;
    }

    /**
     * Returns the outline color of the primitive.
     * @return GAIGS-formated hex color string.
     */
    public String getOutlineColor() {
        return ocolor;
    }

    /**
     * Sets the outline color of the primitive to the given String. 
     * @param ocolor GAIGS-formated hex color string.
     */
    public void setOutlineColor(String ocolor) {
        this.ocolor = ocolor;
    }
    /**
     * Sets the label, or text color of the primitive.
     * @return GAIGS-formated hex color string.
     */
    public String getLabelColor() {
        return lcolor;
    }

    /**
     * Returns the label, or text color of the primitive to the given String.
     * @param lcolor GAIGS-formated hex color string.
     */
    public void setLabelColor(String lcolor) {
        this.lcolor = lcolor;
    }

    /**
     * The thickness of the outline.
     * @return int value of with outline thickness.
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets the outline thickness to the given value
     * @param lineWidth int value of the desired thickness.
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Returns the label text.
     * @return String currently used as the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label text which will be displayed with the object.
     * Use the empty string is you want no text to display.
     * @param label String desired for display.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the value of the font size.
     * The scale is based on the unit lengt of the JHAVÉ.
     * i.e. A font size of 1 would create text with height that would
     * span the unit square.  Therefore, 0.07 size font would have a height
     * of 7% of the same unit square. 
     * @return  The current font size.
     */
    public double getFontSize(){
        return this.fontSize;
    }

    /**
     * Sets the font size for display.
     * @param      fontSize     The desired font size.
     */
    public void setFontSize(double fontSize){
        this.fontSize = fontSize;
    }

    /**
     * Returns the enum that corresponds to how fontSize should change if the object is resized.
     * @return enum of the current mode.
     */
    public TextResizeMode getTextResizeMode(){
        return this.text_resize;
    }

    /**
     * Sets how text should resize when the object is scaled.
     * @param mode Enum of the desired mode.
     */
    public void setTextResizeMode(TextResizeMode mode){
        this.text_resize = mode;
    }
    
    /**
     * A convenience method for scaling font size.
     * All inheritors call this in their toCollectionXML method. 
     * @param bounds
     */
    protected void scaleFont(double x0, double y0, double x1, double y1){
        double[] current = this.getBounds();
        
        double scaleX = (x1-x0)/(current[2]-(current[0]));
        double scaleY = (y1-y0)/(current[3]-(current[1]));
        
        switch (text_resize){
        case SCALEX:
            this.fontSize *= scaleX;
            break;
        case SCALEY:
            this.fontSize *= scaleY;
            break;
        case NONE:
            break;
        }
    }
}