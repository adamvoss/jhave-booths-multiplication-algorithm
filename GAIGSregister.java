package exe.boothsMultiplication;

import exe.*;

/**
* A prototype class to define the desired functionality of a Register
*
*/
public interface GAIGSregister extends GAIGSdatastr {

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
}
