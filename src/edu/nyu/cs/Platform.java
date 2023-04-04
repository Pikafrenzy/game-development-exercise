package edu.nyu.cs;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Platform {
    // instance properties
    protected Game app; // will hold a reference to the main Game object
    protected PImage img; // will hold a reference to an image of the platform
    protected int x; // will hold the x coordinate of this object on the screen
    protected int y; // will hold the y coordinate of this object on the screen
    protected int width; //will hold the x dimension of the platform
    protected int height; //will hold the y dimension of the platform
    protected boolean isFallable;

    public Platform(Game app, int x, int y, int width, int height){
        this.app = app;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isFallable = false;
        this.img = app.missingTile();

    }
    public Platform(Game app, PImage tile, int x, int y, int width, int height){
        this.app = app;
        this.img = tile;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isFallable = false;
    }
    public Platform(Game app, PImage tile, int x, int y){
        this.app = app;
        this.img = tile;
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        this.isFallable = false;
    }

    public String toString(){
        String output = "This is a Platform! Top left corner is "+this.x+", "+this.y+", bottom right corner is "+(this.x+this.width)+", "+(this.y+this.height)+".";
        return output;
    }
    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNER); // setting so the image is drawn centered on the specified x and y coordinates
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
        wallCoordinates[0][1] = this.y;

        wallCoordinates[1][0] = this.x+width;
        wallCoordinates[1][1] = this.y;
        
        return wallCoordinates;
    }
    public int[][] getCeiling(){ //get the bottom surface of the platform
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = this.x;
        wallCoordinates[0][1] = this.y+this.height;

        wallCoordinates[1][0] = this.x+width;
        wallCoordinates[1][1] = this.y+this.height;
        
        return wallCoordinates;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public boolean getFallable(){
        return this.isFallable;
    }
    public int getY(){
        return this.y;
    }
    public void setY(int y){
        this.y = y;
    }
    public void fall(){

    }
    public boolean getRemovalStatus(){
        return false;
    }
}
