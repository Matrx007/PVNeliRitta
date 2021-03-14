package com.pv.neliritta.gui.components;
// Written by Rainis Randmaa

import com.pv.neliritta.GraphicsManager;
import com.pv.neliritta.Utilities;
import com.pv.neliritta.FontManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.localization.Localization;
import processing.core.PConstants;
import processing.core.PFont;

/*
* Used to capture user interactions. Callback is used to do something useful when the button gets pressed.
* */
public class Button implements Component {
    Main main;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint, yConstraint, widthConstraint, heightConstraint;

    private float realTopLeftX, realTopLeftY, realBottomRightX, realBottomRightY;

    /* Appearance */
    public Localization.Term text;
    public float textSize;
    public Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);
    public Color textColor = new Color(0x53, 0x4a, 0x75);
    public PFont font = FontManager.loadedFonts.get("button-font");

    /* State */
    public boolean isMouseOver;
    public boolean isMouseDown;

    /* Behaviour */
    public Action onClick;

    public Button(Main main,
                  Constraint xConstraint, Constraint yConstraint,
                  Constraint widthConstraint, Constraint heightConstraint,
                  Localization.Term text, Action onClick) {

        this.main = main;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void resize() {
        x = (int)xConstraint.calculate();
        y = (int)yConstraint.calculate();
        width = (int)widthConstraint.calculate();
        height = (int)heightConstraint.calculate();

        this.textSize = height * 0.5f;
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
    }

    @Override
    public void render() {

        main.getGame().imageMode(PConstants.CENTER);

        if(isMouseOver) main.getGame().image(GraphicsManager.loadedGraphics.get("button-hover"), x, y, width*1.11f, height*1.43f);
        else            main.getGame().image(GraphicsManager.loadedGraphics.get("button"), x, y, width*1.11f, height*1.43f);


        main.getGame().textFont(font, textSize);
        if(isMouseOver) {
            main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        } else {
            main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        }
        main.getGame().noStroke();

        main.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        main.getGame().text(text.value, x, y);

        /* CALCULATE REAL COORDINATES */
        realTopLeftX = main.getGame().screenX(x - width / 2f, y - height / 2f);
        realTopLeftY = main.getGame().screenY(x - width / 2f, y - height / 2f);
        realBottomRightX = main.getGame().screenX(x + width / 2f, y + height / 2f);
        realBottomRightY = main.getGame().screenY(x + width / 2f, y + height / 2f);
    }
}
