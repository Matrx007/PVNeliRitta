package com.pv.neliritta.gui.states;

import com.pv.neliritta.GUI;

public interface State {
    void setup(GUI gui);
    void update(GUI gui, double deltaTime);
    void render(GUI gui);

    void resize();
}
