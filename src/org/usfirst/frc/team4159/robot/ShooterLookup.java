package org.usfirst.frc.team4159.robot;
 
import java.util.ArrayList;
/**
 * Lookup table of values for shooter aiming
 * 
 * @author Iris
 */
public class ShooterLookup {
<<<<<<< HEAD
=======
    // ArrayList contains array pairs of {Distance, Angle}
    // CURRENTLY CONTAINS RANDOM VALUES! DELETE THIS WHEN ARRAYLIST IS POPUlATED
    static ArrayList<Pair> lookupTable = new ArrayList<Pair>();
    public ShooterLookup(){
        for(int i = 0; i < 90; i += 5)
            lookupTable.add(new Pair(i, (i+30)));
    }
    
>>>>>>> c08d44ee8db0001209792633ea59fea1c154c264
    /**
     * Useless constructor that exists for the sake of having a constructor
     */
<<<<<<< HEAD
    public ShooterLookup(){};
=======
    private int getRoundDist(double x){
        int out = (int)x;
        if( out%10 < 5 ){
            if(out%5 < 3)
                out = out - (out%5);
            else
                out = out + (5 - out%5);
        }else{
            if(out%5 < 8)
                out = out - (out%5);
            else
                out = out + (5 - out%5);
        }
        return out;
    }
>>>>>>> c08d44ee8db0001209792633ea59fea1c154c264
    
    /**
     * Looks up angle in lookup table
     * @param dist the unrounded camera distance
     * @return the angle required to shoot into the high goal or -1 if value not found
     */
<<<<<<< HEAD
    public double getAngle(double dist){
        return 1.862*(dist/12.0) + 34.79;
=======
    public int getAngle(double dist){
        int rDist = getRoundDist(dist);
        for(int i = 0; i < lookupTable.size(); i++){
             if(lookupTable.get(i).distance == rDist)
                 return lookupTable.get(i).angle;
        }
        return -1;
>>>>>>> c08d44ee8db0001209792633ea59fea1c154c264
    }
}
