//Avoids are entities the player needs to avoid colliding with.
//If a player collides with an avoid, it reduces the players Hit Points (HP).
public class Avoid extends Entity implements Consumable, Scrollable {
     
    private static final String AVOID_IMAGE_FILE = "assets/big_blue_monster.png";
    //Dimensions of the Avoid    
    private static final int AVOID_WIDTH = 120;
    private static final int AVOID_HEIGHT = 80;
    //Speed that the avoid moves each time the game scrolls
    private static final int AVOID_SCROLL_SPEED = 5;
   
   
    public Avoid(){
        this(0, 0);        
    }

    public Avoid(int x, int y, int width, int height, String imageFile){
        super(x, y, width, height, imageFile);  
    }

    public Avoid(int x, int y, String imageFile){
        super(x, y, AVOID_WIDTH, AVOID_HEIGHT, imageFile);  
    }
    
    public Avoid(int x, int y){
        super(x, y, AVOID_WIDTH, AVOID_HEIGHT, AVOID_IMAGE_FILE);  
    }
    
    
    public int getScrollSpeed(){
        return AVOID_SCROLL_SPEED;
    }
    
    //Move the avoid left by the scroll speed
    public void scroll(){
        setY(getY() - AVOID_SCROLL_SPEED);
    }
    
    //Colliding with an Avoid does not affect the player's score
    public int getPointsValue(){
       return 0;
  }
    
    //Colliding with an Avoid Reduces players HP by 1
    public int getDamageValue(){
        return -1;
    }
   
}
