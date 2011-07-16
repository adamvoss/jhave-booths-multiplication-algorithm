package exe.boothsMultiplication;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

import exe.GAIGSdatastr;

/**
 * Do not create a loop with items in this structure.
 * This is powered by an ArrayList
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 */
public class GAIGScollection<E extends GAIGSdatastr> extends AbstractCollection<E> implements GAIGSdatastr{
	protected ArrayList<E> items = new ArrayList<E>();
	protected String name = "";
	
	//A semaphore would be overkill 
	private boolean drawing = false;
	
	@Override
	public String toXML() {
	    if (drawing){
	        System.err.println("A loop exists among GAIGScollections," +
	                           "safely aborting excessive toXML calls");
	        return "<-- A loop exists among GAIGScollections, this should be fixed -->";
	    }

	    else{  
	        drawing = true;
	        
	        String xmlString = "";


	        for (E item:items){
	            xmlString += item.toXML();
	        }
	        
	        drawing = false;
	        return xmlString;
	    }
	}

	/**
	 * Note: this name is never displayed in the visualization.
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Note: this name is never displayed in the visualization.
	 */
	@Override
	public void setName(String name) {
		this.name=name;
	}
	
	@Override
	public Iterator<E> iterator() {
		return this.items.iterator();
	}

	@Override
	public int size() {
		return this.items.size();
	}

	@Override
	public boolean add(E data){
		if (this.equals(data)){
			return false;
		}
		
		if (items.contains(data)){
			return false;
		}
		
		return this.items.add(data);
	}
	
	public E get(int index){
		return this.items.get(index);
	}
	
	public void add(int index, E data){
		if (items.contains(data)){
			items.remove(data);
		}
		this.items.add(index, data);
	}
	
	public E remove(int index){
		return this.items.remove(index);
    }
}
