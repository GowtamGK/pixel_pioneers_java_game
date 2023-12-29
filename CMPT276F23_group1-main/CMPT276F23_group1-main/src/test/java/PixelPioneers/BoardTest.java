package PixelPioneers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board board;
    private String[] mazeDesc = {
            "XXXX",
            "X  X",
            "XPBX",
            "XXXX"
    };

    @Before
    public void setUp() {
        board = new Board(4, 4, mazeDesc);
    }

    @Test
    public void testMazeParsingForWalls() {
        // Checking the corners for walls
        assertEquals("Top-left corner should be a wall", 1, board.getTile(0).getVoidTile());
        assertEquals("Top-right corner should be a wall", 1, board.getTile(3).getVoidTile());
        assertEquals("Bottom-left corner should be a wall", 1, board.getTile(12).getVoidTile());
        assertEquals("Bottom-right corner should be a wall", 1, board.getTile(15).getVoidTile());
    }

    @Test
    public void testMazeParsingForSpace() {
        // Checking the space inside walls
        assertEquals("Tile inside walls should be a space", 0, board.getTile(5).getVoidTile());
    }

    @Test
    public void testMazeParsingForPenalty() {
        // Checking for penalty
        assertEquals("Tile should have a penalty", 1, board.getTile(9).getPenalty());
    }

    @Test
    public void testMazeParsingForBonusReward() {
        // Checking for bonus reward
        assertEquals("Tile should have a bonus reward", 1, board.getTile(10).getBonusReward());
    }

}

