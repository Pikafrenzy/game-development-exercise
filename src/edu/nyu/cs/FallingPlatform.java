package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class FallingPlatform extends Platform{
    private boolean triggered = false;
    private int timer = 0;
    
    public FallingPlatform(Game app,PImage tile, int x, int y, int width, int height){
        super(app, tile, x, y, width, height);
        this.isFallable = true;
    }
    public void fall(){
        this.triggered = true;
    }
    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNER); // setting so the image is drawn centered on the specified x and y coordinates
        if(triggered){
            this.setY(getY()+10);
            timer++;
        }
        this.app.image(this.img, this.x, this.y, this.width, this.height);
        if (timer >= 20){

        }
    }
}