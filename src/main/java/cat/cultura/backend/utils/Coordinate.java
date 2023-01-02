package cat.cultura.backend.utils;

public class Coordinate {
    private final double lon;
    private final double lat;

    public Coordinate(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    //takes Coordinates, a distance in meters and a bearing in degrees and returns the new coordinate.
    public static Coordinate calcEndPoint(Coordinate center , int distance, double  bearing)
    {
        double r = 6371000; // meters , earth Radius approx
        double pi = 3.1415926535;
        double radians = pi/180;
        double degrees = 180/pi;

        double lat2;
        double lon2;

        double lat1 = center.getLat() * radians;
        double lon1 = center.getLon() * radians;
        double radbear = bearing * radians;

        lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance / r) +
                Math.cos(lat1)*Math.sin(distance/r)*Math.cos(radbear) );
        lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(distance / r)*Math.cos(lat1),
                Math.cos(distance/r)-Math.sin(lat1)*Math.sin(lat2));

        return new Coordinate( lon2*degrees, lat2*degrees);
    }
}
