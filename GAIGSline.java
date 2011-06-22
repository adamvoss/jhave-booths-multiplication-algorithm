package exe.boothsMultiplication;

import exe.GAIGSdatastr;
import exe.boothsMultiplication.GAIGSprimitiveCollection.Primitive;

/**
 * <p><code>GAIGSprimitiveCollection</code> provides the ability to draw 2D graphics for use in visualizations.
 * This class supports a variety of 2D graphics primitives, including lines, polygons, circles, ellipises, etc.
 * Creation of the primitives adheres to the other GAIGS classes and colors are specified with the standard color string.
 * </p>
 *
 * @author Shawn Recker
 * @author Adam Voss, Seperated out protected classes
 * @version 6/22/2010
 */
public class GAIGSline implements GAIGSdatastr, Primitive {
	public double x[] = new double[2];
	public double y[] = new double[2];
	public String color;
	public String lcolor;
	public String label;
	public double height;
	public int width;
	private String name;
	private GAIGSline t;

	public GAIGSline(double x[], double y[], String color, String lcolor,
			String label, double height, int width)
	{
		for(int i=0;i<2;++i)
		{
			this.x[i]=x[i];
			this.y[i]=y[i];
		}
		this.color = color;
		this.lcolor = lcolor;
		this.label = label;
		this.height = height;
		this.width = width;
	}

	@Override
	public String toXML() {
		GAIGSline l = this;
		String xml = "<primitivecollection>\n\t<name>" + name +
	    "</name>\n\t"+ "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>" +"\n\t" + "<polygon nSides=\"" + 2;
		for(int j=0; j<2 ; ++j) {
			xml += "\" ptsX"+ j + "=\"" + l.x[j] + "\" ptsY"+ j + "=\"" + l.y[j];
		}
		xml += "\" fcolor=\"" + l.color + "\" " +
		"ocolor=\"" + l.color + "\" text=\"" + l.label + "\" lcolor=\"" + l.lcolor +
		"\" height=\"" + l.height + "\" width=\"" + l.width + "\"/>\n" + "</primitivecollection>\n";
		return xml;
	}

	private String computeBounds() {
	    double x1 = Double.MAX_VALUE;
	    double y1 = Double.MAX_VALUE;
	    double x2 = Double.MIN_VALUE;
	    double y2 = Double.MIN_VALUE;
	    
        for(int j = 0; j < 2; ++j) {
            x1 = (x1 < t.x[j] ? x1 : t.x[j]);
            y1 = (y1 < t.y[j] ? y1 : t.y[j]);
            x2 = (x2 > t.x[j] ? x1 : t.x[j]);
            y2 = (y2 > t.y[j] ? y1 : t.y[j]);
          }
        
        return "<bounds x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\"/>";
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
