package com.pv.neliritta.gui.components;

import com.pv.neliritta.FontManager;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.GameComponent;
import com.ydgames.mxe.GameContainer;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import processing.core.PConstants;
import processing.core.PFont;

public class Button implements GameComponent {
    GameContainer gameContainer;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint, yConstraint, widthConstraint, heightConstraint;

    /* Appearance */
    public String text;
    public float textSize;
    public Color backgroundColor = new Color(10, 10, 10);
    public Color textColor = new Color(250, 250, 250);
    public PFont font = FontManager.loadedFonts.get("button-font");

    /* Animations */
    private float offsetX, offsetAnimationSpeed;
    private static final float OFFSET_ANIMATION_ACCELERATION = 1600f;

    /* State */
    public boolean isMouseOver;
    public boolean isMouseDown;

    /* Behaviour */
    public Action onClick;

    public Button(GameContainer gameContainer,
                  Constraint xConstraint, Constraint yConstraint,
                  Constraint widthConstraint, Constraint heightConstraint,
                  String text, Action onClick) {
        this.gameContainer = gameContainer;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;
        this.text = text;
        this.onClick = onClick;

        this.offsetX = 0;
    }

    @Override
    public void resize() {
        x = (int)xConstraint.calculate();
        y = (int)yConstraint.calculate();
        width = (int)widthConstraint.calculate();
        height = (int)heightConstraint.calculate();

        this.textSize = height * 0.4f;
    }

    @Override
    public void update(double deltaTime) {
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

        if(isMouseOver) {
            if(offsetX >= 16+8) {
                offsetX = 16+8;
                offsetAnimationSpeed = 0;
            } else {
                offsetAnimationSpeed += OFFSET_ANIMATION_ACCELERATION * deltaTime;
                offsetX += offsetAnimationSpeed * deltaTime;
            }
        } else {
            if(offsetX <= 0) {
                offsetX = 0;
                offsetAnimationSpeed = 0;
            } else {
                offsetAnimationSpeed += OFFSET_ANIMATION_ACCELERATION * deltaTime;
                offsetX -= offsetAnimationSpeed * deltaTime;
            }
        }
    }

    @Override
    public void render() {

        /* DECORATIONS */

        gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().rect(x, y, 16, height);

        /* BACKGROUND */

        if(isMouseDown) {
            gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        } else {
            gameContainer.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        }

        gameContainer.getGame().noStroke();

        gameContainer.getGame().rect(x + offsetX, y, width, height);

        /* TEXT */


        gameContainer.getGame().textFont(font, textSize);
        if(isMouseDown) {
            gameContainer.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        } else {
            gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        }
        gameContainer.getGame().noStroke();

        gameContainer.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        gameContainer.getGame().text(text, x + width / 2f + offsetX, y + height / 2f);
    }
}
