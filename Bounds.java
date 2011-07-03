package exe.boothsMultiplication;

/**
 * A helper class for wrapping the coordinates of a GAIGSdatastr.
 *
 */
@Deprecated
public class Bounds {
    //Must be between 0.0 and 1.0!
    protected double x1;
    protected double x2;
    protected double y1;
    protected double y2;

    public Bounds(double d1, double d2, double d3, double d4) {
        x1 = d1; y1 = d2; x2 = d3; y2 = d4;
    }
}
