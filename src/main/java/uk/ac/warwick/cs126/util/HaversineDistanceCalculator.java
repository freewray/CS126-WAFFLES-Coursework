package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        double res = Math.pow(Math.sin((lat2-lat1)/2), 2) + (Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon2-lon1)/2), 2));
        res = R * 2 * Math.asin(Math.pow(res, 0.5));
        return (float)res;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        float res = inKilometres(lat1, lon1, lat2, lon2);
        res /= kilometresInAMile;
        return res;
    }

}