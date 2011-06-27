package exe.boothsMultiplication;

public class CountBox extends GAIGSprimitiveRegister {
    public CountBox(int count, String color, double x1, double y1, double x2, double y2, double fontSize) {
        super(1, "", color, x1, y1, x2, y2, fontSize);
        this.setBit(count, 0);
    }

    public CountBox(int count, String color, Bounds bounds, double fontSize) {
        this(count, color, bounds.x1, bounds.y1, bounds.x2, bounds.y2, fontSize);
    }

    public CountBox(CountBox source) {
        super(source);
    }

    public void decrement() {this.setBit(this.getBit(0)-1, 0);}

    public int getCount()   {return this.getBit(0);}

    public void setColor(String color) {this.setColor(0, color);}

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
}
