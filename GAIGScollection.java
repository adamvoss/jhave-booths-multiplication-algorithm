package exe.boothsMultiplication;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import exe.GAIGSdatastr;

/**
 * Do not create a loop with items in this structure.
 * This is powered by an ArrayList
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
//TODO Flush out Javadocs
public class GAIGScollection<E extends GAIGSdatastr> extends AbstractSet<E> implements GAIGSdatastr, Set<E>{
	protected ArrayList<E> items = new ArrayList<E>();
	protected String name = "";
	
	@Override
	//TODO consider adding a lock to prevent infinite loops
	public String toXML() {
		String xmlString = "";
		
		
		for (E item:items){
			xmlString += item.toXML();
		}
		return xmlString;
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
