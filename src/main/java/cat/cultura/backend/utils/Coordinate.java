package cat.cultura.backend.utils;

public class Coordinate {
    private double lon;
    private double lat;

    public Coordinate(double v, double v1) {
        lon = v;
        lat = v1;
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
        Coordinate gp=null;

        double R = 6371000; // meters , earth Radius approx
        double PI = 3.1415926535;
        double RADIANS = PI/180;
        double DEGREES = 180/PI;

        double lat2;
        double lon2;

        double lat1 = center.getLat() * RADIANS;
        double lon1 = center.getLon() * RADIANS;
        double radbear = bearing * RADIANS;

        // System.out.println("lat1="+lat1 + ",lon1="+lon1);

        lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance / R) +
                Math.cos(lat1)*Math.sin(distance/R)*Math.cos(radbear) );
        lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(distance / R)*Math.cos(lat1),
                Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));

        // System.out.println("lat2="+lat2*DEGREES + ",lon2="+lon2*DEGREES);

        gp = new Coordinate( lon2*DEGREES, lat2*DEGREES);

        return(gp);
    }
}
