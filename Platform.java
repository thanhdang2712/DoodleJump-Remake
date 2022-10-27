public class Platform extends Entity implements Scrollable {
    //Location of image file to be drawn for a Get
    private static final String PLATFORM_IMAGE_FILE = "assets/green_platform.png";
 
    private static final int PLATFORM_WIDTH = 50;
    private static final int PLATFORM_HEIGHT = 10;
    private static final int PLATFORM_SCROLL_SPEED = 5;
   

    
    public Platform(){
        this(0, 0);        
    }
    
    public Platform(int x, int y){
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, PLATFORM_IMAGE_FILE);  
    }
    
    public Platform(int x, int y, String imageFileName){
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, imageFileName);
    }
    
    public int getScrollSpeed(){
        return PLATFORM_SCROLL_SPEED;
    }
    
    //Move the Get left by its scroll speed
    public void scroll(){
        setY(getY() - PLATFORM_SCROLL_SPEED);
    }
}
