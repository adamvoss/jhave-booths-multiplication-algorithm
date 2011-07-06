package exe.boothsMultiplication;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public class GAIGSrectangle extends GAIGSpolygon {

	/**
	 * @param nSides
	 * @param ptsX
	 * @param ptsY
	 * @param fillColor
	 * @param outlineColor
	 * @param labelColor
	 * @param labelText
	 * @param textHeight
	 * @param lineWidth
	 */
	public GAIGSrectangle(double[] ptsX, double[] ptsY,
			String fillColor, String outlineColor, String labelColor,
			String labelText, double textHeight, int lineWidth) {
		super(4, ptsX, ptsY, fillColor, outlineColor, labelColor,
				labelText, textHeight, lineWidth);
	}

	/**
	 * @param nSides
	 * @param ptsX
	 * @param ptsY
	 * @param fillColor
	 * @param outlineColor
	 * @param labelColor
	 * @param labelText
	 */
	public GAIGSrectangle(double[] ptsX, double[] ptsY,
			String fillColor, String outlineColor, String labelColor,
			String labelText) {
		super(4, ptsX, ptsY, fillColor, outlineColor, labelColor,
				labelText);
	}

	/**
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param fillColor
	 * @param outlineColor
	 * @param labelColor
	 * @param labelText
	 * @param textHeight
	 * @param lineWidth
	 */
	public GAIGSrectangle(double x0, double y0, double x1, double y1,
			String fillColor, String outlineColor, String labelColor,
			String labelText, double textHeight, int lineWidth) {
		super(4, new double[] {x0,x1,x1,x0}, new double[] {y0,y0,y1,y1}, fillColor, outlineColor, labelColor,
				labelText, textHeight, lineWidth);
	}
	
	/**
	 * @param source
	 */
	public GAIGSrectangle(GAIGSrectangle source) {
		super(source);
	}

    @Override //I certainly wish there were a way to make this generic in AbstractPolygon
    public GAIGSrectangle clone() {
    	return new GAIGSrectangle(this);
    }
}
