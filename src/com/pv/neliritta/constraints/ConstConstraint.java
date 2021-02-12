package com.pv.neliritta.constraints;

public class ConstConstraint implements Constraint {
    private final float constant;

    public ConstConstraint(float constant) {
        this.constant = constant;
    }

    @Override
    public float calculate() {
        return constant;
    }
}
