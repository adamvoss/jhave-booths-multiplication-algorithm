/**
 * 
 */
package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public abstract class AbstractPrimitive implements MutableGAIGSdatastr  {
	protected String label;
	final static double TEXT_HEIGHT = .03;
	final static int LINE_WIDTH = 12;
	protected String fcolor;
	protected String ocolor;
	protected String lcolor;
	protected String name = "";
	protected double fontSize;
	protected int lineWidth;
	
	abstract protected String toCollectionXML();
	abstract public AbstractPrimitive clone();
	
	@Override
	public String toXML() {
		return "<primitivecollection>\n\t<name>" + name +
		"</name>\n\t"+ this.computeBounds() + "\n\t" + 
		this.toCollectionXML() +"</primitivecollection>\n";
	}

	
	/* Due to a client side bug/feature the coordinates of cannot be the coordinates of the bounds.
	 * Instead the coordinates given are drawn relative to the bounds given, wherein a unit square
	 * primitive perfectly fills bounds on screen.
	 */
//	protected String computeBounds() {
//	double[] bounds = this.getBounds();
//	return "<bounds x1=\"" + bounds[0] + "\" y1=\"" + bounds[1] + "\" x2=\"" + bounds[2] + "\" y2=\"" + bounds[3] + "\"/>";
//}
	protected String computeBounds() {
		return "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>";
	}

	/* (non-Javadoc)
	 * @see exe.GAIGSdatastr#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	public String getColor() {
		return fcolor;
	}
	public void setColor(String fcolor) {
		this.fcolor = fcolor;
	}
	public String getOutlineColor() {
		return ocolor;
	}
	public void setOutlineColor(String ocolor) {
		this.ocolor = ocolor;
	}
	public String getLabelColor() {
		return lcolor;
	}
	public void setLabelColor(String lcolor) {
		this.lcolor = lcolor;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Returns the value of the font size.
	 * @return  The current font size.
	 */
	@Override
	public double getFontSize(){
		return this.fontSize;
	}

	/**
	 * Sets the font size for display.
	 * @param      fontSize     The desired font size.
	 */
	@Override
	public void setFontSize(double fontSize){
		this.fontSize = fontSize;
	}
}
