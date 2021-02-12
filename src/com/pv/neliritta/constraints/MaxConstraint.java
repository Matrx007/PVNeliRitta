package com.pv.neliritta.constraints;

public class MaxConstraint implements Constraint {
    private final Constraint a, b;

    public MaxConstraint(Constraint a, Constraint b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float calculate() {
        return Math.max(a.calculate(), b.calculate());
    }
}
