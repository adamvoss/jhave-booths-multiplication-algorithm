package exe.boothsMultiplication;

import exe.*;

class GAIGSarrayRegister implements GAIGSregister{
    private GAIGSarray wrapped;
    private int size;

    public GAIGSarrayRegister(GAIGSarray w, int len) {
        wrapped = w;
        size = len;
    }

    public GAIGSarrayRegister(int length) {
        wrapped = new GAIGSarray(1, length);
        size = length;
    }

    public GAIGSarrayRegister(int length, String name, String color,
                              double x1, double y1, double x2, double y2, double fontSize) {
        wrapped = new GAIGSarray(1, length, name, color, x1, y1, x2, y2, fontSize);
        size = length;
    }

    public String getName() {
        return wrapped.getName();
    }

    public void setName(String name) {
        wrapped.setName(name);
    }

    public String toXML() {
        return wrapped.toXML();
    }

    public int getSize() {return size;}

    public int getBit(int loc) {return ((Integer) wrapped.get(0, loc));}

    public void setBit(int value, int loc) {wrapped.set(new Integer(value), 0, loc);}

    public void setBit(int value, int loc, String cl) {wrapped.set(new Integer(value), 0, loc, cl);}

    public void setLabel(String label) {wrapped.setRowLabel(label, 0);}

    public String getLabel() {return wrapped.getRowLabel(0);}

    public void setColor(int loc, String cl) {wrapped.setColor(0, loc, cl);}

    public void setAllToColor(String cl) {
        for (int i = 0; i < getSize(); ++i)
            this.setColor(i, cl);
    }

    public void set(String binStr){
        //Empty String == 0
        if (binStr.isEmpty()){binStr="0";}

        //Expand string to register size
        if (binStr.length() < getSize() ){
            binStr = boothsMultiplication.signExtend(binStr, getSize()-binStr.length());
        }

        //If string too big, cut off most significant bits
        binStr = binStr.substring(binStr.length()-this.getSize());
        for (int count = (this.getSize()-1); count >= 0; count--){
            this.setBit(Character.getNumericValue(binStr.charAt(count)), count);
        }

    }

    public GAIGSregister copyTo(double x1, double y1, double x2, double y2,
 double fontSize) {
        GAIGSregister ret = new GAIGSarrayRegister(getSize(), "", boothsMultiplication.DEFAULT, x1, y1, x2, y2, fontSize);

       for (int i = 0; i < this.getSize(); ++i)
            ret.setBit(getBit(i), i);

        return ret;
    }

    public String toString() {
        String ret = "";

        for (int i = 0; i < getSize(); ++i) ret = ret + getBit(i);

        return ret;
    }
}

