package exe.boothsMultiplication;

//THIS IS AN UGLY HACK
public class CountBox extends GAIGSprimitiveRegister {
    public CountBox(int count, String color, String fontColor, String outlineColor, double x1, double y1, double x2, double y2, double fontSize) {
        super(String.valueOf(count).length(), "", color, fontColor, outlineColor, x1, y1, x2, y2, fontSize);
        this.set(String.valueOf(count));
    }

    public CountBox(int count, String color, String fontColor, String outlineColor, Bounds bounds, double fontSize) {
        this(count, color, fontColor, outlineColor, bounds.x1, bounds.y1, bounds.x2, bounds.y2, fontSize);
    }

    public CountBox(CountBox source) {
        super(source);
    }

    public void decrement() {
    	System.out.println(getCount());
    	this.set(String.valueOf(getCount()-1));
    	}

    public int getCount()   {return new Integer(this.toString());}

    @Override
    //I don't think you would have needed to overwrite this had you overridden clone.
    public CountBox copyTo(Bounds bounds) {
        CountBox ret = new CountBox(this);
        ret.setBounds(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
        return ret;
    }
    
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
