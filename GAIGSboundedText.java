/**
 * 
 */
package exe.boothsMultiplication;

import java.io.IOException;

import exe.ShowFile;

/**
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07
 */
public class GAIGSboundedText implements MutableGAIGSdatastr {
	GAIGSmonospacedText wrapped;
	
    public static final int HCENTER = 0;
    public static final int HLEFT = 1;
    public static final int HRIGHT = 2;
    
    public static final int VCENTER = 0;
    public static final int VBOTTOM = 1;
    public static final int VTOP = 2;

    public static final double MIN_CWIDTH_RATIO = 0.5;
    public static final double IDEAL_CWIDTH_RATIO = 0.6;
    
    
    //Can save repeated calculations by just storing this
    protected double[] bounds;
	
	public GAIGSboundedText(double x0, double y0, double x1, double y1,
			int halign, int valign, String color, String text){
		this.bounds = new double[] {x0, y0, x1, y1};
		wrapped = new GAIGSmonospacedText();
		wrapped.setText(text);
		wrapped.setBounds(x0, y0, x1, y1);
		wrapped.setColor(color);
		this.setBounds(bounds);
	}
	
	/* (non-Javadoc)
	 * @see exe.GAIGSdatastr#toXML()
	 */
	@Override
	public String toXML() {
		this.setBounds(bounds);
		return wrapped.toXML();
	}

	@Override
	public String getName() {
		return wrapped.getName();
	}

	@Override
	public void setName(String name) {
		wrapped.setName(name);
	}

	@Override
	public double[] getBounds() {
		return bounds;
	}

    /**
     * Gives the value stored to <code>halign</code>.
     *
     * @return The horizontal alignment constant for this
     *         <code>GAIGStext</code> object.
     */
    public int getHalign(){
    	return wrapped.halign;
    }

    /**
     * Gives the value stored to <code>valign</code>.
     *
     * @return The vertical alignment constant for this
     *         <code>GAIGStext</code> object.
     */
    public int getValign(){
    	return wrapped.valign;
    }
    
    public boolean setHalign(int halign){
    	boolean ret = wrapped.setHalign(halign);
//    	this.setBounds(this.bounds);
    	return ret;
    }
    
    public boolean setValign(int valign){
    	boolean ret = wrapped.setHalign(valign);
//    	this.setBounds(this.bounds);
    	return ret;
    }
	
	
	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x0, double y0, double x1, double y1) {
		this.bounds = new double[] {x0, y0, x1, y1};
		
		String[] lines = wrapped.text.split("\n");
		String[] words = wrapped.text.split(" ");
		int longest_word = 0;
		int longest_line = 0;

		for (String line : lines){
			if (line.length() > longest_line)
				longest_line = line.length();
		}
		
		for (String word : words){
			if (word.length() > longest_word)
				longest_word = word.length();
		}
		
		switch (wrapped.halign){
		case HCENTER:
			wrapped.x = (x0+x1)/2;
			break;
		case HLEFT:
			wrapped.x = x0;
			break;
		case HRIGHT:
			wrapped.x = x1;
			break;
		}
		
		switch (wrapped.valign){
		case VCENTER:
			wrapped.y = (y0+y1)/2;
			break;
		case VBOTTOM:
			wrapped.y = y0;
			break;
		case VTOP:
			wrapped.y = y1;
			break;
		}
		
		//Figure out the font size first
		
		double fs = x1-x0;
		if (fs * lines.length < y1-y0){
			
		}
		
		
		wrapped.setBounds(x0, y0, x1, y1);
	}

	private void setBounds(double[] bounds){
		this.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	
	/**
	 * Returns the font size in use.
	 */
	@Override
	public double getFontSize() {
		this.setBounds(bounds);
		return wrapped.getFontSize();
	}

	/**
	 * Does nothing
	 */
	@Override
	public void setFontSize(double fontSize) {}
	
	public GAIGSboundedText clone(){
		return null;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ShowFile show = new ShowFile(args[0] + ".sho");
		
		GAIGSboundedText text = new GAIGSboundedText(0, 0, 1, 1, HCENTER, VCENTER, "#000000", "Hello");
		
		show.writeSnap("Hi", text);
		
		show.close();
	}
	
}
