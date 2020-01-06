
package com.sevanetrebchenko.physicsengine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class Fabric implements GameObject {
    
    private Vertex [][] vertices;
    private Stick [][] horizontalSticks;
    private Stick [][] verticalSticks;
    private Point startingPosition;
    private Dimension spacing;
    private int numberOfVertices;
    private int numberOfRows;
    private int numberOfColumns;
    private double tension;
    
    public Fabric(int numberOfRows, int numberOfColumns, Point startingPosition, Dimension spacing, double tension) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.startingPosition = startingPosition;
        this.spacing = spacing;
        this.tension = tension;
        this.numberOfVertices = this.numberOfRows * this.numberOfColumns;
    }
    
    public void initComponents() {
        // Initialize the vertices
        this.vertices = new Vertex[this.numberOfRows][this.numberOfColumns];
        int startingX = this.startingPosition.x;
        for (Vertex[] vertex : this.vertices) {
            this.startingPosition.x = startingX;
            for (int j = 0; j < vertex.length; j++) {
                vertex[j] = new Vertex(this.startingPosition.x, this.startingPosition.y);
                this.startingPosition.x += this.spacing.width;
            }
            this.startingPosition.y += this.spacing.height;
        }
        
        // Initialize the horizonal sticks
        this.horizontalSticks = new Stick [this.numberOfRows][this.vertices[0].length - 1];
        for (int i = 0; i < this.vertices.length; i++) {
            for (int j = 0; j < this.vertices[i].length - 1; j++) {
                Vertex p1 = this.vertices[i][j];
                Vertex p2 = this.vertices[i][j + 1];
                this.horizontalSticks[i][j] = new Stick (p1, p2);
            }
        }
        // Initialize the vertical sticks
        this.verticalSticks = new Stick [this.numberOfRows - 1][this.vertices[0].length];
        for (int i = 0; i < this.vertices.length - 1; i++) {
            for (int j = 0; j < this.vertices[i].length; j++) {
                Vertex p1 = this.vertices[i][j];
                Vertex p2 = this.vertices[i + 1][j];
                this.verticalSticks[i][j] = new Stick (p1, p2);
            }
        }
        
        // Set top row of vertices to pinned so the fabric hangs
        for (Vertex[] vertex : this.vertices) {
            for (int j = 0; j < vertex.length; j++) {
                if (j % 3 == 0) {
                    this.vertices[0][j].setPinned(true);
                }
            }
        }
//        this.vertices[0][this.numberOfColumns - 1].setPinned(true);
        
    }
    
    public void run() {
        render();
        update();
        constrain();
    }

    @Override
    public void render() {
        for (Stick[] stick : this.horizontalSticks) {
            for (Stick stick1 : stick) {
                stick1.render();
            }
        }
    }

    @Override
    public void update() {
        for (Vertex[] vertex : this.vertices) {
            for (Vertex vertex1 : vertex) {
                vertex1.update();
            }
        }
        
        for (Stick[] stick : this.horizontalSticks) {
            for (Stick stick1 : stick) {
                stick1.render();
            }
        }
        
        for (Stick[] stick : this.verticalSticks) {
            for (Stick stick1 : stick) {
                stick1.render();
            }
        }
    }

    @Override
    public void constrain() {
        for (int i = 0; i < this.tension; i++) {
            for (Vertex[] vertex : this.vertices) {
                for (Vertex vertex1 : vertex) {
                    vertex1.constrain();
                }
            }

            for (Stick[] stick : this.horizontalSticks) {
                for (Stick stick1 : stick) {
                    stick1.constrain();
                }
            }

            for (Stick[] stick : this.verticalSticks) {
                for (Stick stick1 : stick) {
                    stick1.constrain();
                }
            }
        }
    }
    
}
