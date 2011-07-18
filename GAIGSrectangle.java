package exe.boothsMultiplication;

/**
 * GAIGSrectangle provides an easy way to draw rectangles in GAIGS XML.
 * It has the advantage of taking fewer and more intuitive parameters than
 * direct use of GAIGSpolygon.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-15
 */

public class GAIGSrectangle extends GAIGSpolygon {
    /**
     * Creates a GAIGSrectangle with the given parameters.
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     * @param fillColor The internal color of the polygon (use an empty string for no fill color).
     * @param outlineColor The outline color of the polygon.
     * @param labelColor The color of the text in the polygon.
     * @param labelText The text to be drawn in the center of the polygon.
     * @param textHeight The height of the text in the label.
     * @param lineWidth The thickness of the outline of the polygon.
     */
    public GAIGSrectangle(double x0, double y0, double x1, double y1,
            String fillColor, String outlineColor, String labelColor,
            String labelText, double textHeight, int lineWidth) {
        super(4, new double[] {x0,x1,x1,x0}, new double[] {y0,y0,y1,y1}, fillColor, outlineColor, labelColor,
                labelText, textHeight, lineWidth);
    }

    /**
     * Uses the default textHeight and lineWidth.
     * @param x0 lower left-hand corner's x-coordinate.
     * @param y0 lower left-hand corner's y-coordinate.
     * @param x1 upper right-hand corner's x-coordinate.
     * @param y1 upper right-hand corner's y-coordinate.
     * @param fillColor The internal color of the polygon (use an empty string for no fill color).
     * @param outlineColor The outline color of the polygon.
     * @param labelColor The color of the text in the polygon.
     * @param labelText The text to be drawn in the center of the polygon.
     */
    public GAIGSrectangle(double x0, double y0, double x1, double y1,
            String fillColor, String outlineColor, String labelColor,
            String labelText) {
        this(x0, y0, x1, y1, fillColor, outlineColor, labelColor,
                labelText, TEXT_HEIGHT, LINE_WIDTH);
    }
    
    /**
     * Creates a rectangle with no fill and a black outline.
     * Useful for drawing a box around a data structure.
     * @param bounds The coordinates of the rectangle
     */
    public GAIGSrectangle(double[] bounds){
        this(bounds[0], bounds[1], bounds[2], bounds[3], "", "#000000", "#000000", "");
    }

    /**
     * A deep-copy constructor
     * @param The GAIGSrectangle to be copied
     */
    public GAIGSrectangle(GAIGSrectangle source) {
        super(source);
    }

    @Override //I certainly wish there were a way to make this generic in AbstractPolygon
    public GAIGSrectangle clone() {
        return new GAIGSrectangle(this);
    }
}
