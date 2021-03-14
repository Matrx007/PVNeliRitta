package com.pv.neliritta.gui.components;
// Written by Rainis Randmaa

import com.pv.neliritta.FontManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.localization.Localization;
import processing.core.PConstants;
import processing.core.PFont;

/*
* Displays text. Nothing else, no callbacks, no animations, just text.
* */
public class Label implements Component {
    Main main;

    /* Boundaries of the button */
    public int x, y, width, height;
    public Constraint xConstraint, yConstraint, widthConstraint, heightConstraint;

    /* Appearance */
    public Localization.Term text;
    public float textSize;
    public Color backgroundColor = new Color(255, 255, 255, 64);
    public Color textColor = new Color(10, 10, 10, 160);
    public PFont font = FontManager.loadedFonts.get("button-font");

    public Label(Main main,
                 Constraint xConstraint, Constraint yConstraint,
                 Constraint widthConstraint, Constraint heightConstraint,
                 Localization.Term text) {
        this.main = main;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.heightConstraint = heightConstraint;
        this.text = text;
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

    }

    @Override
    public void render() {

        /* TEXT */

        main.getGame().textFont(font, textSize);
        main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        main.getGame().noStroke();

        main.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        main.getGame().text(text.value, x, y);
    }
}
