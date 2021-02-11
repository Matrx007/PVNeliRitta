// Written by Rainis Randmaa

import com.ydgames.mxe.Game;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;

// MX Engine is a custom (mostly) 2D game engine which is built on top of the Processing framework.
// It provides better way to handle user input and an object management system (which I will not be using in this project)
public class Main extends GameContainer {
    public static void main(String[] args) {
        Game.createGame(1366, 768, new Main(), 60f, PConstants.P2D);
    }

    @Override
    public void update(double v) {

    }

    @Override
    public void render() {

    }

    @Override
    public void setup() {

    }

    @Override
    public void init() {

    }

    @Override
    public void updateTick() {
//        Because this game is turn based, I don't need to update the game at a constant rate
    }

    @Override
    public void settings() {
//        Using all of the default settings
    }
}
