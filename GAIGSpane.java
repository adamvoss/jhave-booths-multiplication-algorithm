package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;


//TODO, raise and lower methods
public class GAIGSpane extends GAIGScollection<MutableGAIGSdatastr> implements MutableGAIGSdatastr {
	public static final double JHAVÉ_X_MARGIN = 0.203;
	public static final double JHAVÉ_Y_MARGIN = 0.067;
	public static final double JHAVÉ_ASPECT_RATIO = (1+2*JHAVÉ_X_MARGIN)/(1+2*JHAVÉ_Y_MARGIN);
	private double width;
	private double height;
	private double[] realBounds;
	
	public GAIGSpane(double x0, double y0, double x1, double y1, Double width, Double height){
		super();

		//TODO add aspect ratio preserving constructors that utilize null parameters
		if (width == null) width = ((x1-x0)/(y1-y0)/JHAVÉ_ASPECT_RATIO)*height;
		else if (height == null) height = (JHAVÉ_ASPECT_RATIO/((x1-x0)/(y1-y0)))*width;
		
		this.name="Unnamed";
		this.realBounds = new double[] {x0, y0, x1, y1};
		this.width=width;
		this.height = height;
	}
	
	public GAIGSpane(double x0, double y0, double x1, double y1){
		this(x0, y0, x1, y1, (x1-x0), (y1-y0));
	}
	
	
	public GAIGSpane(double width, double height){
		this(0-JHAVÉ_X_MARGIN, 0-JHAVÉ_Y_MARGIN, 1+JHAVÉ_X_MARGIN, 1+JHAVÉ_Y_MARGIN, width, height);
	}
	
	public GAIGSpane(){
//		this(0-JHAVÉ_X_MARGIN, 0-JHAVÉ_Y_MARGIN, 1+JHAVÉ_X_MARGIN, 1+JHAVÉ_Y_MARGIN, 1, 1);
//		this(0-JHAVÉ_X_MARGIN, 0-JHAVÉ_Y_MARGIN, 1-JHAVÉ_X_MARGIN, 1-JHAVÉ_Y_MARGIN, 1, 1);
		this(0.0, 0.0, 1.0, 1.0, 1.0, 1.0);
	}
	
//	public double getAspectRatio(){
//		return (realBounds[2]-realBounds[0])/(realBounds[3]-realBounds[1]);
//	}
	
	public GAIGSpane(GAIGSpane source){
		super();
		this.width = source.width;
		this.height = source.height;
		this.realBounds = source.realBounds.clone();
		this.name = source.name;
		for (MutableGAIGSdatastr item : source.items){
			this.add(item.clone());
		}
	}
	
	@Override
	public String toXML() {
		String xml = "<!-- Start GAIGSpane: "+ name +" -->\n";
		
		for (MutableGAIGSdatastr item : items){
			MutableGAIGSdatastr clone = item.clone();
			double[] newBds = getRealCoordinates(clone);
			clone.setBounds(newBds[0], newBds[1], newBds[2], newBds[3]);
			xml += clone.toXML();
		}
		xml += "<!-- End of GAIGSpane: "+ name +" -->\n";
		return xml;
	}

	//TODO consider override of equals so this is not needed
	public void forceAdd(MutableGAIGSdatastr data){
		this.items.add(data);
	}
	
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	//TODO add a getRealCoordinates that acts on a double[]
	public double[] getRealCoordinates(double[] srcBounds){
		double scaleX = (realBounds[2]-realBounds[0])/width;
		double scaleY = (realBounds[3]-realBounds[1])/height;
		return new double[] {srcBounds[0]*scaleX + realBounds[0],
							 srcBounds[1]*scaleY + realBounds[1],
							 srcBounds[2]*scaleX + realBounds[0],
							 srcBounds[3]*scaleY + realBounds[1]};
		}
	
	public double[] getRealCoordinates(MutableGAIGSdatastr data){
		return getRealCoordinates(data.getBounds());
		}
	
	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return realBounds;
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		this.realBounds = new double[] {x1, y1, x2, y2};
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getFontSize()
	 */
	@Override
	public double getFontSize() {
		double sum = 0;
		for (MutableGAIGSdatastr item : items){
			sum += item.getFontSize();
		}
		return sum/items.size();
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setFontSize(double)
	 */
	@Override
	public void setFontSize(double fontSize) {
		for (MutableGAIGSdatastr item : items){
			item.setFontSize(fontSize);
		}
	}
	
	public GAIGSpane clone(){
		return new GAIGSpane(this);
	}
}
