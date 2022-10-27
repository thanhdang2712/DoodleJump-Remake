import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//A Basic version of the scrolling game, featuring Avoids, Gets, and RareGets
//Players must reach a score threshold to win
//If player runs out of HP (via too many Avoid collisions) they lose
public class Dang extends AbstractGame {
           
    //Dimensions of game window
    private static final int DEFAULT_WIDTH = 532;
    private static final int DEFAULT_HEIGHT = 800;  
    
    //Starting Player coordinates
    private static final int STARTING_PLAYER_X = 240;
    private static final int STARTING_PLAYER_Y = 500;
    
    //Score needed to win the game
    private static final int SCORE_TO_WIN = 300;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    private static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    private static final int SPEED_CHANGE = 20;    
 
    private static final String INTRO_SPLASH_FILE = "assets/splash-page-001.jpg"; 
    private static final String BACKGROUND_IMG = "assets/background.png"; 
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    private static final int SPAWN_INTERVAL = 30;
    //Maximum Entities that can be spawned on a single call to spawnEntities
    private static final int MAX_SPAWNS = 3;
    //Chance of getting an Avoid or Get: 40%; Chance of getting a RareGet: 20%
    private static final int generateEntities = 20;
    private static boolean blackHole = false;
   
    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();

    //Player's current score
    private int score;
    
    //Stores a reference to game's Player object for quick reference
    //(This Player will also be in the displayList)
    private Player player;
    
  
    
    public Dang(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public Dang(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void preGame(){
        this.setSplashImage(INTRO_SPLASH_FILE);
        this.setBackgroundImage(BACKGROUND_IMG);
        player = new Player(STARTING_PLAYER_X, STARTING_PLAYER_Y);
        displayList.add(player); 
        score = 0;
    }
    
    //Called on each game tick
    protected void updateGame(){
        //scroll all scrollable Entities on the game board
        scrollEntities();  
        player.update();
        //check and handle collisions
        while (true) {
            Entity collidedWith = checkCollision(player);
            if (collidedWith == null) {
                break;
            } else if (collidedWith instanceof RareAvoid) {
                blackHole = true;
                break;
            } else if (collidedWith instanceof Consumable) {
                this.handleCollision((Consumable) collidedWith);
                makeSoundCollide();
            } else if (collidedWith instanceof Platform) {
                if (player.getY() + player.getHeight() >= collidedWith.getY() 
                && player.getY() + player.getHeight() <= collidedWith.getY() + collidedWith.getHeight()) {
                    int minX = collidedWith.getX() - player.getWidth();
                    int maxX = collidedWith.getX() + collidedWith.getWidth();

                    if (player.getX() >= minX && player.getX() <= maxX) {
                        makeSoundJump();
                        player.jump();
                    }
                }
                //think about this: Why does it not work with eveything
                break;
            }
        }
       
        //Spawn new entities only at a certain interval
        if (ticksElapsed % SPAWN_INTERVAL == 0)      
            spawnEntities();
        //Update the title text on the top of the window
        setTitleText("HP: " + player.getHP() + ", Score: " + score);
        }
    
    
    
    //Scroll all scrollable entities per their respective scroll speeds
    protected void scrollEntities() {
        for (int i = 0; i < displayList.size(); i++) {
        	//instance of Scrollable
            if (displayList.get(i) instanceof Scrollable) {
                ((Scrollable) displayList.get(i)).scroll();
            }
        }
    }

    
    //Spawn new Entities on the right edge of the game board
    protected void spawnEntities() {
        //random number of entities spawned
        int numEntities = rand.nextInt(MAX_SPAWNS);
        Entity toCheck = null;

        //Generate a location at top right edge
        int y = this.getWindowHeight();
        int x;

        for (int i = 0; i <= numEntities; i++) {
            //generate a non-colliding location on the screen!
            while (true) {
                x = rand.nextInt(this.getWindowWidth()); 
                //generate a radom toCheck entity 
                toCheck = randomEntityTest(x, y);
                if (checkCollision(toCheck) == null && toCheck.x < DEFAULT_WIDTH - toCheck.getWidth()) {
                    break;
                }
            }
            displayList.add(toCheck);
        }
    }

    private Entity randomEntityTest(int x, int y) {
        //generate a radom toCheck entity 
        Entity toCheck = null;
        int randIdx = rand.nextInt(generateEntities);

        //Chance of RareGet: 0, 1 -> 20%
        //Chance of Get: 2, 4, 6, 8 -> 40%
        //Chance of Avoid: 3, 5, 7, 9 -> 40%
        if (randIdx == 0) {
            toCheck = new RareGet(x, y);
        } else if (randIdx == 1) {
            toCheck = new RareAvoid(x, y);
        } else if (randIdx == 2) {
            toCheck = new Avoid(x, y);
        } else if (randIdx >= 3 && randIdx <= 6) {
            toCheck = new Get(x, y);
        } else {
            toCheck = new Platform(x, y);
        }
        return toCheck;
    }
    
    //Called whenever it has been determined that the Player collided with a consumable
    //1. Update the socre and HP 
    //2. Make the consumed entity null -> repaint -> not on the screen anymore
    protected void handleCollision(Consumable collidedWith) {
        score += collidedWith.getPointsValue();
        player.modifyHP(collidedWith.getDamageValue());
        displayList.remove((Entity) collidedWith); 
    }

    protected void handleCollision(Platform collidedWith) {
         
    }

    /*
    protected void handleCollision(Platform e) {
         if (player.getY() + player.getHeight() == e.getY() 
			&& player.getY() + player.getHeight() <= e.getY() + e.getHeight()) {
			int minX = e.getX() - player.getWidth();
			int maxX = e.getX() + e.getWidth();
			
			while (player.getX() >= minX && player.getX() <= maxX) {
				player.jump();
			}
		} 
    }
    */
    
    //Called once the game is over, performs any end-of-game operations
    protected void postGame(){
        if (score >= SCORE_TO_WIN) {
            super.setTitleText("GAME OVER - You Win!");
        } else {
            this.setBackgroundImage("game-over-page-001.jpg");
            super.setTitleText("GAME OVER - You Lose!");
        }
    }
    
    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    protected boolean isGameOver(){
        return (score >= SCORE_TO_WIN || player.getHP() <= 0 || blackHole ||
                player.getY() < -player.getHeight() || player.getY() > this.getWindowHeight());
    }
     
    //private methods that handle movement keys
    private boolean isMovement(int key) {
        for (int i: MOVEMENT_KEYS) {
            if (key == i) {
                return true;
            }
        }
        return false;
    }

    private void handleMovementKeys(int key) {
        int xCoord = player.getX();
        int yCoord = player.getY(); 
    
        if (key == UP_KEY) {
            makeSoundJump();
            player.jump();
        } else if (key == DOWN_KEY) {
            //do nothing
        } else if (key == LEFT_KEY) {
            player.setImageName("assets/bunny-left.png");
            xCoord = player.getX() - player.getMovementSpeed();
        } else if (key == RIGHT_KEY) {
            player.setImageName("assets/bunny-right.png");
            xCoord = player.getX() + player.getMovementSpeed();
        }

        Entity toCheck = new Player(xCoord, yCoord);

        if (xCoord + player.getWidth() < 0) xCoord = this.getWindowWidth();
        if (xCoord > this.getWindowWidth()) xCoord = player.getWidth()*(-1);
        
            player.setX(xCoord);
            player.setY(yCoord);
        
    }

    //object methods to handle the + and - keys
    private boolean isSpeedKey(int key) {
        return (key == SPEED_UP_KEY || key == SPEED_DOWN_KEY);
    }

    private void handleSpeedKey(int key) {
        int currSpeed = this.getGameSpeed();
        if (key == SPEED_UP_KEY) {
            currSpeed += SPEED_CHANGE;
        } else if (key == SPEED_DOWN_KEY) {
            currSpeed -= SPEED_CHANGE;
        }
        //speed keys are only implemented if new speed is within range
        if (currSpeed <= MAX_GAME_SPEED || currSpeed > 0)  {
            this.setGameSpeed(currSpeed);
        }
    }

    //Reacts to a single key press on the keyboard
    //Override's AbstractGame's handleKey
    protected void handleKey(int key){
        //first, call AbstractGame's handleKey to deal with any of the 
        //fundamental key press operations
        super.handleKey(key);
        
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key));
        //if a splash screen is up, only react to the advance splash key
        handleMouseClick();
        //implement other control keys: P -> movement keys and speed keys

        if (key == KEY_PAUSE_GAME) {
            isPaused = (!isPaused);
        }
        
        //these keys are only implemented if isPaused = false
        if (!isPaused) {
            if (isMovement(key)) {
                handleMovementKeys(key);
            }
            
            if (isSpeedKey(key)) {
                handleSpeedKey(key);
            }
        }  
    } 

    public void makeSoundJump(){
        File jump = new File("assets/jump.wav");
        
    
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(jump));
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void makeSoundCollide(){
        File jump = new File("assets/buzzer_sound.wav");
        
    
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(jump));
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}     

