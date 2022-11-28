package game;

import java.io.Serializable;

/**
 * DescartesVector - implements 2 dimensional vectors for implementing movement, directions etc.
 */
public class DescartesVector implements Serializable {

    // x coordinate
    private double x;

    // y coordinate
    private double y;

    /**
     * constructor - creates a vector which pointing at the given angle (asteroid random spawning, bullets direction)
     *
     * @param angle - vector angle
     */
    public DescartesVector(double angle) {
        // Pythagoras
        x = Math.cos(angle);
        y = Math.sin(angle);
    }

    /**
     * @param vx - vector x coordinate
     * @param vy - vector y coordinate
     */
    public DescartesVector(double vx, double vy) {
        x = vx;
        y = vy;
    }

    /**
     * @param vec - creates a vector from a vector
     */
    public DescartesVector(DescartesVector vec) {
        x = vec.x;
        y = vec.y;
    }

    /**
     * @param vec - adding this to the current one (by coordinates)
     * @return a vector with the size of x + vec.x and y + vec.y
     */
    public DescartesVector addVectorToVector(DescartesVector vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    /**
     * @param scalar - a multiplier double
     * @return a vector times scalar
     */
    public DescartesVector multiply_by_scalar(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * @return unit vector (the vector's length is 1.0)
     */
    public DescartesVector unitVector() {
        double length = vectorSizeSquared();
        if (length != 0.0 && length != 1.0) {
            length = Math.sqrt(length);
            x /= length;
            y /= length;
        }
        return this;
    }

    /**
     * @return the squared value of length (length * length)
     */
    public double vectorSizeSquared() {
        return (x * x + y * y);
    }

    /**
     * @param vec - another vector
     * @return the distance of the current vector from vec
     */
    public double distanceOfVectors(DescartesVector vec) {
        double dx = x - vec.x;
        double dy = y - vec.y;
        return (dx * dx + dy * dy);
    }

    /**
     * @param vx - x coordinate
     * @param vy - y coordinate
     */
    public void setXY(double vx, double vy) {
        x = vx;
        y = vy;
    }

    /**
     * getters - getX(); getY()
     */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}