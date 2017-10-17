package com.example.lenovo.mmfl3.sourse;

import java.io.Serializable;
import java.util.Random;

public class ConfidenceInterval implements Serializable{

    private double lower_boundary;
    private double upper_boundary;
    private double  plural_element;

    public ConfidenceInterval(double lower_boundary, double plural_element, double upper_boundary) {
        this.lower_boundary = lower_boundary;
        this.upper_boundary = upper_boundary;
        this.plural_element = plural_element;
    }

    /**
     *
     * @param alpha value
     * @return a1(alpha)
     */
    public double calculateLeftBound(double alpha) {
        return alpha * (plural_element - lower_boundary) + lower_boundary;
    }

    /**
     *
     * @param alpha value
     * @return a2(alpha)
     */
    public double calculateRightBound(double alpha) {
        return upper_boundary - alpha * (upper_boundary - plural_element);
    }

    public String getAlphaString()  {
        double a1 = plural_element - lower_boundary;
        double a2 = upper_boundary - plural_element;
        return String.format("[%.2f+a*%.2f; %.2f-a*%.2f]", lower_boundary, a1, upper_boundary, a2);
    }

    /**
     * Calculate M(a)
     * @param x value
     * @return degree in range [0;1]
     */
    public double calculateMembershioFunction(double x) {
        if(x <= lower_boundary && x>= upper_boundary) {
            return 0;
        } else if(x >= lower_boundary && x <= plural_element) {
            return (x - lower_boundary) / (plural_element - lower_boundary);
        } else if(x >= plural_element && x <= upper_boundary) {
            return (upper_boundary - x) / (upper_boundary - plural_element);
        }
        return 0;
    }

    public double getLower_boundary() {
        return lower_boundary;
    }

    public double getUpper_boundary() {
        return upper_boundary;
    }

    public double getPlural_element() {
        return plural_element;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f]", lower_boundary, plural_element, upper_boundary);
    }

    /**
     *
     * @param numder
     * @return new ConfidenceInterval
     */
    public static ConfidenceInterval clearNumberToConfidenceInterval(double numder) {
        return new ConfidenceInterval(numder, numder, numder);
    }

    public static ConfidenceInterval getRandomConfidanceInterval(double number) {
        int fuelLevel = (int)number;
        int lowerBound = random(50, 51 + fuelLevel / 10);
        int upperBound = random(lowerBound + fuelLevel / 10, fuelLevel / 3 + lowerBound);
        int midBound = random(lowerBound, upperBound);

//        if (lowerBound <= midBound && midBound <= upperBound){
//            return new ConfidenceInterval(lowerBound, midBound, upperBound);
//        }
//        return getRandomConfidanceInterval(number);
        return new ConfidenceInterval(lowerBound, midBound, upperBound);
    }

    /**
     *
     * @param min
     * @param max
     * @return random number in range [min, max]
     */
    private static int random(int min, int max) {
        return new Random(/*System.currentTimeMillis()*/).nextInt(max + 1 - min) + min;
    }



}
