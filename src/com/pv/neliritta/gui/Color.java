package com.pv.neliritta.gui;
// Written by Rainis Randmaa

public class Color {
    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    @Override
    public String toString() {
        return "Color{" +
                "" + r +
                ", " + g +
                ", " + b +
                ", " + a +
                '}';
    }
}
