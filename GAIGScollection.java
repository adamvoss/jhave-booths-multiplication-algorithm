package exe.boothsMultiplication;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

import exe.GAIGSdatastr;

/**
 * GAIGScollection provides a generic way to keep track of a collection of GAIGSdatastr.
 * As an implementer of GAIGSdatastr a GAIGScollection can be used anywhere a single
 * GAIGSdatastr would be used with the same result as passing each collection element
 * individually.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 * @version 2011-07-16
 */
//TODO, add raise and lower methods
public class GAIGScollection<E extends GAIGSdatastr> extends AbstractCollection<E> implements GAIGSdatastr{
    
	/**
	 * The internal ArrayList used for storing the collection. 
	 */
	protected ArrayList<E> items = new ArrayList<E>();
	
	
	/**
	 * Than name required by GAIGSdatastr.
	 * Note: this name is never displayed in the visualization, but it is written to the XML.
	 */
	protected String name = "";
	
 
	/**
	 * A flag to indicate whether the object is currently in its toXML method.
	 */
    //A semaphore would be overkill
	private boolean drawing = false;
	
	/**
	 * Produces an aggregate XML string of all data structures in the collection.
	 * This method protects against the user creating an infinite loop by nesting arrays. 
	 * 
	 * @see exe.GAIGSdatastr#toXML()
	 */
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

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(String name) {
		this.name=name;
	}
	
    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this collection in proper sequence.
     */
	@Override
	public Iterator<E> iterator() {
		return this.items.iterator();
	}

    /**
     * Returns the number of elements in this collection.  If this collection contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection.
     */
	@Override
	public int size() {
		return this.items.size();
	}

    /**
     * Attempts to add the specified element to the end of this collection.
     * If the element is this or is already in the collection
     * false is immediately returned.
     *
     * @param e element to be appended to this collection.
     * @return <tt>true</tt> if the element was added, otherwise <tt>false</tt>.
     */
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
	
    /**
     * Returns the element at the specified position in this collection.
     *
     * @param  index index of the element to return.
     * @return the element at the specified position in this collection.
     */
	public E get(int index){
		return this.items.get(index);
	}
	
    /**
     * Inserts the specified element at the specified position in this
     * collection. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the element is already in the collection it is first removed from its
     * previous location in the collection.
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     */
	public void add(int index, E data){
		if (items.contains(data)){
			items.remove(data);
		}
		this.items.add(index, data);
	}
	
    /**
     * Removes the element at the specified position in this collection.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to be removed.
     * @return the element that was removed from the collection.
     */
	public E remove(int index){
		return this.items.remove(index);
    }
}
