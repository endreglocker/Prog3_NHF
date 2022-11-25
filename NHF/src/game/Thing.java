package game;

import java.awt.*;
import java.util.ArrayList;

public abstract class Thing {
    protected DescartesVector position;

    protected DescartesVector velocity;

    protected double rotation;

    protected double radius;

    private boolean needsRemoval;

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

    public int getScoredPoints() {
        return scoredPoints;
    }

    public void mustRemove() {
        needsRemoval = true;
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

    public boolean needsRemoval() {
        return needsRemoval;
    }

    /**
     * update - update position vectors
     *
     * @param game - game frame
     */
    public void update(GameFrame game) {
        position.addVectorToVector(velocity);

        if (position.getX() < 0.0) {
            position.setXY(position.getX() + PlayPanel.worldSize, position.getY());
        }
        if (position.getY() < 0.0) {
            position.setXY(position.getX(), position.getY() + PlayPanel.worldSize);
        }
        position.setXY(position.getX() % PlayPanel.worldSize, position.getY() % PlayPanel.worldSize);
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
     *
     * @param game - game frame
     * @param other - a "thing"
     */
    public abstract void handleCollision(GameFrame game, Thing other);

    /**
     *
     * @param g2d - 2D Graphics object for drawing
     * @param game - game frame
     * @param rgb - color from saved data
     */
    public abstract void draw(Graphics2D g2d, GameFrame game, ArrayList<Integer> rgb);
}
