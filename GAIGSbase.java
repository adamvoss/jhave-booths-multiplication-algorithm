/* 
This file is part of JHAVE -- Java Hosted Algorithm Visualization
Environment, developed by Tom Naps, David Furcy (both of the
University of Wisconsin - Oshkosh), Myles McNally (Alma College), and
numerous other contributors who are listed at the http://jhave.org
site

JHAVE is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your
option) any later version.

JHAVE is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with the JHAVE. If not, see:
<http://www.gnu.org/licenses/>.
 */

package exe;

/**
 * Abstract class GAIGSbase - provides basic instance variables and functionality
 * for linear GAIGS structures and for <code>GAIGSarray</code>.  Extending classes
 * need only define a clone method to be able to implement MutatableGAIGSdatastr.
 * 
 * @author Myles McNally
 * @author Adam Voss <vossad01@luther.edu> Added getters and setters.
 * @version 6/20/06
 */

public abstract class GAIGSbase implements GAIGSdatastr {


	/**
	 * Display name.
	 */
	protected String name;

	/**
	 * Display color.
	 */
	protected String color;

	/**
	 * Left bound for display.
	 */
	protected double x1;

	/**
	 * Bottom bound for display.
	 */
	protected double y1;

	/**
	 * Right bound for display.
	 */
	protected double x2;

	/**
	 * Top bound for display.
	 */
	protected double y2;

	/**
	 * Font size for display.
	 */
	protected double fontSize;

	//---------------------- Display Setter/Getter Methods ---------------------------------


	//----------- Name -----------
	/**
	 * Set the value of the name to be displayed.
	 * 
	 * @param       name        The display name.
	 */
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * Get the value of the name.
	 * 
	 * @return      The display name.
	 */
	public String getName () {
		return name;
	} 

	//----------- Bounds -----------
	/**
	 * Returns the bounds of the <code>GAIGS</code> Structure.
	 * @return      Array of coordinates.
	 */
	public double[] getBounds(){
		return new double[] {this.x1, this.y1, this.x2, this.y2};
	}

	/**
	 * Set the position of the <code>GAIGS</code> Structure.
	 * @param     x1     Typically the x-coordinate of lower left corner.
	 * @param     y1     Typically the y-coordinate of the lower left corner.
	 * @param     x2     Typically the x-coordinate of the upper right corner.
	 * @param     y2     Typically the y-coordinate of the upper right corner.
	 */
	public void setBounds(double x1, double y1, double x2, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	//----------- Font Size -----------
	/**
	 * Returns the value of the font size.
	 * @return  The current font size.
	 */
	public double getFontSize(){
		return this.fontSize;
	}

	/**
	 * Sets the font size for display.
	 * @param      fontSize     The desired font size.
	 */
	public void setFontSize(double fontSize){
		this.fontSize = fontSize;
	}

	// Color methods omitted because a brief survey of extending classes
	// Found color was often ignored, or handled independently
}
