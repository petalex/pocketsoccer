package com.example.pocketsoccer.views.game.figures;

public interface Figure {
    /**
     * Vector scaling constant
     */
    float S = 1.0f;

    /**
     * Friction constant
     */
    float F = 10.0f;

    float getX();

    float getY();

    float getR();

    float getM();

    float getDx();

    void setDx(float dx);

    float getDy();

    void setDy(float dy);

    void initMove(float dx, float dy, float speed);

    boolean move();
}
