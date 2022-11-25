package menu;


import game.GameFrame;
import menu.character_creation.CharacterCreationMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu {
    private final JFrame f = new JFrame("Asteroids");
    private final int windowWidth = 900;
    private final int windowHeight = 600;

    /**
     * sets the frame visible, changes the background color, and the size of the frame
     */
    public void menu_init() {
        f.setVisible(true);
        f.setLayout(null);
        f.setResizable(false);
        f.getContentPane().setBackground(new Color(0x888888));
        //f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // either this, or setPreferredSize() with pack()
        f.setPreferredSize(new Dimension(windowWidth, windowHeight));
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.add(menu_panel());
        f.pack();
    }

    /**
     *
     * @return a more or less centered panel where the menu buttons are laying
     */
    JPanel menu_panel() {
        JPanel panel = new JPanel();
        final int PANEL_WIDTH = 150;
        final int PANEL_HEIGHT = 300;

        int x = windowWidth / 2 - PANEL_WIDTH / 2;
        int y = windowHeight / 2 - PANEL_HEIGHT / 2;

        panel.setBounds(x, y, PANEL_WIDTH, PANEL_HEIGHT);

        panel.setLayout(new GridLayout(5, 1));

        setButtons(panel);
        return panel;
    }

    /**
     * setButtons - creates the buttons for each possible option in the main menu and adds them to the JPanel
     *
     * @param panel - JPanel which contains the buttons
     */
    void setButtons(JPanel panel) {
        JButton exit = new JButton("Exit");
        JButton scoreBoard = new JButton("ScoreBoard");
        JButton createPlayer = new JButton("Create Player");
        JButton play = new JButton("Play");
        JButton continueGame = new JButton("Continue");

        exit.addActionListener(toExit -> f.dispose());

        scoreBoard.addActionListener(new ScoreBoardAction());

        createPlayer.addActionListener(new CreatePlayerAction());

        play.addActionListener(new StartGameAction());

        panel.add(continueGame);
        panel.add(play);
        panel.add(createPlayer);
        panel.add(scoreBoard);
        panel.add(exit);

    }

    /**
     * StartGameAction - on click starts the game
     */
    public class StartGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameFrame gameFrame = new GameFrame();
            f.setVisible(false);
            gameFrame.startGame();
        }
    }

    /**
     * ScoreBoardAction - on click shows the scoreboard, disposes the current frame
     */
    public class ScoreBoardAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            f.dispose();
            new ScoreBoard(windowHeight, windowWidth);
        }
    }

    /**
     * on click shows the character creation panel, disposes the current one
     */
    public class CreatePlayerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            f.dispose();
            new CharacterCreationMenu();
        }
    }

    public static void main(String[] args) {
        // Create the main menu:
        new GameMenu().menu_init();

        // Create the playable game:
        //new GameFrame().startGame();
    }
}
