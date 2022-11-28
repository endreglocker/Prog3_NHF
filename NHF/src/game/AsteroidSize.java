package game;

import java.awt.*;

/**
 * AsteroidSize - stores the base type of asteroids
 */
public enum AsteroidSize {

    // asteroid types
    Small(15.0, 100),

    Medium(25.0, 50),

    Large(40.0, 20);

    // number of vertexes of a polygon
    private static final int polygonVertex = 8;

    public final Polygon polygon;

    public final double radius;

    // the number of points for destroying an asteroid (size dependent)
    public final int scoreForKilling;

    /**
     * constructor - creates a polygon (asteroid)
     *
     * @param r     - radius of the asteroid
     * @param value - points for killing an asteroid
     */
    AsteroidSize(double r, int value) {
        polygon = generatePolygon(r);
        radius = r;
        scoreForKilling = value;
    }

    /**
     * generatePolygon - generation an asteroid
     *
     * @param radius - radius of the polygon for its vertexes
     * @return an asteroid
     */
    private static Polygon generatePolygon(double radius) {
        //create an array to store the coordinates for each vertex of the polygon
        int[] x = new int[polygonVertex];
        int[] y = new int[polygonVertex];

        //generate the vertexes of the polygon
        //placing them symmetrically
        double angle = (2 * Math.PI / polygonVertex);
        for (int i = 0; i < polygonVertex; i++) {
            x[i] = (int) (radius * Math.sin(i * angle));
            y[i] = (int) (radius * Math.cos(i * angle));
        }

        //create a new polygon from the generated vertexes and return it.
        return new Polygon(x, y, polygonVertex);
    }

}
