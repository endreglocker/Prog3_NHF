package data_management;

import game.Thing;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileManagement - responsible for the database management of the game like saving the player's name and the chosen color
 */
public class FileManagement {
    // name of the player
    private String player;

    // color of the spaceship
    private String color;

    // file where the scored points and the player's name is saved
    private final File scoreboard = new File("scoreboard.txt");

    // file where the latest modification of character creation is saved
    private final File lastPlayer = new File("lastplayer.txt");

    // the red green & blue (int) values of the spaceship color
    private final ArrayList<Integer> rgb = new ArrayList<>();

    // the data of the scoreboard
    private final ArrayList<String> scores = new ArrayList<>();

    /**
     * appendToScoreBoardTxt - appends the latest round of the game to the "scoreboard.txt" file
     *
     * @param name  - the name of the player
     * @param score - the score of the player
     * @throws IOException - if the file cannot be opened
     */
    public void appendToScoreBoardTxt(String name, int score) throws IOException {
        FileWriter fw = new FileWriter(scoreboard, true);
        //fw.append(score + " " + name + "\n");
        fw.append(String.valueOf(score)).append(" ").append(name).append("\n");
        fw.close();
    }

    /**
     * readFromScoreBoardTxt - reads the data of the scoreboard
     */
    public void readFromScoreBoardTxt() {
        try {
            Scanner scanner = new Scanner(scoreboard);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                scores.add(data);

            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot Open 'scoreboard.txt'");
            e.printStackTrace();
        }
    }

    /**
     * saveLatestPlayer - save the data of the latest state of character creation
     *
     * @param playerName  - name of the player
     * @param playerColor - color of the spaceship
     * @throws IOException - if the file cannot be opened
     */
    public void saveLatestPlayer(String playerName, Color playerColor) throws IOException {
        FileWriter fw = new FileWriter(lastPlayer);

        PrintWriter pw = new PrintWriter(fw);

        pw.println(playerName + "\n" + playerColor);

        pw.close();
    }

    /**
     * readLastPlayerTxt - read the data of the "lastplayer.txt" file
     */
    public void readLastPlayerTxt() {
        try {
            Scanner scanner = new Scanner(lastPlayer);
            while (scanner.hasNextLine()) {
                player = scanner.nextLine();
                color = scanner.nextLine();
                splitPlayerData();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * @return player - name of the player from "lastplayer.txt"
     */
    public String getPlayerName() {
        return player;
    }

    /**
     * @return rgb - an array which contains the red green and blue value of the color (int)
     */
    public ArrayList<Integer> getPlayerColor() {
        return rgb;
    }

    /**
     * splitPlayerData - the problem with saving color into a file is the format:
     * java.awt.Color[r=255,g=255,b=255]
     * in order to change the color of the ship this function split the above-mentioned string into 3 integers: red, green and blue
     */
    public void splitPlayerData() {
        System.out.println(player);
        System.out.println(color);

        int redStart = color.indexOf("r=") + 2;
        int redEnd = color.indexOf(",g");
        String redColorString = color.substring(redStart, redEnd);
        rgb.add(Integer.parseInt(redColorString));
        System.out.println("red:    " + rgb.get(0));

        int greenStart = color.indexOf("g=") + 2;
        int greenEnd = color.indexOf(",b");
        String greenColorString = color.substring(greenStart, greenEnd);
        rgb.add(Integer.parseInt(greenColorString));
        System.out.println("green:    " + rgb.get(1));

        int blueStart = color.indexOf("b=") + 2;
        int blueEnd = color.indexOf(']');
        String blueColorString = color.substring(blueStart, blueEnd);
        rgb.add(Integer.parseInt(blueColorString));
        System.out.println("blue:    " + rgb.get(2));
    }

    /**
     * @return - creates a numerical order among the date of the scoreboard
     */
    public ArrayList<String> reorderScores() {

        // scoreValue - player's score from "scoreboard.txt"
        // nameValue - player's name from "scoreboard.txt"
        ArrayList<Integer> scoreValue = new ArrayList<>();
        ArrayList<String> nameValue = new ArrayList<>();

        // split the scores array into 2 arrays: scoreValue and nameValue
        for (String score : scores) {
            String[] fragment = score.split(" ");
            int handleValue = Integer.parseInt(fragment[0]);
            scoreValue.add(handleValue);
            nameValue.add(score);
        }


        // creating order among values by comparing

        for (int i = 0; i < scoreValue.size() - 1; i++) {
            for (int j = i + 1; j < scoreValue.size(); j++) {
                if (scoreValue.get(i) < scoreValue.get(j)) {
                    int tempInt = scoreValue.get(i);
                    String tempString = nameValue.get(i);

                    scoreValue.set(i, scoreValue.get(j));
                    nameValue.set(i, nameValue.get(j));

                    scoreValue.set(j, tempInt);
                    nameValue.set(j, tempString);
                }
            }
        }


        //clearing the scoreboard array before adding the values back in numerical order
        scores.clear();

        for (int i = 0; i < scoreValue.size(); i++) {
            scores.add(nameValue.get(i));
        }

        return scores;
    }

    /**
     * @return an ArrayList container, which contains all members of the game e.g. spaceship, asteroids
     * @throws IOException            - unable to open "saved_game.txt"
     * @throws ClassNotFoundException -
     */
    public ArrayList<Thing> loadGame() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("saved_game.txt"));

        ArrayList<Thing> container = (ArrayList<Thing>) in.readObject();
        in.close();
        return container;
    }

    /**
     * @param thingArrayList - a list which contains every element from the game e.g. spaceship, asteroids
     * @throws IOException - unable to open "saved_game.txt"
     */
    public void saveGame(ArrayList<Thing> thingArrayList) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saved_game.txt"));

        out.writeObject(thingArrayList);
        out.close();
    }
}
