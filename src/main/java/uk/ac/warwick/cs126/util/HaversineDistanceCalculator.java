package uk.ac.warwick.cs126.util;

import java.text.DecimalFormat;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    
    /** 
     * @param latitude1
     * @param longtitude1
     * @param latitude2
     * @param longtitude2
     * @return distance in kilometers
     */
    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        double distance = getDistance(lat1, lon1, lat2, lon2);
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(distance));
    }

    
    /** 
     * @param latitude1
     * @param longtitude1
     * @param latitude2
     * @param longtitude2
     * @return distance in miles
     */
    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        DecimalFormat df = new DecimalFormat("#.#");
        double distance = getDistance(lat1, lon1, lat2, lon2);
        return Float.parseFloat(df.format(distance / kilometresInAMile));
    }

    
    /** 
     * @param latitude1
     * @param longtitude1
     * @param latitude2
     * @param longtitude2
     * @return distance in kilometers and in the form of double
     */
    private static double getDistance(float lat1, float lon1, float lat2, float lon2) {
        double radianslat1 = Math.toRadians(lat1), radianslat2 = Math.toRadians(lat2);
        double radianslon1 = Math.toRadians(lon1), radianslon2 = Math.toRadians(lon2);
        double distance = Math.pow(Math.sin((radianslat2 - radianslat1) / 2), 2)
                + (Math.cos(radianslat1) * Math.cos(radianslat2) * Math.pow(Math.sin((radianslon2 - radianslon1) / 2), 2));
        return R * 2 * Math.asin(Math.sqrt(distance));
    }

}