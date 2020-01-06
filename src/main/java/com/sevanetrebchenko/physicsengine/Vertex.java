/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sevanetrebchenko.physicsengine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import com.sevanetrebchenko.physicsengine.GameObject;
import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 *
 * @author sevan
 */
public class Vertex implements GameObject, GlobalConstants {
    private int newX;
    private int newY;
    private int oldX;
    private int oldY;
    private boolean pinned;
    private boolean flagged;
    private Rectangle boundingBox;
    
    public Vertex (int newx, int newy, int oldx, int oldy) {
        this.newX = newx;
        this.newY = newy;
        this.oldX = oldx;
        this.oldY = oldy;
        this.pinned = false;
        this.boundingBox = new Rectangle(new Point(this.newX - GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE), new Dimension(this.POINT_SIZE * 2, this.POINT_SIZE * 2));
    }
    
    public Vertex (int newx, int newy, int oldx, int oldy, boolean pinned) {
        this.newX = newx;
        this.newY = newy;
        this.oldX = oldx;
        this.oldY = oldy;
        this.pinned = pinned;
        this.boundingBox = new Rectangle(new Point(this.newX - GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE), new Dimension(this.POINT_SIZE * 2, this.POINT_SIZE * 2));

    }
    
    public Vertex (int x, int y) {
        this.newX = x;
        this.oldX = x;
        this.newY = y;
        this.oldY = y;
        this.pinned = false;
        this.boundingBox = new Rectangle(new Point(this.newX - GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE), new Dimension(this.POINT_SIZE * 2, this.POINT_SIZE * 2));

    }
    
    public Vertex (int x, int y, boolean pinned) {
        this.newX = x;
        this.oldX = x;
        this.newY = y;
        this.oldY = y;
        this.pinned = pinned;
        this.boundingBox = new Rectangle(new Point(this.newX - GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE), new Dimension(this.POINT_SIZE * 2, this.POINT_SIZE * 2));

    }
    
        // Return if this Vertex is stationary or not
    public boolean getPinned() {
        return this.pinned;
    }
    
    // Set if this Vertex is stationary or not
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
    
    // Return the current X
    public int getNewX() {
        return this.newX;
    }
    
    // Set the current X
    public void setNewX (int x) {
        this.newX = x;
    }
    
    // Return the old X
    public int getOldX(){
        return this.oldX;
    }
    
    // set the old X
    public void setOldX (int x) {
        this.oldX = x;
    }
    
    // Return the current Y
    public int getNewY() {
        return this.newY;
    }
    
    // Set the current Y
    public void setNewY (int y) {
        this.newY = y;
    }
    
    // Return the old Y
    public int getOldY() {
        return this.oldY;
    }
    
    // Set the old Y
    public void setOldY (int y) {
        this.oldY = y;
    }
    
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    
    public boolean getFlagged() {
        return this.flagged;
    }
    
    // Print out the position of the Vertex on the Display
    @Override
    public String toString() {
        return "(" + this.newX + ", " + this.newY + ")";
    }
    
    @Override
    public void render() {
        GL11.glBegin(GL_QUADS);
            GL11.glVertex2i(this.newX - GlobalConstants.POINT_SIZE, this.newY - GlobalConstants.POINT_SIZE);
            GL11.glVertex2i(this.newX + GlobalConstants.POINT_SIZE, this.newY - GlobalConstants.POINT_SIZE);
            GL11.glVertex2i(this.newX + GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE);
            GL11.glVertex2i(this.newX - GlobalConstants.POINT_SIZE, this.newY + GlobalConstants.POINT_SIZE);
        GL11.glEnd();
    }

    @Override
    public void constrain() {
        if (!this.pinned) {
            double velocityX = (this.newX - this.oldX) * GlobalConstants.FRICTION;
            double velocityY = (this.newY - this.oldY) * GlobalConstants.FRICTION;
            
            if (this.getNewX() > Display.getWidth()) {
                this.setNewX(Display.getWidth());
                this.setOldX((int)(this.getNewX() + velocityX * GlobalConstants.BOUNCE));
            }
            
            if (this.getNewX() < 0) {
                this.setNewX(0);
                this.setOldX((int)(this.getNewX() + velocityX * GlobalConstants.BOUNCE));
            }
            
            if (this.getNewY() > Display.getHeight()) {
                this.setNewY(Display.getHeight());
                this.setOldY((int)(this.getNewY() + velocityY * GlobalConstants.BOUNCE));
            }
            
            if (this.getNewY() < 0) {
                this.setNewY(0);
                this.setOldY((int)(this.getNewY() + velocityY * GlobalConstants.BOUNCE));
            }
        }
    }

    @Override
    public void update() {
        if (!this.pinned) {
            double velocityX = (this.newX - this.oldX) * GlobalConstants.FRICTION;
            double velocityY = (this.newY - this.oldY) * GlobalConstants.FRICTION;
            
            this.setOldX(this.getNewX());
            this.setOldY(this.getNewY());
            this.setNewX((int)(this.getNewX() + velocityX));
            this.setNewY((int)(this.getNewY() + velocityY + GlobalConstants.GRAVITY));
        }
    }
    
    public void mouseInput() {
        
    }
}
