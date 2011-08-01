package exe.boothsMultiplication;

import java.util.Arrays;

import exe.boothsMultiplication.GAIGSArithmetic;

/**
 * TwosComplementMultiplication provides the ability to display
 * two's complement multiplication within GAIGS XML.  Internally,
 * Booth's Multiplication algorithm is used to calculate the result.
 * 
 * @see exe.GAIGSGAIGSArithmetic
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 */

 /*
 * It would be much cleaner if all my private methods returned a value
 * rather than modified in-place.  That said, this works.
 */
public class TwosComplementMultiplication extends GAIGSArithmetic {
    protected int cmpIndex;
    protected int cmpValue;
    protected char[] product;

    /**
     * Creates a new TwosComplementMultiplication with the given parameters
     * @param term1         First binary number, represented as a String.
     * @param term2         Second binary number, represented as a String.
     * @param x0            Location of the upper right-hand corner. 
     * @param y0            Location of the upper right-hand corner
     * @param fontSize      The font size.
     * @param digitWidth    The spacing between digits.
     * @param color         The drawing color of the object.
     */
    public TwosComplementMultiplication(String term1, String term2,
            double x0, double y0, double fontSize,
            double digitWidth, String color) {
        super('*', term1, term2, 2, x0, y0, fontSize, digitWidth, color);
        if (term1.length() != term2.length()) System.err.println("TwosComplementMultiplication requires same length terms");
        this.maxLength = term1.length() + term2.length();

        product = new char[maxLength];
        terms.set(terms.size()-1, product);
        for (int i = 0; i < product.length; i++ )
            product[i] = '0';

        colors.set(terms.size()-1, new String[maxLength]);


        this.cmpValue=0;
        this.cmpIndex=0;
    }

    /**
     * Uses the fontSize as for the digitWidth.
     * @param term1         First binary number, represented as a String.
     * @param term2         Second binary number, represented as a String.
     * @param x0            Location of the upper right-hand corner. 
     * @param y0            Location of the upper right-hand corner
     * @param fontSize      The font size.
     * @param color         The drawing color of the object.
     */
    public TwosComplementMultiplication(String term1, String term2,
            double x0, double y0, double fontSize, String color) {
        this(term1, term2, x0, y0, fontSize, fontSize, color);
    }

    /**
     * Creates a TwosComplementMultiplication that draws Black.
     * @param term1         First binary number, represented as a String.
     * @param term2         Second binary number, represented as a String.
     * @param x0            Location of the upper right-hand corner. 
     * @param y0            Location of the upper right-hand corner
     * @param fontSize      The font size.
     */
    public TwosComplementMultiplication(String term1, String term2,
            double x0, double y0, double fontSize) {
        this(term1, term2, x0, y0, fontSize, COLOR);
    }

    /**
     * Uses the default font size.
     * @param term1
     * @param term2
     * @param x0
     * @param y0
     */
    public TwosComplementMultiplication(String term1, String term2,
            double x0, double y0) {
        this(term1, term2, x0, y0, FONT_SIZE);
    }

    /**
     * A deep-copy constructor
     * @param source the TwosComplementMultiplication to be copied
     */
    public TwosComplementMultiplication(TwosComplementMultiplication source) {
        super(source);
        this.cmpIndex = source.cmpIndex;
        this.cmpValue = source.cmpValue;
        this.product  = source.product;
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGSArithmetic#clone()
     */
    @Override
    public TwosComplementMultiplication clone(){
        return new TwosComplementMultiplication(this);
    }

    /**
     * Performs a right shift by one bit, padding with the character "fill".
     * @param fill padding character.
     * @param array binary char[] array.
     */
    private void singleRightShift(char fill, char[] array){
        //because we don't need to see -1
        for (int i = 0; i < array.length-1; i++ )
            array[i] = array[i+1];
        array[array.length-1] = fill;
    }

    /**
     * Adds two binary numbers together, storing the result in the addend's array.
     * @param augend binary char[] array.
     * @param addend binary char[] array, location of the result.
     */
    private void addDiscardOverflow(char[] augend, char[] addend){
        char carry = '0';
        for (int i = 0; i < augend.length; i++){
            int fullSum = (Character.getNumericValue(augend[i])
                    + Character.getNumericValue(addend[i])
                    + Character.getNumericValue(carry));
            addend[i]=Character.forDigit(fullSum%2, 2);
            carry    =Character.forDigit(fullSum/2, 2);
        }
    }

    /**
     * Flips all the bits in the char[].  This calculates the diminished
     * radix complement or the 1's complement
     * @param array binary char[] array.
     */
    private void bitComplement(char[] array){
        for (int i = 0; i < array.length; i++){
            if (array[i] == '0')
                array[i] = '1';
            else if (array[i] == '1')
                array[i] = '0';
            else
                System.err.println("Invalid char[] pased tobitComplement");
        }
    }

    /**
     * Adds one to the binary value stored in the char[].
     * @param addend binary char[] array.
     */
    private void addOne(char[] addend){
        char[] augend = new char[addend.length];
        for (int i = 1; i < augend.length; i++)
            augend[i] = '0';
        augend[0] = '1';

        addDiscardOverflow(augend, addend);
    }

    /**
     * Calculates the radix (or Two's) complement of the array and store it.
     * @param array binary char[] array.
     */
    private void radixComplement(char[] array){
        bitComplement(array);
        addOne(array);
    }

    /**
     * Overwrite the most highest index elements in a char[].
     * Used for storing a new value starting with the highest order bits.
     * In this case to set the result of a sum.
     *  
     * @param source the char[] with the desired values.
     * @param dest the char[] where the values are to be put.
     */
    private void mostSignificantOverwrite(char[] source, char[] dest){
        int index = dest.length-source.length;
        for (char ch : source){
            dest[index]=ch;
            index++;				
        }

    }

    /**
     * Performs a single iteration of Booth's Multiplication Algorithm.
     */
    @Override
    public void step(){
        //Uses more memory at the expense of more readable code
        char[] augend = terms.get(firstTermIndex);
        char[] invAug = augend.clone(); radixComplement(invAug);
        char[] addTo = Arrays.copyOfRange(product, product.length-augend.length, product.length);

        //Yep, that next line is confusing
        switch (Character.getNumericValue(terms.get(lastTermIndex)[cmpIndex])-cmpValue){
        case -1:
            addDiscardOverflow(augend, addTo);
            mostSignificantOverwrite(addTo, product);
            break;
        case 1:
            addDiscardOverflow(invAug, addTo);
            mostSignificantOverwrite(addTo, product);
            break;
        }

        singleRightShift(product[product.length-1], product);

        cmpValue = Character.getNumericValue(terms.get(lastTermIndex)[cmpIndex++]);

        currentDigit+=2;
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGSArithmetic#toXML()
     */
    @Override
    public String toXML() {
        if (!this.hasStep()){
            return super.toXML();
        }
        TwosComplementMultiplication clone = this.clone();
        char[] empty = new char[maxLength];
        for (int i = 0; i < maxLength; i++)
            empty[i] = ' ';
        clone.terms.set(clone.terms.size()-1, empty);
        clone.currentDigit = maxLength;
        return clone.toXML();
    }
}
