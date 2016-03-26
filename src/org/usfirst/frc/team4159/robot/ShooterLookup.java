package org.usfirst.frc.team4159.robot;
 
import java.util.ArrayList;
/**
 * Lookup table of values for shooter aiming
 * 
 * @author Iris
 */
public class ShooterLookup {        
    /**
     * Finds an angle given a distance
     * 
     * @param dist the distance, in inches, provided by the camera
     * @return the angle to shoot at
     */
    public static double getAngle(double dist){
        return 1.862*(dist/12.0) + 34.79;
    }
}
