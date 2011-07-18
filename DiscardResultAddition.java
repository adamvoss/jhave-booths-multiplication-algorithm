package exe.boothsMultiplication;

public class DiscardResultAddition extends ColoredResultArithmetic{

    public DiscardResultAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, double digitWidth,
            String color, String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, digitWidth, color, resultColor);
        // TODO Auto-generated constructor stub
    }

    public DiscardResultAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, String color,
            String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, color, resultColor);
        // TODO Auto-generated constructor stub
    }

    public DiscardResultAddition(char op, String term1, String term2,
            int radix, double x, double y, double fontSize, String resultColor) {
        super(op, term1, term2, radix, x, y, fontSize, resultColor);
        // TODO Auto-generated constructor stub
    }

    public DiscardResultAddition(char op, String term1, String term2,
            int radix, double x, double y, String resultColor) {
        super(op, term1, term2, radix, x, y, resultColor);
        // TODO Auto-generated constructor stub
    }

    public DiscardResultAddition(ColoredResultArithmetic source) {
        super(source);
        // TODO Auto-generated constructor stub
    }

}
