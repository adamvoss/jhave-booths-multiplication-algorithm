package exe.boothsMultiplication;

import exe.GAIGSpane;
import exe.MutableGAIGSdatastr;

/**
 * @author Adam Voss <vossad01@luther.edu>
 *
 */
public abstract class WrappedGAIGSpane<E extends MutableGAIGSdatastr> implements MutableGAIGSdatastr {

    protected GAIGSpane<E> pane;

    public abstract WrappedGAIGSpane<E> clone();
    
    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return pane.getBounds();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getFontSize()
     */
    @Override
    public double getFontSize() {
        // TODO Auto-generated method stub
        return pane.getFontSize();
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#getName()
     */
    @Override
    public String getName() {
        return pane.getName();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x0, double y0, double x1, double y1) {
        pane.setBounds(x0, y0, x1, y1);
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setFontSize(double)
     */
    @Override
    public void setFontSize(double fontSize) {
        pane.setFontSize(fontSize);
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        pane.setName(name);
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#toXML()
     */
    @Override
    public String toXML() {
        return pane.toXML();
    }
    
    public double getWidth() {
        return pane.getWidth();
    }
    
    public double getHeight(){
        return pane.getHeight();
    }
}
