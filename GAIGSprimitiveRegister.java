/**
 * 
 */
package exe.boothsMultiplication;

/**
 * @author vossad01
 *
 */
public class GAIGSprimitiveRegister implements GAIGSregister {
	private GAIGSpolygon wrapped;
	
	
	/**
	 * @see exe.boothsMultiplication.GAIGSregister#getName()
	 */
	@Override
    public String getName() {
        return wrapped.getName();
    }

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#setName(java.lang.String)
	 */
	@Override
    public void setName(String name) {
        wrapped.setName(name);
    }

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#toXML()
	 */
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#getBit(int)
	 */
	@Override
	public int getBit(int loc) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int)
	 */
	@Override
	public void setBit(int value, int loc) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#setBit(int, int, java.lang.String)
	 */
	@Override
	public void setBit(int value, int loc, String cl) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see exe.boothsMultiplication.GAIGSregister#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		wrapped.setLabel(label);
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#getLabel()
	 */
	@Override
	public String getLabel() {
		return wrapped.getLabel();
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#setColor(int, java.lang.String)
	 */
	@Override
	public void setColor(int loc, String cl) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#setAllToColor(java.lang.String)
	 */
	@Override
	public void setAllToColor(String cl) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#set(java.lang.String)
	 */
	@Override
	public void set(String binaryString) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#copyTo(exe.boothsMultiplication.Bounds)
	 */
	@Override
	public GAIGSregister copyTo(Bounds bounds) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see exe.boothsMultiplication.GAIGSregister#toIntArray()
	 */
	@Override
	public int[] toIntArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
