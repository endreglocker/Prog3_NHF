package test;


import data_management.FileManagement;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * FileTest - testing functions in connection with file management
 */
public class FileTest {
    static FileManagement data = new FileManagement();

    /**
     * reads the data from lastplayer.txt file and splits the data into sections
     */
    @BeforeClass
    public static void init() {
        data.readLastPlayerTxt();
        data.splitPlayerData();
    }

    /**
     * tests if the reordered date from scoreboard is not null
     */
    @Test
    public void reorderTest() {
        assertNotNull(data.reorderScores());
    }

    /**
     * tests if the RGB color array from lastplayer.txt is not null
     */
    @Test
    public void colorTest() {
        assertNotNull(data.getPlayerColor());
    }

    /**
     * tests a saved game if it can be loaded successfully
     */
    @Test
    public void loadTest() {
        try {
            assertNotNull(data.loadGame());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
