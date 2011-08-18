package exe.boothsMultiplication;

import exe.boothsMultiplication.GAIGSArithmetic;

/**
 * NOT A GENERAL USE CLASS.
 * @author Adam Voss <vossad01@luther.edu
 *
 */

public class ColoredResultArithmetic extends GAIGSArithmetic {

    private String result_color;
    
    public ColoredResultArithmetic(char op, String term1, String term2, int radix,
            double x, double y, double fontSize, double digitWidth, String color, String result_color) {
        super(op, term1, term2, radix, x, y, fontSize, digitWidth, color);
        this.result_color = result_color;
    }

    public ColoredResultArithmetic(char op, String term1, String term2, int radix,
            double x, double y, double fontSize, String color, String result_color) {
        super(op, term1, term2, radix, x, y, fontSize, color);
        this.result_color = result_color;
    }

    public ColoredResultArithmetic(char op, String term1, String term2, int radix,
            double x, double y, double fontSize, String result_color) {
        super(op, term1, term2, radix, x, y, fontSize);
        this.result_color = result_color;
    }

    public ColoredResultArithmetic(char op, String term1, String term2, int radix,
            double x, double y, String result_color) {
        super(op, term1, term2, radix, x, y);
        this.result_color = result_color;
    }

    public ColoredResultArithmetic(ColoredResultArithmetic source) {
        super(source);
        this.result_color = source.result_color;
    }
    
    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGSArithmetic#toXML()
     */
    @Override
    public String toXML() {
        String[] colors =  new String[this.terms.get(this.colors.size()-1).length];
        for (int i = 0; i < colors.length; i++)
            colors[i] = result_color;
        this.colors.set(this.colors.size()-1, colors);
        return super.toXML();
    }
    
    @Override
    public ColoredResultArithmetic clone(){
        return new ColoredResultArithmetic(this);
    }
}
