package org.usfirst.frc.team4159.robot;
 
import java.util.ArrayList;
/**
 * Lookup table of values for shooter aiming
 * 
 * @author Iris
 */
public class ShooterLookup {
    // ArrayList contains array pairs of {Distance, Angle}
    // CURRENTLY CONTAINS RANDOM VALUES! DELETE THIS WHEN ARRAYLIST IS POPUlATED
    static ArrayList<Pair> lookupTable = new ArrayList<Pair>();
    public ShooterLookup(){
        for(int i = 0; i < 90; i += 5)
            lookupTable.add(new Pair(i, (i+30)));
    }
    
    /**
     * Rounds input
     * @param x the number to be rounded
     * @return input rounded to the nearest 5
     */
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
    
    /**
     * Looks up angle in lookup table
     * @param dist the unrounded camera distance
     * @return the angle required to shoot into the high goal or -1 if value not found
     */
    public int getAngle(double dist){
        int rDist = getRoundDist(dist);
        for(int i = 0; i < lookupTable.size(); i++){
             if(lookupTable.get(i).distance == rDist)
                 return lookupTable.get(i).angle;
        }
        return -1;
    }
}
