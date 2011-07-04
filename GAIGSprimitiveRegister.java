package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;


/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
//TODO Add a working label functionality.
//TODO Grab documentation from the interface that can be found in history
public class GAIGSprimitiveRegister implements MutableGAIGSdatastr {
	protected int[] bits;
	private String[] colors;
	private GAIGSpolygon wrapped;


	public GAIGSprimitiveRegister(GAIGSprimitiveRegister source){
		this.bits = source.bits.clone();
		this.colors = source.colors.clone();
		this.wrapped = new GAIGSpolygon(source.wrapped);
	}

	public GAIGSprimitiveRegister(int length, String name, String color,
			String fontColor, String outlineColor, double x1, double y1,
			double x2, double y2, double fontSize) {
		this.bits = new int[length];
		this.colors = new String[length];

		length--;
		while (length >= 0) {
			this.bits[length] = 0;
			this.colors[length] = "\\" + DEFAULT_COLOR;
			length--;
		}

		int line_width = 1;
		
		wrapped = new GAIGSpolygon(4, new double[] {x1, x2, x2, x1}, new double[] {y1, y1, y2, y2},
				color, fontColor, outlineColor, name, fontSize, line_width);
	}
	
	public GAIGSprimitiveRegister(int length, String name, String color,
			String fontColor, String outlineColor, double[] bounds, double fontSize) {
		this(length, name, color, fontColor, outlineColor , bounds[0], bounds[1], bounds[2], bounds[3], fontSize);
	}


	/**
	 * @see exe.boothsMultiplication.GAIGSregister#getName()
	 */
	 @Override
	 public String getName() {
		 return wrapped.getName();
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setName(java.lang.String)
	  */
	 @Override
	 public void setName(String name) {
		 wrapped.setName(name);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#toXML()
	  */
	 @Override
	 public String toXML() {
		 String label = "";
		 for (int loc = 0 ; loc < bits.length; loc++){
			 label += "\\" + colors[loc] + bits[loc];
		 }
		 wrapped.setLabel(label);
		 return wrapped.toXML();
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getSize()
	  */
	 public int getSize() {
		 return bits.length;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getBit(int)
	  */
	 public int getBit(int loc) {
		 return bits[loc];
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int)
	  */
	 public void setBit(int value, int loc) {
		 bits[loc] = value;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int, java.lang.String)
	  */
	 public void setBit(int value, int loc, String color) {
		 bits[loc] = value;
		 this.colors[loc] = color;
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#setColor(int, java.lang.String)
	  */
	 public void setBitColor(int loc, String color) {
		 this.colors[loc] = color;
	 }

	 public void setColor(String color) {
		 this.wrapped.setColor(color);
	 }
	 
	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#setAllToColor(java.lang.String)
	  */
	 public void setAllToColor(String color) {
		 for (int loc = 0 ; loc < bits.length; loc++){
			 this.colors[loc]=color;
		 }
	 }

     public void setOutlineColor(String color) {
         wrapped.setOutlineColor(color);
     }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#set(java.lang.String)
	  */
	 //TODO Move Sign Extend out of BoothsMultiplication
	 public void set(String binStr) {
		 //Empty String == 0
		 if (binStr.isEmpty()){binStr="0";}

		 //Expand string to register size
		 if (binStr.length() < getSize() ){
			 binStr = BoothsMultiplication.signExtend(binStr, getSize()-binStr.length());
		 }

		 //If string too big, cut off most significant bits
		 binStr = binStr.substring(binStr.length()-this.getSize());
		 for (int count = (this.getSize()-1); count >= 0; count--){
			 this.setBit(Character.getNumericValue(binStr.charAt(count)), count);
		 }
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#toIntArray()
	  */
	 public int[] toIntArray() {
		 return bits;
	 }

    /**
    * Returns a string representation of the bits in the register.
    * 
    */
    @Override
    public String toString() {
        String ret = "";

        for (int i = 0; i < bits.length; ++i)
            ret = ret + bits[i];

        return ret;
    }

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return wrapped.getBounds();
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		this.wrapped.setBounds(x1, y1, x2, y2);
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getFontSize()
	 */
	@Override
	public double getFontSize() {
		return wrapped.getFontSize();
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setFontSize(double)
	 */
	@Override
	public void setFontSize(double fontSize) {
		wrapped.setFontSize(fontSize);
	}

	@Override
	public GAIGSprimitiveRegister clone(){
		return new GAIGSprimitiveRegister(this);
	}
}
