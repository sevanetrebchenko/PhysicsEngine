
package com.sevanetrebchenko.physicsengine;

import java.util.ArrayList;
import java.util.Arrays;
import com.sevanetrebchenko.physicsengine.GameObject;

public class Shape implements GameObject {
    private Vertex [] vertices;
    private Stick [] shapeSticks;
    private Stick [] supportSticks;
    private Vertex initialPosition;
    private Vertex center;

    private int numberOfPoints;
    private int radius;
    private double mass;
    
    public Shape (Vertex initialPosition, int numberOfPoints, int radius, double mass) {
        this.initialPosition = initialPosition;
        this.numberOfPoints = numberOfPoints;
        this.radius = radius;
        this.mass = mass;
    }
    
    public void initComponents() {
        // VERTICES
        
        // Initialize vertex array
        this.vertices = new Vertex[this.numberOfPoints + 1];
        double angleIncrement = 360 / this.numberOfPoints;
        double angle = 0;
        this.center = new Vertex (this.initialPosition.getNewX() + (this.radius), this.initialPosition.getNewY() + (this.radius)); 
        
        // Circle gets divided by the number of vertices
        // The angle increment is determined and added to each subsequent vertice to create a regular shape around a circle
        for (int i = 0; i < this.numberOfPoints; i++) {
            int newVertexX = (int)(this.center.getNewX() + (Math.sin(angle * Math.PI / 180) * this.radius));
            int newVertexY = (int)(this.center.getNewY() + (Math.cos(angle * Math.PI / 180) * this.radius));
            
            int changeX = this.initialPosition.getNewX() - this.initialPosition.getOldX();
            int changeY = this.initialPosition.getNewY() - this.initialPosition.getOldY();
              
            int oldVertexX = (int)(this.center.getNewX() + (Math.sin(angle * Math.PI / 180) * this.radius) - changeX);
            int oldVertexY = (int)(this.center.getNewY() + (Math.cos(angle * Math.PI / 180) * this.radius) - changeY);
            this.vertices[i] = new Vertex(newVertexX, newVertexY, oldVertexX, oldVertexY);
            angle += angleIncrement;
        }
        
        this.vertices[this.vertices.length - 1] = this.center;
        
        // SHAPE STICKS
        
        // Initialize the shapeSticks array
        this.shapeSticks = new Stick[this.numberOfPoints];
        for (int i = 0; i < this.vertices.length - 2; i++) {
            this.shapeSticks[i] = new Stick (this.vertices[i], this.vertices[i + 1]);
        }
        this.shapeSticks[this.shapeSticks.length - 1] = new Stick (this.vertices[this.vertices.length - 2], this.vertices[0]);
        
        // SUPPORT STICKS
        
        // Total calculated number of support sticks
        int numberOfSupportSticks = 0;
        for (int i = 0; i < this.numberOfPoints - 3; i++) {
            numberOfSupportSticks += i;
        }
        
        // Initialize the supportSticks array
        // Enough sticks to both connect each vertex to the center vertex
        // and connect each vertex to every other vertex only once
        this.supportSticks = new Stick[this.numberOfPoints + (2 * (this.numberOfPoints - 3) + numberOfSupportSticks)];
        int counter = 0;
        
        // Connecting each vertex to the center vertex
        for (int i = 0; i < this.numberOfPoints; i++) {
            this.supportSticks[i] = new Stick (this.vertices[i], this.vertices[this.vertices.length - 1]);
            counter++;
        }
        
        if (this.numberOfPoints > 3) {
            
            // Connecting each vertex to every other vertex only once
            
            // ArrayList to store all the possible sticks that can be created
            ArrayList <Stick> storage = new ArrayList <Stick>();
            
            // Add all the possible sticks 
            for (int i = 0; i < this.numberOfPoints; i++) {
                for (int j = 0; j < this.numberOfPoints; j++) {
                    if (j != i) {
                        storage.add(new Stick(this.vertices[i], this.vertices[j]));
                    }
                }
            }
            
            for (int i = 0; i < storage.size(); i++) {
                for (Stick stick : this.shapeSticks) {
                    
                    // Remove sticks that are the same distance as the sticks connecting the vertices
                    // as those sticks already exist in the shapeSticks array
                    double error = stick.getDistance() / 100 * 3;
                    double stickDistance = stick.getDistance() + error;
                    if (storage.get(i).getDistance() < stickDistance) {
                        storage.remove(i);
                        
                        // Removal from an ArrayList requires a reset of the index
                        if (i == 0) {
                            i = 0;
                        }
                        else {
                            i--;
                        }
                    }
                }
            }
            
            // Remove sticks that exists as duplicates with opposite starting and ending vertices
            for (int i = 0; i < storage.size(); i++) {
                Stick stick1 = storage.get(i);
                for (int j = 0; j < storage.size(); j++) {
                    Stick stick2 = storage.get(j);
                    if (i != j) {
                        if (stick1.getStart() == stick2.getEnd() && stick1.getEnd() == stick2.getStart()) {
                            storage.remove(j);
                            
                            // Removal from an ArrayList requires a reset of the index
                            if (i == 0) {
                                i = 0;
                            }
                            else {
                                i--;
                            }
                            
                            if (j == 0) {
                                j = 0;
                            }
                            else {
                                j--;
                            }
                        }
                    }
                }
            }
            
            for (Stick stick : storage) {
                this.supportSticks[counter] = stick;
                counter++;
            }
        }
    }
    
    public void run() {
        render();
        update();
        constrain();
    }

    @Override
    public void render() {
        for (Vertex vertex : this.vertices) {
            vertex.render();
        }
        
        for (Stick stick : this.shapeSticks) {
            stick.render();
        }
        
        for (Stick stick : this.supportSticks) {
            stick.render();
        }
    }

    @Override
    public void update() {
        for (Vertex vertex : this.vertices) {
            vertex.update();
        }
        
        for (Stick stick : this.shapeSticks) {
            stick.update();
        }
        
        for (Stick stick : this.supportSticks) {
            stick.update();
        }
    }

    @Override
    public void constrain() {
        for (Vertex vertex : this.vertices) {
            vertex.constrain();
        }
        
        for (Stick stick : this.shapeSticks) {
            stick.constrain();
        }
        
        for (Stick stick : this.supportSticks) {
            stick.constrain();
        }
    }
    
    public boolean getStationary() {
        return this.center.getPinned();
    }
    
    public int getRadius() {
        return this.radius;
    }
    
    public double getMass() {
        return this.mass;
    }

    public Vertex[] getVertices() {
        return this.vertices;
    }   
    
    public Vertex getCenter() {
        return this.center;
    }
}
