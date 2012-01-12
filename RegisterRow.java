/**
 * 
 */
package exe.boothsMultiplication;

import exe.GAIGSline;
import exe.GAIGSpane;
import exe.MutableGAIGSdatastr;

public class RegisterRow implements MutableGAIGSdatastr{

    public GAIGSpane<MutableGAIGSdatastr> currentRow;

    public RegisterRow() {
        currentRow = new GAIGSpane<MutableGAIGSdatastr>();
    }
    
    public RegisterRow(String name){
        this();
        setName(name);
    }
    
    public void setName(String name){
        currentRow.setName(name);
    }
    
    public void add(MutableGAIGSdatastr register){
        currentRow.add(register);
    }
    
    public void addCount(MutableGAIGSdatastr register){
        currentRow.add(BoothMultiplication.COUNT, register);
    }
    
    public void removeCount(){
        currentRow.remove(BoothMultiplication.COUNT);
    }
    
    public GAIGSbigEdianRegister getRegister(int register){
        return (GAIGSbigEdianRegister) currentRow.get(register);
    }
    
    public double[] getFirstRegiterPosition(){
        return currentRow.get(0).getBounds();
    }
    
    public void addUnderline(double traceWidth) {
        double[] firstRegPosition = this.getFirstRegiterPosition();
        currentRow.add(
                new GAIGSline(
                        new double[] { firstRegPosition[0], traceWidth },
                        new double[] { firstRegPosition[1] - BoothMultiplication.ROW_SPACE / 2,
                        firstRegPosition[1] - BoothMultiplication.ROW_SPACE / 2 }));
    }
    
    public RegisterRow clone(){
        RegisterRow ret = new RegisterRow();
        ret.currentRow = this.currentRow.clone();
        
        return ret;
        
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#toXML()
     */
    @Override
    public String toXML() {
        return currentRow.toXML();
    }

    /* (non-Javadoc)
     * @see exe.GAIGSdatastr#getName()
     */
    @Override
    public String getName() {
        return currentRow.getName();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getBounds()
     */
    @Override
    public double[] getBounds() {
        return currentRow.getBounds();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setBounds(double, double, double, double)
     */
    @Override
    public void setBounds(double x0, double y0, double x1, double y1) {
        currentRow.setBounds(x0, y0, x1, y1);
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#getFontSize()
     */
    @Override
    public double getFontSize() {
        return currentRow.getFontSize();
    }

    /* (non-Javadoc)
     * @see exe.MutableGAIGSdatastr#setFontSize(double)
     */
    @Override
    public void setFontSize(double fontSize) {
        currentRow.setFontSize(fontSize);
    }

    /**
     * @param color
     */
    public void setFillColor(String color) {
        getRegister(BoothMultiplication.REGM).setFillColor(color);
        getRegister(BoothMultiplication.REGA).setFillColor(color);
        getRegister(BoothMultiplication.REGQ).setFillColor(color);
        getRegister(BoothMultiplication.Q1).setFillColor(color);
        getRegister(BoothMultiplication.COUNT).setFillColor(color);
    }

    /**
     * @param color
     */
    public void setOutlineColor(String color) {
        getRegister(BoothMultiplication.REGM).setOutlineColor(color);
        getRegister(BoothMultiplication.REGA).setOutlineColor(color);
        getRegister(BoothMultiplication.REGQ).setOutlineColor(color);
        getRegister(BoothMultiplication.Q1).setOutlineColor(color);
        getRegister(BoothMultiplication.COUNT).setOutlineColor(color);
    }

    /**
     * @param color
     */
    public void setTextColor(String color) {
        getRegister(BoothMultiplication.REGM).setTextColor(color);
        getRegister(BoothMultiplication.REGA).setTextColor(color);
        getRegister(BoothMultiplication.REGQ).setTextColor(color);
        getRegister(BoothMultiplication.Q1).setTextColor(color);
        getRegister(BoothMultiplication.COUNT).setTextColor(color);
    }
}