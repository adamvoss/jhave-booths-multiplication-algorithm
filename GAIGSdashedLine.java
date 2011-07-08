package exe.boothsMultiplication;

/**
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 * 
 * This is not XML equivalent to the GAIGSprimitiveCollection.addDashedLine from before
 * refactoring, only because the old code had serious bugs which are here fixed.
 * 
 * This class still contains bugs; it is unused so has been deprecated;  Its use is not
 * recommended unless you first fix bugs.  Problems exist when using this GAIGSdashedLine
 * with-in a pane in that the wrong coordinates will be used.  It is recommended setBounds
 * be changed to resize existing lines so that the number or line segments remains constant.
 */
//TODO this needs further refactoring because many of the inherited methods have no effect
@Deprecated
public class GAIGSdashedLine extends AbstractPrimitive {
	private double dashSize;
	public double x[] = new double[2];
	public double y[] = new double[2];
	
	GAIGSpane<GAIGSline> lines = new GAIGSpane<GAIGSline>();

	public GAIGSdashedLine(double x[], double y[], String color, String labelColor,
			String label, double dashSize, double textHeight, int lineWidth){
		for(int i=0;i<2;++i)
		{
			this.x[i]=x[i];
			this.y[i]=y[i];
		}
		
		this.dashSize = dashSize;
		this.ocolor   = color;
		this.lcolor   = labelColor;
		this.fontSize = textHeight;
		this.lineWidth= lineWidth;
		this.label    = label;
		
		this.lines = initLines();
	}
	
	public GAIGSdashedLine(double x[], double y[], String color, String labelColor,
			String label, double dashSize){
		this(y, y, color, labelColor, label, dashSize, TEXT_HEIGHT, LINE_WIDTH);
	}
	
	public GAIGSdashedLine(GAIGSdashedLine source){
		this.lines=source.lines.clone();
		
		this.x = source.x.clone();
		this.y = source.y.clone();
		this.dashSize = source.dashSize;
		
		this.fcolor = source.fcolor;
		this.ocolor = source.ocolor;
		this.lcolor = source.lcolor;
		this.label = source.label;
		this.fontSize = source.fontSize;
		this.lineWidth = source.lineWidth;
		this.name = source.name;
	}
	
	//This was used instead of resizing existing lines so that dashSize is constant
	//The logic for calculating dashes could be improved.
	private GAIGSpane<GAIGSline> initLines() {
		GAIGSpane<GAIGSline> lines = new GAIGSpane<GAIGSline>();
		
		double dist = Math.sqrt((x[1] - x[0])*(x[1] - x[0]) + (y[1] - y[0])*(y[1] - y[0]));
		int numLines = ((int)Math.ceil(dist / dashSize))/2;

		if (numLines == 0) System.err.println("You need a smaller dashSize in GAIGSdashedLine");

		double [] xvals = new double[2];
		double [] yvals = new double[2];

		for(int i = 0; i < 2*numLines; i+=2) {
			xvals[0] = x[0] + (i / (2*(double)numLines)) * x[1];
			yvals[0] = y[0] + (i / (2*(double)numLines)) * y[1];
			xvals[1] = x[0] + ((i+1) / (2*(double)numLines)) * x[1];
			yvals[1] = y[0] + ((i+1) / (2*(double)numLines)) * y[1];
			lines.add(new GAIGSline(xvals, yvals,
					  this.ocolor, this.lcolor,
					  (i == numLines|| i==numLines-1 ? label : ""), //That is messy, but at least it now always draws the label
					  this.fontSize, this.lineWidth));
		}
		return lines;
	}
	
	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		double x1 = Double.POSITIVE_INFINITY;
		double y1 = Double.POSITIVE_INFINITY;
		double x2 = Double.NEGATIVE_INFINITY;
		double y2 = Double.NEGATIVE_INFINITY;

		for (GAIGSline line : this.lines){
			double[] bounds = line.getBounds();
			
			for(int j = 0; j < 3; j+=2) {
				x1 = (x1 < bounds[j]    ? x1 : bounds[j]);
				y1 = (y1 < bounds[j+1]  ? y1 : bounds[j+1]);
				x2 = (x2 > bounds[j]    ? x2 : bounds[j]);
				y2 = (y2 > bounds[j+1]  ? y2 : bounds[j+1]);
			}
		}

		return new double[] {x1, y1, x2, y2};
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		x[0] = x1;
		y[0] = y1;
		x[1] = x2;
		y[1] = y2;

		this.lines = initLines();
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		String ret = "";
		
		for (GAIGSline line: lines){
			ret += line.toCollectionXML();
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#clone()
	 */
	@Override
	public GAIGSdashedLine clone() {
		return new GAIGSdashedLine(this);
	}

}
