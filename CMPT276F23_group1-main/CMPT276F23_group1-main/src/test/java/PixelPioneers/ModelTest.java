package PixelPioneers;
import org.junit.Test;
import org.junit.Before;

import java.awt.event.KeyEvent;

import static org.junit.Assert.*;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() {
        model = new Model();
    }

    @Test
    public void testGameInitialization() {
        assertFalse("Game should not be active initially", model.isInGame());
        assertEquals("Initial score should be zero", 0, model.getScore());
        assertEquals("Initial lives should be correctly set", 1, model.getLives());
    }

    @Test
    public void testCharacterMovement() {
        model.setInGame(true); // Simulating the start of the game
        int initialX = model.getCharacterX();

        // Simulate pressing the right arrow key
        model.simulateKeyPress(KeyEvent.VK_D);
        model.moveCharacterPublic();

        assertTrue("Character should move to the right", model.getCharacterX() > initialX);
    }

    @Test
    public void testCollectingPellet() {
        model.setInGame(true); // Start the game

        // Place a pellet at the character's position
        int pelletPos = model.getCharacterY() * model.getNBlocks() + model.getCharacterX(); // Use the getter for N_BLOCKS
        model.myBoard.getTile(pelletPos).setPellet(1);

        int initialScore = model.getScore();
        model.moveCharacterPublic(); // This should collect the pellet

        assertTrue("Score should increase after collecting a pellet", model.getScore() > initialScore);
    }

    @Test
    public void testAlienCollision() {
        model.setInGame(true);

        // Place the character at the same position as an alien
        model.setCharacterX(model.getAlienX()[0]); // Assuming you have a getter for alienX
        model.setCharacterY(model.getAlienY()[0]); // Assuming you have a getter for alienY
        model.moveAliensPublic(null);

        assertTrue("Character should be dying after collision with alien", model.isDying());
    }

}