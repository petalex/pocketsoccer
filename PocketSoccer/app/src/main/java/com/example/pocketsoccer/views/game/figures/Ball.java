package com.example.pocketsoccer.views.game.figures;

import android.graphics.PointF;

public class Ball {
    public static final float R = 35;

    private PointF center;

    public Ball(PointF center) {
        this.center = center;
    }

    public float getX() {
        return center.x;
    }

    public void setX(float x) {
        center.x = x;
    }

    public float getY() {
        return center.y;
    }

    public void setY(float y) {
        center.y = y;
    }

    public void move(float dx, float dy) {
        center.set(center.x + dx, center.y + dy);
    }
}
