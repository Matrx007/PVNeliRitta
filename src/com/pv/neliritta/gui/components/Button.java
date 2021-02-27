package com.pv.neliritta.gui.components;

import com.pv.neliritta.Utilities;
import com.pv.neliritta.FontManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.GameComponent;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import processing.core.PConstants;
import processing.core.PFont;

public class Button implements GameComponent {
    Main main;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint, yConstraint, widthConstraint, heightConstraint;

    private float realTopLeftX, realTopLeftY, realBottomRightX, realBottomRightY;

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

    public Button(Main main,
                  Constraint xConstraint, Constraint yConstraint,
                  Constraint widthConstraint, Constraint heightConstraint,
                  String text, Action onClick) {
        this.main = main;
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

        isMouseOver =
                Utilities.isPointInsideTriangle(
                        realTopLeftX, realTopLeftY,
                        realTopLeftX, realBottomRightY,
                        realBottomRightX, realTopLeftY,
                        main.getGame().mouseX,
                        main.getGame().mouseY)
                ||
                Utilities.isPointInsideTriangle(
                        realBottomRightX, realTopLeftY,
                        realTopLeftX, realBottomRightY,
                        realBottomRightX, realBottomRightY,
                        main.getGame().mouseX,
                        main.getGame().mouseY);

        if(isMouseOver) {
            /* If mouse is being pressed while it's inside the button */
            if(main.getGame().input.isButton(PConstants.LEFT)) {
                isMouseDown = true;
            }

            /* If mouse was released while it was inside the button, trigger the 'onClick' function */
            if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
                onClick.run();
            }
        }

        /* If mouse inside the button at the moment
        if(main.getGame().mouseX > (x - width / 2f) &&
                main.getGame().mouseY > (y - height / 2f) &&
                main.getGame().mouseX < (x + width / 2f) &&
                main.getGame().mouseY < (y + height / 2f)) {
            isMouseOver = true;

             If mouse is being pressed while it's inside the button
            if(main.getGame().input.isButton(PConstants.LEFT)) {
                isMouseDown = true;
            }

             If mouse was released while it was inside the button, trigger the 'onClick' function
            if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
                onClick.run();
            }
        }*/

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

        main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        main.getGame().noStroke();

        main.getGame().rect(x - width/2f + 8, y, 16, height);

        /* BACKGROUND */

        if(isMouseDown) {
            main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        } else {
            main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        }

        main.getGame().noStroke();

        main.getGame().rect(x + offsetX, y, width, height);

        /* TEXT */


        main.getGame().textFont(font, textSize);
        if(isMouseDown) {
            main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        } else {
            main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        }
        main.getGame().noStroke();

        main.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        main.getGame().text(text, x + offsetX, y);

        /* CALCULATE REAL COORDINATES */
        realTopLeftX = main.getGame().screenX(x - width / 2f, y - height / 2f);
        realTopLeftY = main.getGame().screenY(x - width / 2f, y - height / 2f);
        realBottomRightX = main.getGame().screenX(x + width / 2f, y + height / 2f);
        realBottomRightY = main.getGame().screenY(x + width / 2f, y + height / 2f);
    }
}
