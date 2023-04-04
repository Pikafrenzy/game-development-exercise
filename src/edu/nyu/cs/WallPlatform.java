package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;

public class WallPlatform extends Platform{

    public WallPlatform(Game app,PImage tile, int x, int y, int width, int height){
        super(app, tile, x, y, width, height);
    }

    public int[][] getFloor(){ //no floor!
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[0][1] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[1][0] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[1][1] = Integer.MIN_VALUE; //null but works with math!
        return wallCoordinates;
    }
    public int[][] getCeiling(){ //no ceiling!
        int[][] wallCoordinates = new int[2][2];
        wallCoordinates[0][0] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[0][1] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[1][0] = Integer.MIN_VALUE; //null but works with math!
        wallCoordinates[1][1] = Integer.MIN_VALUE; //null but works with math!
        return wallCoordinates;
    }
}
