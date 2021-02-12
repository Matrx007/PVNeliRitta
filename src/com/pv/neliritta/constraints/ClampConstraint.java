package com.pv.neliritta.constraints;

public class ClampConstraint implements Constraint {
    private final Constraint a, min, max;

    public ClampConstraint(Constraint a, Constraint min, Constraint max) {
        this.a = a;
        this.min = min;
        this.max = max;
    }

    @Override
    public float calculate() {
        return Math.min(min.calculate(), Math.max(max.calculate(), a.calculate()));
    }
}
