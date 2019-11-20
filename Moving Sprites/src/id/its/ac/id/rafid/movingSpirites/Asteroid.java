package id.its.ac.id.rafid.movingSpirites;

import javafx.scene.transform.Rotate;

public class Asteroid extends Sprite {

    public Asteroid(int x, int y) {
        super(x, y);
        
        initAsteroid();
    }
    
    private void initAsteroid() {
        
        loadImage("pic/Asteroid.png");  
        getImageDimensions();
    }
 
    
}
