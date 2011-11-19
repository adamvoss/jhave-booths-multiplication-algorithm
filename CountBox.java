package exe.boothsMultiplication;

import exe.boothsMultiplication.GAIGSbigEdianRegister;

//THIS IS A VERY UGLY HACK
//This Should Never Have Used Inheritance Like This
//I Have Lost So Much Time To Maintaining This
/**
 * THIS IS NOT A GENERAL USE CLASS.
 * It was designed specifically for use in the
 * Booths Multiplication Algorithm Visualization.
 * 
 * It is a bad example of inheritance. 
 * 
 * @author Chris Jenkins <cjenkin1@trinity.edu>
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-15
 */
public class CountBox extends GAIGSbigEdianRegister {
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

    @Override
    public CountBox clone() {
        return new CountBox(this);
    }

    /**
     * This exists to have set always pad with 0s.
     * It is untested
     */
    @Override
    public String signExtend(String binStr, int i){
        String firstBit = "0";
        String extension = "";
        while (i>0){extension = extension.concat(firstBit); i--;}
        return extension.concat(binStr);
    }
}
