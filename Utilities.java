package exe.boothsMultiplication;

/**
* A class with useful functions for dealing with binary and decimal values.
*
* @author Chris Jenkins <cjenkin1@trinity.edu>
* @author Adam Voss <vossad01@luther.edu>
*/
public class Utilities {

    /**
    * Converts an int to its shortest-length two's complement binary representative
    * @author Adam Voss <vossad01@luther.edu>
    */
    public static String toBinary(int a){
        if (a<0){
            return Integer.toBinaryString(a).replaceFirst("11*", "1");
        }

        //positive numbers are already shortest length
        return "0"+Integer.toBinaryString(a);
    }

    /**
    * Sign extends binStr by i bits
    * @author Adam Voss <vossad01@luther.edu>
    */
    public static String signExtend(String binStr, int i){
        String firstBit = String.valueOf(binStr.charAt(0));
        String extension = "";
        while (i>0){extension = extension.concat(firstBit); i--;}
        return extension.concat(binStr);
    }

    /**
    * Converts 2's complement binary value to decimal number.
    * @author Chris Jenkins <cjenkin1@trinity.edu>
    */
    public static String toDecimal(String binstr) {
        int sum    = 0;
        int maxPow = 1;

        for (int i = 0; i < binstr.length(); ++i) {
            sum = (2 * sum) + binstr.charAt(i) - '0';
            maxPow *= 2;
        }

        //Two's complement madness
        return ((sum < maxPow / 2) ? "" + sum : "" + (sum - maxPow) );
    }

   /**
   * Returns true if value input is a well-formed binary number, false otherwise.
   * @author Chris Jenkins <cjenkin1@trinity.edu>
   */
   public static boolean isWellFormedBinary(String input) {
        if (input.length() == 0) return false;

        boolean flag = true;

        for (int i = 0; i < input.length(); ++i)
            flag = flag && (input.charAt(i) == '0' || input.charAt(i) == '1');

        return flag;
    }

   /**
   * Returns true if value input is a well-formed decimal number, false otherwise.
   * @author Chris Jenkins <cjenkin1@trinity.edu>
   */
   public static boolean isWellFormedDecimal(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {return false;}
    }
}
