package exe.boothsMultiplication;

/**
 * A helper class for wrapping the coordinates of a GAIGSdatastr.
 *
 */

public class GAIGSpoints {
    //Must be between 0.0 and 1.0!
    public double x1;
    public double x2;
    public double y1;
    public double y2;

    public GAIGSpoints(double d1, double d2, double d3, double d4) {
        x1 = d1; y1 = d2; x2 = d3; y2 = d4;
    }
}
