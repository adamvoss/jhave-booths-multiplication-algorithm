package exe.boothsMultiplication;

/**
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
//TODO this needs further refactoring because many of the inherited methods have no effect

//This should maybe instead be a Decorator of GAIGSline;
public class GAIGSarrow extends AbstractPrimitive {
	protected GAIGSline line;
	protected GAIGSpolygon head;
	protected double headSize;

	public GAIGSarrow(double x[], double y[], String color, String labelColor,
			String label, double headSize, double textHeight, int lineWidth){
		
		this.headSize = headSize;
		
		this.ocolor = color;
		this.lcolor = labelColor;
		this.label = label;
		this.fontSize = textHeight;
		this.lineWidth = lineWidth;
		
		this.line = new GAIGSline(x, y, color, labelColor, "", textHeight, lineWidth);
		this.head = makeArrowHead();
	}
	
	//This could be cleaned a lot, but not touching because it works
	//TODO clean this code.
	private GAIGSpolygon makeArrowHead(){
	    double [] x1 = {this.line.x[1], 0};
	    double [] y1 = {this.line.y[1], 0};
	    double [] x2 = {this.line.x[1], 0};
	    double [] y2 = {this.line.y[1], 0};

	    double theta = Math.atan((this.line.y[1] - this.line.y[0])/(this.line.x[1] - this.line.x[0]));
	    double end1 = theta + Math.toRadians(30);
	    double end2 = theta - Math.toRadians(30);

	    x1[1] = this.line.x[1] - headSize * Math.cos(end1);
	    x2[1] = this.line.x[1] - headSize * Math.cos(end2);
	    y1[1] = this.line.y[1] - headSize * Math.sin(end1);
	    y2[1] = this.line.y[1] - headSize * Math.sin(end2);

	    double [] xvals = {this.line.x[1], x1[1], x2[1]};
	    double [] yvals = {this.line.y[1], y1[1], y2[1]};

		return new GAIGSpolygon(3, xvals, yvals,
								this.ocolor, this.ocolor, this.lcolor,
								this.label, this.fontSize, this.lineWidth);
	}
	
	public GAIGSarrow(GAIGSarrow source){
		this.head = source.head.clone();
		this.line = source.line.clone();
		this.headSize = source.headSize;
		
		this.ocolor = source.ocolor;
		this.lcolor = source.lcolor;
		this.label = source.label;
		this.fontSize = source.fontSize;
		this.lineWidth = source.lineWidth;
	}
	
	public GAIGSarrow(double x[], double y[], String color, String labelColor,
			String label, double headSize){
		this(x, y, color, labelColor, label, headSize, TEXT_HEIGHT, LINE_WIDTH);
	}
	
	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return this.line.getBounds();
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		double newLen = Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1,2));
		double[] oldBds = this.line.getBounds();
		double oldLen = Math.sqrt(Math.pow(oldBds[2]-oldBds[0], 2)+Math.pow(oldBds[3]-oldBds[1],2));
		
		this.headSize*=(newLen/oldLen);
		
		this.line.setBounds(x1, y1, x2, y2);
		this.head = makeArrowHead();
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		return this.line.toCollectionXML() + this.head.toCollectionXML();
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#clone()
	 */
	@Override
	public AbstractPrimitive clone() {
		return new GAIGSarrow(this);
	}

}
