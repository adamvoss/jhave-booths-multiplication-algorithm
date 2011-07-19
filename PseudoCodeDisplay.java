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

package exe.boothsMultiplication;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.*;
import org.jdom.xpath.*;

/**
 * PsuedoCodeDisplay is used to generate a URI String from a
 *     pseudocode XML file (defined by pseudo.dtd).  For more information, see
 *     the pseudo_uri methods.
 * 
 * @author Justin Henry, William Gates
 * @author Adam Voss <vossad01@luther.edu> (Allowed user specified colors; added full Unicode support; Improved Javadocs) 
 * @version 2011-07-17
 */
public class PseudoCodeDisplay {
    // First we define some default highlight colors
    private final String[][] HIGHLIGHTS = {
            {"#FF0000","#FFDDDD"}, // Red
            {"#FF6600","#FFEECC"}, // Orange
            {"#999900","#FFFFBB"}, // Yellow
            {"#009900","#DDFFDD"}, // Green
            {"#0000FF","#DDEEFF"}, // Blue
            {"#880088","#FFDDFF"}, // Purple
            {"#666666","#EEEEEE"}, // Gray
    };

    public static final int RED = 0;
    public static final int ORANGE = 1;
    public static final int YELLOW = 2;
    public static final int GREEN = 3;
    public static final int BLUE = 4;
    public static final int PURPLE = 5;
    public static final int GRAY = 6;

    private final int DEFAULT_COLOR = RED;

    // Where our files live
    private final String XML = "../../src/exe/pseudocode/pseudo.xml";
    private final String XSL = "../../src/exe/pseudocode/pseudo.xsl";

    SAXBuilder builder;
    private Document doc;
    private String uriStr;

    private HashMap<String, String> vars;
    private int[] selected;
    private String[] highlights;
    private String[] textColors;
    private int currSel;

    /**
     * Constructor - creates a new instance of the PseudoCodeDisplay class.  The
     *     constructor also initializes the original document tree which is
     *     built from the file which the user passes in.
     * 
     * @param fileName the name of the XML file (defined by pseudo.dtd)
     * @throws JDOMException
     * @throws IOException
     */
    public PseudoCodeDisplay(String fileName) throws JDOMException,
    IOException {
        builder = new SAXBuilder();
        doc = builder.build(fileName);
    }

    /**
     * Return URI with many lines selected in (potentially) different colors using color constants.
     * 
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumbers which line numbers should be highlighted
     * @param lineColors array consisting of the publicly declared color constants
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int[] lineNumbers,
            int[] lineColors) throws JDOMException {
        this.vars = vars;
        this.selected = lineNumbers;
        this.currSel = 0;

        String[] highlights = new String[lineNumbers.length];
        String[] textColors = new String[lineNumbers.length];

        /* If the int array did not specify a color 0 would be
         * present.  This corresponds to DEFAULT_COLOR, so we
         * do not need to prefill the array with default color
         * like a previous version of this code did. 
         */
        for (int i = 0; i < lineNumbers.length; i++){
            highlights[i] = HIGHLIGHTS[lineColors[i]][1];
            textColors[i] = HIGHLIGHTS[lineColors[i]][0];
        }//This may throw an index out of bounds exception
        //The last version of this code did too

        return pseudo_uri(vars, lineNumbers, highlights, textColors);
    }

    /**
     * Returns a URI with a single line highlighted in RED.
     * 
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumber which line should be highlighted
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int lineNumber)
    throws JDOMException {
        return pseudo_uri(vars, new int[]{lineNumber}, new int[]{DEFAULT_COLOR});
    }

    /**
     * Returns a URI with a single line highlighted in according to the color constant.
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumber which line should be highlighted
     * @param color one of the publicly declared color constants
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int lineNumber,
            int color) throws JDOMException {
        return pseudo_uri(vars, new int[]{lineNumber}, new int[]{color});
    }

    /**
     * Returns a URI with multiple lines highlighted in RED.
     * 
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumbers which line numbers should be highlighted
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int[] selected)
    throws JDOMException {
        int[] colors = new int[selected.length];
        for(int i = 0; i < colors.length; i++) {
            colors[i] = DEFAULT_COLOR;
        }

        return pseudo_uri(vars, selected, colors);
    }

    /**
     * Returns a URI with a single line colored and highlighted.   
     * 
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumbers which line numbers should be highlighted
     * @param highlights the color of the line's background (empty string for none)
     * @param textColors the color of the line's text (empty string for none)
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int lineNumber,
            String highlight, String textColor) throws JDOMException {
        return pseudo_uri(vars, new int[]{lineNumber},
                new String[]{highlight}, new String[]{textColor});
    }

    /**
     * <p>Returns a URI with multiple lines each in the
     * specified color with the specified highlight.</p>
     * 
     * <p>Colors are specified in GAIGS Color Strings.
     * The arrays are presumed to be in the same order
     * and of the same size.</p>
     * 
     * <p>Pass the empty string for any of the color parameters
     * to get the system default colors(?) (was white and black on test system)</p>
     * 
     * @param vars a HashMap of the variables to be replaced in the XML template
     * @param lineNumbers which line numbers should be highlighted
     * @param highlights array of colors for the lines' background (empty string for none)
     * @param textColors array of colors of the lines' text (empty string for none)
     * @return a valid URI in String format with its special characters escaped
     * @throws JDOMException
     */
    public String pseudo_uri(HashMap<String, String> vars, int[] lineNumbers,
            String[] highlights, String[] textColors) throws JDOMException {
        this.vars = vars;
        this.selected = lineNumbers;
        this.currSel = 0;

        this.highlights = highlights;
        this.textColors = textColors;

        // Use XPath to traverse the pseudocode XML document
        uriStr = "";
        XPath x = XPath.newInstance("/pseudocode");
        List<?> list = x.selectNodes(doc);

        uriStr += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        listElements(list, "");

        try {
            writeXML();
            convertToHTML();
        } catch(JDOMException jdome) {
            // Big error here!
            jdome.printStackTrace();
        } catch(IOException ioe) {
            // Big error here!
            ioe.printStackTrace();
        }

        // We're done; delete the intermediate XML file
        new File(XML).delete();

        try {
            // Make the URI, escape special characters and clean it up 
            String answer = new URI("str", uriStr, "").toString();
            return answer.replace("&", "%26").replace("%3C?xml%20version=%221" +
                    ".0%22%20encoding=%22UTF-8%22?%3E%0D%0A", "");
        } catch(URISyntaxException urise) {
            // Big error here!
            return null;
        }
    }

    /**
     * Recursive function which takes a List of elements and adds itself and all
     *     its children to the URI String.
     *
     * @param list the list of elements to be added to the URI String
     * @param indent the amount of space to indent these elements
     */
    private void listElements(List<?> list, String indent) {
        Iterator<?> iter = list.iterator();
        while(iter.hasNext()) {
            Element e = (Element)iter.next();
            listElement(e, indent);
        }
    }

    /**
     * Non-recursive function which adds a specific element to the URI String,
     *     replacing it if it is a "replace" element, then adding all its
     *     attributes, and finally printing any children elements (using the
     *     recursive method above).
     * 
     * @param e the element to add to the URI String
     * @param indent the amount of space to indent this element
     */
    private void listElement(Element e, String indent) {
        // If it's a "replace" element, replace it with the corresponding string
        // from our HashMap.
        if(e.getName().equals("replace")) {
            Attribute att = (Attribute)e.getAttributes().get(0);
            uriStr += vars.get(att.getValue());
        } else {
            // Add the element and its attributes
            uriStr += indent + "<" + e.getName();
            listAttributes(e);
            uriStr += ">";

            // If its text is not blank, escape the special characters and add
            // the text
            if(!e.getTextTrim().equals("")) {
                uriStr += e.getText().replace("<", "&lt;").replace(">", "&gt;");
            }

            // Some formatting to make the XML output file look "pretty"
            if(e.getChildren("replace").isEmpty() && 
                    e.getTextTrim().equals("")) {
                uriStr += "\n";

                List<?> children = e.getChildren();
                listElements(children, indent + "  ");

                uriStr += indent + "</" + e.getName() + ">\n";
            } else {
                List<?> children = e.getChildren();
                listElements(children, indent + "  ");

                uriStr += "</" + e.getName() + ">\n";
            }
        }
    }

    /**
     * Non-recursive function which takes an element and adds all its
     *     attributes to the URI String, adding extra attributes as necessary if
     *     the element is a "line."
     * 
     * @param e the element whose attributes should be added
     */
    private void listAttributes(Element e) {
        // Add each existing attribute and its corresponding value
        for(Iterator<?> i = e.getAttributes().iterator(); i.hasNext();) {
            Attribute a = (Attribute)i.next();
            uriStr += " " + a.getName() + "=\"" + a.getValue() + "\"";
        }

        // If this is a line, check to see if it is supposed to be highlighted
        // and add the corresponding attributes
        if(e.getName().equals("line")) {
            boolean isSel = false;
            int lineNum = Integer.parseInt(e.getAttribute("num").getValue());
            for(int i = 0; i < selected.length; i++) {
                if(selected[i] == lineNum) {
                    uriStr += " text=\"" + textColors[currSel] +
                    "\" back=\"" + highlights[currSel++] + "\"";
                    isSel = true;
                    break;
                }
            }

            uriStr += " sel=\"" + isSel + "\"";
        }
    }

    /**
     * Writes the current data stored in the URI String to a temporary XML file
     *     so that it can be transformed into an HTML URI using our XSL
     *     Stylesheet (pseudo.xsl).
     * 
     * @throws IOException
     */
    /*
     * FileOutputStream outputs the raw bytes so Unicode will be preserved.
     * However, we must make sure to get the Unicode representation from the
     * String.  By default it would have given the system default encoding.
     */  
    private void writeXML() throws IOException {
        FileOutputStream out = new FileOutputStream(XML);
        out.write(uriStr.getBytes("UTF8"));
        out.flush();
        out.close();
    }

    /**
     * Takes the newly written XML file and transforms it using our XSL
     *     Stylesheet (pseudo.xsl), storing the HTML output in the URI String.
     * 
     * @throws IOException 
     * @throws JDOMException 
     */
    /*
     * JDOM defaults to UTF8.
     */
    private void convertToHTML() throws JDOMException, IOException {
        // Autobots, transform and roll out!
        XSLTransformer autobots = new XSLTransformer(XSL);
        Document doc = autobots.transform(builder.build(XML));
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());

        uriStr = out.outputString(doc);
    }
}
