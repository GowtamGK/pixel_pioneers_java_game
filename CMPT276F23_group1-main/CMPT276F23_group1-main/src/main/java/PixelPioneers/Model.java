package PixelPioneers;

//imports for GUI and Interactions 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {
    //variables
	private Dimension d;                                    //dimesion of game
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);    //to print small text to screen
    private final Font largeFont = new Font("Arial", Font.BOLD, 30);    //to print large text to screen
    private boolean inGame = false;                         //is user in game
    private boolean dying = false;                          //should character die
    private boolean end, won, loss, finished = false;       //game ending requirements
    private boolean noBonus = true;                         //is there a bonus rewrd or not on board

    private final int BLOCK_SIZE = 24;                      //pixels of one cell
    private final int N_BLOCKS = 23;                        //how many cells are on the board
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;  //length of screen 
    private final int CHARACTER_SPEED = 2;                  //speed of character
    private final int ALIEN_SPEED = 1;                      //speed of enemy
    private final int DELAY = 10;                           //delay in ms of refresh rate
    private final int N_ALIENS = 3;                         //number of aliens of the board

    private int lives, score, time;                             //game attributes
    private int[] alienX, alienY, alienDX, alienDY, alienSpeed; //alien attributes
    private int[] alienToCharacter;                             //where enemy is compared to player
    private int validTiles, bonusTile;                          //num of valid tiles for bonus reward, and randomly selected tile for bonus reward

    private Image alien;                                            //alien sprite
    private Image up, down, left, right;                            //character sprites
    private int characterX, characterY, characterDX, characterDY;   //character attributes
    private int moveHorizontal, moveVertical;                       //movement
    private Image curCharacterImage;                                //hold current character sprite

    public boolean isInGame() {
        return inGame;
    }

    // Public setter for the 'inGame' field
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getCharacterX() {
        return characterX;
    }

    public int getCharacterY() {
        return characterY;
    }


    // Getters and Setters for characterX and characterY
    public void setCharacterX(int x) {
        characterX = x;
    }

    public void setCharacterY(int y) {
        characterY = y;
    }

    // Assuming alienX and alienY are arrays, you need getters for the entire array.
    // If you need to get or set individual alien positions, you'll need more specific methods.
    public int[] getAlienX() {
        return alienX;
    }

    public int[] getAlienY() {
        return alienY;
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isWon() {
        return won;
    }

    // Getter for N_BLOCKS
    public int getNBlocks() {
        return N_BLOCKS;
    }

    // Add public methods for actions
    public void moveCharacterPublic() {
        moveCharacter();
    }

    public void moveAliensPublic(Graphics2D g2d) {
        moveAliens();
        if (g2d != null) {
            for (int i = 0; i < N_ALIENS; i++) {
                drawAlien(g2d, alienX[i] + 1, alienY[i] + 1);
            }
        }
    }

    // A method to simulate key presses for testing
    public void simulateKeyPress(int keyCode) {
        KeyAdapter keyAdapter = new TAdapter();
        KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        keyAdapter.keyPressed(keyEvent);
    }

    public Board getBoard() {
        return myBoard;
    }

    public void loseLife() {
        lives = 0;
    }

    public void checkGameStatus() {
        if (lives <= 0) {
            inGame = false;
        }
    }

    public void testMoveAndCheckPosition() {
        moveCharacter(); // This calls the existing private method to move the character
    }

    String[] simple_maze = {        //Layout of maze to be parsed
        "XXXXXXXXXXXXXXXXXXXXXXXXX",
        "X                       X",  
        "X      XXXXX XXXXX    P X",
        "X  XXX XXXXX XXXXX XXX  X",
        "X PXXX XXXXX XXXXX XXX  X",
        "X                       X",
        "X XXX  X    P    X  XXX X",
        "X XXX  X  XXXXX  X  XXX X",
        "X      X         X      X",
        "X XXX  X   X X   X  XXX X",
        "X XXX  X   X X   X  XXX X",
        "X XXX  X         X  XXX X",
        "X XXX  X  XXXXX  X  XXX X",
        "X  P                    X",
        "X      XXXXX XXXXX      X",
        "X                      PX",
        "X         P             X",
        "X X XX XX XXXXX XX XX X X",
        "X X XX XX XXXXX XX XX X X",
        "X                       X",
        "X   XXXXXXXX XXXXXXXX   X",
        "X   XXXXXXXX XXXXXXXX P X",
        "X  PXXXXXXXX XXXXXXXX   X",  
        "X                      EX",
        "XXXXXXXXXXXXXXXXXXXXXXXXX",
    };
    Board myBoard = new Board(25, 25, simple_maze); //intantiate parsed board

    private Timer timer;                                                        //keep time in ms
    private int timeCount = 0;                                                  //keep track of time
    private int bonusTimeCount, bonusTime, noBonusTimeCount, noBonusTime = 0;   //bonus reward related time vars

    public Model() {    //constructor
        //prepare the game
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    private void loadImages() {
        try{
            //get sprites from resources
            down = ImageIO.read(Model.class.getClassLoader().getResource("down.png"));
            up = ImageIO.read(Model.class.getClassLoader().getResource("up.png"));
            left = ImageIO.read(Model.class.getClassLoader().getResource("left.png"));
            right = ImageIO.read(Model.class.getClassLoader().getResource("right.png"));
            alien = ImageIO.read(Model.class.getClassLoader().getResource("alien.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initVariables() {
        //initialize various variables
        d = new Dimension(SCREEN_SIZE+40, SCREEN_SIZE+40);
        alienX = new int[N_ALIENS];
        alienDX = new int[N_ALIENS];
        alienY = new int[N_ALIENS];
        alienDY = new int[N_ALIENS];
        alienSpeed = new int[N_ALIENS];
        alienToCharacter = new int[N_ALIENS];
        
        //begin timer with 10ms delay
        timer = new Timer(DELAY, this);
        timer.start();

        //default start sprite is downward facing
        curCharacterImage = down;
    }

    private void playGame(Graphics2D g2d) {
        //should character die
        if (dying) {
            death();
        //else continue playing game
        } else {
            moveCharacter();
            drawCharacter(g2d);
            moveAliens();
            checkMaze();

            //game timer
            if (timer.isRunning()) {
                timeCount++;
            }
            if (timeCount%(1000/DELAY)==0) {
                time++; //count seconds
            }

            //timer for when to spawn bonus item
            int spawnTimer = (int)(10 + (Math.random() * 5)); //random time between 10-15 sec
            if (timer.isRunning() && (noBonus == true)) {
                bonusTimeCount++; //increase while no bonus on board
            }
            if ((bonusTimeCount%(1000/DELAY)==0) && (noBonus == true)) {
                bonusTime++; //get time in seconds while there has been no bonus
            }
            if ((bonusTime == spawnTimer) && (noBonus == true)) {
                //when bonus reaches random time, spawn bonus and reset timers
                spawnBonusReward(); 
                bonusTimeCount = 0;
                bonusTime = 0;
            }

            //timer for when to despawn bonus item
            int despawnTimer = 8;   //despawn after 8 sec
            if (timer.isRunning() && (noBonus == false)) {
                noBonusTimeCount++; //increase while there is a bonus on board
            }
            if ((noBonusTimeCount%(1000/DELAY)==0) && (noBonus == false)) {
                noBonusTime++; //get time in seconds while there has been a bonus
            }
            if ((noBonusTime == despawnTimer) && (noBonus == false)) {
                //after 8 sec, despawn bonus and reset timers
                despawnBonusReward();
                noBonusTimeCount = 0;
                noBonusTime = 0;
            }
        }
        moveAliens(); // Move aliens without drawing
        if (g2d != null) {
            drawAliens(g2d); // Only draw if g2d is not null
        }
    }

    private void spawnBonusReward() {
        Tile curTile;
        validTiles = 0;
        //go through all cells
        for(int i = 0; i < (N_BLOCKS * N_BLOCKS); i++) {
            curTile = myBoard.getTile(i);
            //count cells valid for bonus
            if(curTile.getPellet() == 1 || curTile.getPelletCollected() == 1) {
                validTiles++;
            }
        }
        //select random valid tile for bonus
        bonusTile = (int) ((Math.random() * validTiles) - 1);
        int i = 0;
        int tilesChecked = 0;
        //loop until bonus is placed on correct tile
        while(tilesChecked < validTiles) {
            curTile = myBoard.getTile(i);
            //increment tiles checked when valid tile is passed
            if(curTile.getPellet() == 1 || curTile.getPelletCollected() == 1) {
                tilesChecked++;
            }
            //spawn bonus if correct tile
            if((noBonus == true) && (tilesChecked == bonusTile)) {
                curTile.setBonusReward(1);
                noBonus = false;
                break;
            }
            i++;
        }
    }

    private void despawnBonusReward() {
        //loop through all cells
        for(int i = 0; i < (N_BLOCKS * N_BLOCKS); i++) {
            Tile curTile = myBoard.getTile(i);
            //despawn bonus
            if(curTile.getBonusReward() == 1) {
                curTile.setBonusReward(0);
                noBonus = true;
            }
        }
    }

    private void showIntroScreen(Graphics2D g2d) { //display the starting screen
    	String start = "Press SPACE to start";
        g2d.setFont(smallFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/2 - 170, SCREEN_SIZE + 16);
    }

    private void drawScore(Graphics2D g) { //write score to screen
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);
    }

    private void drawTime(Graphics2D g) { //write time to screen
        g.setFont(smallFont);
        g.setColor(new Color(255, 255, 0));
        String s = "Time " + time;
        g.drawString(s, (SCREEN_SIZE / 2), SCREEN_SIZE + 16);
    }

    private void drawWin(Graphics2D g) { //write winning message to screen
        g.setFont(largeFont);
        g.setColor(new Color(255, 255, 0));
        String s = "YOU WIN!";
        g.drawString(s, (SCREEN_SIZE / 4) + 50, 120);
    }

    private void drawLoss(Graphics2D g) { //write losing message to screen
        g.setFont(largeFont);
        g.setColor(new Color(255, 0, 0));
        String s = "YOU LOSE!";
        g.drawString(s, (SCREEN_SIZE / 4) + 60, 120);
    }

    private void checkMaze() {
        int i = 0;
        finished = true;

        //loop through all cells
        while (i < N_BLOCKS * N_BLOCKS && finished) {
            Tile curTile = myBoard.getTile(i);
            //not finished collecting rewards if any pellet is on board
            if (curTile.getPellet() == 1) {
                finished = false;
            }
            i++;
        }
        if (end) {  //if player collected all rewards and is on end tile
            inGame = false;
            won = true;
            end = false;
        }
    }

    private void death() {
    	lives--; //decrement lives for death
        //end game if lives are 0
        if (lives == 0) {
            inGame = false;
            loss = true;
        }
    }

    private void moveAliens() {
        int pos;
        //loop through all aliens
        for (int i = 0; i < N_ALIENS; i++) {
            //make sure alien is in line with cell
            if (alienX[i] % BLOCK_SIZE == 0 && alienY[i] % BLOCK_SIZE == 0) {
                //get alien position
                pos = alienX[i] / BLOCK_SIZE + N_BLOCKS * (int) (alienY[i] / BLOCK_SIZE);
                Tile curTile = myBoard.getTile(pos);
            
                //get alien position in relation to character
                alienToCharacter[0] = alienX[i] - characterX; //x direction
                alienToCharacter[1] = alienY[i] - characterY; //y direction

                //find which direction alien should move (x)
                if((alienToCharacter[0] < 0) && (curTile.getRightWall() == 0)) {
                    alienDX[i] = 1;
                } else if((alienToCharacter[0] > 0) && (curTile.getLeftWall() == 0)) {
                    alienDX[i] = -1;
                } else {
                    alienDX[i] = 0;
                }

                //find which direction alien should move (y)
                if((alienToCharacter[1] < 0) && (curTile.getBottomWall() == 0)) {
                    alienDY[i] = 1;
                } else if((alienToCharacter[1] > 0) && (curTile.getTopWall() == 0)) {
                    alienDY[i] = -1;
                } else {
                    alienDY[i] = 0;
                }

                //randomly choose to move in x or y if both option move closer to player
                if(alienDX[i] != 0 && alienDY[i] != 0) {
                    int randDir = (int)(Math.random() * 2);
                    if(randDir == 0) {
                        alienDY[i] = 0;
                    } else if(randDir == 1) {
                        alienDX[i] = 0;
                    }
                }
            }
            //loop through aliens to check for alien on alien collision
            for(int j = 0; j < N_ALIENS; j++) {
                //don't check against self
                if(j == i) {

                //check against other aliens
                } else {
                    //find what new position of current alien will be
                    int newPosx = alienX[i] + (alienDX[i] * alienSpeed[i]);
                    int newPosy = alienY[i] + (alienDY[i] * alienSpeed[i]);
                    //compare to location of other aliens
                    if((newPosx < alienX[j] + 24) && (newPosx > alienX[j] - 24) && 
                       (newPosy < alienY[j] + 24) && (newPosy > alienY[j] - 24)) {
                        //if alien will overlap dont move alien
                        alienDX[i] = 0;
                        alienDY[i] = 0;
                        //set aliens position to compare with other aliens
                        alienX[i] = alienX[i] + (alienDX[i] * alienSpeed[i]);
                        alienY[i] = alienY[i] + (alienDY[i] * alienSpeed[i]); 
                    }   
                }
            }
            //actually move alien
            alienX[i] = alienX[i] + (alienDX[i] * alienSpeed[i]);
            alienY[i] = alienY[i] + (alienDY[i] * alienSpeed[i]); 

            //if alien touches character, kill character
            if (characterX > (alienX[i] - 12) && characterX < (alienX[i] + 12)
                    && characterY > (alienY[i] - 12) && characterY < (alienY[i] + 12)
                    && inGame) {
                dying = true;
            }
            //if score is below 0 kill character
            if (score < 0) {
                dying = true;
            }
        }
    }

    private void drawAlien(Graphics2D g2d, int x, int y) { //draw alien sprite
    	g2d.drawImage(alien, x, y, this);
    }

    private void drawAliens(Graphics2D g2d) {
        // Now this method is only responsible for drawing aliens
        for (int i = 0; i < N_ALIENS; i++) {
                drawAlien(g2d, alienX[i] + 1, alienY[i] + 1);
        }
    }

    private void moveCharacter() {
        int pos;
        //make sure character is in line with cell
        if (characterX % BLOCK_SIZE == 0 && characterY % BLOCK_SIZE == 0) {
            //get character position
            pos = characterX / BLOCK_SIZE + N_BLOCKS * (int) (characterY / BLOCK_SIZE);
            Tile curTile = myBoard.getTile(pos);

            //if character is on reward pick up and add point to score
            if (curTile.getPellet() == 1) {
                curTile.setPellet(0);
                score++;
            }
            
            //if character is on penalty pick up and subtract 25 points from score
            if (curTile.getPenalty() == 1) {
                curTile.setPenalty(0);
                score = score - 25;
            }

            //if character is on end tile and picked up all regular reward end game for win
            if ((curTile.getEndTile() == 1) && finished) {
                end = true;
            }
            
            //if character is on bonus reward pick up and add 10 points to score
            if (curTile.getBonusReward() == 1) {
                curTile.setBonusReward(0);
                noBonus = true; //no more bonus on board
                noBonusTime = 0;
                noBonusTimeCount = 0;
                score = score + 10;
            }

            //if no key input dont move
            if (moveHorizontal == 0 && moveVertical == 0) {
                characterDX = 0;
                characterDY = 0;
            } else {
                //if character isnt moving into wall, set right direction
                if (!((moveHorizontal == -1 && moveVertical == 0 && curTile.getLeftWall() == 1)
                        || (moveHorizontal == 1 && moveVertical == 0 && curTile.getRightWall() == 1)
                        || (moveHorizontal == 0 && moveVertical == -1 && curTile.getTopWall() == 1)
                        || (moveHorizontal == 0 && moveVertical == 1 && curTile.getBottomWall() == 1))) {
                    characterDX = moveHorizontal;
                    characterDY = moveVertical;
                }
            

                // Check for standstill if character moves into wall
                if ((characterDX == -1 && characterDY == 0 && curTile.getLeftWall() == 1)
                        || (characterDX == 1 && characterDY == 0 && curTile.getRightWall() == 1)
                        || (characterDX == 0 && characterDY == -1 && curTile.getTopWall() == 1)
                        || (characterDX == 0 && characterDY == 1 && curTile.getBottomWall() == 1)) {
                    characterDX = 0;
                    characterDY = 0;
                }
            }
        } 
        //actually move character
        characterX = characterX + CHARACTER_SPEED * characterDX;
        characterY = characterY + CHARACTER_SPEED * characterDY;
    }

    private void drawCharacter(Graphics2D g2d) { //draw character sprite depending on direction of movement
        if (moveHorizontal == -1) {
            curCharacterImage = left;
        } else if (moveHorizontal == 1) {
            curCharacterImage = right;
        } else if (moveVertical == -1) {
        	curCharacterImage = up;
        } else if(moveVertical == 1) {
        	curCharacterImage = down;
        }
        g2d.drawImage(curCharacterImage, characterX + 1, characterY + 1, this); //draw sprite
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;
        //loop through all cells of maze
        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                Tile curTile = myBoard.getTile(i);
                //set default color pink
                g2d.setColor(new Color(255,0,255));
                g2d.setStroke(new BasicStroke(5));
                
                if (curTile.getVoidTile() == 1) { //fill in tiles surrounded by walls with pink
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                if (curTile.getLeftWall() == 1) { //draw left wall
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if (curTile.getTopWall() == 1) { //draw top wall
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if (curTile.getRightWall() == 1) { //draw right wall
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if (curTile.getBottomWall() == 1) { //draw bottom wall
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if (curTile.getPellet() == 1) { //draw reward
                    g2d.setColor(new Color(255,255,255)); //white
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                if (curTile.getPenalty() == 1) { //draw penalty
                    g2d.setColor(new Color(255,0,0)); //red
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                if (curTile.getEndTile() == 1) { //draw end tile
                    g2d.setColor(new Color(0,255,0)); //green
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                if (curTile.getBonusReward() == 1) { //draw bonus rewrd
                    g2d.setColor(new Color(0,255,0)); //green
                    g2d.fillOval(x + 6, y + 6, 12, 12);
                }
                i++;
            }
        }
    }

    private void initGame() {
        //give variables correct value at start of game
    	lives = 1;
        score = 0;
        time = 0;
        loss = false;
        won = false;
        bonusTimeCount = 0;
        bonusTime = 0;
        noBonusTimeCount = 0;
        noBonusTime = 0;
        noBonus = true;
        initLevel();
    }

    private void initLevel() {
        myBoard = new Board(25, 25, simple_maze);
        for (int i = 0; i < N_ALIENS; i++) {

            alienY[i] = (N_BLOCKS - 1) * BLOCK_SIZE; //alien start positions x
            alienX[i] = (0+11*i) * BLOCK_SIZE;       //y

            alienSpeed[i] = ALIEN_SPEED;
        }

        characterX = 0 * BLOCK_SIZE;  //character start position x
        characterY = 0 * BLOCK_SIZE;  //y
        characterDX = 0;    //reset character direction x
        characterDY = 0;    //y
        moveHorizontal = 0;	    //reset character direction input x
        moveVertical = 0;       //y
        dying = false;  //character not dying
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        //colour entire game black
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        //draw game to screen
        drawMaze(g2d);
        drawScore(g2d);
        drawTime(g2d);

        if (inGame) {
            //keep playing game
            playGame(g2d);
        } else {
            //when out of game show instruction to start game
            showIntroScreen(g2d);
            if(won) { //user won
                drawWin(g2d);
            }
            if(loss) { //user lost
                drawLoss(g2d);
            }
        }
        //sync and dispose graphics
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { //when key is pressed
            //get the pressed key
            int key = e.getKeyCode();
            //if user is in game
            if (inGame) {
                // A goes left
                if (key == KeyEvent.VK_A) {
                    moveHorizontal = -1;
                    moveVertical = 0;
                // D goes right
                } else if (key == KeyEvent.VK_D) {
                    moveHorizontal = 1;
                    moveVertical = 0;
                // W goes up
                } else if (key == KeyEvent.VK_W) {
                    moveHorizontal = 0;
                    moveVertical = -1;
                // S goes down
                } else if (key == KeyEvent.VK_S) {
                    moveHorizontal = 0;
                    moveVertical = 1;
                //esacape to end game
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                //if not in game space bar starts game
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) { //when key is released
            //get released kay
            int key = e.getKeyCode();
            //if user in game
            if (inGame) {
                //releasing any movement key results in character no longer moving
                if (key == KeyEvent.VK_A) {
                    moveHorizontal = 0;
                    moveVertical = 0;
                } else if (key == KeyEvent.VK_D) {
                    moveHorizontal = 0;
                    moveVertical = 0;
                } else if (key == KeyEvent.VK_W) {
                    moveHorizontal = 0;
                    moveVertical = 0;
                } else if (key == KeyEvent.VK_S) {
                    moveHorizontal = 0;
                    moveVertical = 0;
                }
            }
        }
    }
	
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint(); //redraw when action has occured
    }
}