package edu.nyu.cs;

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
  private SoundFile soundClick; // will refer to a sound file to play when the user clicks the mouse
  private PImage imgMe; // will hold a photo of me

  private String scene = "start";
  private PImage imgLogo;
  private PFont eightBitFont;
  private Player player;
  private ArrayList<Platform> currentScenePlatforms = new ArrayList<Platform>();

  private int score = 0; // the user's score
  private boolean gameStarted = false;

  private boolean isRightPressed = false;
  private boolean isLeftPressed = false;


	/**
	 * This method will be automatically called by Processing when the program runs.
   * - Use it to set up the initial state of any instance properties you may use in the draw method.
	 */
	public void setup() {
    // hide Cursor
    this.noCursor();

    // load up a sound file and play it once when program starts up
		String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
		String path = Paths.get(cwd, "sounds", "vibraphon.mp3").toString(); // e.g "sounds/vibraphon.mp3" on Mac/Unix vs. "sounds\vibraphon.mp3" on Windows
    this.soundStartup = new SoundFile(this, path);
    this.soundStartup.play();

    // load up a sound file and play it once when the user clicks
		path = Paths.get(cwd, "sounds", "thump.aiff").toString(); // e.g "sounds/thump.aiff" on Mac/Unix vs. "sounds\thump.aiff" on Windows
    this.soundClick = new SoundFile(this, path); // if you're on Windows, you may have to change this to "sounds\\thump.aiff"
 
    // load up an image of me
    //TODO: delete along with all other demo code
		path = Paths.get(cwd, "images","demo","me.png").toString(); // e.g "images/me.png" on Mac/Unix vs. "images\me.png" on Windows
    this.imgMe = loadImage(path);

    // load up game logo
    path = Paths.get(cwd,"images","logo.png").toString();
    this.imgLogo = loadImage(path);

    // load game font
    path = Paths.get(cwd,"fonts","PixeloidSans-JR6qo.ttf").toString();
    this.eightBitFont = createFont(path,32);
    textFont(this.eightBitFont);

    

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
    switch(scene){
      case "start":
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

      case "default": //TODO: remove this from sample code
          // fill the window with solid color
        this.background(0, 0, 0); // fill the background with the specified r, g, b color.

        // show an image of me that wanders around the window
        image(this.imgMe, this.width / 2, this.height/2); // draw image to center of window

        // draw an ellipse at the current position of the mouse
        this.fill(255, 255, 255); // set the r, g, b color to use for filling in any shapes we draw later.
        this.ellipse(this.mouseX, this.mouseY, 60, 60); // draw an ellipse wherever the mouse is

        // show the score at the bottom of the window
        String scoreString = String.format("SCORE: %d", this.score);
        text(scoreString, this.width/2, this.height-50);
        break;
      }

      if (isRightPressed){
        player.moveRight();
      }
      if (isLeftPressed){
        player.moveLeft();
      }

      player.processMovements();
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
              startGame();
            }
            break;
          case 'c':
            player.jump();
            break;
          case 'x':
            player.doubleJump();
            break;
          case 'z':
            player.glide();
            break;
          case 'r':
            player.restart();
            break;
          case '`': //TODO: remove this debug feature
            System.exit(0); //for temporary testing 
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
        break;
      case LEFT:
        isLeftPressed = false;
        break;
      default:
        switch(this.key){
          
        }
    }
  }

	/**
	 * This method is automatically called by Processing every time the user clicks a mouse button.
	 * - The `mouseX` and `mouseY` variables are automatically is assigned the coordinates on the screen when the mouse was clicked.
   * - The `mouseButton` variable is automatically assigned the value of either the PApplet.LEFT or PApplet.RIGHT constants, depending upon which button was pressed.
   */
	public void mouseClicked() {
		System.out.println(String.format("Mouse clicked at: %d:%d.", this.mouseX, this.mouseY));

	}

	/**
	 * This method is automatically called by Processing every time the user presses down and drags the mouse.
	 * The `mouseX` and `mouseY` variables are automatically is assigned the coordinates on the screen when the mouse was clicked.
   */
	public void mouseDragged() {
		System.out.println(String.format("Mouse dragging at: %d:%d.", mouseX, mouseY));
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
    String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
    String path = Paths.get(cwd,"images","playerStandingStill.png").toString();
    player = new Player(this, path, 0, 0,64,64);

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
