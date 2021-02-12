package com.pv.neliritta.constraints;

public class SubConstraint implements Constraint {
    private final Constraint a, b;

    public SubConstraint(Constraint a, Constraint b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float calculate() {
        return a.calculate() - b.calculate();
    }
}
