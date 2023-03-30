package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;

import java.nio.file.Paths;
import java.util.ArrayList;

public class Player {
     // instance properties
    private Game app; // will hold a reference to the main Game object
    private PImage img; // will hold a reference to an image of the player
    private int x; // will hold the bottom-left x coordinate of this object on the screen
    private int y; // will hold the bottom-left y coordinate of this object on the screen
    private int width; //will hold the width of this object
    private int height; //will hold the height of this object
    private int[][] boundingBox;

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

    public Player(Game app, String imgFilePath, int x, int y, int width, int height){
        this.app = app;
        this.img = app.loadImage(imgFilePath);
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        establishBoundingBox();

    }
    private void establishBoundingBox(){
        int[][] boundingBox = new int[4][2];

        //bottom left corner
        boundingBox[0][0] = this.x;
        boundingBox[0][1] = this.y;

        //bottom right corner
        boundingBox[1][0] = this.x+this.width;
        boundingBox[1][1] = this.y;

        //top left corner
        boundingBox[2][0] = this.x;
        boundingBox[2][1] = this.y+this.height;

        //top right corner
        boundingBox[3][0] = this.x+this.width;
        boundingBox[3][1] = this.y+this.height;
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
            setImage(path);
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
    private void processVelocity(){
        this.setX((int)(getX()+getXVelocity()));
        this.setY((int)(getY()+getYVelocity()));
    }
    public void checkCollisions(ArrayList<Platform> platforms){
        checkLeftWallCollision(platforms);
        checkRightWallCollision(platforms);
        checkFloorCollision(platforms);
        checkCeilingCollision(platforms);
    }
    private void checkLeftWallCollision(ArrayList<Platform> platforms){ //checks collision to the right of the player
        boolean colliding = false;
        for (int i = 0; i<platforms.size(); i++){
            int[][] currentWall = platforms.get(i).getLeftWall();
            //check top right player corner
            if (boundingBox[3][1] <= currentWall[0][1] && boundingBox[3][1] >= currentWall[1][1]){
                if (boundingBox[3][0]>=currentWall[0][0] && boundingBox[3][0] <= (currentWall[0][0]+platforms.get(i).getWidth())){
                    colliding = true;
                }
            }
            //check bottom right player corner
            else if (boundingBox[1][1] <= currentWall[0][1] && boundingBox[1][1] >= currentWall[1][1]){
                if (boundingBox[1][0]>=currentWall[0][0] && boundingBox[1][0] <= (currentWall[0][0]+platforms.get(i).getWidth())){
                    colliding = true;
                }
            }
        }
        if (colliding){
            if(xVelocity>0){
                setXVelocity(0);
            }
        }
    }
    private void checkRightWallCollision(ArrayList<Platform> platforms){
        boolean colliding = false;
        for (int i = 0; i<platforms.size(); i++){
            int[][] currentWall = platforms.get(i).getRightWall();
            //check top left player corner
            if (boundingBox[2][1] <= currentWall[0][1] && boundingBox[2][1] >= currentWall[0][1]){
                if (boundingBox[2][0]<=currentWall[0][0] && boundingBox[2][0] >= (currentWall[0][0]-platforms.get(i).getWidth())){
                    colliding = true;
                }
            }
            //check bottom left player corner
            else if (boundingBox[0][1] <= currentWall[0][1] && boundingBox[0][1] >= currentWall[1][1]){
                if (boundingBox[0][0]<=currentWall[0][0] && boundingBox[0][0] >= (currentWall[0][0]-platforms.get(i).getWidth())){
                    colliding = true;
                }
            }
        }
        if (colliding){
            if(xVelocity<0){
                setXVelocity(0);
            }
        }
    }
    private void checkFloorCollision(ArrayList<Platform> platforms){
        boolean colliding = false;
        for (int i = 0; i<platforms.size(); i++){
            int[][] currentFloor = platforms.get(i).getFloor();
            //check top left player corner
            if (boundingBox[2][0] <= currentFloor[1][0] && boundingBox[2][0] >= currentFloor[0][0]){
                if (boundingBox[2][1]<= currentFloor[0][1] && boundingBox[2][1] >= (currentFloor[0][1]-platforms.get(i).getHeight())){
                    colliding = true;
                }
            }
            //check top right player corner
            if (boundingBox[3][0] <= currentFloor[1][0] && boundingBox[3][0] >= currentFloor[0][0]){
                if (boundingBox[3][1]<= currentFloor[0][1] && boundingBox[3][1] >= (currentFloor[0][1]-platforms.get(i).getHeight())){
                    colliding = true;
                }
            }
            if (colliding){
                isTouchingGround = true;
                if (yVelocity<0){
                    setYVelocity(0);
                }
            }
            else {
                isTouchingGround = false;
            }
        }

    }
    private void checkCeilingCollision(ArrayList<Platform> platforms){
        boolean colliding = false;
        for (int i = 0; i<platforms.size(); i++){
            int[][] currentCeiling = platforms.get(i).getCeiling();
            //check bottom left player corner
            if (boundingBox[0][0] <= currentCeiling[1][0] && boundingBox[0][0] >= currentCeiling[0][0]){
                if (boundingBox[0][1]>= currentCeiling[0][1] && boundingBox[0][1] <= (currentCeiling[0][1]+platforms.get(i).getHeight())){
                    colliding = true;
                }
            }
            //check top right player corner
            if (boundingBox[1][0] <= currentCeiling[1][0] && boundingBox[1][0] >= currentCeiling[0][0]){
                if (boundingBox[1][1]>= currentCeiling[0][1] && boundingBox[1][1] <= (currentCeiling[0][1]+platforms.get(i).getHeight())){
                    colliding = true;
                }
            }
            if (colliding){
                if (yVelocity>0){
                    setYVelocity(0);
                }
            }
        }
    }

    public void draw() {
        // draw this object's image at its x and y coordinates
        this.app.imageMode(PApplet.CORNERS); // setting so the image is drawn centered on the specified x and y coordinates
        this.app.image(this.img, this.x, this.y,this.width,this.height);
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

    public void restart(){

    }
}
