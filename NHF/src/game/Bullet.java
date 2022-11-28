package game;

import java.awt.*;
import java.util.ArrayList;

public class Bullet extends Thing {

    // bullet speed
    private static final double bulletSpeed = 5;

    // how long does the bullet "live"
    private int life;

    /**
     * @param owner - the object which fires the bullet (it is the spaceship)
     * @param angle - in which angle does the bullet flows
     */
    public Bullet(Thing owner, double angle) {
        // creates a bullet from the spaceship's position, with the exiting angle, and the speed of the bullet
        // for the collision of the bullet you can gain 0 points
        super(new DescartesVector(owner.position), new DescartesVector(angle).multiply_by_scalar(bulletSpeed), 2.0, 0);
        // sets the bullets "lifespan"
        life = 60;
    }

    /**
     * update - updates the bullet until it dies
     *
     * @param game - game frame
     */
    @Override
    public void update(GameFrame game) {
        super.update(game);

        // decrement the lifespan of the bullet, and remove it if needed.
        life--;
        if (life <= 0) {
            mustRemove();
        }
    }

    /**
     * @param game  - game
     * @param other - thing for identifying if the collision is valid
     */
    @Override
    public void handleCollision(GameFrame game, Thing other) {
        // if it is not the spaceship what the bullet hit, then it can be removed
        if (other.getClass() != SpaceShip.class) {
            mustRemove();
        }
    }

    /**
     * @param g2d    - 2D graphics for drawing
     * @param game - game frame
     * @param rgb  - coloring
     */
    @Override
    public void draw(Graphics2D g2d, GameFrame game, ArrayList<Integer> rgb) {
        // drawing an oval as a bullet
        g2d.drawOval(-1, -1, 2, 2);
    }

}
