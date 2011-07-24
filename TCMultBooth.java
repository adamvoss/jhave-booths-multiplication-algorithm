package exe.boothsMultiplication;

/**
 * NOT A GENERAL USE CLASS.
 * @author Adam Voss <vossad01@luther.edu
 *
 */

public class TCMultBooth extends TwosComplementMultiplication {

    String result_color;
    
    public TCMultBooth(String term1, String term2, double x0, double y0,
            double fontSize, double digitWidth, String color, String result_color) {
        super(term1, term2, x0, y0, fontSize, digitWidth, color);
        this.result_color = result_color;
        // TODO Auto-generated constructor stub
    }
    
    public TCMultBooth(TCMultBooth source){
        super(source);
        this.result_color = source.result_color;
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGSArithmetic#toXML()
     */
    @Override
    public String toXML() {
        if (!this.hasStep()){
            String[] colors =  new String[maxLength];
            for (int i = 0; i < maxLength; i++)
                colors[i] = result_color;
            this.colors.set(this.colors.size()-1, colors);
            return super.toXML();
        }
        TCMultBooth clone = this.clone();
        char[] empty = new char[maxLength];
        for (int i = 0; i < maxLength; i++)
            empty[i] = ' ';
        clone.terms.set(clone.terms.size()-1, empty);
        clone.currentDigit = maxLength;
        return clone.toXML();
    }
    
    @Override
    public TCMultBooth clone(){
        return new TCMultBooth(this);
    }
}
