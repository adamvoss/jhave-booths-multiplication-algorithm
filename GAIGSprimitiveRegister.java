
package exe.boothsMultiplication;


/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public class GAIGSprimitiveRegister implements GAIGSregister {
	private int[] bit;
	private String[] color;
	private GAIGSpolygon wrapped;


	public GAIGSprimitiveRegister(GAIGSprimitiveRegister source){
		this.bit = source.bit.clone();
		this.color = source.color.clone();
		this.wrapped = new GAIGSpolygon(source.wrapped);
	}

	public GAIGSprimitiveRegister(int length, String name, String color,
			double x1, double y1, double x2, double y2, double fontSize) {
		this.bit = new int[length];
		this.color = new String[length];
		
		length--;
		while (length >= 0){
			this.bit[length]=0;
			this.color[length]="\\" + DEFAULT_COLOR;
			length--;
		}
		
		int line_width = 1;
		
		wrapped = new GAIGSpolygon(4, new double[] {x1, x2, x2, x1}, new double[] {y1, y1, y2, y2},
				"#FF0000", color, DEFAULT_COLOR, name, fontSize, line_width);
	}	

	public GAIGSprimitiveRegister(int length, String name, String color,
			Bounds b, double fontSize) {
		this(length, name, color, b.x1, b.y1, b.x2, b.y2, fontSize);
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
		 for (int loc = 0 ; loc < bit.length; loc++){
			 label += "\\" + color[loc] + bit[loc];
		 }
		 wrapped.setLabel(label);
		 return wrapped.toXML();
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getSize()
	  */
	 @Override
	 public int getSize() {
		 return bit.length;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#getBit(int)
	  */
	 @Override
	 public int getBit(int loc) {
		 return bit[loc];
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int)
	  */
	 @Override
	 public void setBit(int value, int loc) {
		 bit[loc] = value;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int, java.lang.String)
	  */
	 @Override
	 public void setBit(int value, int loc, String color) {
		 bit[loc] = value;
		 this.color[loc] = color;
	 }

	 /**
	  * @see exe.boothsMultiplication.GAIGSregister#setLabel(java.lang.String)
	  */
	 @Override
	 public void setLabel(String label) {
		 wrapped.setName(label);
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#getLabel()
	  */
	 @Override
	 public String getLabel() {
		 return wrapped.getName();
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#setColor(int, java.lang.String)
	  */
	 @Override
	 public void setColor(int loc, String color) {
		 this.color[loc] = color;
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#setAllToColor(java.lang.String)
	  */
	 @Override
	 public void setAllToColor(String color) {
		 for (int loc = 0 ; loc < bit.length; loc++){
			 this.color[loc]=color;
		 }
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#set(java.lang.String)
	  */
	 @Override
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
	  * @see exe.boothsMultiplication.GAIGSregister#copyTo(exe.boothsMultiplication.Bounds)
	  */
	 @Override
	 public GAIGSregister copyTo(Bounds bounds) {
		 GAIGSprimitiveRegister ret = new GAIGSprimitiveRegister(this);
		 //TODO finish refactoring primitives so this doesn't work
		 ret.wrapped.ptsX = new double[] {bounds.x1, bounds.x2, bounds.x2, bounds.x1};
		 ret.wrapped.ptsY = new double[] {bounds.y1, bounds.y1, bounds.y2, bounds.y2};
		 return ret;
	 }

	 /* (non-Javadoc)
	  * @see exe.boothsMultiplication.GAIGSregister#toIntArray()
	  */
	 @Override
	 public int[] toIntArray() {
		 return bit;
	 }

}
