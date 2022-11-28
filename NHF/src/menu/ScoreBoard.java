package menu;

import data_management.FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * creates a frame where the score of the players are visible
 */
public class ScoreBoard {
    private final JPanel scores = new JPanel();
    private final JPanel back_panel = new JPanel();
    private final JFrame frame = new JFrame();
    private final int window_height;
    private final int window_width;

    /**
     * scoreData - stores the data from "scoreboard.txt"
     */
    private final ArrayList<String> scoreData;

    /**
     * @param h - height of the GameMenu
     * @param w - width of the GameMenu
     */
    public ScoreBoard(int h, int w) {
        window_height = h;
        window_width = w;
        /**
         * fileManagement - for reading data from "scoreboard.txt"
         */
        FileManagement fileManagement = new FileManagement();
        fileManagement.readFromScoreBoardTxt();
        scoreData = fileManagement.reorderScores();

        scores();
    }

    /**
     * score() - initialize the frame, sets up the parameters
     */
    public void scores() {
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(window_width, window_height));
        frame.getContentPane().setBackground(new Color(0x888888));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupScoreTable();
        back_button();
    }

    /**
     * setupScoreTable - creates a panel in the middle of the frame and adds a list into it
     */
    public void setupScoreTable() {

        /* DATA INTO SCORES */
        /**
         * trying to position the panel
         */
        final int scoretableWidth = 300;
        final int scoretableHeight = 400;
        int x = window_width / 2 - scoretableWidth / 2;
        int y = 0;

        /**
         * creates a layout for the scores
         */
        scores.setLayout(new BorderLayout());
        scores.setBounds(x, y, scoretableWidth, scoretableHeight);
        scores.setBackground(Color.RED);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        for (String scoreDatum : scoreData) {
            model.addElement(scoreDatum);
        }
        list.setFont(new Font("Tahoma", Font.PLAIN, 15));
        list.setBackground(new Color(0x888888));
        scores.setBackground(new Color(0x888888));
        scores.setBorder(BorderFactory.createTitledBorder("ScoreBoard:"));
        scores.add(list, BorderLayout.CENTER);
        frame.add(scores, BorderLayout.WEST);
    }

    /**
     * creates a button
     * on click it returns to the main menu
     */
    public void back_button() {
        JButton back = new JButton("Back");
        final int BUTTON_HEIGHT = 150;
        final int BUTTON_WIDTH = 150;
        int y_back = window_height - BUTTON_HEIGHT;
        int x_back = window_width / 2 - BUTTON_WIDTH / 2;

        back_panel.setLayout(new GridLayout(1, 1));
        back_panel.add(back);
        back_panel.setBounds(x_back, y_back, BUTTON_WIDTH, (BUTTON_HEIGHT / 2));
        frame.add(back_panel);

        back.addActionListener(new BackButtonAction());
    }

    /**
     * BackButtonAction - responsible for the action of "Back" button
     * disposes the current frame
     * malloc a GameMenu and initialize it
     */
    private class BackButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            new GameMenu().menuInit();
        }

    }
}