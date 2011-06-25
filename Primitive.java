/**
 * 
 */
package exe.boothsMultiplication;

import exe.GAIGSdatastr;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public abstract class Primitive implements GAIGSdatastr {
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
	
	protected abstract String computeBounds();
	
	@Override
	public String toXML() {
		return "<primitivecollection>\n\t<name>" + name +
		"</name>\n\t"+ "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>" +"\n\t" + 
		this.toCollectionXML() +"</primitivecollection>\n";
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
}
