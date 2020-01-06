
package com.sevanetrebchenko.physicsengine;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GridRectangle {
    
    private Vertex position;
    private int resolution;
    private Vertex [] gridVertices;
    private Vertex [] fluidVertices;
    private Stick [] sticks;
    private ArrayList <FluidParticle> particles;
            
    public GridRectangle(Vertex position, int resolution, ArrayList <FluidParticle> particles) {
        this.position = position;
        this.resolution = resolution;
        this.particles = particles;
    }
    
    public void initComponents() {
        this.fluidVertices = new Vertex [4];
        this.gridVertices = new Vertex [4];
        this.sticks = new Stick [2];
        
        // bottom left vertex
        this.gridVertices[0] = new Vertex(this.position.getNewX(), Display.getHeight() - this.position.getNewY());
        
        // bottom right vertex
        this.gridVertices[1] = new Vertex(this.position.getNewX() + this.resolution, Display.getHeight() - this.position.getNewY());
        
        // top right vertex
        this.gridVertices[2] = new Vertex(this.position.getNewX() + this.resolution, Display.getHeight() - this.position.getNewY() + this.resolution);
        
        // top left vertex
        this.gridVertices[3] = new Vertex(this.position.getNewX(), Display.getHeight() - this.position.getNewY() + this.resolution);
    }
    
    public void fillSquare() {
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(this.position.getNewX(), this.position.getNewY());
            GL11.glVertex2i(this.position.getNewX() + this.resolution, this.position.getNewY());
            GL11.glVertex2i(this.position.getNewX() + this.resolution, this.position.getNewY() + this.resolution);
            GL11.glVertex2i(this.position.getNewX(), this.position.getNewY() + this.resolution);
        GL11.glEnd();
    }
    
    public void drawLines() {
        if (this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configOne();
        }
        
        if (!this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configTwo();
        }
        
        if (this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configThree();
        }
        
        if (!this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configFour();
        }
        
        if (this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configFive();
        }
        
        if (!this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configSix();
        }
        
        if (this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && !this.gridVertices[3].getFlagged()) {
            configSeven();
        }
        
        if (!this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configEight();
        }
        
        if (this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configNine();
        }
        
        if (!this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configTen();
        }
        
        if (this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && !this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configEleven();
        }
        
        if (!this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configTwelve();
        }
        
        if (this.gridVertices[0].getFlagged() && !this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configThirteen();
        }
        
        if (!this.gridVertices[0].getFlagged() && this.gridVertices[1].getFlagged() && this.gridVertices[2].getFlagged() && this.gridVertices[3].getFlagged()) {
            configFourteen();
        }
    }
    
    private void configOne() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configTwo() {
        Vertex one = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configThree() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configFour() {
        Vertex one = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        Vertex two = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configFive() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        Vertex three = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        Vertex four = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
            
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
            
            GL11.glVertex2i(three.getNewX(), three.getNewY());
            GL11.glVertex2i(four.getNewX(), four.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.fluidVertices[0] = three;
        this.fluidVertices[1] = four;
        this.sticks[0] = new Stick(one, two);
        this.sticks[1] = new Stick(three, four);
    }
    
    private void configSix() {
        Vertex one = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configSeven() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configEight() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configNine() {
        Vertex one = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configTen() {
        Vertex one = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        Vertex two = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        Vertex three = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex four = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
            
            GL11.glVertex2i(three.getNewX(), three.getNewY());
            GL11.glVertex2i(four.getNewX(), four.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.fluidVertices[0] = three;
        this.fluidVertices[1] = four;
        this.sticks[0] = new Stick(one, two);
        this.sticks[1] = new Stick(three, four);
    }
    
    private void configEleven() {
        Vertex one = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        Vertex two = calculateLinesX(this.gridVertices[2], this.gridVertices[3]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configTwelve() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configThirteen() {
        Vertex one = calculateLinesY(this.gridVertices[2], this.gridVertices[1]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    private void configFourteen() {
        Vertex one = calculateLinesY(this.gridVertices[3], this.gridVertices[0]);
        Vertex two = calculateLinesX(this.gridVertices[1], this.gridVertices[0]);
        
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(one.getNewX(), one.getNewY());
            GL11.glVertex2i(two.getNewX(), two.getNewY());
        GL11.glEnd();
        
        this.fluidVertices[0] = one;
        this.fluidVertices[1] = two;
        this.sticks[0] = new Stick(one, two);
    }
    
    public Stick[] getSticks() {
        return this.sticks;
    }
    
    public int getResolution() {
        return this.resolution;
    }
    
    public Vertex getPosition() {
        return this.position;
    }
    
    public Vertex[] getGridVertices() {
        return this.gridVertices;
    }
    
    public Vertex[] getFluidVertices() {
        return this.fluidVertices;
    }
    
    private Vertex calculateLinesY(Vertex top, Vertex bottom) {
        int xCoordinate = top.getNewX();
        int yCoordinate = (int)(top.getNewY() + (bottom.getNewY() - top.getNewY()) * ((1 - calculateShaded(top)) / (calculateShaded(bottom) - calculateShaded(top))));
        return new Vertex(xCoordinate, yCoordinate);
    }
    
    private Vertex calculateLinesX(Vertex top, Vertex bottom) {
        int yCoordinate = top.getNewY();
        int xCoordinate = (int)(top.getNewX() + (bottom.getNewX() - top.getNewX()) * ((1 - calculateShaded(top)) / (calculateShaded(bottom) - calculateShaded(top))));
        return new Vertex(xCoordinate, yCoordinate);
    }
    
    private double calculateShaded(Vertex rectanglePoint) {
        double sum = 0;
        for (FluidParticle particle : this.particles) {
            Vertex c = particle.getCenter();
            double radius = particle.getRadius() * particle.getRadius();
            double xDist = (rectanglePoint.getNewX() - c.getNewX()) * (rectanglePoint.getNewX() - c.getNewX());
            double yDist = (rectanglePoint.getNewY() - c.getNewY()) * (rectanglePoint.getNewY() - c.getNewY());
            double dist = radius / (xDist + yDist);
            sum += dist;
        }
        return sum;
    }
}
