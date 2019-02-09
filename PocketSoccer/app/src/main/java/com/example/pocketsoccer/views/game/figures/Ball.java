package com.example.pocketsoccer.views.game.figures;

import android.graphics.PointF;

import com.example.pocketsoccer.utils.GameInfoManager;
import com.example.pocketsoccer.views.game.MoveDetector;

public class Ball implements Figure {
    /**
     * Football radius
     */
    public static final float R = 35;

    private PointF center;

    private float r, m, dx, dy;

    public Ball(PointF center) {
        this.center = center;
        this.r = R;
        this.m = R * R;
        this.dx = 0;
        this.dy = 0;
    }

    @Override
    public float getX() {
        return center.x;
    }

    @Override
    public void setX(float x) {
        this.center.x = x;
    }

    @Override
    public float getY() {
        return center.y;
    }

    @Override
    public void setY(float y) {
        this.center.y = y;
    }

    @Override
    public float getR() {
        return r;
    }

    @Override
    public float getM() {
        return m;
    }

    @Override
    public float getDx() {
        return dx;
    }

    @Override
    public void setDx(float dx) {
        this.dx = dx;
    }

    @Override
    public float getDy() {
        return dy;
    }

    @Override
    public void setDy(float dy) {
        this.dy = dy;
    }

    @Override
    public void initMove(float dx, float dy) {
        // Nobody ever initialize move for football, it only gets its delta from collision
    }

    @Override
    public boolean move() {
        updateDelta();
        if (dx != 0 && dy != 0) {
            center.set(center.x + dx, center.y + dy);
            return true;
        } else {
            return false;
        }
    }

    private void updateDelta() {
        float absDx, absDy, friction, frictionX, frictionY;
        friction = F / MoveDetector.REFRESH_RATE;
        absDx = Math.abs(dx);
        absDy = Math.abs(dy);
        if (absDx > absDy) {
            frictionX = friction;
            frictionY = friction * absDy / absDx;

        } else {
            frictionX = friction * absDx / absDy;
            frictionY = friction;
        }
        dx = (absDx - frictionX > 0) ? (dx - Math.signum(dx) * frictionX) : 0;
        dy = (absDy - frictionY > 0) ? (dy - Math.signum(dy) * frictionY) : 0;
    }
}
