package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;

/* All game/GUI states must implement this interface */
public interface State {
    void setup(GUI gui);
    void update(GUI gui, double deltaTime);
    void render(GUI gui);

    void resize();
}
