package com.pv.neliritta.gui.components;

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

import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input implements Component {
    Main main;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint;
    public Constraint yConstraint;
    public Constraint widthConstraint;
    public Constraint heightConstraint;

    private float realTopLeftX, realTopLeftY, realBottomRightX, realBottomRightY;

    /* Appearance */
    public Localization.Term placeholder;
    public float textSize;
    public PFont font = FontManager.loadedFonts.get("button-font");

    public Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);
    public Color textColor = new Color(0x53, 0x4a, 0x75);

    /* State */
    public boolean isMouseOver;
    public boolean isMouseDown;
    public boolean isActive;

    /* Behaviour */
    public Action onEnter;
    private String currentText = "";
    private String newText = "";

    public String getInput() { return currentText + newText; }

    /* Properties */
    public static final Pattern CHARACTER_PATTERN_NUMBERS = Pattern.compile("[0-9]");
    public static final Pattern CHARACTER_PATTERN_FILENAME = Pattern.compile("[a-zA-Z0-9_\\-]");

    public Pattern resultPattern;
    public Pattern characterPattern;
    public int maximumLength = 64;

    public Input(Main main,
                 Constraint xConstraint, Constraint yConstraint,
                 Constraint widthConstraint, Constraint heightConstraint,
                 Localization.Term placeholder,
                 Pattern resultPattern, Pattern characterPattern,
                 Action onEnter) {

        this.main = main;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;
        this.placeholder = placeholder;
        this.onEnter = onEnter;
        this.resultPattern = resultPattern;
        this.characterPattern = characterPattern;
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
            if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
                isActive = true;
                main.getGame().readCharacters = "";
            }
        } else {
            /* If mouse is being pressed outside of this field, deactivate it */
            if(main.getGame().input.isButton(PConstants.LEFT) ||
                    main.getGame().input.isButtonUp(PConstants.LEFT) ||
                    main.getGame().input.isButtonDown(PConstants.LEFT)) {
                isActive = false;
            }
        }

        if(isActive && main.getGame().input.isKeyUp(PConstants.ENTER)) {
            onEnter.run();
            isActive = false;
            currentText += newText;
            newText = "";
        }

        if(isActive) {
            Matcher characterMatcher = characterPattern.matcher(main.getGame().readCharacters);
            StringBuilder stringBuilder = new StringBuilder(newText);
            while(characterMatcher.find()) {
                stringBuilder.append(characterMatcher.group());
            }
            newText = stringBuilder.toString();


            if(main.getGame().input.isKeyDown(KeyEvent.VK_BACK_SPACE)) {
                if(newText.length() > 0) {
                    newText = newText.substring(0, newText.length()-1);
                } else if(currentText.length() > 0) {
                    currentText = currentText.substring(0, currentText.length()-1);
                }
            }
        }
    }

    @Override
    public void render() {
        final float TEXT_PADDING = 24f;

        main.getGame().textFont(font, textSize);

        float textWidth = main.getGame().textWidth(currentText+newText)+TEXT_PADDING*2;
        float actualWidth = Math.max(width, textWidth);


        main.getGame().imageMode(PConstants.CENTER);

        if(isActive) main.getGame().image(GraphicsManager.loadedGraphics.get("button-active"), x, y, actualWidth*1.11f, height*1.43f);
        else if(isMouseOver) main.getGame().image(GraphicsManager.loadedGraphics.get("button-hover"), x, y, actualWidth*1.11f, height*1.43f);
        else            main.getGame().image(GraphicsManager.loadedGraphics.get("button"), x, y, actualWidth*1.11f, height*1.43f);




        main.getGame().noStroke();
        main.getGame().textAlign(PConstants.LEFT, PConstants.CENTER);

        if(isMouseOver && !isActive) {
            main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        } else {
            main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        }

        if((currentText.length()+newText.length()) > 0 || isActive) {
            main.getGame().text(currentText+newText, x-actualWidth/2f+TEXT_PADDING, y);
        } else {
            main.getGame().tint(255, 128);
            main.getGame().text(placeholder.value, x-actualWidth/2f+TEXT_PADDING, y);
        }
        main.getGame().noTint();

        /* CALCULATE REAL COORDINATES */
        realTopLeftX = main.getGame().screenX(x - actualWidth / 2f, y - height / 2f);
        realTopLeftY = main.getGame().screenY(x - actualWidth / 2f, y - height / 2f);
        realBottomRightX = main.getGame().screenX(x + actualWidth / 2f, y + height / 2f);
        realBottomRightY = main.getGame().screenY(x + actualWidth / 2f, y + height / 2f);
    }
}
