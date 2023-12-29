package PixelPioneers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TileTest {
    private Tile tile;

    @Before
    public void setUp() {
        tile = new Tile();
    }

    @Test
    public void testSetBottomWall() {
        tile.setBottomWall(1);
        assertEquals("Bottom wall should be set to 1", 1, tile.getBottomWall());
    }

    @Test
    public void testSetLeftWall() {
        tile.setLeftWall(1);
        assertEquals("Left wall should be set to 1", 1, tile.getLeftWall());
    }

    @Test
    public void testSetRightWall() {
        tile.setRightWall(1);
        assertEquals("Right wall should be set to 1", 1, tile.getRightWall());
    }

    @Test
    public void testSetPellet() {
        tile.setPellet(1);
        assertEquals("Pellet should be set to 1", 1, tile.getPellet());
    }

    @Test
    public void testSetPenalty() {
        tile.setPenalty(1);
        assertEquals("Penalty should be set to 1", 1, tile.getPenalty());
    }

    @Test
    public void testSetEndTile() {
        tile.setEndTile(1);
        assertEquals("End tile should be set to 1", 1, tile.getEndTile());
    }

    @Test
    public void testSetBonusReward() {
        tile.setBonusReward(1);
        assertEquals("Bonus reward should be set to 1", 1, tile.getBonusReward());
    }

    @Test
    public void testSetVoidTile() {
        tile.setVoidTile(1);
        assertEquals("Void tile should be set to 1", 1, tile.getVoidTile());
    }

    @Test
    public void testSetPelletCollected() {
        tile.setPelletCollected(1);
        assertEquals("Pellet collected should be set to 1", 1, tile.getPelletCollected());
    }
}
