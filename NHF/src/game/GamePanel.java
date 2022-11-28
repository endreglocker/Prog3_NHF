package game;

import data_management.FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    // red green blue values for color
    private final ArrayList<Integer> rgb;

    // player name
    private final String playerName;

    // font for writing massage on screen
    private static final Font title = new Font("Dialog", Font.PLAIN, 25);

    private final GameFrame game;

    // size of the panel
    static final int worldSize = 900;

    /**
     * @param g - game frame
     */
    public GamePanel(GameFrame g) {
        game = g;

        // setting size, background color
        setPreferredSize(new Dimension(worldSize, worldSize));
        setBackground(Color.BLACK);

        // color & player name
        rgb = g.getRgb();
        playerName = g.getPlayerName();
    }

    /**
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.0F));

        // changing color to rgb
        g2d.setColor(new Color(rgb.get(0), rgb.get(1), rgb.get(2)));

        /*
        	getTransform()
            Returns a copy of the current Transform in the Graphics2D context.
         */

        AffineTransform id = g2d.getTransform();

        // every "thing" in game
        for (Thing thing : game.getThingList()) {
            // get the position of each thing
            DescartesVector position = thing.getPosition();

            // draw thing
            drawThing(g2d, thing, position.getX(), position.getY());
            g2d.setTransform(id);

            // if a "thing" leaves the panel from one side it should "re-appear" on the other side
            // calculating positions

            // the size of the thing for calculating coordinates
            double thingHitBoxSize = thing.getCollisionRadius();

            // determine the X coordinate of the reappearing asteroid
            double x = position.getX();
            if (position.getX() < thingHitBoxSize) {
                x = position.getX() + worldSize;
            } else if (position.getX() > worldSize - thingHitBoxSize) {
                x = position.getX() - worldSize;
            }

            // determine the Y coordinate of the reappearing asteroid
            double y = position.getY();
            if (position.getY() < thingHitBoxSize) {
                y = position.getY() + worldSize;
            } else if (position.getX() > worldSize - thingHitBoxSize) {
                y = position.getY() - worldSize;
            }


            // draw them again from the other side of the panel
            if (x != position.getX() || y != position.getY()) {
                drawThing(g2d, thing, x, y);
                g2d.setTransform(id);
            }
        }

        // showing score till GameOver (bottom left corner)
        if (!game.isGameOver()) {
            graphics.drawString(playerName + " Score: " + game.getScore(), 10, worldSize - 15);
        }

        // showing final score
        // saving score + username
        if (game.isGameOver()) {
            drawText("Game Over", g2d, 25);
            drawText(playerName + " scored: " + game.getScore(), g2d, 0);
            try {
                new FileManagement().appendToScoreBoardTxt(playerName, game.getScore());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param text   - the text that appears on the screen
     * @param g2d    - 2D Graphics object
     * @param offset - offset for positioning
     */
    private void drawText(String text, Graphics2D g2d, int offset) {
        // try it later to center!

        //coordinates for drawing text
        int y = worldSize / 2 + offset - 100;

        // centering on the X axis based on the world size and the length of the text
        int halfTextLength = g2d.getFontMetrics().stringWidth(text) / 2;
        int x = worldSize / 2 - halfTextLength - offset;

        g2d.setFont(title);
        g2d.drawString(text, x, y);
    }

    /**
     * @param g2d   - 2D Graphics object
     * @param thing - spaceship / bullet / asteroid
     * @param x     - x coordinate
     * @param y     - y coordinate
     */
    private void drawThing(Graphics2D g2d, Thing thing, double x, double y) {
        /*
            translate(double tx, double ty)
            concatenates the current Graphics2D Transform with a translation transform.
         */
        g2d.translate(x, y);
        double rotation = thing.getRotation();
        if (rotation != 0.0) {
            g2d.rotate(thing.getRotation());
        }
        thing.draw(g2d, game, rgb);
    }

}