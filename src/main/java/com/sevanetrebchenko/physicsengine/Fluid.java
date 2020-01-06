package com.sevanetrebchenko.physicsengine;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_LINES;

public class Fluid {
    
    private ArrayList <FluidParticle> particles;
    private GridRectangle[][] grid;
    private int resolution;
    private int numberOfColumns;
    private int numberOfRows;
    
    public Fluid (int resolution) {
        this.resolution = resolution;
    }
    
    public void run() {
        // Draw the lines on the grid for calculations 
//        drawGridLines();

        // Run each particle
        for (FluidParticle particle : this.particles) {
            particle.run();
        }
        
        // Draw the calculated lines
        drawLines();
        
        // Detect the collision of the FluidParticles and restore their positions
//        collision();
        
        // Color the FluidParticles a passed color
//        color(0, 1, 1);
        
    }
    
    public void initComponents() {
        
        // Add particles into the ArrayList to render on the Display
        // utilize mouse input or generate singlehandedly
        this.particles = new ArrayList <FluidParticle> ();
        this.particles.add(new FluidParticle(new Vertex(200, 100, 180, 110), 100, 10));
        this.particles.add(new FluidParticle(new Vertex(600, 600), 150, 10));
        this.particles.add(new FluidParticle(new Vertex(620, 500, 630, 490), 100, 10));
        this.particles.add(new FluidParticle(new Vertex(500, 500, 500, 500), 100, 10));
        this.particles.add(new FluidParticle(new Vertex(170, 140, 150, 150), 100, 10));
        
        for (FluidParticle particle : this.particles) {
            particle.initComponents();
//            particle.getCenter().setPinned(true);
        }
        
        // Initialize the number of columns and rows for the Display
        this.numberOfColumns = Display.getWidth() / this.resolution;
        this.numberOfRows = Display.getHeight() / this.resolution;
        
        // Initialize grid rectangles for the grid
        this.grid = new GridRectangle[this.numberOfColumns][this.numberOfRows];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = new GridRectangle(new Vertex(i * this.resolution, j * this.resolution + this.resolution), this.resolution, this.particles);
                this.grid[i][j].initComponents();
            }
        }
    }
    
    // Draw the grid lines for visual aid in seeing the grid array and subsequent calculations
    private void drawGridLines() {
        // draw vertical grid lines
        GL11.glColor3f((float)0.3, (float)0.3, (float)0.3);
        for (int i = 0; i < this.numberOfColumns; i++) {
            GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2i(i * this.resolution, 0);
                GL11.glVertex2i(i * this.resolution, Display.getHeight());
            GL11.glEnd();
        }
        
        // draw horizontal grid lines
        for (int i = 0; i < this.numberOfRows; i++) {
            GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2i(0, i * this.resolution);
                GL11.glVertex2i(Display.getWidth(), i * this.resolution);
            GL11.glEnd();
        }
    }
    
    // Determine if a passed Point parameter's rectangle on the grid should be filled
    private boolean calculateShaded(Point rectanglePoint) {
        double sum = 0;
        for (int i = 0; i < this.particles.size(); i++) {
            FluidParticle p = this.particles.get(i);
            Vertex c = p.getCenter();
            double radius = p.getRadius() * p.getRadius();
            double xDist = (rectanglePoint.x - c.getNewX()) * (rectanglePoint.x - c.getNewX());
            double yDist = (rectanglePoint.y - c.getNewY()) * (rectanglePoint.y - c.getNewY());
            double dist = radius / (xDist + yDist);
            sum += dist;
        }
        // Determine if the total sum using the equation from (Wong, Jamie) is less than or equal to 1
        // Rectangle on the grid should not be filled
        return sum > 1;
        
    }
    
    private void drawLines() {
        for (GridRectangle[] rectangle : this.grid) {
            for (GridRectangle r : rectangle) {
                for (int k = 0; k < r.getGridVertices().length; k++) {
                    if (calculateShaded(new Point (r.getGridVertices()[k].getNewX(), r.getGridVertices()[k].getNewY()))) {
                        r.getGridVertices()[k].setFlagged(true);
                    }
                    else {
                        r.getGridVertices()[k].setFlagged(false);
                    }
                }
                r.drawLines();
            }
        }
    }

    public void collision() {
        for (FluidParticle particle : this.particles) {
            for (FluidParticle other : this.particles) {
                if (!particle.equals(other)) {
                    particle.collisionResponse(other);
                }
            }
        }
    }
    
    private void color(double red, double blue, double green) {
        GL11.glColor3f((float)red, (float)green, (float)blue);
        ArrayList <Stick> stickDump = new ArrayList <Stick> ();
        for (GridRectangle[] row : this.grid) {
            for (GridRectangle column : row) {
                for (Stick stick : column.getSticks()) {
                    if (stick != null) {
                        stickDump.add(stick);
                        int __100 = 0;
                    }
                }
            }
        }
        
        ArrayList <Vertex> vertices = sortVertices(stickDump);
        
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (i != j) {
                    if ((vertices.get(i).getNewX() == vertices.get(j).getNewX()) && (vertices.get(i).getNewY() == vertices.get(j).getNewY())) {
                        vertices.remove(vertices.get(j));
                        j--;
                    }
                }
            }
        }
        
        int buffer = vertices.size() % 2;

        GL11.glBegin(GL11.GL_TRIANGLES);
        for (int i = 0; i < vertices.size() - (buffer + 1); i++) {
                GL11.glVertex2d(vertices.get(i).getNewX(), vertices.get(i).getNewY());
                GL11.glVertex2d(vertices.get(i + 1).getNewX(), vertices.get(i + 1).getNewY());
                GL11.glVertex2d(/* center */ this.particles.get(0).getCenter().getNewX(), this.particles.get(0).getCenter().getNewY());
        }
        GL11.glEnd();
        
        stickDump.clear();
        vertices.clear();
    }
     
    private ArrayList<Vertex> sortVertices (ArrayList <Stick> sticks) {
        ArrayList <Vertex> sortedVertices = new ArrayList <Vertex> ();
        sortedVertices.add(sticks.get(0).getEnd());
        int pointerToMostCurrent = 0;
        for (int i = 0; i < sticks.size(); i++) {
            // If the start of the next stick is equal to the end of the previously sorted stick
            // the end of the stick gets added as a new vertex of the polygon
            
            // if comparing ends add starts, if comparing starts add ends
            if (sticks.get(i).getStart().getNewX() == sortedVertices.get(pointerToMostCurrent).getNewX()) {
                if (sticks.get(i).getStart().getNewY() == sortedVertices.get(pointerToMostCurrent).getNewY()) {
                    pointerToMostCurrent++;
                    sortedVertices.add(sticks.get(i).getEnd());
                    sticks.remove(sticks.get(i));
                    i = 0;
                }
            }
            
            else if ((sticks.get(i).getEnd().getNewX()) == sortedVertices.get(pointerToMostCurrent).getNewX()) {
                if ((sticks.get(i).getEnd().getNewY() == sortedVertices.get(pointerToMostCurrent).getNewY())) {
                    pointerToMostCurrent++;
                    sortedVertices.add(sticks.get(i).getStart());
                    sticks.remove(sticks.get(i));
                    i = 0;
                }
            }
            
            else {
            }
        }
        return sortedVertices;
    }
}
