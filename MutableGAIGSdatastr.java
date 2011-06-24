package exe;

/**
 * 
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */

/* Not sure if Cloneable is a good or bad idea because this doesn't follow
 * Cloneable convention due to backwards compatibility reasons.  We don't 
 * gain anything needed by its use, so it was omitted.  No harm is expected
 * if this decision changes later.
 */ 

//TODO Document this
public interface MutableGAIGSdatastr extends GAIGSdatastr {
	//TODO rethink the difference of parameters here.
	public double[] getBounds();
	public void setBounds(double x1, double y1, double x2, double y2);
	
	//Note there is no storage requirement for the font size 
	public double getFontSize();
	public void setFontSize(double fontSize);
	public MutableGAIGSdatastr clone();
	//Color was not included because many classes would ignore it.
	//TODO consider: scale, resize, move.
}
