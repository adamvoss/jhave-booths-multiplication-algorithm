package exe.boothsMultiplication;

import exe.GAIGSdatastr;

/**
 * MutableGAIGSdatastr interface provides additional requirements on GAIGSdatastr
 * in order to provide the guarantee of a consistent interface to
 * clone, move, resize the data structure.
 * <br/><br/>
 * This interface provides no guarantees with regards to how implementing data
 * structures scale text; but, it does require fontSize methods to allow for that 
 * level of control. 
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-15
 */

/* Authors note: Cloneable is purposely not implemented.  Then Object's clone would be callable,
 * meaning implementers of this interface my not implement their own clone. 
 */ 

//Color was not included in this interface because many classes would ignore it.
//TODO consider: scale, resize, move.
public interface MutableGAIGSdatastr extends GAIGSdatastr {
    /**
     * Returns an array containing the rectangular bounds of the data structure.
     * The order of these coordinates should be
     * lower-left x, lower-left y, upper-right x, upper-right y.
     *  
     * @return array of coordinates
     */
    public double[] getBounds();


    /**
     * Sets the bounds of the object.  This provides the ability to
     * scale, move, and/or resize in a single command.
     * <br/>
     * This provides no guarantee that fontSize of the object is scaled according.
     * 
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     */
    public void setBounds(double x0, double y0, double x1, double y1);


    //Note there is no storage requirement for the font size

    /**
     * Returns the fontSize.
     * Note: while this method must be implemented and return a valid double,
     * not all data structures must display text. 
     * @return the font size.
     */
    public double getFontSize();

    /**
     * Sets the fontSize of the data structure.
     * Note: while this method must be implemented and not throw an exception,
     * not all data structures must display text.  Thus the implementing method
     * may return without taking any action. 
     * @param fontSize the desired the font size.
     */
    public void setFontSize(double fontSize);

    /**
     * Returns a deep copy of the data structure object.
     * The copy should be deep enough that any mutator on the object can 
     * be used without affecting the original object.
     * The intention is that every classes will override this method,
     * so clone will always return the same type as the object, making casts
     * less necessary.
     *   
     * @return a deep copy of the object. 
     */
    public MutableGAIGSdatastr clone();
}
