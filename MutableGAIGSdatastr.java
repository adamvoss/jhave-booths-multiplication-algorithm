package exe.boothsMultiplication;

import exe.GAIGSdatastr;

/**
 * 
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */

/* Mutable is purposely not implemented.  Then Object's clone would be callable,
 * meaning implementers of this interface my not implement their own clone. 
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
