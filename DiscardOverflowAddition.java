package exe.boothsMultiplication;

/**
 * NOT A GENERAL USE CLASS.
 * @author Adam Voss <vossad01@luther.edu
 *
 */

public class DiscardOverflowAddition extends ColoredResultArithmetic{

    private String over_flow_color = "#FFFFFF";
    private String carry_color = "#FF0000";
    
    public DiscardOverflowAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, double digitWidth,
            String color, String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, digitWidth, color, resultColor);
    }

    public DiscardOverflowAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, String color,
            String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, color, resultColor);
    }

    public DiscardOverflowAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, resultColor);
    }

    public DiscardOverflowAddition(char op, String term1, String term2,
            int radix, double x, double y, String resultColor) {
        super(op, term1, term2, radix, x, y, resultColor);
    }

    public DiscardOverflowAddition(ColoredResultArithmetic source) {
        super(source);
    }

    /* (non-Javadoc)
     * @see exe.boothsMultiplication.GAIGSArithmetic#toXML()
     */
    @Override
    public String toXML() {
        this.colors.get(0)[this.terms.get(0).length-1]=carry_color;
        this.terms.get(this.terms.size()-1)[this.terms.get(this.terms.size()-1).length-1]=' ';
        
        return super.toXML();
    }
    
    @Override
    public DiscardOverflowAddition clone(){
        return new DiscardOverflowAddition(this);
    }
}
