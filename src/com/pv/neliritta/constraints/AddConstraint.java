package com.pv.neliritta.constraints;

public class AddConstraint implements Constraint {
    private final Constraint a, b;

    public AddConstraint(Constraint a, Constraint b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float calculate() {
        return a.calculate() + b.calculate();
    }
}
