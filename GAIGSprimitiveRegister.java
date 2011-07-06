package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;


/**
 * @author Adam Voss <vossad01@luther.edu>
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 */
//TODO Grab documentation from the interface that can be found in history
public class GAIGSprimitiveRegister implements MutableGAIGSdatastr {
	private GAIGSpane<GAIGSrectangle> wrapped;


	public GAIGSprimitiveRegister(GAIGSprimitiveRegister source){
		this.wrapped = new GAIGSpane<GAIGSrectangle>(source.wrapped);
	}

	public GAIGSprimitiveRegister(int length, String name, String fillColor,
			String fontColor, String outlineColor, double x0, double y0,
			double x1, double y1, double fontSize) {

		int line_width = 1;
		
		wrapped = new GAIGSpane<GAIGSrectangle>();
		wrapped.setName("GAIGSregister");

		double width = (x1-x0)/length;
		
		length--;
		while (length >= 0){
			wrapped.add(new GAIGSrectangle(x1-width*(length+1), y0, x1-width*length, y1,
						fillColor, fillColor, fontColor,
						"0", fontSize, line_width));
			length--;
		}
		
		//The outline rectangle, here last so it is always on top.
		wrapped.add(new GAIGSrectangle(x0, y0, x1, y1,
				"", fontColor, outlineColor,
				"", fontSize, line_width));
	}
	
	public GAIGSprimitiveRegister(int length, String name, String fillColor,
			String fontColor, String outlineColor, double[] bounds, double fontSize) {
		this(length, name, fillColor, fontColor, outlineColor , bounds[0], bounds[1], bounds[2], bounds[3], fontSize);
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
		 return wrapped.toXML();
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getSize()
	  */
	 public int getSize() {
		 return wrapped.size()-1;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getBit(int)
	  */
	 public int getBit(int loc) {
		 return Integer.valueOf(wrapped.get(loc).getLabel());
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int)
	  */
	 public void setBit(int value, int loc) {
		 this.wrapped.get(loc).setLabel("" + value);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int, java.lang.String)
	  */
	 @Deprecated
	 public void setBit(int value, int loc, String color) {
		 setBit(value, loc);
		 setBitColor(loc, color);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setColor(int, java.lang.String)
	  */
	 public void setBitColor(int loc, String color) {
		 this.wrapped.get(loc).setLabelColor(color);
	 }

	 public void setColor(String color) {
		 for (int loc = this.getSize()-1; loc >= 0; loc--){
			 wrapped.get(loc).setColor(color);
		 }
	 }
	 
	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setAllToColor(java.lang.String)
	  */
	 public void setAllToColor(String color) {
		 for (int loc = this.getSize()-1; loc >= 0; loc--){
			 wrapped.get(loc).setLabelColor(color);
		 }
	 }

	 /**
	  * Sets the outline color of the perimeter of the register.
	  * @param color
	  */
     public void setOutlineColor(String color) {
    	 //The outline is always the last item on the pane
         wrapped.get(this.getSize()-1).setOutlineColor(color);
     }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#set(java.lang.String)
	  */
	 public void set(String binStr) {
		 //Empty String == 0
		 if (binStr.isEmpty()){binStr="0";}

		 //Expand string to register size
		 if (binStr.length() < getSize() ){
			 binStr = signExtend(binStr, getSize()-binStr.length());
		 }

		 //If string too big, cut off most significant bits
		 binStr = binStr.substring(binStr.length()-this.getSize());
		 for (int count = (this.getSize()-1); count >= 0; count--){
			 this.setBit(Character.getNumericValue(binStr.charAt(count)), count);
		 }
	 }

	 /**
	  * Sign extends a binary number by i bits
	  * @param binStr The binary number represented as a string
	  * @param i The number of bits to extend
	  * @return
	  */
	 public String signExtend(String binStr, int i){
		 String firstBit = String.valueOf(binStr.charAt(0));
		 String extension = "";
		 while (i>0){extension = extension.concat(firstBit); i--;}
		 return extension.concat(binStr);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#toIntArray()
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
    * 
    */
    @Override
    public String toString() {
        String ret = "";
        
        int[] bits = this.toIntArray();

        for (int i = 0; i < bits.length; ++i)
            ret = ret + bits[i];

        return ret;
    }

	/**
	 * @see exe.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return wrapped.getBounds();
	}

	/**
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		this.wrapped.setBounds(x1, y1, x2, y2);
	}

	/**
	 * @see exe.MutableGAIGSdatastr#getFontSize()
	 */
	@Override
	public double getFontSize() {
		return wrapped.getFontSize();
	}

	/**
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
