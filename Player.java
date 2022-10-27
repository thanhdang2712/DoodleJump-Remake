//The entity that the human player controls in the game window
//The player moves in reaction to player input
public class Player extends Entity {
  
  private static final String PLAYER_IMAGE_FILE = "assets/bunny-right.png";
  //Dimensions of the Player
  private static final int PLAYER_WIDTH = 75;
  private static final int PLAYER_HEIGHT = 75;
  //Default speed that the Player moves (in pixels) each time the user moves it
  private static final double DEFAULT_GRAVITY = 0.2;
  private static final double MIN_VELOCITY = -8;
  private static final int DEFAULT_JUMP_FORCE = 9;
  private static final int DEFAULT_MOVEMENT_SPEED = 5;
  
  //Starting hit points
  private static final int STARTING_HP = 3;
  
  //Current movement speed
  private int movementSpeed;
  //Remaining Hit Points (HP) -- indicates the number of "hits" (ie collisions
  //with Avoids) that the player can take before the game is over
  private int hp;
  public double velocity;
  private double gravity;
  private double jumpForce;
  
  
  public Player(){
    this(0, 0);        
  }
  
  public Player(int x, int y){
    super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_IMAGE_FILE);  
    this.hp = STARTING_HP;
    this.movementSpeed = DEFAULT_MOVEMENT_SPEED;
    this.velocity = 0;
    this.gravity = DEFAULT_GRAVITY;
    this.jumpForce = DEFAULT_JUMP_FORCE;
  }
  
  
  //Retrieve and set the Player's current movement speed 
  public int getMovementSpeed(){
    return this.movementSpeed;
  }
  
  public void setMovementSpeed(int newSpeed){
    this.movementSpeed = newSpeed;
  }  
  
  
  //Retrieve the Player's current HP
  public int getHP(){
    return hp;
  }
  
  
  //Set the player's HP to a specific value.
  //Returns an boolean indicating if Player still has HP remaining
  public boolean setHP(int newHP){
    this.hp = newHP;
    return (this.hp > 0);
  }

  //Set the player's HP to a specific value.
  //Returns an boolean indicating if Player still has HP remaining
  public boolean modifyHP(int delta){
    this.hp += delta;
    return (this.hp > 0);
  }    

  public void update() {
    if (this.velocity >= MIN_VELOCITY)
        this.velocity += this.gravity;
    else
        this.velocity = MIN_VELOCITY;
    this.y += this.velocity;
}

  public void jump() {
    this.velocity -= this.jumpForce;
  }
}
