/**
 * Lookup table of values for shooter aiming
 * 
 * @author Iris
 */
public class ShooterLookup {
    // ArrayList contains array pairs of {Distance, Angle}
    // CURRENTLY CONTAINS RANDOM VALUES! DELETE THIS WHEN ARRAYLIST IS POPUlATED
    static ArrayList<int[2]> lookupTable = new ArrayList<>({5,40},{10,45}, {15,36}); 
    public ShooterLookup(){};
    
    /**
     * Rounds input
     * @param x the number to be rounded
     * @return input rounded to the nearest 5
     */
    int getRoundDist(double x){
        int out = x;
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
             if(lookupTable.get(i)[0] == rDist)
                 return lookupTable.get(i)[1];
        }
        return -1;
    }
}
