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
    JFrame f;
    JPanel mainPanel;
    CharacterCreationPanel create;

    /**
     * constructor - creates the frame and sets up the layout (a color picker, a text field for the player's name, and a button for saving the data and return to the main menu)
     */
    public CharacterCreationMenu() {
        f = new JFrame("Character Creation");
        mainPanel = new JPanel(new BorderLayout());

        create = new CharacterCreationPanel();

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel innerPanel = new JPanel();
        innerPanel.setBounds(new Rectangle(300, 150));
        JButton startGame = new JButton("Save Character");
        startGame.addActionListener(new StartGameButtonAction());
        innerPanel.add(startGame);
        buttonPanel.add(innerPanel, BorderLayout.CENTER);

        mainPanel.add(create, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        f.add(mainPanel);
        f.setVisible(true);
        f.pack();
    }

    /**
     * StartGameButtonAction - on click the button closes the current panel, saves the color and the username and opens the main menu
     */
    private class StartGameButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new FileManagement().saveLatestPlayer(create.getUserName(), create.getUserColor());
                f.dispose();
                new GameMenu().menu_init();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
