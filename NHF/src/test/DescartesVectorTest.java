package test;

import game.DescartesVector;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DescartesVectorTest - tests the functionality of 2D vectors
 */
public class DescartesVectorTest {
    static DescartesVector angularVector;
    static DescartesVector coordinateVector;

    /**
     * init - initialize 2 vectors for testing one with an angle parameter, and one with (X,Y) coordinates
     */
    @BeforeClass
    public static void init() {
        angularVector = new DescartesVector(3);
        coordinateVector = new DescartesVector(3, 15);
    }

    /**
     * vectorSizeSquaredTest - tests if the vectorSizeSquared() function works correctly
     */
    @Test
    public void vectorSizeSquaredTest() {
        double x = coordinateVector.getX();
        double y = coordinateVector.getY();
        double squared = x * x + y * y;

        assertEquals(squared, coordinateVector.vectorSizeSquared(), 0.01);

    }

    /**
     * unitVectorTest - tests if the unit vector transformation is correct
     */
    @Test
    public void unitVectorTest() {
        angularVector = angularVector.unitVector();

        assertEquals(1.0, angularVector.vectorSizeSquared(), 0.01);
    }

    /**
     * distanceTest - tests if the function calculates the distance of 2 vectors correctly
     */
    @Test
    public void distanceTest() {
        double x = angularVector.getX() - coordinateVector.getX();
        double y = angularVector.getY() - coordinateVector.getY();
        double distance = x * x + y * y;

        assertEquals(distance, angularVector.distanceOfVectors(coordinateVector), 0.01);
    }

    /**
     * multiplicationXTest - tests the X coordinate of a vector after multiplying it by a scalar
     */
    @Test
    public void multiplicationXTest() {
        double x = angularVector.getX();
        angularVector.multiply_by_scalar(5);

        assertEquals(5 * x, angularVector.getX(), 0.01);
    }

    /**
     * multiplicationYTest - tests the Y coordinate of a vector after multiplying it by a scalar
     */
    @Test
    public void multiplicationYTest() {
        double y = angularVector.getY();
        angularVector.multiply_by_scalar(5);

        assertEquals(5 * y, angularVector.getY(), 0.01);
    }

    /**
     * setterXTest - test if the vectors setter correctly works with X coordinate manipulation
     */
    @Test
    public void setterXTest() {
        double x = 5.0;
        coordinateVector.setXY(x, coordinateVector.getY());

        assertEquals(x, coordinateVector.getX(), 0.01);
    }

    /**
     * setterYTest - test if the vectors setter correctly works with Y coordinate manipulation
     */
    @Test
    public void setterYTest() {
        double y = 8.0;
        coordinateVector.setXY(coordinateVector.getX(), y);

        assertEquals(y, coordinateVector.getY(), 0.01);
    }

}
