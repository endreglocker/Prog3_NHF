package menu.character_creation;

import data_management.FileManagement;
import menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * CharacterCreationMenu - creates a frame which includes a color picker panel
 */
public class CharacterCreationMenu {
    JFrame frame;

    JPanel mainPanel;

    CharacterCreationPanel characterCreationPanel;

    /**
     * constructor - creates the frame and sets up the layout (a color picker, a text field for the player's name, and a button for saving the data and return to the main menu)
     */
    public CharacterCreationMenu() {
        // create a frame
        frame = new JFrame("Character Creation");

        // create a panel
        mainPanel = new JPanel(new BorderLayout());

        // create a character creation panel
        characterCreationPanel = new CharacterCreationPanel();

        // create a panel which contains a button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel innerPanel = new JPanel();
        // setting the inner panel's size
        innerPanel.setBounds(new Rectangle(300, 150));
        //button for saving within the panel
        JButton startGame = new JButton("Save Character");
        // adding an on click action for the button
        startGame.addActionListener(new StartGameButtonAction());

        // embedding the elements
        innerPanel.add(startGame);
        buttonPanel.add(innerPanel, BorderLayout.CENTER);
        mainPanel.add(characterCreationPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.pack();
    }

    /**
     * StartGameButtonAction - on click the button closes the current panel, saves the color and the username and opens the main menu
     */
    private class StartGameButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new FileManagement().saveLatestPlayer(characterCreationPanel.getUserName(), characterCreationPanel.getUserColor());
                frame.dispose();
                new GameMenu().menuInit();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
