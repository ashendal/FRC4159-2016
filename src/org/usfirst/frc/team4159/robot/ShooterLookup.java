/**
 * Lookup table of values for shooter aiming
 * 
 * @author Iris
 */
public class ShooterLookup {
    /**
     * Useless constructor that exists for the sake of having a constructor
     */
    public ShooterLookup(){};
    
    /**
     * Looks up angle in lookup table
     * @param dist the unrounded camera distance
     * @return the angle required to shoot into the high goal or -1 if value not found
     */
    public double getAngle(double dist){
        return 1.862*(dist/12.0) + 34.79;
    }
}
