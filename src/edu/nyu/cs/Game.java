package edu.nyu.cs;
//TODO: design and implement levels, design falling platform
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;

import processing.core.*; // import the base Processing library
import processing.sound.*; // import the processing sound library

/**
 * Describe your game succinctly here, and update the author info below.
 * Some starter code has been included for your reference - feel free to delete or modify it.
 * 
 * @author Ho Hin Ambrose Lo
 * @version 0.1
 */
public class Game extends PApplet {

  private SoundFile soundStartup; // will refer to a sound file to play when the program first starts
  private SoundFile soundtrack;
  private Sound adjuster;

  private String scene = "start";
  private PImage imgLogo;
  private PImage dirtTile;
  private PImage fallingTile;
  private PImage missingTile;
  private PFont eightBitFont;
  private Player player;
  private PImage playerStill;
  private PImage playerRFootMove;
  private PImage playerLFootMove;
  private PImage playerGlide;
  private ArrayList<Platform> currentScenePlatforms = new ArrayList<Platform>();
  private ArrayList<Integer> remove = new ArrayList<Integer>();
  private boolean instructionsShowing = false;
  private boolean levelOneCreated = false;
  private boolean levelTwoCreated = false;
  private boolean levelThreeCreated = false;
  private boolean levelFourCreated = false;
  private boolean levelFiveCreated = false;
  private boolean gameStarted = false;
  private boolean gameFinished = false;
  private double timer = 0.0;
  private int instructionTimer = 0;

  private boolean isRightPressed = false;
  private boolean isLeftPressed = false;

  private boolean notPlayedStart = true;


	/**
	 * This method will be automatically called by Processing when the program runs.
   * - Use it to set up the initial state of any instance properties you may use in the draw method.
	 */
	public void setup() {
    // hide Cursor
    this.noCursor();

    // load up a sound file and play it once when program starts up
		String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
		String path = Paths.get(cwd, "sounds", "introJingle.mp3").toString(); // e.g "sounds/vibraphon.mp3" on Mac/Unix vs. "sounds\vibraphon.mp3" on Windows
    this.soundStartup = new SoundFile(this, path);

    //load up soundtrack for the game
    path = Paths.get(cwd,"sounds","soundtrack.mp3").toString();
    this.soundtrack = new SoundFile(this,path);

    // load up game logo
    path = Paths.get(cwd,"images","logo.png").toString();
    this.imgLogo = loadImage(path);

    // load game font
    path = Paths.get(cwd,"fonts","PixeloidSans-JR6qo.ttf").toString();
    this.eightBitFont = createFont(path,32);
    textFont(this.eightBitFont);

    // load player
    path = Paths.get(cwd,"images","playerStandingStill.png").toString();
    this.playerStill = loadImage(path);

    path = Paths.get(cwd, "images","playerGliding.png").toString();
    this.playerGlide = loadImage(path);

    path = Paths.get(cwd, "images","playerMove1.png").toString();
    this.playerRFootMove = loadImage(path);

    path = Paths.get(cwd,"images","playerMove2.png").toString();
    this.playerLFootMove = loadImage(path);

    // load dirt tile
    path = Paths.get(cwd,"images","dirtTile.png").toString();
    this.dirtTile = loadImage(path);

    // load falling tile
    path = Paths.get(cwd,"images","fallingTile.png").toString();
    this.fallingTile = loadImage(path);
  
    // load missing tile
    path = Paths.get(cwd,"images","missingTile.png").toString();
    this.missingTile = loadImage(path);


    adjuster = new Sound(this);
    adjuster.volume(0.5f);

    // some basic settings for when we draw shapes
    this.ellipseMode(PApplet.CENTER); // setting so ellipses radiate away from the x and y coordinates we specify.
    this.imageMode(PApplet.CENTER); // setting so the ellipse radiates away from the x and y coordinates we specify.
    textAlign(CENTER);
	}

	/**
	 * This method is called automatically by Processing every 1/60th of a second by default.
   * - Use it to modify what is drawn to the screen.
   * - There are methods for drawing various shapes, including `ellipse()`, `circle()`, `rect()`, `square()`, `triangle()`, `line()`, `point()`, etc.
	 */
	public void draw() {
    checkLevelWin();
    if (instructionsShowing){
      instructionTimer++;
      if (instructionTimer >=180){
        startGame();
        instructionsShowing = false;
      }
    }
    switch(scene){
      case "start":
        if (notPlayedStart){
          this.soundStartup.play();
          notPlayedStart = false;
        }
        this.background(0, 49, 82);
        image(this.imgLogo,(this.width/2),(this.height/2-90));
        String startString = "Press SPACEBAR to start!";
        textSize(70);
        text(startString, this.width/2, this.height-50);
        fill(238,28,37);
        textSize(71);
        text(startString, this.width/2, this.height-50);
        fill(255,200,200);
        break;
      case "instructions":
        currentScenePlatforms.clear();
        this.background(0, 49, 82);
        textSize(25);
        String help = "Press LEFTARROW to move left. \n Press RIGHTARROW to move right. \n Press Z to glide. \n Press X to double -jump. \n Press C to jump. \n Press R to restart level.";
        text(help, this.width/2, 100);
        fill(255,255,255);
        break;
      case "0m":
        if(!levelOneCreated){
          currentScenePlatforms.clear();
          levelOne();
        }
        this.background(160,82,45);
        remove = new ArrayList<Integer>();
        for (int i = 0; i<currentScenePlatforms.size();i++){
          Platform p = currentScenePlatforms.get(i);
          p.draw();
          if (p.getRemovalStatus()){
            remove.add(i);
          }
        }
        for (int i = 0; i<remove.size();i++){
          currentScenePlatforms.remove(currentScenePlatforms.get(remove.get(i)));
        }
        break;
      case "100m":
        if (!levelTwoCreated){
          currentScenePlatforms.clear();
          levelTwo();
        }
        this.background(160,82,45);
        remove = new ArrayList<Integer>();
        for (int i = 0; i<currentScenePlatforms.size();i++){
          Platform p = currentScenePlatforms.get(i);
          p.draw();
          if (p.getRemovalStatus()){
            remove.add(i);
          }
        }
        for (int i = 0; i<remove.size();i++){
          currentScenePlatforms.remove(currentScenePlatforms.get(remove.get(i)));
        }
        break;
      case "200m":
        if(!levelThreeCreated){
          currentScenePlatforms.clear();
          levelThree();
        }
        this.background(160,82,45);
        remove = new ArrayList<Integer>();
        for (int i = 0; i<currentScenePlatforms.size();i++){
          Platform p = currentScenePlatforms.get(i);
          p.draw();
          if (p.getRemovalStatus()){
            remove.add(i);
          }
        }
        for (int i = 0; i<remove.size();i++){
          currentScenePlatforms.remove(currentScenePlatforms.get(remove.get(i)));
        }
        break;
      case "300m":
        if(!levelFourCreated){
          currentScenePlatforms.clear();
          levelFour();
        }
        this.background(160,82,45);
        remove = new ArrayList<Integer>();
        for (int i = 0; i<currentScenePlatforms.size();i++){
          Platform p = currentScenePlatforms.get(i);
          p.draw();
          if (p.getRemovalStatus()){
            remove.add(i);
          }
        }
        for (int i = 0; i<remove.size();i++){
          currentScenePlatforms.remove(currentScenePlatforms.get(remove.get(i)));
        }
        break;
      case "400m":
        if(!levelFiveCreated){
          currentScenePlatforms.clear();
          levelFive();
        }
        this.background(160,82,45);
        remove = new ArrayList<Integer>();
        for (int i = 0; i<currentScenePlatforms.size();i++){
          Platform p = currentScenePlatforms.get(i);
          p.draw();
          if (p.getRemovalStatus()){
            remove.add(i);
          }
        }
        for (int i = 0; i<remove.size();i++){
          currentScenePlatforms.remove(currentScenePlatforms.get(remove.get(i)));
        }
        break;
      case "495m":
        gameFinished = true;
        currentScenePlatforms.clear();
        levelEnd();
        this.background(0, 49, 82);
        textSize(80);
        text("THE END",this.width/2, this.height/2);
        fill(255,0,0);
        textSize(81);
        text("THE END",this.width/2, this.height/2);
        fill(255,200,200);
        break;
      }
      if (gameStarted){
        if (!soundtrack.isPlaying() && !soundStartup.isPlaying()){
          soundtrack.play();
        }
      }
      if (gameStarted && !gameFinished){
        if (isRightPressed){
          player.moveRight();
        }
        if (isLeftPressed){
          player.moveLeft();
        }
        player.boundingBox();
        player.processVelocity();
        player.checkCollisions(currentScenePlatforms);
        player.changePlayerPosition();
        player.draw();
        
        if (player.getY() > this.height+100){
          player.restart();
          resetLevels();
        }
        deathCounter(player.getDeaths());
        updateTimer();
        displayTimer(getTimerDisplayValue());
        printLevel();
      }
	}

	/**
	 * This method is automatically called by Processing every time the user presses a key while viewing the map.
	 * - The `key` variable (type char) is automatically is assigned the value of the key that was pressed.
	 * - The `keyCode` variable (type int) is automatically is assigned the numeric ASCII/Unicode code of the key that was pressed.
	 */
	public void keyPressed() {
    // the `key` variable holds the char of the key that was pressed, the `keyCode` variable holds the ASCII/Unicode numeric code for that key.
		System.out.println(String.format("Key pressed: %s, key code: %d.", this.key, this.keyCode));
    switch(this.keyCode){
      case RIGHT:
        isRightPressed = true;
        break;
      case LEFT:
        isLeftPressed = true;
        break;
      default:
        switch(this.key){
          case ' ':
            if (!gameStarted){
              instruct();
            }
            break;
          case 'c':
            if(gameStarted&&!gameFinished){
              player.jump();
            }
            break;
          case 'x':
            if(gameStarted&&!gameFinished){
              player.doubleJump();
            }
            break;
          case 'z':
            if(gameStarted&&!gameFinished){
              player.glide();
            }
            break;
          case 'r':
            if(gameStarted&&!gameFinished){
              resetLevels();
              player.restart();
            }
            break;
        }
    }
	}  

  public void keyReleased(){
        // the `key` variable holds the char of the key that was released, the `keyCode` variable holds the ASCII/Unicode numeric code for that key.
		System.out.println(String.format("Key released: %s, key code: %d.", this.key, this.keyCode));
    switch(this.keyCode){
      case RIGHT:
        isRightPressed = false;
        key = '\0';
        break;
      case LEFT:
        isLeftPressed = false;
        key = '\0';
        break;
      default:
        switch(this.key){
          
        }
    }
  }
  /**
   * A method that can be used to modify settings of the window, such as set its size.
   * This method shouldn't really be used for anything else.  
   * Use the setup() method for most other tasks to perform when the program first runs.
   */
  public void settings() {
		size(1200, 800); // set the map window size, using the OpenGL 2D rendering engine
		System.out.println(String.format("Set up the window size: %d, %d.", width, height));    
  }

  private void startGame(){
    gameStarted = true;
    // create Player object
    player = new Player(this, playerStill, playerGlide, playerRFootMove, playerLFootMove, 50, 600,128,128);
    scene = "0m";
    timer = 0.0;
  }

  private void instruct(){
    instructionsShowing = true;
    scene = "instructions";
  }
  private void updateTimer(){
    if (gameStarted && !gameFinished){
      this.timer += (1.0/60);
    }
  }
  private String getTimerDisplayValue(){
    String display = "";
    int seconds = (int) timer %60;
    String secondsString = "";
    if (seconds <10){
      secondsString += "0" + seconds;
    }
    else {
      secondsString += seconds;
    }
    int minutes = (int) (timer/60);
    int milliseconds = (int) ((timer - seconds - minutes*60)*1000);
    display = minutes + ":" + secondsString + "." + milliseconds;
    return display;
  }

  private void levelOne(){ //0m
    //ceiling
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 768,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,128,128,128));

    //border walls
    currentScenePlatforms.add(new WallPlatform(this, missingTile, -128,0,128,this.height));
    currentScenePlatforms.add(new WallPlatform(this, missingTile, this.width,0,128,this.height));

    // jumping platforms
    currentScenePlatforms.add(new Platform(this, dirtTile, 50,600, 128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 350, 550, 128, 128));
    currentScenePlatforms.add(new Platform(this, dirtTile,700,400,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile,this.width-128, 300, 128, 128));

    levelOneCreated = true;
  }
  private void levelTwo(){ //100m
    

    //ceiling
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,384,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 768,0,128,128));
    
    //border walls
    currentScenePlatforms.add(new WallPlatform(this, missingTile, -128,0,128,this.height));
    currentScenePlatforms.add(new WallPlatform(this, missingTile, this.width,0,128,this.height));
    
     // jumping platforms
    currentScenePlatforms.add(new Platform(this, dirtTile, 50,600, 128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,this.height-64,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 704,this.height-64,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 832,576,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,384,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, this.width-128,256,128,128));
    
    levelTwoCreated = true;
    }
  private void levelThree(){ //200m
    //ceiling
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,384,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 704,384,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 704,448,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 768,384,128,128));

    //border walls
    currentScenePlatforms.add(new WallPlatform(this, missingTile, -128,0,128,this.height));
    currentScenePlatforms.add(new WallPlatform(this, missingTile, this.width,0,128,this.height));

    // jumping platforms
    currentScenePlatforms.add(new Platform(this, dirtTile, 50,600, 128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, this.width-128,700,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, this.width-64,572,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, this.width-64,636,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile,896,384,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile,896,448,64,64));
    currentScenePlatforms.add(new Platform(this, dirtTile, 768,256,128,128));
    
    levelThreeCreated = true;
  }
  private void levelFour(){ //300m
     //ceiling
     currentScenePlatforms.add(new Platform(this, dirtTile, 0,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 128,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 256,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 384,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 512,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 640,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 768,0,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 0,128,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 0,256,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 128,128,128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 256,128,128,128));
 
     //border walls
     currentScenePlatforms.add(new WallPlatform(this, missingTile, -128,0,128,this.height));
     currentScenePlatforms.add(new WallPlatform(this, missingTile, this.width,0,128,this.height));
 
     // jumping platforms
     currentScenePlatforms.add(new Platform(this, dirtTile, 50,600, 128,128));
     currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 300,500, 128,128));
     currentScenePlatforms.add(new Platform(this, dirtTile, 700,450,128,128));
     currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 900,300, 128,128));

    levelFourCreated = true;
  }
  private void levelFive(){ //400m
    //ceiling
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 384,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 512,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 640,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 768,0,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 0,256,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 128,128,128,128));
    currentScenePlatforms.add(new Platform(this, dirtTile, 256,128,128,128));

    //border walls
    currentScenePlatforms.add(new WallPlatform(this, missingTile, -128,0,128,this.height));
    currentScenePlatforms.add(new WallPlatform(this, missingTile, this.width,0,128,this.height));

    // jumping platforms
    currentScenePlatforms.add(new Platform(this, dirtTile, 50,600, 128,128));
    currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 384,576, 128,128));
    currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 448,448, 128,128));
    currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 832,384, 128,128));
    currentScenePlatforms.add(new FallingPlatform(this, fallingTile, 1024,256, 128,128));

    levelFiveCreated = true;
  }
  private void levelEnd(){ //495m

  }

  public PImage missingTile(){
    return missingTile;
  }
  private void deathCounter(int deaths){
    textSize(25);
    String deathString = "Deaths: "+deaths;
    text(deathString, 96, 30);
    fill(255,255,255);
  }
  private void displayTimer(String displayTime){
    textSize(25);
    textMode(CORNER);
    text("Timer is at " +displayTime,128,60);
    fill(255,255,255);
    textMode(CENTER);
  }
  private void printLevel(){
    textSize(50);
    text(scene,600,100);
    fill(255,255,255);
  }
  private void checkLevelWin(){
    if (gameStarted&&!gameFinished){
      if (player.getY()<=0){
        player.restart();
        player.setDeaths(player.getDeaths()-1);
        switch(scene){
          case "0m":
            this.scene = "100m";
            break;
          case "100m":
            this.scene = "200m";
            break;
          case "200m":
            this.scene = "300m";
            break;
          case "300m":
            this.scene = "400m";
            break;
          case "400m":
            this.scene = "495m";
            break;
        }
      }
    }
  }

  private void resetLevels(){
    levelOneCreated = false;
    levelTwoCreated = false;
    levelThreeCreated = false;
    levelFourCreated = false;
    levelFiveCreated = false;
  }

















  /**
   * The main function is automatically called first in a Java program.
   * When using the Processing library, this method must call PApplet's main method and pass it the full class name, including package.
   * You shouldn't need to modify this method.
   * 
   * @param args An array of any command-line arguments.
   */
  public static void main(String[] args) {
    // make sure we're using Java 1.8
		System.out.printf("\n###  JDK IN USE ###\n- Version: %s\n- Location: %s\n### ^JDK IN USE ###\n\n", SystemUtils.JAVA_VERSION, SystemUtils.getJavaHome());
		boolean isGoodJDK = SystemUtils.IS_JAVA_1_8;
		if (!isGoodJDK) {
			System.out.printf("Fatal Error: YOU MUST USE JAVA 1.8, not %s!!!\n", SystemUtils.JAVA_VERSION);
		}
		else {
			PApplet.main("edu.nyu.cs.Game"); // do not modify this!
		}
  }

}
