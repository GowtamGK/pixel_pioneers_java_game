package PixelPioneers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModelBoardIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new Model();
    }

    @Test
    public void testBoardInitialization() {
        Board board = model.getBoard(); // Assuming Model class has a getBoard() method
        assertNotNull("Board should be initialized", board);

        // Testing the initialization of the first tile
        Tile firstTile = board.getTile(0);
        assertEquals("First tile should be a wall", 1, firstTile.getVoidTile());

        // Additional checks can be added as necessary
    }
}
