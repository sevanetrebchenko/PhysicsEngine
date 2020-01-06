/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sevanetrebchenko.physicsengine;

import org.lwjgl.opengl.GL11;
import com.sevanetrebchenko.physicsengine.GameObject;
import static org.lwjgl.opengl.GL11.GL_LINES;

/**
 *
 * @author sevan
 */
public class Stick implements GameObject, GlobalConstants {
    private Vertex start;
    private Vertex end;
    private double desiredDistance;
    private double currentDistance;
    private boolean visible;
    boolean collided;
    
    public Stick (Vertex a, Vertex b) {
        this.start = a;
        this.end = b;
        this.visible = true;
        this.desiredDistance = calculateDistance();
    }
    
    public Stick (Vertex a, Vertex b, boolean visible) {
        this.start = a;
        this.end = b;
        this.visible = visible;
        calculateDistance();
    }
    
    // Calculate the desired stick distance between two Vertices
    private double calculateDistance() {
        double xDist = this.end.getNewX() - this.start.getNewX();
        double yDist = this.end.getNewY() - this.start.getNewY();
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
    
    public Vertex getStart() {
        return this.start;
    }
    
    public Vertex getEnd() {
        return this.end;
    }
    
    public double getDistance() {
        return this.desiredDistance;
    }
    
    public boolean getCollided() {
        return this.collided;
    }
    
    public void setCollided(boolean collided) {
        this.collided = collided;
    }
    
    @Override
    public void render() {
        if (this.visible) {
            GL11.glBegin(GL_LINES);
                GL11.glVertex2i(this.start.getNewX(), this.start.getNewY());
                GL11.glVertex2i(this.end.getNewX(), this.end.getNewY());
            GL11.glEnd();
        }
    }

    @Override
    public void constrain() {
        double xDistance = this.start.getNewX() - this.end.getNewX();
        double yDistance = this.start.getNewY() - this.end.getNewY();
        this.currentDistance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        
        double distanceDifference = this.desiredDistance - this.currentDistance;
        double percentDifference = distanceDifference / this.currentDistance / 2;
        int toMoveX = (int)(xDistance * percentDifference);
        int toMoveY = (int)(yDistance * percentDifference);
        
        if (!this.start.getPinned()) {
            this.start.setNewX(this.start.getNewX() + toMoveX);
            this.start.setNewY(this.start.getNewY() + toMoveY);
        }
        
        if (!this.end.getPinned()) {
            this.end.setNewX(this.end.getNewX() - toMoveX);
            this.end.setNewY(this.end.getNewY() - toMoveY);
        }
    }

    @Override
    public void update() {
    }
    
    @Override
    public String toString() {
        return "Start Vertex: " + this.start.toString() + ", End Vertex: " + this.end.toString() + ", Length: " + this.desiredDistance;
    }
}
