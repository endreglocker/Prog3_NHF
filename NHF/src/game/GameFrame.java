package game;

import data_management.FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * GameFrame - the framework of the game, "child" class of JFrame, responsible for starting, updating and maintaining the game
 */
@SuppressWarnings("BusyWait")
public class GameFrame extends JFrame {

    // managing file input and output like reading/ writing the latest player, and saving the game
    private final FileManagement data = new FileManagement();

    // rgb color from "lastplayer.txt"
    private final ArrayList<Integer> rgb;

    // player's name from "lastplayer.txt"
    private final String playerName;

    //
    private final GamePanel worldPanel;

    // random for calculating random variables (e.g. starting position for asteroids)
    private Random random;

    // storing the current member of the game (bullets, asteroids, spaceship)
    private ArrayList<Thing> thingList;

    // storing incoming asteroids
    private ArrayList<Thing> thingsInQueue;

    // spaceship
    private SpaceShip ship;

    // score of the game
    private int score;

    // variable for flagging the game's state
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
        data.readLastPlayerTxt();
        rgb = data.getPlayerColor();
        playerName = data.getPlayerName();


        // creating the "playing field"
        worldPanel = new GamePanel(this);
        add(worldPanel, BorderLayout.CENTER);

        // create menu
        setJMenuBar(jMenuBar());

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
     * @return a JMenuBar which contains the save & exit function (used in constructor)
     */
    public JMenuBar jMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem exit = new JMenuItem("Save & Exit");

        exit.addActionListener(new SaveAndExitAction(this));

        menu.add(exit);
        menuBar.add(menu);
        return menuBar;
    }

    /**
     * SaveAndExitAction - on click closes the window, and saves the game
     */
    public class SaveAndExitAction implements ActionListener {
        GameFrame frame;

        // my goal is closing the GameFrame
        // to close the GameFrame the system need a reference to it
        // and cannot use "this" within SaveAndExitAction since it's a reference to SaveAndExitAction itself
        public SaveAndExitAction(GameFrame f) {
            frame = f;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // using data to save the current state of the game
            // by serialization
            try {
                data.saveGame(thingList);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // closing the frame
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));//dispose(); // dispose does not work somehow
        }
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
     * reset - setting the variables for a starting value
     */
    public void reset() {
        random = new Random();
        thingList = new ArrayList<>();
        thingsInQueue = new ArrayList<>();
        score = 0;
        isGameOver = false;
    }

    /**
     * GameLoop -  starts the updating process till GameOver
     */
    public void GameLoop() {
        Thread gameLoop = new Thread(() -> {
            while (!isGameOver) {
                //System.out.println(thingList.size()); // for debugging reasons

                // updating game members, and repaints them
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
                //serialize = thingList;
            }
        });
        gameLoop.start();
    }

    /**
     * startGame - sets variables + starts the updating process till GameOver by using GameLoop
     */
    public void startGame() {
        // sets variables
        reset();
        ship = new SpaceShip();
        clearThingList();

        // without it repaint will crash since it cannot repaint all members of the panel
        // auto self-killing if removed
        // too large number -> the program will run slow
        // too small -> crash, instant death
        // loop does not have enough time for repaint
        GameLoop();
    }

    /**
     * loadGame - loads the saved game (on the main menu select "Continue" to reach it)
     */
    public void loadGame() {
        try {
            // before loading the object the program clears / resets the game
            // to prevent crashing by unwanted game members / loading a different state of the game compared to the saved one
            reset();

            // loading the objects
            thingList = data.loadGame();
            // setting the spaceship
            ship = (SpaceShip) thingList.get(0);


            // without it repaint will crash since it cannot repaint all members of the panel
            // auto self-killing if removed
            // too large number -> the program will run slow
            // too small -> crash, instant death
            // loop does not have enough time for repaint
            GameLoop();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * updateGame - update the game's state, adds all the elements, creates the game-loop,
     */
    private void updateGame() {
        // if all the asteroids are destroyed, loads the next round of them
        thingList.addAll(thingsInQueue);
        thingsInQueue.clear();

        // if all asteroids are destroyed and the ship hasn't died yet
        // creates a new "wave" of asteroids
        if (!isGameOver && destroyedAsteroids()) {
            // creates random number of asteroids
            // 5 to 10 asteroids
            int randomNumberAsteroids = 5 + (int) (Math.random() % (15 - 5));
            //int randomNumberAsteroids = 1; // for testing reasons
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

        // remove destroyed asteroids
        thingList.removeIf(Thing::getNeedsRemoval);

        // clearing asteroids if the game is over
        if (isGameOver) {
            clearThingList();
            thingList.clear();
        }
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
     * @return true - if there isn't any asteroids in thinglist / false - if any asteroid is left in the list
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