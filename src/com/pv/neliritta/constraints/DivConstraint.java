package com.pv.neliritta.constraints;

public class DivConstraint implements Constraint {
    private final Constraint a, b;

    public DivConstraint(Constraint a, Constraint b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float calculate() {
        return a.calculate() / b.calculate();
    }
}
