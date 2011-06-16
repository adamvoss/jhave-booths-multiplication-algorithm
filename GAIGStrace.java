package exe.boothsMultiplication;

import java.util.*;

import exe.*;

public class GAIGStrace implements GAIGSdatastr {
    ArrayList<HashMap<String, GAIGSdatastr> > trace;

    public GAIGStrace() {
        trace = new ArrayList<HashMap<String, GAIGSdatastr> >();
        trace.add(new HashMap<String, GAIGSdatastr>() );
    }

    public void add(String ref, GAIGSdatastr elem) {
        add(trace.size()-1, ref, elem);
    }

    public void add(int loc, String ref, GAIGSdatastr elem) {
        HashMap<String, GAIGSdatastr> line = trace.get(loc);

        if (line.containsKey(ref) ) {
            System.err.println("Warning, attempt to map at existing key. Attempt aborted");
            return;
        }

        line.put(ref, elem);
    }

    public GAIGSdatastr get(String ref) {
        return get(trace.size()-1, ref);
    }

    public GAIGSdatastr get(int loc, String ref) {
        return trace.get(loc).get(ref);
    }

    public void newLine() {
        trace.add(new HashMap<String, GAIGSdatastr>() );
    }

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

    public String getName() {return "Unnamed";}

    public void setName(String newName) {}

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
