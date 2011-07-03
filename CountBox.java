package exe.boothsMultiplication;

//THIS IS AN UGLY HACK
public class CountBox extends GAIGSprimitiveRegister {
    public CountBox(int count, String color, String fontColor, String outlineColor, double x1, double y1, double x2, double y2, double fontSize) {
        super(String.valueOf(count).length(), "", color, fontColor, outlineColor, x1, y1, x2, y2, fontSize);
        this.set(String.valueOf(count));
    }
    
    public CountBox(int count, String color, String fontColor, String outlineColor, double[] bounds, double fontSize) {
        this(count, color, fontColor, outlineColor, bounds[0], bounds[1], bounds[2], bounds[3], fontSize);
    }

    public CountBox(CountBox source) {
        super(source);
    }

    public void decrement() {
    	this.set(String.valueOf(getCount()-1));
    	}

    public int getCount()   {return new Integer(this.toString());}
    
    public CountBox clone() {
    	return new CountBox(this);
    }
    
    public void set(String binStr){
    	int[] ints = new int[binStr.length()];
    	
    	int i = 0;
    	for (char ch : binStr.toCharArray()){
    		ints[i] = Character.getNumericValue(ch);
    		i++;
    	}
    	
    	this.bits = ints;
    }
}
