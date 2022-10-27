public class RareAvoid extends Avoid{
    
    private static final int RARE_AVOID_WIDTH = 100;
    private static final int RARE_AVOID_HEIGHT = 80;
    //Location of image file to be drawn for a RareGet
    private static final String RAREAVOID_IMAGE_FILE = "assets/hole.png";
  
    public RareAvoid(){
        this(0, 0);        
    }
    
    public RareAvoid(int x, int y){
        super(x, y, RARE_AVOID_WIDTH, RARE_AVOID_HEIGHT, RAREAVOID_IMAGE_FILE);  
    }
    
    public int getDamageValue(){
        return 1;
    }
   
}