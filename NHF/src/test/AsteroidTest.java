package test;

import game.Asteroid;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * AsteroidTest - tests the functionality of Asteroids
 */
public class AsteroidTest {
    static Random random;
    static Asteroid asteroid;

    /**
     * init - initialize a Random and an Asteroid variable
     */
    @BeforeClass
    public static void init() {
        random = new Random();
        asteroid = new Asteroid(random);
    }

    /**
     * collisionRadiusTest - checks if the asteroid sets the correct default value for the radius of the asteroid
     */
    @Test
    public void collisionRadiusTest() {
        // 40.0 - is the radius of a Large sized asteroid
        assertEquals(40.0, asteroid.getCollisionRadius(), 0.01);
    }

    /**
     * scoreTest - checks if the asteroid sets the correct default value for killing an asteroid
     */
    @Test
    public void scoreTest() {
        // 20 - is the number of points you can earn for destroying a Large sized asteroid
        assertEquals(20, asteroid.getScoredPoints());
    }
}
