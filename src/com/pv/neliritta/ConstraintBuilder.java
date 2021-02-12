package com.pv.neliritta;

import com.pv.neliritta.constraints.*;

public class ConstraintBuilder {
    private Constraint constraint;

    public ConstraintBuilder(Constraint constraint) {
        this.constraint = constraint;
    }

    public void add(Constraint a) { this.constraint = new AddConstraint(a, constraint); }
    public void add(float a) { this.constraint = new AddConstraint(() -> a, constraint); }

    public void sub(Constraint a) { this.constraint = new SubConstraint(a, constraint); }
    public void sub(float a) { this.constraint = new SubConstraint(() -> a, constraint); }

    public void mul(Constraint a) { this.constraint = new MulConstraint(a, constraint); }
    public void mul(float a) { this.constraint = new MulConstraint(() -> a, constraint); }

    public void div(Constraint a) { this.constraint = new DivConstraint(a, constraint); }
    public void div(float a) { this.constraint = new DivConstraint(() -> a, constraint); }

    public void min(Constraint a) { this.constraint = new MinConstraint(a, constraint); }
    public void min(float a) { this.constraint = new MinConstraint(() -> a, constraint); }

    public void max(Constraint a) { this.constraint = new MaxConstraint(a, constraint); }
    public void max(float a) { this.constraint = new MaxConstraint(() -> a, constraint); }

    public void clamp(Constraint min, Constraint max) { this.constraint = new ClampConstraint(constraint, min, max); }
    public void clamp(Constraint min, float max) { this.constraint = new ClampConstraint(constraint, min, () -> max); }
    public void clamp(float min, float max) { this.constraint = new ClampConstraint(constraint, () -> min, () -> max); }
    public void clamp(float min, Constraint max) { this.constraint = new ClampConstraint(constraint, () -> min, max); }

    public Constraint getConstraint() {
        return constraint;
    }
}
