package exe.boothsMultiplication;

import exe.*;

/**
* A prototype class to define the desired functionality of a Register
*
*/
//Poor form hack to make trace work without refactoring.
public interface GAIGSregister extends MutableGAIGSdatastr, Cloneable {

    /**
    * From GAIGSdatastr
    *
    */
    public String getName();

    /**
    * From GAIGSdatastr
    *
    */
    public void setName(String name);

    /**
    * From GAIGSdatastr
    *
    */
    public String toXML();

    /**
    * Returns the number of bits in the register.
    *
    */
    public int getSize();

    /**
    * Fetch the value of the bit at loc
    *
    */
    public int getBit(int loc); 

    /**
    * Set the value of the bit at loc
    *
    */
    public void setBit(int value, int loc); 

    /**
    * Set the value of the bit at loc with color
    *
    */
    public void setBit(int value, int loc, String cl); 

    /**
    * Set the label of the register
    *
    */
    public void setLabel(String label);

    /**
    * Fetch the label of the register
    *
    */
    public String getLabel();

    /**
    * Set the color of the bit at loc
    *
    */
    public void setColor(int loc, String cl);

    /**
    * Sets every bit in the register to the color
    * given by cl
    */
    public void setAllToColor(String cl);

    /**
    * Set the value in the register, discarding overflow
    *
    */
    public void set(String binaryString);

    /**
    * Create a new GAIGSregister with the same data, at a new position.
    *
    */
    public GAIGSregister copyTo(Bounds bounds);

    /**
    * Return an array of ints with the values in the GAIGSregister
    *
    */
    public int[] toIntArray();

    /**
    * Returns a String representing the binary values in the register.
    * Must be overwritten!
    */
    @Override
    public String toString();
    
}
