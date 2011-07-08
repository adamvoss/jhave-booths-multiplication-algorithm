package exe.boothsMultiplication;

import java.util.Arrays;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 * It would be much cleaner if all my private methods returned a value
 * rather than modified in-place.  That said, this works.
 */
public class TwosComplementMultiplication extends GAIGSArithmetic {

	protected int cmpIndex;
	protected int cmpValue;
	char[] product;
	
	/**
	 * @param op
	 * @param term1
	 * @param term2
	 * @param radix
	 * @param x0
	 * @param y0
	 * @param fontSize
	 * @param digitWidth
	 * @param color
	 */
	public TwosComplementMultiplication(String term1, String term2,
			double x0, double y0, double fontSize,
			double digitWidth, String color) {
		super('×', term1, term2, 2, x0, y0, fontSize, digitWidth, color);
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

	public TwosComplementMultiplication(String term1, String term2,
			double x0, double y0, double fontSize, String color) {
		this(term1, term2, x0, y0, fontSize, fontSize, color);
	}

	public TwosComplementMultiplication(String term1, String term2,
			double x0, double y0, double fontSize) {
		this(term1, term2, x0, y0, fontSize, COLOR);
	}

	public TwosComplementMultiplication(String term1, String term2,
			double x0, double y0) {
		this(term1, term2, x0, y0, FONT_SIZE);
	}

	public TwosComplementMultiplication(TwosComplementMultiplication source) {
		super(source);
	}

	@Override
	public TwosComplementMultiplication clone(){
		return new TwosComplementMultiplication(this);
	}

	private void singleRightShift(char fill, char[] array){
		//because we don't need to see -1
		for (int i = 0; i < array.length-1; i++ )
			array[i] = array[i+1];
		array[array.length-1] = fill;
	}
	
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
	
	private void addOne(char[] addend){
		char[] augend = new char[addend.length];
		for (int i = 1; i < augend.length; i++)
			augend[i] = '0';
		augend[0] = '1';
		
		addDiscardOverflow(augend, addend);
	}
	
	private void radixComplement(char[] array){
		bitComplement(array);
		addOne(array);
	}
	
	private void mostSignificantOverwrite(char[] source, char[] dest){
		int index = dest.length-source.length;
		for (char ch : source){
			dest[index]=ch;
			index++;				
		}
		
	}
	
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
