package exe.boothsMultiplication;

/**
 * @author Shawn Recker
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
//TODO this needs further refactoring because many of the inherited methods have no effect

//This should maybe instead be a Decorator of GAIGSline;
public class GAIGSarrow extends AbstractPrimitive {
	GAIGSpane<AbstractPrimitive> primitives = new GAIGSpane<AbstractPrimitive>(); 

	public GAIGSarrow(double x[], double y[], String color, String labelColor,
			String label, double headSize, double textHeight, int lineWidth){
		
		primitives.add(new GAIGSline(x, y, color, labelColor, "", textHeight, lineWidth));

		double size = headSize;

		double [] x1 = {x[1], 0};
		double [] y1 = {y[1], 0};
		double [] x2 = {x[1], 0};
		double [] y2 = {y[1], 0};

		double theta = Math.atan((y[1] - y[0])/(x[1] - x[0]));
		double end1 = theta + Math.toRadians(30);
		double end2 = theta - Math.toRadians(30);

		x1[1] = x[1] - size * Math.cos(end1);
		x2[1] = x[1] - size * Math.cos(end2);
		y1[1] = y[1] - size * Math.sin(end1);
		y2[1] = y[1] - size * Math.sin(end2);

		double [] xvals = {x[1], x1[1], x2[1]};
		double [] yvals = {y[1], y1[1], y2[1]};

		primitives.add(new GAIGSpolygon(3, xvals, yvals, color, color, labelColor, label, textHeight, lineWidth));
	}
	
	public GAIGSarrow(GAIGSarrow source){
		this.primitives = source.primitives.clone();
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
		double x1 = Double.POSITIVE_INFINITY;
		double y1 = Double.POSITIVE_INFINITY;
		double x2 = Double.NEGATIVE_INFINITY;
		double y2 = Double.NEGATIVE_INFINITY;

		for (AbstractPrimitive prim : this.primitives){
			double[] bounds = prim.getBounds();
			
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
		double[] bounds = this.getBounds();
		double scaleX = (x2-x1)/(bounds[2]-bounds[0]);
		double scaleY = (y2-y1)/(bounds[3]-bounds[1]);
		
		for (int i = 0; i < primitives.size(); i++){
			double[] currentBds = primitives.get(i).getBounds();
			
			
			
			
			

			double translateX = x1-((currentBds[0]-bounds[0])*scaleX);
			double translateY = y1-(currentBds[1]*scaleY);
			
			primitives.get(i).setBounds(currentBds[0]*scaleX + realBounds[0],
					currentBds[1]*scaleY + realBounds[1],
					currentBds[2]*scaleX + realBounds[0],
					currentBds[3]*scaleY + realBounds[1]
			
		}

	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#toCollectionXML()
	 */
	@Override
	protected String toCollectionXML() {
		String ret = "";
		
		for (AbstractPrimitive prim : primitives){
			ret += prim.toCollectionXML();
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.AbstractPrimitive#clone()
	 */
	@Override
	public AbstractPrimitive clone() {
		return new GAIGSarrow(this);
	}

}
