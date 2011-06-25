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
	
	/* (non-Javadoc)
	 * @see exe.GAIGSdatastr#toXML()
	 */
	@Override
	abstract public String toXML();
	
	abstract protected String toCollectionXML();
	
	protected abstract String computeBounds();

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
