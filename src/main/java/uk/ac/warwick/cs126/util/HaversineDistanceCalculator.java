package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        double rlat1 = Math.toRadians(lat1);
        double rlat2 = Math.toRadians(lat2);
        double rlon1 = Math.toRadians(lon1);
        double rlon2 = Math.toRadians(lon2);
        double res = Math.pow(Math.sin((rlat2 - rlat1) / 2), 2)
                + (Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin((rlon2 - rlon1) / 2), 2));
        res = R * 2 * Math.asin(Math.pow(res, 0.5));
        return (float) (Math.round(res * 10) / 10.0);
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        float km = inKilometres(lat1, lon1, lat2, lon2);
        return (float) (Math.round((km / kilometresInAMile) * 10) / 10.0);
    }

}