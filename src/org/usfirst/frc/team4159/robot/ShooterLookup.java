package org.usfirst.frc.team4159.robot;
 
import java.util.ArrayList;
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
     * Finds an angle given a distance
     * 
     * @param dist the distance, in inches, provided by the camera
     * @return the angle to shoot at
     */
    public double getAngle(double distanceInInches){
        double feet = distanceInInches / 12.0;
        return 1.862 * feet + 34.79;
    }
}
