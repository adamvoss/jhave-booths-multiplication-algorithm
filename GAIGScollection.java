package exe.boothsMultiplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import exe.GAIGSdatastr;

/**
 * Do not create a loop with items in this structure.
 * 
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public class GAIGScollection implements GAIGSdatastr, Set<GAIGSdatastr>{
	protected ArrayList<GAIGSdatastr> items;
	protected String name = "";
	
	@Override
	//TODO consider adding a lock to prevent infinite loops
	public String toXML() {
		String xmlString = "";
		
		
		for (GAIGSdatastr item:items){
			xmlString += item.toXML();
		}
		return xmlString;
	}

	public boolean add(GAIGSdatastr struct){
		if (items.contains(struct)){
			return false;
		}
		return items.add(struct);
	}
	
	public add(int index, GAIGSdatastr struct){
	}
	
	/**
	 * Note this name is never displayed
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Note this name is never displayed
	 */
	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public int size() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.items.contains(o);
	}

	@Override
	public Iterator<GAIGSdatastr> iterator() {
		return this.items.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.items.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return this.items.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.containsAll(c);
	}

	@Override
	//TODO Fix this
	public boolean addAll(Collection<? extends GAIGSdatastr> c) {
		return this.items.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.items.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
