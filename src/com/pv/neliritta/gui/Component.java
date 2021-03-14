package com.pv.neliritta.gui;
// Written by Rainis Randmaa

/* All GUI components must implement this interface */
public interface Component {
    void update(double deltaTime);
    void render();
    void resize();
}
