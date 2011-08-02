package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;

/**
 * GAIGSregister provides a way to visualize machine registers
 * using GAIGS XML. 
 * 
 * It does not enforce that only 0s and 1s be entered.
 * This GAIGSregister does not display its name field.
 * Currently the outline thickness is fixed, though it would be easy
 * to modify this code to make it a parameter.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */

public class GAIGSregister implements MutableGAIGSdatastr {
    private GAIGSpane<GAIGSrectangle> wrapped;


    /**
     * A deep-copy constructor
     * @param The GAIGSregister to be copied
     */
    public GAIGSregister(GAIGSregister source){
        this.wrapped = new GAIGSpane<GAIGSrectangle>(source.wrapped);
    }

    /**
     * Creates a register.
     * @param length The number of bits in the register.
     * @param name The name of the register, it will only appear in the XML.
     * @param fillColor color the register.
     * @param fontColor color of bits in the register.
     * @param outlineColor color of the outline of the register.
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     * @param fontSize font size of the bits in the register.
     */
    public GAIGSregister(int length, String name, String fillColor,
            String fontColor, String outlineColor, double x0, double y0,
            double x1, double y1, double fontSize) {

        int line_width = 1;

        wrapped = new GAIGSpane<GAIGSrectangle>(x0, y0, x1, y1, (double) length, 1.0);
        wrapped.setName("GAIGSregister");

        double width = 1;

        while (length > 0){
            wrapped.add(new GAIGSrectangle(length-width, 0, length, 1,
                    fillColor, fillColor, fontColor,
                    "0", fontSize, line_width));
            length--;
        }

        //The outline rectangle, here last so it is always on top.
        wrapped.add(new GAIGSrectangle(0, 0, wrapped.size(), 1,
                "", fontColor, outlineColor,
                "", fontSize, line_width));
    }

    /**
     * This constructor takes its bounds as an an array rather than individually.
     * @param length The number of bits in the register.
     * @param name The name of the register, it will only appear in the XML.
     * @param fillColor color the register.
     * @param fontColor color of bits in the register.
     * @param outlineColor color of the outline of the register.
     * @param bounds an array containing x0, y0, x1, y1 in that order.
     * @param fontSize font size of the bits in the register.
     */
    public GAIGSregister(int length, String name, String fillColor,
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
        return wrapped.toXML();
    }


    /**
     * Returns the number of bits in the register.
     * @return number of bits in the register.
     */
    public int getSize() {
        return wrapped.size()-1;
    }

    /**
     * Fetch the value of the bit at the given location.
     * The right-most bit is at location 0. 
     * @param loc location of the value to be returned.
     * @return value at the specified location.
     */
    public int getBit(int loc) {
        return Integer.valueOf(wrapped.get(loc).getLabel());
    }

    /**
     * Set the value of the bit at the given location to the specified value
     * @param loc location of the value to be set.
     * @param value new value.
     */
    public void setBit(int loc, int value) {
        this.wrapped.get(loc).setLabel("" + value);
    }

    /**
     * Sets the text display color of the bit at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setTextColor(int loc, String color) {
        this.wrapped.get(loc).setLabelColor(color);
    }

    /**
     * Sets the text display color of the of all bits in the register. 
     * @param color GAIGS color String.
     */
    public void setTextColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setLabelColor(color);
        }
    }

    /**
     * Sets the color around the bit at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillColor(int loc, String color) {
        this.wrapped.get(loc).setColor(color);
    }

    /**
     * Sets the color around of the of all bits in the register. 
     * @param color GAIGS color String.
     */
    public void setFillColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setColor(color);
        }
    }

    /**
     * Sets the color of vertical bar on both sides of the bit at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setOutlineColor(int loc, String color) {
        this.wrapped.get(loc).setOutlineColor(color);
    }

    /**
     * Sets the color of the perimeter of the register.
     * @param color
     */
    public void setOutlineColor(String color) {
        //The outline is always the last item on the pane
        wrapped.get(wrapped.size()-1).setOutlineColor(color);
    }

    /**
     * Sets both the fill and the outline color around the bit at the given location. 
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillOutlineColor(int loc, String color) {
        wrapped.get(loc).setColor(color);
        wrapped.get(loc).setOutlineColor(color);
    }

    /**
     * Sets both the fill and the outline color around all bits in the register.
     * This leaves the outline of the register unaffected.  Use this to color all the
     * contents of a register.   
     * @param loc location of the color to be set.
     * @param color GAIGS color String.
     */
    public void setFillOutlineColor(String color) {
        for (int loc = this.getSize()-1; loc >= 0; loc--){
            wrapped.get(loc).setColor(color);
            wrapped.get(loc).setOutlineColor(color);
        }
    }


    /**
     * Overwrite the value contained in the register.
     * Overflow will be silently discarded.  Underflow will be padded
     * by left extending the sign.  An empty string will be treated as 0.  
     * 
     * @param binStr The binary number represented as a string.
     * @return String containing the previous contents of the register
     */
    public String set(String binStr) {
        String oldValue = this.toString();

        //Empty String == 0
        if (binStr.isEmpty()){binStr="0";}

        //Expand string to register size
        if (binStr.length() < getSize() ){
            binStr = signExtend(binStr, getSize()-binStr.length());
        }

        //If string too big, cut off most significant bits
        binStr = binStr.substring(binStr.length()-this.getSize());
        for (int count = 0; count < this.getSize(); count++){
            this.setBit(count, Character.getNumericValue(binStr.charAt(binStr.length()-1-count)));
        }

        return oldValue;
    }

    /**
     * Sign extends a binary number by i bits.
     * @param binStr The binary number represented as a string.
     * @param i The number of bits to extend.
     * @return the sign-extended string.
     */
    public String signExtend(String binStr, int i){
        String firstBit = String.valueOf(binStr.charAt(0));
        String extension = "";
        while (i>0){extension = extension.concat(firstBit); i--;}
        return extension.concat(binStr);
    }

    /**
     * Returns an array representation of the register contents.
     * The least significant bit is index 0.
     * @return Array representation
     */
    public int[] toIntArray() {
        int size = this.getSize();

        if (size == 0)
            return new int[0];

        int[] ret = new int[size];

        for (int loc = size-1; loc >= 0; loc--){
            ret[loc] = Integer.valueOf(wrapped.get(loc).getLabel());
        }

        return ret;
    }

    /**
     * Returns a string representation of the bits in the register.
     */
    @Override
    public String toString() {
        String ret = "";

        int[] bits = this.toIntArray();

        for (int i = 0; i < bits.length; ++i)
            ret = bits[i] + ret;

        return ret;
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
    public GAIGSregister clone(){
        return new GAIGSregister(this);
    }
}
