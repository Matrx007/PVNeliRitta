package com.pv.neliritta.gui.components;
// Written by Rainis Randmaa

import com.pv.neliritta.FontManager;
import com.pv.neliritta.GraphicsManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.Utilities;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.localization.Localization;
import processing.core.PConstants;
import processing.core.PFont;

/*
* Used to get user preference. User can cycle through options with left and right arrow.
* */
public class Options implements Component {
    final Main main;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint, yConstraint, widthConstraint, heightConstraint;

    private float realTopLeftX, realTopLeftY, realBottomRightX, realBottomRightY;
    private float leftButtonX1, leftButtonY1, leftButtonX2, leftButtonY2;
    private float rightButtonX1, rightButtonY1, rightButtonX2, rightButtonY2;

    /* Appearance */
    public float textSize;
    public Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);
    public Color textColor = new Color(0x53, 0x4a, 0x75);
    public PFont font = FontManager.loadedFonts.get("button-font");

    /* State */
    public boolean isMouseOver;
    public boolean isMouseOverLeftButton, isMouseOverRightButton;

    /* Behaviour */
    public Action onChange;

    /* Properties */
    private final Localization.Term[] options;
    private int selectedID = 0;

    public Localization.Term getSelected() { return options[selectedID]; }

    public Options(Main main,
                  Constraint xConstraint, Constraint yConstraint,
                  Constraint widthConstraint, Constraint heightConstraint,
                  Localization.Term[] options, Action onChange) {
        this.main = main;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;
        this.options = options;
        this.onChange = onChange;
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
        isMouseOverLeftButton = false;
        isMouseOverRightButton = false;


        isMouseOver = Utilities.isMouseInsidePerspectiveRectangle(
                main,
                realTopLeftX, realTopLeftY,
                realBottomRightX, realBottomRightY
        );



        isMouseOverLeftButton = Utilities.isMouseInsidePerspectiveRectangle(
                main,
                leftButtonX1, leftButtonY1,
                leftButtonX2, leftButtonY2
        );

        isMouseOverRightButton = Utilities.isMouseInsidePerspectiveRectangle(
                main,
                rightButtonX1, rightButtonY1,
                rightButtonX2, rightButtonY2
        );

        if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
            if(isMouseOverLeftButton) {
                selectedID = Math.max(0, selectedID - 1);
            } else if(isMouseOverRightButton) {
                selectedID = Math.min(options.length-1, selectedID + 1);
            }
        }
    }

    @Override
    public void render() {

        main.getGame().imageMode(PConstants.CENTER);

        if(isMouseOver) main.getGame().image(GraphicsManager.loadedGraphics.get("button-hover"), x, y, width*1.11f, height*1.43f);
        else            main.getGame().image(GraphicsManager.loadedGraphics.get("button"), x, y, width*1.11f, height*1.43f);

        if(isMouseOverLeftButton && !main.getGame().input.isButton(PConstants.LEFT))
            main.getGame().image(GraphicsManager.loadedGraphics.get("options-left-hover"),
                    x - width/2f - height/2f,
                    y, height*1.43f, height*1.43f);
        else main.getGame().image(GraphicsManager.loadedGraphics.get("options-left"),
                x - width/2f - height/2f,
                y, height*1.43f, height*1.43f);

        if(isMouseOverRightButton && !main.getGame().input.isButton(PConstants.LEFT))
            main.getGame().image(GraphicsManager.loadedGraphics.get("options-right-hover"),
                    x + width/2f + height/2f,
                    y, height*1.43f, height*1.43f);
        else main.getGame().image(GraphicsManager.loadedGraphics.get("options-right"),
                x + width/2f + height/2f,
                y, height*1.43f, height*1.43f);


        main.getGame().textFont(font, textSize);
        if(isMouseOver) {
            main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        } else {
            main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        }
        main.getGame().noStroke();

        main.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        main.getGame().text(options[selectedID].value, x, y);


        /* CALCULATE REAL COORDINATES */
        realTopLeftX = main.getGame().screenX(x - width / 2f, y - height / 2f);
        realTopLeftY = main.getGame().screenY(x - width / 2f, y - height / 2f);
        realBottomRightX = main.getGame().screenX(x + width / 2f, y + height / 2f);
        realBottomRightY = main.getGame().screenY(x + width / 2f, y + height / 2f);

        leftButtonX1 = main.getGame().screenX(x - width / 2f - height, y - height / 2f);
        leftButtonY1 = main.getGame().screenY(x - width / 2f - height, y - height / 2f);
        leftButtonX2 = main.getGame().screenX(x - width / 2f, y + height / 2f);
        leftButtonY2 = main.getGame().screenY(x - width / 2f, y + height / 2f);

        rightButtonX1 = main.getGame().screenX(x + width / 2f, y - height / 2f);
        rightButtonY1 = main.getGame().screenY(x + width / 2f, y - height / 2f);
        rightButtonX2 = main.getGame().screenX(x + width / 2f + height, y + height / 2f);
        rightButtonY2 = main.getGame().screenY(x + width / 2f + height, y + height / 2f);
    }
}
