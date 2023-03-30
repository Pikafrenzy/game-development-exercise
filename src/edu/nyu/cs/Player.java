package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;

import java.nio.file.Paths;
import java.util.ArrayList;

public class Player {
     // instance properties
    private Game app; // will hold a reference to the main Game object
    private PImage img; // will hold a reference to an image of the player
    private int x; // will hold the x coordinate of this object on the screen
    private int y; // will hold the y coordinate of this object on the screen

    private boolean canGlide = false;
    private boolean isGliding = false;
    private boolean isFalling = false;
    private boolean isJumping = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isTouchingGround = false;
    private int doubleJump = 1;

    private final int maxKeyPressInertia = 5;
    private int jumpInertia = 0;
    private int leftInertia = 0;
    private int rightInertia = 0;

    private final float speed = 20;
    private final float jumpStrength = 5;
    private final float gravityFall = 10;
    private final float gravityGlide = 5;
    private float xVelocity = 0;
    private float yVelocity = 0;

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
    public void doubleJump(){
        if (doubleJump>0){
            isJumping = true;
            doubleJump--;
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
        isFalling = false;
    }
    public void fallGlideless(){
        isFalling = true;
        isGliding = false;
    }
    public void fall(){
        if (canGlide){
            fallGliding();
        }
        else{
            fallGlideless();
        }
    }
    public void glide(){
        if (!isTouchingGround){
            canGlide = true;
            String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
            String path = Paths.get(cwd,"images","playerGliding.png").toString();
            this.img = app.loadImage(path);
        }
    }
    public void processMovements(){
        if (!isTouchingGround){
            if (isFalling){
                this.addYVelocity(-gravityFall);
            }
            else if (isGliding){
                this.addYVelocity(-gravityGlide);
            }
        }
        else{
            doubleJump=1;
        }

        if (isJumping){
            if(jumpInertia < maxKeyPressInertia){
                //jump
                float jumpVelocity = (jumpStrength * (maxKeyPressInertia-jumpInertia));
                this.addYVelocity(jumpVelocity);
                jumpInertia++;
            }

        }
        if (isMovingLeft){
            if(leftInertia < maxKeyPressInertia){
                //move left
                this.setXVelocity(speed);
                leftInertia++;
            }
            else {
                this.setXVelocity(0);
                isMovingLeft = false;
            }

        }
        if (isMovingRight){
            if(rightInertia < maxKeyPressInertia){
                //move right
                this.setXVelocity(-speed);
                rightInertia++;
            }
            else {
                this.setYVelocity(0);
                isMovingLeft = false;
            }

        }
        processVelocity();
    }
    public void processVelocity(){
        this.setX((int)(getX()+getXVelocity()));
        this.setY((int)(getY()+getYVelocity()));
    }
    public void checkWallCollisionLeft(ArrayList<Platform> platforms){

    }
    public void checkWallCollisionRight(ArrayList<Platform> platforms){

    }
    public void checkGroundCollision(ArrayList<Platform> platforms){

    }

    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNER); // setting so the image is drawn centered on the specified x and y coordinates
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

    public void setXVelocity(float xVelocity){
        this.xVelocity = xVelocity;
    }
    public float getXVelocity(){
        return xVelocity;
    }
    public void addXVelocity(float addedXVelocity){
        this.xVelocity += addedXVelocity;
    }
    public void setYVelocity(float yVelocity){
        this.yVelocity = yVelocity;
    }
    public float getYVelocity(){
        return yVelocity;
    }
    public void addYVelocity(float addedYVelocity){
        this.yVelocity += addedYVelocity;
    }
}
