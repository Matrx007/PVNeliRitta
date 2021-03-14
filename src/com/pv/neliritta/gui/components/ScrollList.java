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
* Scrolalble list, used as save file laoder
*  */
public class ScrollList implements Component {
    Main main;

    /* Boundaries of the button */
    public int x, y, width, height, entryHeight;
    public Constraint xConstraint, yConstraint, widthConstraint, entryHeightConstraint;

    private float realTopLeftX, realTopLeftY, realBottomRightX, realBottomRightY;
    private float[][] realEntryCoordinates;

    private int entriesAtOnce;
    private static final int SCROLL_SUB_STEPS = 10;
    private int scroll = 0, scrollSubStep = 0;
    private float scrollMomentum = 0f;


    /* Appearance */
    public Localization.Term[] entries;
    public float textSize;
    public Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);
    public Color textColor = new Color(0x53, 0x4a, 0x75);
    public PFont font = FontManager.loadedFonts.get("button-font");

    /* State */
    public boolean isMouseOver;
    private boolean[] isMouseOverEntry;

    private int lastClicked = -1;
    public int mouseOverEntryIndex = -1;
    private float lastClickedTimer = 0f;

    /* Behaviour */
    public Action onDoubleClick;

    public Localization.Term getCurrentEntry() {
        if(mouseOverEntryIndex == -1) return null;
        return entries[mouseOverEntryIndex+scroll];
    }

    public ScrollList(Main main,
                  Constraint xConstraint, Constraint yConstraint,
                  Constraint widthConstraint, Constraint entryHeightConstraint,
                  int entriesAtOnce, Localization.Term[] entries,
                  Action onDoubleClick) {

        this.main = main;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.widthConstraint = widthConstraint;
        this.entryHeightConstraint = entryHeightConstraint;
        this.entriesAtOnce = entriesAtOnce;
        this.entries = entries;
        this.onDoubleClick = onDoubleClick;

        realEntryCoordinates = new float[entriesAtOnce][4];
        isMouseOverEntry = new boolean[entriesAtOnce];
    }

    @Override
    public void resize() {
        x = (int)xConstraint.calculate();
        y = (int)yConstraint.calculate();
        width = (int)widthConstraint.calculate();
        entryHeight = (int)entryHeightConstraint.calculate();
        height = entryHeight * entriesAtOnce;

        this.textSize = entryHeight * 0.5f;
    }

    @Override
    public void update(double deltaTime) {
        mouseOverEntryIndex = -1;

        // Highlight entries

        for(int i = 0; i < entriesAtOnce; i++) {
            isMouseOverEntry[i] =
                    Utilities.isPointInsideTriangle(
                            realEntryCoordinates[i][0], realEntryCoordinates[i][1],
                            realEntryCoordinates[i][0], realEntryCoordinates[i][3],
                            realEntryCoordinates[i][2], realEntryCoordinates[i][1],
                            main.getGame().mouseX,
                            main.getGame().mouseY)
                            ||
                            Utilities.isPointInsideTriangle(
                                    realEntryCoordinates[i][2], realEntryCoordinates[i][1],
                                    realEntryCoordinates[i][0], realEntryCoordinates[i][3],
                                    realEntryCoordinates[i][2], realEntryCoordinates[i][3],
                                    main.getGame().mouseX,
                                    main.getGame().mouseY);
            if(isMouseOverEntry[i]) mouseOverEntryIndex = i;
        }

        // Double-click

        if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
            if(lastClicked != -1 && lastClicked == mouseOverEntryIndex) {
                if(lastClickedTimer < 1f) {
                    onDoubleClick.run();
                }
            }
            lastClicked = mouseOverEntryIndex;
            lastClickedTimer = 0;
        }
        lastClickedTimer += deltaTime;

        // Smooth scroll

        scrollMomentum += main.getGame().input.scroll;
        scrollSubStep += scrollMomentum;

        if(scroll == 0 && scrollSubStep < 0) {
            scrollSubStep = 0;
            scrollMomentum = 0;
        }
        if(scroll == entries.length-entriesAtOnce+1 && scrollSubStep > 0) {
            scrollSubStep = 0;
            scrollMomentum = 0;
        }

        if(scrollSubStep < 0) {
            if(scroll > 0) {
                scroll--;
                scrollSubStep = SCROLL_SUB_STEPS;
            }
        } else if(scrollSubStep > SCROLL_SUB_STEPS) {
            scrollSubStep = 0;
            if(scroll < entries.length-entriesAtOnce+1) {
                scroll++;
            }
        }

        scroll = Math.max(0, Math.min(entries.length-entriesAtOnce+1, scroll));

        scrollMomentum -= Math.max(-0.1f, Math.min(0.1f, scrollMomentum));
    }

    @Override
    public void render() {
        main.getGame().imageMode(PConstants.CENTER);


        main.getGame().noStroke();
        for(int i = 0; i < entriesAtOnce; i++) {
            int n = i+scroll;
            if(n >= entries.length) break;

            float alpha = 1f;
            if(i == 0) alpha = (SCROLL_SUB_STEPS-scrollSubStep)/(float)SCROLL_SUB_STEPS;
            else if(i == entriesAtOnce-1) alpha = scrollSubStep/(float)SCROLL_SUB_STEPS;
            main.getGame().tint(255, alpha*255f);

            float offset = (SCROLL_SUB_STEPS-scrollSubStep)/(float)SCROLL_SUB_STEPS*entryHeight;

            main.getGame().textFont(font, textSize);
            if(isMouseOverEntry[i] && !main.getGame().input.isButton(PConstants.LEFT)) {
                main.getGame().image(GraphicsManager.loadedGraphics.get("button-hover"), x, y + entryHeight*i + offset, width*1.11f, entryHeight*1.43f);
                main.getGame().fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a*alpha);
            } else {
                main.getGame().image(GraphicsManager.loadedGraphics.get("button"), x, y + entryHeight*i + offset, width*1.11f, entryHeight*1.43f);
                main.getGame().fill(textColor.r, textColor.g, textColor.b, textColor.a*alpha);
            }

            main.getGame().textAlign(PConstants.LEFT, PConstants.CENTER);
            main.getGame().text(entries[i+scroll].value, x-width/2f+16f, y+entryHeight*i + offset);

            // Calculate real coordinates
            realEntryCoordinates[i] = new float[]{
                    realTopLeftX = main.getGame().screenX(x - width / 2f, y + entryHeight*i - entryHeight/2f + offset),
                    realTopLeftY = main.getGame().screenY(x - width / 2f, y + entryHeight*i - entryHeight/2f + offset),
                    realBottomRightX = main.getGame().screenX(x + width / 2f, y + entryHeight*i + entryHeight/2f + offset),
                    realBottomRightY = main.getGame().screenY(x + width / 2f, y + entryHeight*i + entryHeight/2f + offset)
            };
        }
    }
}
