/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sevanetrebchenko.physicsengine;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

/**
 *
 * @author sevan
 */
public class FluidParticle extends Shape implements Collidable {
    private double mass;
    private Vertex position;
    private int radius;
    
    private double xVelocity;
    private double yVelocity;
    
    // Collision
    private double distance;
    
    public FluidParticle(Vertex position, int radius, double mass) {
        super(position, 8, radius, mass);
        this.position = position;
        this.radius = radius;
        this.mass = mass;
    }
    
    @Override
    public void run() {
//        render();
        update();
        constrain();
//        drawLines();
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public void update() {
        super.update();
    }
    
    @Override
    public void constrain() {
        super.constrain();
    }
    
    @Override
    public Vertex getCenter() {
        return super.getCenter();
    }
    
    @Override
    public int getRadius() {
        return super.getRadius();
    }
    
    @Override
    public double getMass() {
        return this.mass;
    }
    
    public void drawLines() {
        
        // Draw lines representing the velocity normal to the FluidParticle (blue)
        this.xVelocity = this.getCenter().getNewX() - this.getCenter().getOldX();
        this.yVelocity = this.getCenter().getNewY() - this.getCenter().getOldY();
        
        GL11.glColor3d(0, 0, 255);
        glBegin(GL11.GL_LINES);
            GL11.glVertex2d(this.getCenter().getNewX(), this.getCenter().getNewY());
            GL11.glVertex2d(this.getCenter().getNewX() + (xVelocity * GlobalConstants.LINE_PROPORTION), this.getCenter().getNewY() + (yVelocity * GlobalConstants.LINE_PROPORTION));
        glEnd();
        GL11.glColor3d(255, 255, 255);
        
        // Draw lines representing the velocity perpendicular to the FluidParticle (red)
        GL11.glColor3d(255, 0, 0);
        glBegin(GL11.GL_LINES);
            GL11.glVertex2d(this.getCenter().getNewX(), this.getCenter().getNewY());
            GL11.glVertex2d(this.getCenter().getNewX() + (yVelocity * GlobalConstants.LINE_PROPORTION), this.getCenter().getNewY() - (xVelocity * GlobalConstants.LINE_PROPORTION));
        glEnd();
        GL11.glColor3d(255, 255, 255);
    }
    
    @Override
    // Handle FluidParticle collisions with other GameObjects
    public void collisionResponse(GameObject collided) {
        
        // If the other GameObject is another FluidParticle
        if (collided instanceof FluidParticle) {
            
            FluidParticle other = (FluidParticle) collided;
            
            // Centers of the FluidParticles before any collisions occur
            int firstCenterX = this.getCenter().getNewX();
            int firstCenterY = this.getCenter().getNewY();
            int secondCenterX = other.getCenter().getNewX();
            int secondCenterY = other.getCenter().getNewY();
            
            // Detect if the circles are colliding
            int dy = other.getCenter().getNewY() - this.getCenter().getNewY();
            int dx = other.getCenter().getNewX() - this.getCenter().getNewX();
            this.distance = Math.sqrt(dx * dx + dy * dy);

            // If the distance between the two FluidParticles is less than the sum of
            // their radii, the FluidParticles are colliding
            if (this.distance < (other.radius + this.radius)) {
                
                // Velocities to determine what kind of collision has occurred
                int xVelocityOne = this.getCenter().getNewX() - this.getCenter().getOldX();
                int yVelocityOne = this.getCenter().getNewY() - this.getCenter().getOldY();
                int xVelocityTwo = other.getCenter().getNewX() - other.getCenter().getOldX();
                int yVelocityTwo = other.getCenter().getNewY() - other.getCenter().getOldY();
                
                // Collision response if both FluidParticles are stationary
                    
                if (this.getCenter().getPinned() && other.getCenter().getPinned()) {
                    System.out.println("one");
                    // Determine the point of the midpoint between the two circles
                    int midpointX = (this.getCenter().getNewX() + other.getCenter().getNewX()) / 2;
                    int midpointY = (this.getCenter().getNewY() + other.getCenter().getNewY()) / 2;
                    
                    // Distance to move each point from its current position
                    int firstMoveX = (int)(firstCenterX - (midpointX + this.radius * (this.getCenter().getNewX() - other.getCenter().getNewX()) / this.distance));
                    int firstMoveY = (int)(firstCenterY - (midpointY + this.radius * (this.getCenter().getNewY() - other.getCenter().getNewY()) / this.distance));
                    int secondMoveX = (int)(secondCenterX - (midpointX + other.radius * (other.getCenter().getNewX() - this.getCenter().getNewX()) / this.distance));
                    int secondMoveY = (int)(secondCenterY - (midpointY + other.radius * (other.getCenter().getNewY() - this.getCenter().getNewY()) / this.distance));
                    
                    // Setting the new and old positions of the current FluidParticle
                    // using the distances determined
                    for (Vertex vertex : this.getVertices()) {
                        vertex.setNewX(vertex.getNewX() - firstMoveX);
                        vertex.setNewY(vertex.getNewY() - firstMoveY);
                        vertex.setOldX(vertex.getOldX() - firstMoveX);
                        vertex.setOldY(vertex.getOldY() - firstMoveY);
                    }
                    
                    // Setting the new and old position of the other FluidParticle
                    // using the distances determined
                    for (Vertex vertex : other.getVertices()) {
                        vertex.setNewX(vertex.getNewX() - secondMoveX);
                        vertex.setNewY(vertex.getNewY() - secondMoveY);
                        vertex.setOldX(vertex.getOldX() - secondMoveX);
                        vertex.setOldY(vertex.getOldY() - secondMoveY);
                    }
                }
                
                // Collision when one FluidParticle is stationary
                else if ((this.getCenter().getPinned() && !other.getCenter().getPinned()) || (!this.getCenter().getPinned()) && other.getCenter().getPinned()) {
                    Vertex v = this.getClosestPointOnLine(this.getCenter(), new Vertex(this.getCenter().getNewX() + xVelocityTwo, this.getCenter().getNewY() + yVelocityTwo), other.getCenter());
                    double distanceToCenter = Math.pow(other.getCenter().getNewX() - v.getNewX(), 2) + Math.pow(other.getCenter().getNewY() - v.getNewY(), 2);
                    System.out.println(distanceToCenter);
                    if (distanceToCenter <= Math.pow(other.radius + this.radius, 2)) {
                        double backdist = Math.sqrt(Math.pow(this.radius + other.radius, 2) - distanceToCenter); 
//                        System.out.println(backdist);
                        double movementvectorlength = Math.sqrt(xVelocityTwo * xVelocityTwo + yVelocityTwo * yVelocityTwo);
//                        System.out.println(movementvectorlength);
                        double c_x = v.getNewX() - backdist * (xVelocityTwo / movementvectorlength); 
//                        System.out.println(c_x);
                        double c_y = v.getNewY() - backdist * yVelocityTwo / movementvectorlength;
                        double collisiondist = Math.sqrt(Math.pow(other.getCenter().getNewX() - c_x, 2) + Math.pow(other.getCenter().getNewY() - c_y, 2)); 
                        double n_x = (other.getCenter().getNewX() - c_x) / collisiondist; 
                        double n_y = (other.getCenter().getNewY() - c_y) / collisiondist; 
                        double p = 2 * (xVelocityTwo * n_x + yVelocityTwo * n_y) / 
                                    (this.mass + other.mass); 
                        double w_x = xVelocityTwo - p * this.mass * n_x - p * other.mass * n_x; 
                        double w_y = yVelocityOne - p * this.mass * n_y - p * other.mass * n_y;
                        
                        if (this.getCenter().getPinned()) {

                            other.getCenter().setOldX(other.getCenter().getNewX());
                            other.getCenter().setOldY(other.getCenter().getNewY());
                            other.getCenter().setNewX((int)(other.getCenter().getNewX() + w_x * GlobalConstants.BOUNCE));
                            other.getCenter().setNewY((int)(other.getCenter().getNewY() + w_y * GlobalConstants.BOUNCE));

                            for (Vertex vertex : other.getVertices()) {
                                vertex.setOldX(vertex.getNewX());
                                vertex.setOldY(vertex.getNewY());
                                vertex.setNewX((int)(vertex.getNewX() + w_x * GlobalConstants.BOUNCE));
                                vertex.setNewY((int)(vertex.getNewY() + w_y * GlobalConstants.BOUNCE));
                            }
                        }
                        
                        if (other.getCenter().getPinned()) {
                            this.getCenter().setOldX(other.getCenter().getNewX());
                            this.getCenter().setOldY(other.getCenter().getNewY());
                            this.getCenter().setNewX((int)(this.getCenter().getNewX() + w_x * GlobalConstants.BOUNCE));
                            this.getCenter().setNewY((int)(this.getCenter().getNewY() + w_y * GlobalConstants.BOUNCE));

                            for (Vertex vertex : this.getVertices()) {
                                vertex.setOldX(vertex.getNewX());
                                vertex.setOldY(vertex.getNewY());
                                vertex.setNewX((int)(vertex.getNewX() + w_x * GlobalConstants.BOUNCE));
                                vertex.setNewY((int)(vertex.getNewY() + w_y * GlobalConstants.BOUNCE));
                            }
                        }
                    }
                }
                // Collision between two moving FluidParticles
                else {
                    System.out.println("two");
                    double d = Math.sqrt(Math.pow(this.getCenter().getNewX() - other.getCenter().getNewX(), 2) + Math.pow(this.getCenter().getNewY() - other.getCenter().getNewY(), 2)); 
                    double nx = (other.getCenter().getNewX() - this.getCenter().getNewX()) / d; 
                    double ny = (other.getCenter().getNewY() - this.getCenter().getNewY()) / d; 
                    double p = 2 * (xVelocityOne * nx + yVelocityOne * ny - xVelocityTwo * nx - yVelocityTwo * ny) / (this.mass + other.mass); 
                    
                    for (Vertex vertex : this.getVertices()) {
                        vertex.setOldX(vertex.getNewX());
                        vertex.setOldY(vertex.getNewY());
                        vertex.setNewX((int)(vertex.getNewX() + (xVelocityOne - p * this.mass * nx) * GlobalConstants.BOUNCE));
                        vertex.setNewY((int)(vertex.getNewY() + (yVelocityOne - p * this.mass * ny) * GlobalConstants.BOUNCE));
                    }
                    
                    for (Vertex vertex : other.getVertices()) {
                        vertex.setOldX(vertex.getNewX());
                        vertex.setOldY(vertex.getNewY());
                        vertex.setNewX((int)(vertex.getNewX() + (xVelocityTwo + p * other.mass * nx) * GlobalConstants.BOUNCE));
                        vertex.setNewY((int)(vertex.getNewY() + (yVelocityTwo + p * other.mass * ny) * GlobalConstants.BOUNCE));
                    }
                }
            }
        }
    }
    
    private Vertex getClosestPointOnLine(Vertex oldPosition, Vertex newPosition, Vertex outsidePoint) {
        int xVelocity = newPosition.getNewX() - oldPosition.getNewX();
        int yVelocity = newPosition.getNewY() - oldPosition.getNewY();
        int normalLineVelocity = xVelocity * oldPosition.getNewY() + yVelocity * oldPosition.getNewX();
        int perpendicularLineVelocity = -yVelocity * outsidePoint.getNewX() + xVelocity * outsidePoint.getNewY();
        
        int determinant = yVelocity * yVelocity + xVelocity * xVelocity;
        int xToReturn = 0;
        int yToReturn = 0;
        
        if (determinant != 0) {
            xToReturn = (yVelocity * normalLineVelocity - xVelocity * perpendicularLineVelocity) / determinant;
            yToReturn = (yVelocity * perpendicularLineVelocity + xVelocity * normalLineVelocity) / determinant;
        }
        else {
            xToReturn = outsidePoint.getNewX();
            yToReturn = outsidePoint.getNewY();
        }
        
        return new Vertex(xToReturn, yToReturn);
    }
}