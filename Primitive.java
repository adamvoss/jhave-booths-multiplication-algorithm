/**
 * 
 */
package exe.boothsMultiplication;

import exe.MutableGAIGSdatastr;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public abstract class Primitive implements MutableGAIGSdatastr  {
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
	abstract public Primitive clone();
	
	@Override
	public String toXML() {
		return "<primitivecollection>\n\t<name>" + name +
		"</name>\n\t"+ this.computeBounds() + "\n\t" + 
		this.toCollectionXML() +"</primitivecollection>\n";
	}
	
	protected String computeBounds() {
		double[] bounds = this.getBounds();
		return "<bounds x1=\"" + bounds[0] + "\" y1=\"" + bounds[1] + "\" x2=\"" + bounds[2] + "\" y2=\"" + bounds[3] + "\"/>";
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
	//@Override
	public String getLabel() {
		return this.label;
	}
	//@Override
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Returns the value of the font size.
	 * @return  The current font size.
	 */
	public double getFontSize(){
		return this.fontSize;
	}

	/**
	 * Sets the font size for display.
	 * @param      fontSize     The desired font size.
	 */
	public void setFontSize(double fontSize){
		this.fontSize = fontSize;
	}
}
