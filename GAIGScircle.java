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
public class GAIGScircle implements GAIGSdatastr, Primitive {
	public double x;
	public double y;
	public double r;
	public String fcolor;
	public String ocolor;
	public String lcolor;
	public String label;
	public double height;
	public int width;
	private String name;
	private GAIGScircle c; 

	public GAIGScircle(double cx, double cy, double r, String fcolor,
			String ocolor, String lcolor, String label, double height, int width) {
		this.x = cx;
		this.y = cy;
		this.r = r;
		this.fcolor = fcolor;
		this.ocolor = ocolor;
		this.lcolor = lcolor;
		this.label = label;
		this.height = height;
		this.width = width;
	}

	@Override
	public String toXML() {
        GAIGScircle c = this;
        return "<primitivecollection>\n\t<name>" + name +
        "</name>\n\t"+ "<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>" +"\n\t" + "\t<circle x=\"" + c.x + "\" y=\"" + c.y + "\" " +
          "r=\"" + c.r + "\" fcolor=\"" + c.fcolor + "\" " +
          "ocolor=\"" + c.ocolor + "\" text=\"" + c.label + "\" lcolor=\"" + c.lcolor + "\" height=\"" +
          c.height + "\" width=\""+ c.width +"\"/>\n" + "</primitivecollection>\n";
	}
	
	private String computeBounds() {
	    double x1 = Double.MAX_VALUE;
	    double y1 = Double.MAX_VALUE;
	    double x2 = Double.MIN_VALUE;
	    double y2 = Double.MIN_VALUE;
	    
        x1 = (x1 < (c.x - c.r) ? x1 : c.x - c.r);
        y1 = (y1 < (c.y - c.r) ? y1 : c.y - c.r);
        x2 = (x2 > (c.x + c.r) ? x2 : c.x + c.r);
        y2 = (y2 > (c.y + c.r) ? y2 : c.y + c.r);
        
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
