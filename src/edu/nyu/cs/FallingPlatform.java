package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class FallingPlatform extends Platform{
    
    public FallingPlatform(Game app,PImage tile, int x, int y, int width, int height){
        super(app, tile, x, y, width, height);
        this.isFallable = true;
    }
    public void fall(ArrayList<Platform> platforms){
        //TODO: animate
        platforms.remove(this);
    }
}