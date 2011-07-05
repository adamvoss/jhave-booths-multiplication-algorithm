package exe.boothsMultiplication;

import exe.boothsMultiplication.MutableGAIGSdatastr;


//TODO, raise and lower methods
public class GAIGSpane<E extends MutableGAIGSdatastr> extends GAIGScollection<E> implements MutableGAIGSdatastr {
	public static final double JHAVÉ_X_MARGIN = 0.203;
	public static final double JHAVÉ_Y_MARGIN = 0.067;
	public static final double JHAVÉ_ASPECT_RATIO = (1+2*JHAVÉ_X_MARGIN)/(1+2*JHAVÉ_Y_MARGIN);
	public static final double DEFAULT_ASPECT_RATIO = 1.0;
	private double width;
	private double height;
	private double[] realBounds;
	
	public GAIGSpane(double x0, double y0, double x1, double y1, Double width, Double height){
		super();

		if (width == null) width = ((x1-x0)/(y1-y0)/DEFAULT_ASPECT_RATIO)*height;
		else if (height == null) height = (DEFAULT_ASPECT_RATIO/((x1-x0)/(y1-y0)))*width;
		
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
		this(0.0, 0.0, 1.0, 1.0, 1.0, 1.0);
	}

	@SuppressWarnings("unchecked")
	public GAIGSpane(GAIGSpane<E> source){
		super();
		this.width = source.width;
		this.height = source.height;
		this.realBounds = source.realBounds.clone();
		this.name = source.name;
		for (MutableGAIGSdatastr item : source.items){
			this.add((E) item.clone());
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

	public double getAspectRatio(){
		return (width/(realBounds[2]-realBounds[0]))/(height/(realBounds[3]-realBounds[1]));
	}

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
	
	/**
	 * @see exe.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return realBounds;
	}

	/**
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
		this.realBounds = new double[] {x1, y1, x2, y2};
	}

	/**
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

	/**
	 * @see exe.MutableGAIGSdatastr#setFontSize(double)
	 */
	@Override
	public void setFontSize(double fontSize) {
		for (MutableGAIGSdatastr item : items){
			item.setFontSize(fontSize);
		}
	}
	
	public GAIGSpane<E> clone(){
		return new GAIGSpane<E>(this);
	}
}
