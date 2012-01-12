/**
 * 
 */
package exe.boothsMultiplication;

public class BoothsMultiplicationRegister extends GAIGSbigEdianRegister{
           
    public BoothsMultiplicationRegister(int regSize, double[] position, String initialValue) {
        super(regSize,
                "",
                BoothMultiplication.DEFAULT_COLOR,
                BoothMultiplication.FONT_COLOR,
                BoothMultiplication.OUTLINE_COLOR,
                position,
                BoothMultiplication.REG_FONT_SIZE);
        
        this.set(initialValue);
    }
    
    public BoothsMultiplicationRegister(double[] position, String initialValue, int registerSize) {
        this(registerSize, position, initialValue);
    }
}