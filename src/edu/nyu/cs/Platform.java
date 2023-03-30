package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;

public class Platform {
    // instance properties
    private Game app; // will hold a reference to the main Game object
    private PImage img; // will hold a reference to an image of the platform
    private int x; // will hold the x coordinate of this object on the screen
    private int y; // will hold the y coordinate of this object on the screen
    
    private int width; //will hold the x dimension of the platform
    private int height; //will hold the y dimension of the platform

    public Platform(Game app, String imgFilePath, int x, int y, int width, int height){
        this.app = app;
        this.img = app.loadImage(imgFilePath);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNERS); // setting so the image is drawn centered on the specified x and y coordinates
        this.app.image(this.img, this.x, this.y, this.width, this.height);
    }
    public int[][] getLeftWall(){
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = this.x;
        wallCoordinates[0][1] = this.y;

        wallCoordinates[1][0] = this.x;
        wallCoordinates[1][1] = this.y+this.height;

        return wallCoordinates;
    }
    public int[][] getRightWall(){
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = this.x+this.width;
        wallCoordinates[0][1] = this.y;

        wallCoordinates[1][0] = this.x+this.width;
        wallCoordinates[1][1] = this.y+this.height;
        
        return wallCoordinates;
    }
    public int[][] getFloor(){ //get the top surface of the platform
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = this.x;
        wallCoordinates[0][1] = this.y+this.height;

        wallCoordinates[1][0] = this.x+width;
        wallCoordinates[1][1] = this.y+this.height;
        
        return wallCoordinates;
    }
    public int[][] getCeiling(){ //get the bottom surface of the platform
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = this.x;
        wallCoordinates[0][1] = this.y;

        wallCoordinates[1][0] = this.x+width;
        wallCoordinates[1][1] = this.y;
        
        return wallCoordinates;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
}
