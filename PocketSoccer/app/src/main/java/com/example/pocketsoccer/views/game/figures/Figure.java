package com.example.pocketsoccer.views.game.figures;

public interface Figure {
    /**
     * Speed constant
     */
    float S = 5.0f;

    /**
     * Vector scaling constant
     */
    float V = 1.0f;

    /**
     * Friction constant
     */
    float F = 20.0f;

    /**
     * Energy loss on collision with a wall
     */
   float L = 0.75f;

    float getX();

    void setX(float x);

    float getY();

    void setY(float y);

    float getR();

    float getM();

    float getDx();

    void setDx(float dx);

    float getDy();

    void setDy(float dy);

    void initMove(float dx, float dy);

    boolean move();
}