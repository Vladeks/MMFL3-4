package com.example.lenovo.mmfl3.sourse;

public class AddConfidanceInterval implements ConfidenceIntervalOperations {

    @Override
    public ConfidenceInterval calculate(ConfidenceInterval leftOperand, ConfidenceInterval rightOperand) {
        return new ConfidenceInterval(
                leftOperand.getLower_boundary() + rightOperand.getLower_boundary(),
                leftOperand.getPlural_element() + rightOperand.getPlural_element(),
                leftOperand.getUpper_boundary() + rightOperand.getUpper_boundary()
        );
    }
}
