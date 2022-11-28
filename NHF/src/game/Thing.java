package game;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Thing implements Serializable {
    // position of a thing (X,Y) coordinates
    protected DescartesVector position;

    // vector for measuring the speed of a thing
    protected DescartesVector velocity;

    // turning / orienting a thing
    protected double rotation;

    // approaching / estimate the size of a thing by a circle
    protected double radius;

    // flag for removing a "thing"
    private boolean needsRemoval;

    // points for destroying an asteroid
    private final int scoredPoints;

    /**
     * @param pos  - position vector
     * @param vel  - velocity vector
     * @param rad  - radius
     * @param kill - points for killing
     */
    public Thing(DescartesVector pos, DescartesVector vel, double rad, int kill) {
        position = pos;
        velocity = vel;
        radius = rad;
        rotation = 0.0;
        scoredPoints = kill;
        needsRemoval = false;
    }

    /**
     * @param angle - a radian angle
     */
    public void setRotation(double angle) {
        rotation += angle;
        rotation %= Math.PI * 2;
    }

    /**
     * mustRemove - sets the removal flag True
     */
    public void mustRemove() {
        needsRemoval = true;
    }

    /**
     * update - update position vectors
     *
     * @param game - game frame
     */
    public void update(GameFrame game) {
        position.addVectorToVector(velocity);

        if (position.getX() < 0.0) {
            position.setXY(position.getX() + GamePanel.worldSize, position.getY());
        }
        if (position.getY() < 0.0) {
            position.setXY(position.getX(), position.getY() + GamePanel.worldSize);
        }
        position.setXY(position.getX() % GamePanel.worldSize, position.getY() % GamePanel.worldSize);
    }

    /**
     * @param thing - a "thing" in the game: spaceship / bullet / asteroid
     * @return true - if the thing is colliding; false - if it's not
     */
    public boolean collisionDetector(Thing thing) {
        // Pythagoras theorem
        // if the distance between the 2 things is less than the squared value of the 2 things' radius then it will collide
        //  thing1.position.distance(thing2.position) < (thing1.size + thing2.size)*(thing1.size + thing2.size)

        double radius = thing.getCollisionRadius() + getCollisionRadius();
        return (position.distanceOfVectors(thing.position) < radius * radius);
    }

    /**
     * @param game  - game frame
     * @param other - a "thing"
     */
    public abstract void handleCollision(GameFrame game, Thing other);

    /**
     * @param g2d  - 2D Graphics object for drawing
     * @param game - game frame
     * @param rgb  - color from saved data
     */
    public abstract void draw(Graphics2D g2d, GameFrame game, ArrayList<Integer> rgb);


    /**
     * getters - getScoredPoints(); getPosition(); getRotation(); getCollisionRadius(); getNeedsRemoval();
     */
    public int getScoredPoints() {
        return scoredPoints;
    }

    public DescartesVector getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }

    public double getCollisionRadius() {
        return radius;
    }

    public boolean getNeedsRemoval() {
        return needsRemoval;
    }
}
