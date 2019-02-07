package com.example.pocketsoccer.views.game.figures;

import android.graphics.PointF;

import com.example.pocketsoccer.views.game.MoveDetector;

public class Player {
    public static final float minD = 1.2f; // Adjustable

    public static final float R = 75;

    private PointF center;

    private float dx, dy, kFriction;

    public Player(PointF center) {
        this.center = center;
        this.dx = 0;
        this.dy = 0;
        this.kFriction = 1.0f;
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

    public float getDx() {
        return dx;
    }

    public void setDx(float dx, float width, float speed) {
        float kPull = (float) Math.log10((Math.abs(dx) * 100 / width) + 1);
        float kSpeed = speed;
        float deltaX = 0.2f * width; // Adjustable
        this.dx = Math.signum(dx) * kPull * kSpeed * deltaX / MoveDetector.REFRESH_RATE;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy, float height, float speed) {
        float kPull = (float) Math.log10((Math.abs(dy) * 100 / height) + 1);
        float kSpeed = speed;
        float deltaY = 0.2f * height; // Adjustable
        this.dy = Math.signum(dy) * kPull * kSpeed * deltaY / MoveDetector.REFRESH_RATE;
    }

    public void initMove(float dx, float dy, float width, float height, float speed) {
        setDx(dx, width, speed);
        setDy(dy, height, speed);
        kFriction = 1.0f;
    }

    public boolean move() {
        if (Math.abs(dx) / kFriction > minD || Math.abs(dy) / kFriction > minD) {
            center.set(center.x + dx / kFriction, center.y + dy / kFriction);
            kFriction += 7.0f / MoveDetector.REFRESH_RATE; // Adjustable
            return true;
        } else {
            dx = 0;
            dy = 0;
            kFriction = 1.0f;
            return false;
        }
    }
}
