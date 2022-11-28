package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Asteroid - child class of Thing, responsible for the movement, collision and drawing of asteroids
 */
public class Asteroid extends Thing {
    private final AsteroidSize asteroidSize;

    /**
     * constructor - creates an asteroid in a random position
     *
     * @param random - a random number for positioning
     */
    public Asteroid(Random random) {
        super(spawnPoint(random), spawnVelocity(random), AsteroidSize.Large.radius, AsteroidSize.Large.scoreForKilling);
        asteroidSize = AsteroidSize.Large;
    }

    /**
     * constructor - creates the "child" asteroids -> if an asteroid is destroyed it fell apart to 3 smaller asteroids (used in handleCollision)
     *
     * @param parent - parent asteroid
     * @param size   - size of parent asteroid
     * @param random - random number
     */
    public Asteroid(Asteroid parent, AsteroidSize size, Random random) {
        super(new DescartesVector(parent.position), spawnVelocity(random), size.radius, size.scoreForKilling);
        asteroidSize = size;

    }

    /**
     * spawnPoint - where the asteroid will spawn (randomly)
     *
     * @param random - random number
     * @return spawning coordinates
     */
    private static DescartesVector spawnPoint(Random random) {
        // minimal distance from the middle of the panel
        double minDistance = 300.0;

        // maximal distance from the middle of the panel
        double maxDistance = GamePanel.worldSize / 2.0;

        double deltaDistance = maxDistance - minDistance;

        // setting the vector to the middle of the game
        double halfMaxDistance = maxDistance / 2.0;
        DescartesVector vec = new DescartesVector(halfMaxDistance, halfMaxDistance);

        // creating a random position
        // by randomly choosing an angle
        // then multiply it by
        return vec.addVectorToVector(new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(minDistance + random.nextDouble() * deltaDistance));
    }

    /**
     * spawnVelocity - creates a spawning velocity for the asteroid (randomly)
     *
     * @param random - random number
     * @return determine the speed of an asteroid randomly
     */
    private static DescartesVector spawnVelocity(Random random) {
        // trying to set an optimal speed randomly for the asteroid velocity
        double minVelocity = 0.6;
        double maxVelocity = 1.6;
        double deltaVelocity = maxVelocity - minVelocity;

        // creating a vector for velocity
        // the direction is determined with an angle: random.nextDouble() * Math.PI * 2
        // then multiply it with a speed which is more than the minimal speed of the asteroid but less than the max speed: minVelocity + random.nextDouble() * deltaVelocity
        return new DescartesVector(random.nextDouble() * Math.PI * 2).multiply_by_scalar(minVelocity + random.nextDouble() * deltaVelocity);
    }

    /**
     * update - updates the game
     *
     * @param game - the frame of the game
     */
    @Override
    public void update(GameFrame game) {
        super.update(game);
        // optionally rotate the image each frame (using radian)
        //rotate(0);
    }

    /**
     * @param g2d    - 2D graphics for drawing the asteroid
     * @param game - game frame
     * @param rgb  - color array
     */
    @Override
    public void draw(Graphics2D g2d, GameFrame game, ArrayList<Integer> rgb) {
        // drawing asteroids
        g2d.drawPolygon(asteroidSize.polygon); //Draw the Asteroid.
        g2d.setColor(new Color(rgb.get(0), rgb.get(1), rgb.get(2)));
        g2d.fill(asteroidSize.polygon);
    }

    /**
     * @param game  - game frame
     * @param other - prevent unnecessary collisions with other asteroids
     */
    @Override
    public void handleCollision(GameFrame game, Thing other) {
        // prevent collisions with other asteroids
        // without it, they can collide with one-another which makes the game almost unplayable
        if (other.getClass() != Asteroid.class) {
            // only spawn if not a "Small" asteroid.
            if (asteroidSize != AsteroidSize.Small) {
                // determine the size of the smaller ones.
                AsteroidSize spawnSize = AsteroidSize.values()[asteroidSize.ordinal() - 1];
                // create the children asteroids (3 from each)
                for (int i = 0; i < 3; i++) {
                    game.registerThing(new Asteroid(this, spawnSize, game.getRandom()));
                }
            }
            // removes the destroyed asteroid and adds the score
            mustRemove();
            game.addScore(getScoredPoints());
        }
    }

}
