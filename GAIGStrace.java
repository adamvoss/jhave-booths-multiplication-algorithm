package exe.boothsMultiplication;

import java.util.*;

import exe.*;

/**
* A class to contain many GAIGSdatastr objects, referenced by time ("line") and a String.
* The main use of the class is to aggregate GAIGSdatastr objects so that calls to 
* a ShowFile's writeSnap method can be more easily managed.
*
* A "line" represents a time in history and is referenced by an integer. The particular object
* on a line is referenced by a String value, given as an argument when the object is added.
*/
public class GAIGStrace implements GAIGSdatastr {
    private ArrayList<HashMap<String, GAIGSdatastr> > trace;

    /**
    * Creates a new GAIGStrace object with an initial first line.
    *
    */
    public GAIGStrace() {
        trace = new ArrayList<HashMap<String, GAIGSdatastr> >();
        trace.add(new HashMap<String, GAIGSdatastr>() );
    }

    /**
    * Add a GAIGSdatastr object to the trace at the current line with a String reference.
    * @param ref A reference for future access of the GAIGSdatastr object.
    *
    */
    public void add(String ref, GAIGSdatastr elem) {
        add(trace.size()-1, ref, elem);
    }

    /**
    * Add a GAIGSdatastr object to the trace at the specified line with a String reference.
    * @param ref A reference for future access of the GAIGSdatastr object.
    * @param loc An int indicating the line to add the object to.
    * WARNING: WILL NOT NOTIFY IF loc IS BADLY FORMED.
    */
    public void add(int loc, String ref, GAIGSdatastr elem) {
        if (loc >= trace.size() ) {
            System.err.println("Warning, attempt to access a line which does not exist. Attempt aborted.");
            return;
        }
        HashMap<String, GAIGSdatastr> line = trace.get(loc);

        if (line.containsKey(ref) ) {
            System.err.println("Warning, attempt to map at existing key. Attempt aborted");
            return;
        }

        line.put(ref, elem);
    }

    /**
    * Return the GAIGSdatastr in the current line referenced by the String ref.
    * @param ref The String reference for the sought object.
    */
    public GAIGSdatastr get(String ref) {
        return get(trace.size()-1, ref);
    }

    /**
    * Return the GAIGSdatastr in the specified line referenced by the String ref.
    * WARNING: WILL NOT NOTIFY IF loc IS BADLY FORMED.
    * @param ref The String reference for the sought object.
    * @param loc An int indicating the line to get the object from.
    */
    public GAIGSdatastr get(int loc, String ref) {
        if (loc >= trace.size() ) {
            System.err.println("Warning, attempt to access a line which does not exist. Attempt aborted.");
            return null;
        }

        return trace.get(loc).get(ref);
    }

    public HashMap<String, GAIGSdatastr> popLine() {
        return trace.remove(trace.size()-1);
    }

    public void pushLine(HashMap<String, GAIGSdatastr> line) {
        trace.add(line);
    }

    /**
    * Creates a new line for objects to be stored in, and switches the current line to the newly created one.
    * 
    */
    public void newLine() {
        trace.add(new HashMap<String, GAIGSdatastr>() );
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
        for (HashMap<String, GAIGSdatastr> hm : trace) {
            Collection<GAIGSdatastr> coll = hm.values();

            for (GAIGSdatastr gds : coll) {
                if (gds instanceof GAIGSregister) ((GAIGSregister) gds).setAllToColor(cl);
            }
        }
    
    }
*/    
    /**
    * An overridden method from GAIGSdatastr which has no effect.
    *
    */
    public String getName() {return "Unnamed";}

    /**
    * An overridden method from GAIGSdatastr which has no effect.
    *
    */
    public void setName(String newName) {}

    /**
    * Returns the concatenation of all the toXML() Strings of the GAIGSdatastr objects added to all lines.
    *
    */
    public String toXML() {
        String xml = "";
        for (HashMap<String, GAIGSdatastr> hm : trace) {
            Collection<GAIGSdatastr> coll = hm.values();

            for (GAIGSdatastr gds : coll) {
                xml += gds.toXML();
            }
        }

        return xml;
    }
}
