package com.pv.neliritta.gui.components;

import com.pv.neliritta.FontManager;
import com.pv.neliritta.constraints.Constraint;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.GameComponent;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;
import processing.core.PFont;

public class Label implements GameComponent {
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

    public Label(GameContainer gameContainer,
                 Constraint xConstraint, Constraint yConstraint,
                 Constraint widthConstraint, Constraint heightConstraint,
                 String text) {
        this.gameContainer = gameContainer;
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

        /* DECORATIONS */

        gameContainer.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().rect(x, y, width, height);

        /* TEXT */

        gameContainer.getGame().textFont(font, textSize);
        gameContainer.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().textAlign(PConstants.CENTER, PConstants.CENTER);

        gameContainer.getGame().text(text, x + width / 2f, y + height / 2f);
    }
}
