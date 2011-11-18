package exe.boothsMultiplication;

import exe.GAIGSpane;
import exe.GAIGSrectangle;
import exe.MutableGAIGSdatastr;

/**
 * GAIGSprimitiveArray uses the GAIGS primitives classes to provide
 * a horizontal array.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
public class GAIGSprimitiveArray implements MutableGAIGSdatastr {
    private GAIGSpane<GAIGSrectangle> wrapped;

    private Object[] storage;

    /**
     * A deep-enough copy constructor.
     * 
     * Because Object cannot be cloned, know that the stored objects
     * are not cloned.  
     * 
     * @param The GAIGSprimitiveArray to be copied
     */
    public GAIGSprimitiveArray(GAIGSprimitiveArray source){
        this.wrapped = new GAIGSpane<GAIGSrectangle>(source.wrapped);
        this.storage = source.storage.clone();
    }

    /**
     * Creates a array.
     * @param length The number of elements in the array.
     * @param name The name of the array, it will only appear in the XML.
     * @param fillColor color the array.
     * @param fontColor color of elements text in the array.
     * @param outlineColor color of the outline of the array.
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     * @param fontSize font size of the elements in the array.
     */
    public GAIGSprimitiveArray(int length, String name, String fillColor,
            String fontColor, String outlineColor, double x0, double y0,
            double x1, double y1, double fontSize) {

        this.storage = new Object[length];
        
        for (int i = 0 ; i < storage.length ; i++){
            storage[i] = "";
        }
        
        int line_width = 1;

        wrapped = new GAIGSpane<GAIGSrectangle>(x0, y0, x1, y1, (double) length, 1.0);
        wrapped.setName("GAIGSprimitiveArray");

        double width = 1;

        while (length > 0){
            wrapped.add(new GAIGSrectangle(length-width, 0, length, 1,
                    fillColor, fillColor, fontColor,
                    "", fontSize, line_width));
            length--;
        }

        //The outline rectangle, here last so it is always on top.
        wrapped.add(new GAIGSrectangle(0, 0, wrapped.size(), 1,
                "", fontColor, outlineColor,
                "", fontSize, line_width));
    }

    /**
     * This constructor takes its bounds as an an array rather than individually.
     * @param length The number of elements in the array.
     * @param name The name of the array, it will only appear in the XML.
     * @param fillColor color the array.
     * @param fontColor color of elements text in the array.
     * @param outlineColor color of the outline of the array.
     * @param bounds an array containing x0, y0, x1, y1 in that order.
     * @param fontSize font size of the elements in the array.
     */
    public GAIGSprimitiveArray(int length, String name, String fillColor,
            String fontColor, String outlineColor, double[] bounds, double fontSize) {
        this(length, name, fillColor, fontColor, outlineColor , bounds[0], bounds[1], bounds[2], bounds[3], fontSize);
    }


    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#getName()
     */
    @Override
    public String getName() {
        return wrapped.getName();
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        wrapped.setName(name);
    }


    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#toXML()
     */
    @Override
    public String toXML() {
        for (int loc=0 ; loc < storage.length ; loc++ ){
            wrapped.get(loc).setLabel(storage[loc].toString());
        }
        
        return wrapped.toXML();
    }


    /**
     * Returns the number of elements in the array.
     * @return number of elements in the array.
     */
    public int getSize() {
        return wrapped.size()-1;
    }

    /**
     * Fetch the value of the element at the given location.
     * The right-most element is at location 0. 
     * @param loc location of the value to be returned.
     * @return value at the specified location.
     */
    public Object get(int loc) {
        return storage[loc];
    }

    /**
     * Set the element at the given location to the specified element
     * @param loc location of the value to be set.
     * @param value new value.
     */
    public void set( Object item, int loc) {
        storage[loc] = item;
    }

    /**
     * Sets the text display color of the element at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setTextColor(int loc, String color) {
        this.wrapped.get(loc).setLabelColor(color);
    }

    /**
     * Sets the text display color of the of all elements in the array. 
     * @param color GAIGS color String.
     */
    public void setTextColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setLabelColor(color);
        }
    }

    /**
     * Sets the color around the element at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillColor(int loc, String color) {
        this.wrapped.get(loc).setColor(color);
    }

    /**
     * Sets the color around of the of all elements in the array. 
     * @param color GAIGS color String.
     */
    public void setFillColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setColor(color);
        }
    }

    /**
     * Sets the color of vertical bar on both sides of the element at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setOutlineColor(int loc, String color) {
        this.wrapped.get(loc).setOutlineColor(color);
    }

    /**
     * Sets the color of the perimeter of the array.
     * @param color
     */
    public void setOutlineColor(String color) {
        //The outline is always the last item on the pane
        wrapped.get(wrapped.size()-1).setOutlineColor(color);
    }

    /**
     * Sets both the fill and the outline color around the element at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillOutlineColor(int loc, String color) {
        wrapped.get(loc).setColor(color);
        wrapped.get(loc).setOutlineColor(color);
    }

    /**
     * Sets both the fill and the outline color around all elements in the array.
     * This leaves the outline of the array unaffected.  Use this to color all the
     * contents of a array.   
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillOutlineColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setColor(color);
            wrapped.get(loc).setOutlineColor(color);
        }
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return wrapped.getBounds();
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x1, double y1, double x2, double y2) {
        this.wrapped.setBounds(x1, y1, x2, y2);
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#getFontSize()
     */
    @Override
    public double getFontSize() {
        return wrapped.getFontSize();
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.MutableGAIGSdatastr#setFontSize(double)
     */
    @Override
    public void setFontSize(double fontSize) {
        wrapped.setFontSize(fontSize);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public GAIGSprimitiveArray clone(){
        return new GAIGSprimitiveArray(this);
    }
}
