package com.example.lenovo.mmfl3.sourse;

import java.io.Serializable;

public class ConfidanceIntervalSaver implements Serializable {

    private ConfidenceInterval intervalA;
    private ConfidenceInterval intervalB;

    public ConfidanceIntervalSaver(ConfidenceInterval intervalA, ConfidenceInterval intervalB) {
        this.intervalA = intervalA;
        this.intervalB = intervalB;
    }

    public ConfidenceInterval getIntervalA() {
        return intervalA;
    }

    public ConfidenceInterval getIntervalB() {
        return intervalB;
    }

    @Override
    public String toString() {
        return "ConfidanceIntervalSaver{" +
                "intervalA=" + intervalA.toString() +
                ", intervalB=" + intervalB.toString() +
                '}';
    }
}
