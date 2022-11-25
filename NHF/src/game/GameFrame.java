package game;

import data_management.FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

@SuppressWarnings("ALL")
public class GameFrame extends JFrame {
    // rgb color from "lastplayer.txt"
    private final ArrayList<Integer> rgb;

    // player's name from "lastplayer.txt"
    private final String playerName;

    private final PlayPanel worldPanel;

    // random for calculating random variables (e.g. starting position for asteroids)
    private Random random;

    // storing the current member of the game (bullets, asteroids, spaceship)
    private List<Thing> thingList;

    // storing incoming asteroids
    private List<Thing> thingsInQueue;

    //
    private SpaceShip ship;

    //
    private int score;

    //
    private boolean isGameOver;

    /**
     * GameFrame - creates the layout, reads the data from "lastplayer.txt" and sets the variables
     */
    public GameFrame() {
        // create name, layout, etc.
        super("Asteroids");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // reading "lastplayer.txt"
        FileManagement data = new FileManagement();
        data.readLastPlayerTxt();
        rgb = data.getPlayerColor();
        playerName = data.getPlayerName();

        // creating the "playing field"
        worldPanel = new PlayPanel(this);
        add(worldPanel, BorderLayout.CENTER);

        // creates control
        controller();

        // make it visible
        setVisible(true);

        // centering the frame
        //setLocationRelativeTo(null);

        // verify modifications
        pack();


    }

    /**
     * controller - sets the spaceship's movement control and let it fire
     */
    public void controller() {

        addKeyListener(new KeyAdapter() {
            // event for pressing the key
            @Override
            public void keyPressed(KeyEvent e) {
                // determine which key was pressed.
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> ship.setMoveForward(true);
                    case KeyEvent.VK_A -> ship.setRotateLeft(true);
                    case KeyEvent.VK_D -> ship.setRotateRight(true);
                    case KeyEvent.VK_SPACE -> ship.setFiring(true);
                    default -> {
                    }
                }
            }

            // event for releasing the key
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> ship.setMoveForward(false);
                    case KeyEvent.VK_A -> ship.setRotateLeft(false);
                    case KeyEvent.VK_D -> ship.setRotateRight(false);
                    case KeyEvent.VK_SPACE -> ship.setFiring(false);
                }
            }
        });
    }

    /**
     * startGame - sets variables + starts the updating process till gameover
     */
    public void startGame() {
        // sets variables
        random = new Random();
        thingList = new LinkedList<>();
        thingsInQueue = new ArrayList<>();
        ship = new SpaceShip();
        score = 0;
        isGameOver = false;
        clearThingList();

        while (!isGameOver) {
            updateGame();
            worldPanel.repaint();

            // without it repaint will crash since it cannot repaint all members of the panel
            // auto self-killing if removed
            try {
                // too large number -> the program will run slow
                // too small -> crash, instant death
                sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * updateGame - update the game's state
     */
    private void updateGame() {
        // if all the asteroids are destroyed, loads the next round of them
        thingList.addAll(thingsInQueue);
        thingsInQueue.clear();

        // if all asteroids are destroyed and the ship hasn't died yet
        // creates a new "wave" of asteroids
        if (!isGameOver && destroyedAsteroids()) {
            //firing bug ?
            //ship.setFiringEnabled(true);

            // creates random number of asteroids
            // 5 to 10 asteroids
            int randomNumberAsteroids = 5 + (int) (Math.random() % (15 - 5));
            for (int i = 0; i < randomNumberAsteroids; i++) {
                registerThing(new Asteroid(random));
            }
        }

        // update every "thing" in thingList
        for (Thing thing : thingList) {
            thing.update(this);
        }

        // looking for collision possibilities

        for (int i = 0; i < thingList.size(); i++) {
            Thing thingInstance1 = thingList.get(i);
            for (int j = i + 1; j < thingList.size(); j++) {
                Thing thingInstance2 = thingList.get(j);
                if (i != j && thingInstance1.collisionDetector(thingInstance2) && ((thingInstance1 != ship && thingInstance2 != ship) || !isGameOver)) {
                    thingInstance1.handleCollision(this, thingInstance2);
                    thingInstance2.handleCollision(this, thingInstance1);
                }
            }
        }

        // clearing asteroids if the game is over
        if (isGameOver) {
            clearThingList();
        }

        // remove destroyed asteroids
        thingList.removeIf(Thing::needsRemoval);
    }

    /**
     * clearThingList - clears every list (except the spaceship)
     */
    private void clearThingList() {
        thingsInQueue.clear();
        thingList.clear();
        thingList.add(ship);
    }

    /**
     * @return true - if there isn't any asteroids in thinglist || false - if any asteroid is left in the list
     */
    private boolean destroyedAsteroids() {
        for (Thing e : thingList) {
            if (e.getClass() == Asteroid.class) {
                return false;
            }
        }
        return true;
    }

    /**
     * destroyShip - if the ship is destroyed it cannot fire and the game is over
     */
    public void destroyShip() {
        isGameOver = true;
        //ship.setFiringEnabled(false);
    }

    /**
     * @param points - increment score by the points for destroying an asteroid
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * @param thing - asteroid / bullet (/ spaceship - this isnt an option)
     */
    public void registerThing(Thing thing) {
        thingsInQueue.add(thing);
    }

    /**
     * @return if the game is over or not
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return random number
     */
    public Random getRandom() {
        // changed my mind
        //return new Random();
        return random;
    }

    /**
     * @return things in the game
     */
    public List<Thing> getThingList() {
        return thingList;
    }

    /**
     * @return color values of the spaceship
     */
    public ArrayList<Integer> getRgb() {
        return rgb;
    }

    /**
     * @return name of the player
     */
    public String getPlayerName() {
        return playerName;
    }
}