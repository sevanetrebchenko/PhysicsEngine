/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sevanetrebchenko.physicsengine;

/**
 *
 * @author sevan
 */
public interface GameObject {
    void render();
    void update();
    void constrain();
}
