/**
 * 
 */
package exe.boothsMultiplication;

import exe.GAIGSmonospacedText;
import exe.GAIGSpane;
import exe.MutableGAIGSdatastr;

public class RegisterRowHistoryTrace implements MutableGAIGSdatastr {
    public GAIGSpane<MutableGAIGSdatastr> trace = new GAIGSpane<MutableGAIGSdatastr>();
    
    private GAIGSpane<GAIGSmonospacedText> labels = new GAIGSpane<GAIGSmonospacedText>();
    
    public RegisterRowHistoryTrace() {
    }

    public RegisterRowHistoryTrace(double[] bounds) {
        trace = new GAIGSpane<MutableGAIGSdatastr>(
                bounds[0], bounds[1],
                bounds[2], bounds[3],
                null, 1.0);
        trace.add(labels);
        setName("Trace");
    }
    
    public void add(GAIGSpane<?> data){
        trace.add(data);
    }
    
    public void addRow(RegisterRow registerRow){
        trace.add(registerRow);            
    }
    
    public double getWidth(){
        return trace.getWidth();
    }
    
    public double getHeight(){
        return trace.getHeight();
    }
    
    public int size(){
        return trace.size();
    }
    
    
    // TODO: When I can stop worrying about XML being identical,
    //    change the type of trace to eliminate this cast.
    public RegisterRow removeLast(){
        return (RegisterRow) trace.remove(trace.size() - 1);
    }
           
    public RegisterRow getRow(int rowNumber){
        return (RegisterRow) trace.get(rowNumber);
    }
    
    // There is a bug here because labels will be null in the clone.
    // Doesn't currently affect anything.
    public RegisterRowHistoryTrace clone(){
        RegisterRowHistoryTrace ret = new RegisterRowHistoryTrace(this.getBounds());
        ret.trace = this.trace.clone();
        
        return ret;
    }
    
    public void addLabel(double[] coords, String displayText) {
        labels.add(new GAIGSmonospacedText(
                (coords[2]-coords[0])/2.0+coords[0], coords[3],
                GAIGSmonospacedText.HCENTER, GAIGSmonospacedText.VBOTTOM,
                BoothMultiplication.COLBL_FONT_SIZE, BoothMultiplication.FONT_COLOR, displayText, BoothMultiplication.COLBL_FONT_SIZE/2));
    }
    
    public void fadeLastRow(){
        fadeRow(this.size() - 2);
    }
    
    private void fadeRow(int row) {
        getRow(row).setTextColor(BoothMultiplication.INACTIVE_TEXT);
        getRow(row).setOutlineColor(BoothMultiplication.INACTIVE_OUTLINE);
        getRow(row).setFillColor(BoothMultiplication.INACTIVE_FILL);
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#toXML()
     */
    @Override
    public String toXML() {
        return trace.toXML();
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#getName()
     */
    @Override
    public String getName() {
        return trace.getName();
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        trace.setName(name);
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return trace.getBounds();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x0, double y0, double x1, double y1) {
        trace.setBounds(x0, y0, x1, y1);
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getFontSize()
     */
    @Override
    public double getFontSize() {
        return trace.getFontSize();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setFontSize(double)
     */
    @Override
    public void setFontSize(double fontSize) {
        trace.setFontSize(fontSize);
    }
}