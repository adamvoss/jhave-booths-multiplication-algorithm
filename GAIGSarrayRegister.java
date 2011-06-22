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

    public GAIGSarrayRegister(int length, String name, String color,
                              Bounds b, double fontSize) {
        this(length, name, color, b.x1, b.y1, b.x2, b.y2, fontSize);
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
            binStr = BoothsMultiplication.signExtend(binStr, getSize()-binStr.length());
        }

        //If string too big, cut off most significant bits
        binStr = binStr.substring(binStr.length()-this.getSize());
        for (int count = (this.getSize()-1); count >= 0; count--){
            this.setBit(Character.getNumericValue(binStr.charAt(count)), count);
        }

    }

    public GAIGSregister copyTo(Bounds bounds) {
        GAIGSregister ret = new GAIGSarrayRegister(new GAIGSarray(this.wrapped), this.size);
        ((GAIGSarrayRegister)ret).wrapped.setBounds(bounds.x1, bounds.y1, bounds.x2, bounds.y2);

        return ret;
    }

    public String toString() {
        String ret = "";

        for (int i = 0; i < getSize(); ++i) ret = ret + getBit(i);

        return ret;
    }

    public int[] toIntArray() {
        int[] ret = new int[this.getSize()];

        for (int i = 0; i < ret.length; ++i) {
            ret[i] = this.getBit(i);
        }

        return ret;
    }
}

