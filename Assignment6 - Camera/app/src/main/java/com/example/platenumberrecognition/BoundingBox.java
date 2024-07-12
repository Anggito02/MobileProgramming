package com.example.platenumberrecognition;

public class BoundingBox {
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float confidence;
    private String label;


    public BoundingBox(float x1, float y1, float x2, float y2, float confidence) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.confidence = confidence;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
