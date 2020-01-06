package com.sevanetrebchenko.physicsengine;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import com.sevanetrebchenko.physicsengine.Fabric;
import com.sevanetrebchenko.physicsengine.Fluid;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

public class PhysicsEngine {
    
    private ArrayList <Object> gameObjects;
    private ArrayList <Vertex> vertices;
    private ArrayList <Stick> sticks;
    
    public PhysicsEngine() {
        
        // Initialize the objects ArrayList.
        this.gameObjects = new ArrayList <Object> ();
        this.vertices = new ArrayList <Vertex> ();
        this.sticks = new ArrayList <Stick> ();
        
        // Initialize the components for the Display.
        try {
            Display.setFullscreen(true);
            Display.create();
            Keyboard.create();
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        }
        
        catch (LWJGLException e) {
            System.out.println(e.toString());
        }
    }
    
    // Run the physics engine
    public void run() {
            
        initializeComponents();
        
        // While the Display is still active on the screen.
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            for (Object gameObject : this.gameObjects) {
                if (gameObject instanceof Shape) {
                    ((Shape) gameObject).run();
                }
                
                if (gameObject instanceof Fabric) {
                    ((Fabric) gameObject).run();
                }
                
                if (gameObject instanceof Fluid) {
                    ((Fluid) gameObject).run();
                }
            }
            
            for (Vertex vertex : this.vertices) {
                vertex.constrain();
                vertex.update();
                vertex.render();
            }
            
            for (Stick stick : this.sticks) {
                stick.constrain();
                stick.render();
            }
            
            // Update and sync the Display to the desired FPS value.
            Display.update();
            Display.sync(60);
        }
    }
    
    private void initializeComponents() {
        Shape shape = new Shape(new Vertex(500, 500, 480, 550), 4, 200, 10);
        this.gameObjects.add(shape);
        
        Fabric fabric = new Fabric(10, 20, new Point(100, 100), new Dimension(40, 40), 10);
        this.gameObjects.add(fabric);
        
        Fluid fluid = new Fluid(10);
        this.gameObjects.add(fluid);
        
        for (Object gameObject : this.gameObjects) {
            if (gameObject instanceof Shape) {
                ((Shape) gameObject).initComponents();
            }
            
            if (gameObject instanceof Fabric) {
                ((Fabric) gameObject).initComponents();
            }
            
            if (gameObject instanceof Fluid) {
                ((Fluid) gameObject).initComponents();
            }
        }
        
    }
}
