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
 * @author Adam Voss <vossad01@luther.edu>, Seperated out protected classes
 * @version 6/22/2010
 */
public class GAIGSellipse implements GAIGSdatastr, Primitive {
	public double x;
	public double y;
	public double stAngle;
	public double endAngle;
	public double xR;
	public double yR;
	public String color;
	public String lcolor;
	public String label;
	public double height;
	public int width;
	private String name;
	private GAIGSellipse e = this;
	
	public GAIGSellipse(double x, double y, double stAngle, double endAngle, double xR,
			double yR, String color, String lcolor, String label, double height, int width)
	{
		this.x = x;
		this.y = y;
		this.stAngle = stAngle;
		this.endAngle = endAngle;
		this.xR = xR;
		this.yR = yR;
		this.color = color;
		this.lcolor = lcolor;
		this.label = label;
		this.height = height;
		this.width = width;
	}
	@Override
	public String toXML() {
		return "\t<ellipse x=\"" + e.x + "\" y=\"" + e.y + "\" " +
		"sa=\"" + e.stAngle + "\" ea=\"" + e.endAngle + "\" rx=\"" + e.xR + "\" ry=\"" + e.yR + "\" color=\"" + e.color + "\" " +
		"text=\"" + e.label + "\" lcolor=\"" + e.lcolor + "\" height=\"" + e.height + "\" width=\"" + e.width + "\"/>\n";
	}

	private String computeBounds() {
	    double x1 = Double.MAX_VALUE;
	    double y1 = Double.MAX_VALUE;
	    double x2 = Double.MIN_VALUE;
	    double y2 = Double.MIN_VALUE;
	    
        x1 = (x1 < e.x ? x1 : e.x);
        x1 = (x1 < e.x + e.xR ? x1 : e.x + e.xR);
        x2 = (x2 > e.x ? x2 : e.x);
        x2 = (x2 > e.x + e.xR ? x2 : e.x + e.xR);
        y1 = (y1 < e.y ? y1 : e.y);
        y1 = (y1 < e.y + e.yR ? y1 : e.y + e.yR);
        y2 = (y2 > e.y ? y2 : e.y);
        y2 = (y2 > e.y + e.yR ? y2 : e.y + e.yR);
        
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
