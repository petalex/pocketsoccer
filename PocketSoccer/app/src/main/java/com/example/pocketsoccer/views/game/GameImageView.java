package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.List;

@SuppressLint("AppCompatCustomView")
public class GameImageView extends ImageView {
    private GameData data;

    private MoveDetector detector;

    private Drawable team1, team2, ball;

    private Paint paint;

    private boolean start = false, reset = false;

    public GameImageView(Context context) {
        super(context);
    }

    public GameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public GameImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        data = new GameData();
        detector = new MoveDetector(this);
        paint = new Paint();
    }

    public GameData getData() {
        return data;
    }

    public MoveDetector getDetector() {
        return detector;
    }

    public void setSpeed(int speed) {
        detector.setSpeed(speed);
    }

    public void setTeam1(Drawable team1) {

        this.team1 = team1;
    }

    public void setTeam2(Drawable team2) {
        this.team2 = team2;
    }

    public void setBall(Drawable ball) {
        this.ball = ball;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (start) {
            if (reset) {
                data.reset(canvas.getWidth(), canvas.getHeight());
                reset = false;
            }

            boolean noMovement = true;
            noMovement = !moveTeam(data.getTeam1()) && noMovement;
            noMovement = !moveTeam(data.getTeam2()) && noMovement;
            noMovement = !moveBall() && noMovement;
            if (noMovement) {
                detector.endMovement();
            }

            drawTeam(canvas, data.getTeam1(), team1);
            drawTeam(canvas, data.getTeam2(), team2);
            drawBall(canvas, ball);
        }
    }

    public void start() {
        start = true;
        reset();
    }

    public void reset() {
        reset = true;
    }

    private void drawTeam(Canvas canvas, List<Player> team, Drawable drawable) {
        for (int i = 0; i < GameData.N; ++i) {
            drawPlayer(canvas, team.get(i), drawable);
        }
    }

    private void drawPlayer(Canvas canvas, Player player, Drawable drawable) {
        drawable.setBounds(
                (int) (player.getX() - Player.R),
                (int) (player.getY() - Player.R),
                (int) (player.getX() + Player.R),
                (int) (player.getY() + Player.R)
        );
        drawable.draw(canvas);
    }

    private void drawBall(Canvas canvas, Drawable drawable) {
        Ball ball = data.getBall();
        drawable.setBounds(
                (int) (ball.getX() - Ball.R),
                (int) (ball.getY() - Ball.R),
                (int) (ball.getX() + Ball.R),
                (int) (ball.getY() + Ball.R)
        );
        drawable.draw(canvas);
    }

    private boolean moveTeam(List<Player> team) {
        boolean movement = false;
        for (int i = 0; i < GameData.N; ++i) {
            movement = team.get(i).move() || movement;
        }
        return movement;
    }

    private boolean moveBall() {
        return false;
    }
}
