package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Player {
     // instance properties
    private Game app; // will hold a reference to the main Game object
    private PImage img; // will hold a reference to an image of the player
    private int x; // will hold the x coordinate of this object on the screen
    private int y; // will hold the y coordinate of this object on the screen


    private boolean isGliding = false;
    private boolean isFalling = false;
    private boolean isJumping = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isTouchingGround = false;

    private final int maxKeyPressInertia = 5;
    private int jumpInertia = 0;
    private int leftInertia = 0;
    private int rightInertia = 0;

    public Player(Game app, String imgFilePath, int x, int y){
        this.app = app;
        this.img = app.loadImage(imgFilePath);

        this.x = x;
        this.y = y;
    }

    public void jump(){ 
        if (isTouchingGround){
            isJumping = true;
            jumpInertia = 0;
        }
    }
    public void moveLeft(){
        isMovingLeft = true;
        leftInertia = 0;
    }
    public void moveRight(){
        isMovingRight = true;
        rightInertia = 0;
    }
    public void fallGliding(){
        isGliding = true;
    }
    public void fallGlideless(){
        isFalling = true;
    }
    public void glide(){

    }
    public void processMovements(){
        if (isGliding){
            if(!isTouchingGround){

            }
        }
        if (isFalling){
            if(!isTouchingGround){

            }
        }
        if (isJumping){
            if(jumpInertia < maxKeyPressInertia){
                //jump

                jumpInertia++;
            }

        }
        if (isMovingLeft){
            if(leftInertia < maxKeyPressInertia){
                //move left

                leftInertia++;
            }

        }
        if (isMovingRight){
            if(rightInertia < maxKeyPressInertia){
                //move right
                
                rightInertia++;
            }
        }
    }
    public void checkGroundCollision(ArrayList<Platform> platforms){

    }
    public void checkWallCollision(ArrayList<Platform> platforms){

    }

    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CENTER); // setting so the image is drawn centered on the specified x and y coordinates
        this.app.image(this.img, this.x, this.y);
    }

    public void setImage(String imgFilePath){
        this.img = app.loadImage(imgFilePath);
    }
    public void setX(int x){
        this.x = x;
    }
    public int getX(){
        return x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getY(){
        return y;
    }
}
