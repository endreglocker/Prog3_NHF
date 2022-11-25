package game;

import java.awt.*;
import java.util.ArrayList;


public class SpaceShip extends Thing {
    private static final double rotationSpeed = 0.05;

    private int fireCD;

    private boolean canMoveForward;

    private boolean rotateLeftPressed;

    private boolean rotateRightPressed;

    private boolean firePressed;

    //private int consecutiveShots;


    private final ArrayList<Bullet> bullets;

    /**
     * SpaceShip - initialize a spaceship (creates spaceship, maximise the number of bullet, sets default rotation, disable firing etc.)
     */
    public SpaceShip() {
        super(new DescartesVector(PlayPanel.worldSize / 2.0, PlayPanel.worldSize / 2.0), new DescartesVector(0.0, 0.0), 10.0, 0);
        bullets = new ArrayList<>();
        // from thing!
        rotation = -Math.PI / 2.0;
        canMoveForward = false;
        rotateLeftPressed = false;
        rotateRightPressed = false;
        firePressed = false;
        fireCD = 0;

    }

    /**
     * @param state -  if "w" is pressed -> true; else -> false
     */
    public void setMoveForward(boolean state) {
        canMoveForward = state;
    }

    /**
     * @param state if "d" is pressed -> true; else -> false
     */
    public void setRotateLeft(boolean state) {
        rotateLeftPressed = state;
    }

    /**
     * @param state if "a" is pressed -> true; else -> false
     */
    public void setRotateRight(boolean state) {
        rotateRightPressed = state;
    }

    /**
     * @param state if the space bar is pressed -> true; else -> false
     */
    public void setFiring(boolean state) {
        firePressed = state;
    }

    /**
     * @param game - game frame
     */
    @Override
    public void update(GameFrame game) {
        super.update(game);
        // mapping out "a" & "d" pressing at the same time
        if (rotateLeftPressed != rotateRightPressed) {
            // "a" pressed action:
            if (rotateLeftPressed) {
                setRotation(-rotationSpeed);
            }
            // "d" pressed action:
            if (rotateRightPressed) {
                setRotation(rotationSpeed);
            }
        }

        // "w" pressed action:
        if (canMoveForward) {
            // acceleration:
            velocity.addVectorToVector(new DescartesVector(rotation).multiply_by_scalar(0.05));


            // Limiting infinite speed
            /*
            double maxSpeed = 4;
            if (velocity.vectorSizeSquared() >= maxSpeed * maxSpeed) {
                velocity.unitVector().multiply_by_scalar(maxSpeed);
            }
            */
        }

        // if the spaceship has velocity apply a slowing force
        // smaller number means slower acceleration
        if (velocity.vectorSizeSquared() != 0.0) {
            double slowingForce = 0.999;
            velocity.multiply_by_scalar(slowingForce);
        }

        // remove bullets if it is out of their range
        bullets.removeIf(Thing::needsRemoval);

        // fire cool-down is for preventing machine-gun-effect
        fireCD--;

        // if space bar is pressed and firing isn't on cool-down
        if (firePressed && fireCD <= 0) {
            // max number of bullets if holding space bar
            int bulletNumber = 3;
            if (bullets.size() < bulletNumber) {
                // reset fire cool-down
                fireCD = 3;

                // creating new bullets from spaceship & it's rotation / orientation
                Bullet bullet = new Bullet(this, rotation);
                // adding bullets to the "ammunition storage"
                bullets.add(bullet);
                // adding bullet to the game
                game.registerThing(bullet);
            }
        /*
            //consecutiveShots++;

            int maxShots = 8;
            //if (consecutiveShots == maxShots) {
            //  consecutiveShots = 0;
            //}
        } //else if (consecutiveShots > 0) {
        //consecutiveShots--;
        //}
        */
        }
    }

    /**
     * handleCollision - only handles the case of asteroid & spaceship collision, any other option is invalid
     *
     * @param game  - game frame
     * @param other - every "thing" in the game
     */
    @Override
    public void handleCollision(GameFrame game, Thing other) {
        if (other.getClass() == Asteroid.class) {
            game.destroyShip();
        }
    }

    /**
     * @param g2d  - 2D Graphics object for drawing
     * @param game - game frame
     * @param rgb  - coloring
     */
    @Override
    public void draw(Graphics2D g2d, GameFrame game, ArrayList<Integer> rgb) {
        // coordinates of a triangle
        int[] x = new int[]{-10, 10, -10};
        int[] y = new int[]{-8, 0, 8};

        // creates a triangle
        Polygon ship = new Polygon(x, y, 3);

        // draw the ship & color it
        g2d.draw(ship);
        g2d.setColor(new Color(rgb.get(0), rgb.get(1), rgb.get(2)));
        g2d.fill(ship);
    }

}