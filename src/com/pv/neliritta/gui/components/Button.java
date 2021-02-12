package com.pv.neliritta.gui.components;

import com.pv.neliritta.FontManager;
import com.ydgames.mxe.GameContainer;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import processing.core.PConstants;
import processing.core.PFont;

public class Button {
    GameContainer gameContainer;

    /* Boundaries of the button */
    public int x, y, width, height;

    /* Appearance */
    public String text;
    public float textSize;
    public Color backgroundColor = new Color(10, 10, 10);
    public Color textColor = new Color(250, 250, 250);
    public PFont font = FontManager.loadedFonts.get("button-font");

    /* State */
    public boolean isMouseOver;
    public boolean isMouseDown;

    /* Behaviour */
    public Action onClick;

    public Button(GameContainer gameContainer, int x, int y, int width, int height, String text, Action onClick) {
        this.gameContainer = gameContainer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;

        this.textSize = height * 0.4f;
    }

    public void update() {
        isMouseOver = false;
        isMouseDown = false;

        /* If mouse inside the button at the moment */
        if(gameContainer.getGame().mouseX > x &&
                gameContainer.getGame().mouseY > y &&
                gameContainer.getGame().mouseX < x + width &&
                gameContainer.getGame().mouseY < y + height) {
            isMouseOver = true;

            /* If mouse is being pressed while it's inside the button */
            if(gameContainer.getGame().input.isButton(PConstants.LEFT)) {
                isMouseDown = true;
            }

            /* If mouse was released while it was inside the button, trigger the 'onClick' function */
            if(gameContainer.getGame().input.isButtonUp(PConstants.LEFT)) {
                onClick.run();
            }
        }
    }

    public void render() {

        /* BACKGROUND */

        gameContainer.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().rect(x + (isMouseOver ? (16+8) : 0), y, width, height);

        /* DECORATIONS */

        if(isMouseOver) {

            gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
            gameContainer.getGame().noStroke();

            gameContainer.getGame().rect(x, y, 16, height);

        }

        /* TEXT */

        gameContainer.getGame().textFont(font, textSize);
        gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        gameContainer.getGame().text(text, x + width / 2f + (isMouseOver ? (16+8) : 0), y + height / 2f);
    }
}
