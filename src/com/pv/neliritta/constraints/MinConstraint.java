package com.pv.neliritta.constraints;

public class MinConstraint implements Constraint {
    private final Constraint a, b;

    public MinConstraint(Constraint a, Constraint b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float calculate() {
        return Math.min(a.calculate(), b.calculate());
    }
}
