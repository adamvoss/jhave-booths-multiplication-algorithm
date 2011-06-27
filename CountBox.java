package exe.boothsMultiplication;

import exe.*;

//Hack to make trace work without refactoring
public class CountBox extends GAIGSprimitiveRegister{
    GAIGSarray wrapped;

    public CountBox(int count, String color, double x1, double y1, double x2, double y2, double fontSize) {
        wrapped = new GAIGSarray(1, 1, "", color, x1, y1, x2, y2, fontSize);
        wrapped.set(new Integer(count), 0, 0);
    }

    public CountBox(int count, String color, Bounds bounds, double fontSize) {this(count, color, bounds.x1, bounds.y1, bounds.x2, bounds.y2, fontSize);}

    private CountBox(GAIGSarray newWrap) {
        wrapped = newWrap;
    }

    public void decrement() {wrapped.set( ((Integer)wrapped.get(0, 0))-1, 0, 0);}

    public int getCount()   {return (Integer)wrapped.get(0, 0);}

    public void setLabel(String label) {wrapped.setRowLabel(label, 0);}

    public void setColor(String color) {wrapped.setColor(0, 0, color);}

    public CountBox copyTo(Bounds bounds) {
        CountBox ret = new CountBox(new GAIGSarray(this.wrapped) );
        ret.wrapped.setBounds(bounds.x1, bounds.y1, bounds.x2, bounds.y2);

        return ret;
    }

    public String toXML() {return wrapped.toXML();}
 
}
