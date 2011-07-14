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
	 public void setBit(int loc, int value) {
		 this.wrapped.get(loc).setLabel("" + value);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setColor(int, java.lang.String)
	  */
	 public void setTextColor(int loc, String color) {
		 this.wrapped.get(loc).setLabelColor(color);
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setTextColor(java.lang.String)
	  */
	 public void setTextColor(String color) {
		 for (int loc = this.getSize()-1; loc >= 0; loc--){
			 wrapped.get(loc).setLabelColor(color);
		 }
	 }
	 
	 public void setFillColor(int loc, String color) {
		 this.wrapped.get(loc).setColor(color);
	 }
	 
	 public void setFillColor(String color) {
		 for (int loc = this.getSize()-1; loc >= 0; loc--){
			 wrapped.get(loc).setColor(color);
		 }
	 }

	 public void setOutlineColor(int loc, String color) {
		 this.wrapped.get(loc).setOutlineColor(color);
	 }
	 
	 /**
	  * Sets the outline color of the perimeter of the register.
	  * @param color
	  */
     public void setOutlineColor(String color) {
    	 //The outline is always the last item on the pane
         wrapped.get(wrapped.size()-1).setOutlineColor(color);
     }

	 public void setFillOutlineColor(int loc, String color) {
		 wrapped.get(loc).setColor(color);
		 wrapped.get(loc).setOutlineColor(color);
	 }
     
	 public void setFillOutlineColor(String color) {
		 for (int loc = this.getSize()-1; loc >= 0; loc--){
			 wrapped.get(loc).setColor(color);
			 wrapped.get(loc).setOutlineColor(color);
		 }
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
		 for (int count = 0; count < this.getSize(); count++){
			 this.setBit(count, Character.getNumericValue(binStr.charAt(binStr.length()-1-count)));
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
            ret = bits[i] + ret;

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
