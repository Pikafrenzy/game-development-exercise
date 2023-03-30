package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;

public class Platform {
    // instance properties
    private Game app; // will hold a reference to the main Game object
    private PImage img; // will hold a reference to an image of the platform
    private int x; // will hold the x coordinate of this object on the screen
    private int y; // will hold the y coordinate of this object on the screen

    public Platform(Game app, String imgFilePath, int x, int y){
        this.app = app;
        this.img = app.loadImage(imgFilePath);
        this.x = x;
        this.y = y;

    }
    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNER); // setting so the image is drawn centered on the specified x and y coordinates
        this.app.image(this.img, this.x, this.y);
    }
    
}
