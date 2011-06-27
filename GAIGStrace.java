package exe.boothsMultiplication;

import java.util.*;

import exe.*;

/**
* A class to contain many MutableGAIGSdatastr objects, referenced by time ("line") and a String.
* The main use of the class is to aggregate MutableGAIGSdatastr objects so that calls to 
* a ShowFile's writeSnap method can be more easily managed.
*
* A "line" represents a time in history and is referenced by an integer. The particular object
* on a line is referenced by a String value, given as an argument when the object is added.
*/
//This is a Hacky Implementation of MutableGAIGSdatastr for this purpose specific application
//Other use is strongly discouraged.
//For general use, please see GAIGSPane and GAIGScollection.
public class GAIGStrace implements MutableGAIGSdatastr, Cloneable {
    private ArrayList<HashMap<String, MutableGAIGSdatastr> > trace;

    /**
    * Creates a new GAIGStrace object with an initial first line.
    *
    */
    public GAIGStrace() {
        trace = new ArrayList<HashMap<String, MutableGAIGSdatastr> >();
        trace.add(new HashMap<String, MutableGAIGSdatastr>() );
    }

    /**
    * Add a MutableGAIGSdatastr object to the trace at the current line with a String reference.
    * @param ref A reference for future access of the MutableGAIGSdatastr object.
    *
    */
    public void add(String ref, MutableGAIGSdatastr elem) {
        add(trace.size()-1, ref, elem);
    }

    /**
    * Add a MutableGAIGSdatastr object to the trace at the specified line with a String reference.
    * @param ref A reference for future access of the MutableGAIGSdatastr object.
    * @param loc An int indicating the line to add the object to.
    * WARNING: WILL NOT NOTIFY IF loc IS BADLY FORMED.
    */
    public void add(int loc, String ref, MutableGAIGSdatastr elem) {
        if (loc >= trace.size() ) {
            System.err.println("Warning, attempt to access a line which does not exist. Attempt aborted.");
            return;
        }
        if (elem instanceof GAIGStrace) {
            System.err.println("Warning, attempt to add GAIGStrace type to GAIGStrace. This action is forbidden.");
            System.err.println("You should meet my friend Adam.");
            return;
        }
        HashMap<String, MutableGAIGSdatastr> line = trace.get(loc);

        if (line.containsKey(ref) ) {
            System.err.println("Warning, attempt to map at existing key. Attempt aborted");
            return;
        }

        line.put(ref, elem);
    }

    /**
    * Return the MutableGAIGSdatastr in the current line referenced by the String ref.
    * @param ref The String reference for the sought object.
    */
    public MutableGAIGSdatastr get(String ref) {
        return get(trace.size()-1, ref);
    }

    /**
    * Return the MutableGAIGSdatastr in the specified line referenced by the String ref.
    * WARNING: WILL NOT NOTIFY IF loc IS BADLY FORMED.
    * @param ref The String reference for the sought object.
    * @param loc An int indicating the line to get the object from.
    */
    public MutableGAIGSdatastr get(int loc, String ref) {
        if (loc >= trace.size() ) {
            System.err.println("Warning, attempt to access a line which does not exist. Attempt aborted.");
            return null;
        }

        return trace.get(loc).get(ref);
    }

    public HashMap<String, MutableGAIGSdatastr> popLine() {
        return trace.remove(trace.size()-1);
    }

    public void pushLine(HashMap<String, MutableGAIGSdatastr> line) {
        trace.add(line);
    }

    /**
    * Creates a new line for objects to be stored in, and switches the current line to the newly created one.
    * 
    */
    public void newLine() {
        trace.add(new HashMap<String, MutableGAIGSdatastr>() );
    }

    /**
    * Returns the number of lines available.
    * 
    */
    public int size() {return trace.size();}
/*
    public void setLineColor(String cl) {
        setLineColor(trace.size()-1, cl);
    }

    public void setLineColor(int loc, String cl) {
        for (HashMap<String, MutableGAIGSdatastr> hm : trace) {
            Collection<MutableGAIGSdatastr> coll = hm.values();

            for (MutableGAIGSdatastr gds : coll) {
                if (gds instanceof GAIGSregister) ((GAIGSregister) gds).setAllToColor(cl);
            }
        }
    
    }
*/    
    /**
    * An overridden method from MutableGAIGSdatastr which has no effect.
    *
    */
    public String getName() {return "Unnamed";}

    /**
    * An overridden method from MutableGAIGSdatastr which has no effect.
    *
    */
    public void setName(String newName) {}

    /**
    * Returns the concatenation of all the toXML() Strings of the MutableGAIGSdatastr objects added to all lines.
    *
    */
    public String toXML() {
        String xml = "";
        for (HashMap<String, MutableGAIGSdatastr> hm : trace) {
            Collection<MutableGAIGSdatastr> coll = hm.values();

            for (MutableGAIGSdatastr gds : coll) {
                xml += gds.toXML();
            }
        }

        return xml;
    }

    //Yuck! But I think that cloned deep enough
    public GAIGStrace clone(){
    	try {
			GAIGStrace clone = (GAIGStrace) super.clone();
			clone.trace = (ArrayList<HashMap<String, MutableGAIGSdatastr>>) this.trace.clone();
			
			for (int i = 0; i < clone.trace.size(); i++){
				clone.trace.set(i, (HashMap<String, MutableGAIGSdatastr>) clone.trace.get(i).clone());
				
				for (String s : clone.trace.get(i).keySet()){
					clone.trace.get(i).put(s, clone.trace.get(i).get(s).clone());
				}
				
			}
			
			return clone;
		} catch (CloneNotSupportedException e) {
			//This should never happen
			e.printStackTrace();
			return null;
		}
    	
    }
    
	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return new double[] {0,0,1,1};
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
	 */
	@Override
	public void setBounds(double x1, double y1, double x2, double y2) {
        for (HashMap<String, MutableGAIGSdatastr> hm : trace) {
            Collection<MutableGAIGSdatastr> coll = hm.values();

            for (MutableGAIGSdatastr gds : coll) {
               gds.setBounds(x1, y1, x2, y2);
            }
        }
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#getFontSize()
	 */
	@Override
	public double getFontSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see exe.MutableGAIGSdatastr#setFontSize(double)
	 */
	@Override
	public void setFontSize(double fontSize) {
	}
}
