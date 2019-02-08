package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Figure;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class GameImageView extends ImageView {
    private GameData data;

    private MoveDetector detector;

    private Drawable team1, team2, ball, goals;

    private int teamOnMove;

    private boolean draw = false, reset = false;

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
        setTeam1OnMove();
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

    public void setGoals(Drawable goals) {
        this.goals = goals;
    }

    public void start() {
        draw = true;
        reset = true;
        invalidate();
    }

    public void stop() {
        draw = false;
    }

    public void swapTeamOnMove() {
        switch (teamOnMove) {
            case 1:
                setTeam2OnMove();
                break;
            case 2:
                setTeam1OnMove();
                break;
        }
    }

    public void setTeam1OnMove() {
        teamOnMove = 1;
        data.setTeamOnMove(data.getTeam1());
    }

    public void setTeam2OnMove() {
        teamOnMove = 2;
        data.setTeamOnMove(data.getTeam2());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (draw) {
            if (reset) {
                data.reset(getWidth(), getHeight(), teamOnMove);
                reset = false;
            }
            if (move()) {
                resolveCollisions();
            } else {
                endMovement();
            }

            checkForGoal();

            drawGame(canvas);
        }
    }

    private boolean move() {
        boolean movementTeam1, movementTeam2, movementBall;

        movementTeam1 = moveTeam(data.getTeam1());
        movementTeam2 = moveTeam(data.getTeam2());
        movementBall = moveBall();

        return movementTeam1 || movementTeam2 || movementBall;
    }

    private boolean moveTeam(List<Figure> team) {
        boolean movement = false;
        for (int i = 0; i < GameData.N; ++i) {
            movement = team.get(i).move() || movement;
        }
        return movement;
    }

    private boolean moveBall() {
        return data.getBall().move();
    }

    private void resolveCollisions() {
        resolveBallVsBallCollisions();
        resolveBallVsEdgeCollisions();
    }

    private void resolveBallVsBallCollisions() {
        List<Figure> figures = new ArrayList<>();
        figures.addAll(data.getTeam1());
        figures.addAll(data.getTeam2());
        figures.add(data.getBall());
        for (Figure figure1 : figures) {
            for (Figure figure2 : figures) {
                if (figure1 != figure2) {
                    if (inContact(figure1, figure2)) {
                        staticResolution(figure1, figure2);
                        //dynamicResolution(figure1, figure2);
                    }
                }
            }
        }
    }

    private void resolveBallVsEdgeCollisions() {
        List<Figure> figures = new ArrayList<>();
        figures.addAll(data.getTeam1());
        figures.addAll(data.getTeam2());
        figures.add(data.getBall());
        for (Figure figure : figures) {
            egdesResolution(figure);
            goalsResolution(figure);
        }
    }

    private void egdesResolution(Figure figure) {
        if (figure.getX() - figure.getR() <= 0.005f * getWidth() || figure.getX() + figure.getR() >= 0.995f * getWidth()) {
            figure.setDx(-1.0f * figure.getDx());
        }
        if (figure.getY() - figure.getR() <= 0.015f * getHeight() || figure.getY() + figure.getR() >= 0.985f * getHeight()) {
            figure.setDy(-1.0f * figure.getDy());
        }
    }

    private void goalsResolution(Figure figure) {
        leftGoalResolution(figure, data.getGoals().get(0));
        leftGoalResolution(figure, data.getGoals().get(1));
        rightGoalResolution(figure, data.getGoals().get(2));
        rightGoalResolution(figure, data.getGoals().get(3));
    }

    private void leftGoalResolution(Figure figure, RectF goal) {
        float delta = (float) Math.sqrt(figure.getDx() * figure.getDx() + figure.getDy() * figure.getDy());
        float distanceTopRight = getDistance(figure, new Ball(new PointF(goal.right, goal.top)));
        float distanceBottomRight = getDistance(figure, new Ball(new PointF(goal.right, goal.bottom)));

        if (goal.right <= figure.getX()) {
            // Top right corner
            if (figure.getR() <= distanceTopRight && distanceTopRight <= figure.getR() + delta) {
                figure.setDx(Math.abs(figure.getDx()));
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
            // Bottom right corner
            if (figure.getR() <= distanceBottomRight && distanceBottomRight <= figure.getR() + delta) {
                figure.setDx(Math.abs(figure.getDx()));
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
        } else if (goal.right + figure.getR() - Math.abs(figure.getDx()) <= figure.getX() && figure.getX() <= goal.right + figure.getR() &&
                goal.top <= figure.getY() && figure.getY() < goal.bottom) { // Right edge
            figure.setDx(-1.0f * figure.getDx());
            return;
        } else if (goal.left + figure.getR() <= figure.getX() && figure.getX() < goal.right) {
            // Top edge
            if (goal.top - figure.getR() <= figure.getY() && figure.getY() <= goal.top - figure.getR() + Math.abs(figure.getDy())) {
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
            // Bottom edge
            if (goal.bottom + figure.getR() - Math.abs(figure.getDy()) <= figure.getY() && figure.getY() <= goal.bottom + figure.getR()) {
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
        }
    }

    private void rightGoalResolution(Figure figure, RectF goal) {
        float delta = (float) Math.sqrt(figure.getDx() * figure.getDx() + figure.getDy() * figure.getDy());
        float distanceTopLeft = getDistance(figure, new Ball(new PointF(goal.left, goal.top)));
        float distanceBottomLeft = getDistance(figure, new Ball(new PointF(goal.left, goal.bottom)));

        if (figure.getX() <= goal.left) {
            // Top left corner
            if (figure.getR() <= distanceTopLeft && distanceTopLeft <= figure.getR() + delta) {
                figure.setDx(-Math.abs(figure.getDx()));
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
            // Bottom left corner
            if (figure.getR() <= distanceBottomLeft && distanceBottomLeft <= figure.getR() + delta) {
                figure.setDx(-Math.abs(figure.getDx()));
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
        } else if (goal.left - figure.getR() + Math.abs(figure.getDx()) <= figure.getX() && figure.getX() <= goal.left - figure.getR() &&
                goal.top <= figure.getY() && figure.getY() < goal.bottom) { // Left edge
            figure.setDx(-1.0f * figure.getDx());
            return;
        } else if (goal.left < figure.getX() && figure.getX() <= goal.right - figure.getR()) {
            // Top edge
            if (goal.top - figure.getR() <= figure.getY() && figure.getY() <= goal.top - figure.getR() + Math.abs(figure.getDy())) {
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
            // Bottom edge
            if (goal.bottom + figure.getR() - Math.abs(figure.getDy()) <= figure.getY() && figure.getY() <= goal.bottom + figure.getR()) {
                figure.setDy(-1.0f * figure.getDy());
                return;
            }
        }
    }

    private void staticResolution(Figure figure1, Figure figure2) {
        float D = getDistance(figure1, figure2);
        float delta1 = (1 - figure1.getM() / (figure1.getM() + figure2.getM())) * (D - figure1.getR() - figure2.getR());
        float delta2 = (1 - figure2.getM() / (figure1.getM() + figure2.getM())) * (D - figure1.getR() - figure2.getR());
        figure1.setDx(figure1.getDx() - delta1 * (figure1.getX() - figure2.getX()) / D);
        figure1.setDy(figure1.getDy() - delta1 * (figure1.getY() - figure2.getY()) / D);
        figure2.setDx(figure2.getDx() + delta2 * (figure1.getX() - figure2.getX()) / D);
        figure2.setDy(figure2.getDy() + delta2 * (figure1.getY() - figure2.getY()) / D);
    }

    private void dynamicResolution(Figure figure1, Figure figure2) {
        float D = getDistance(figure1, figure2);
        float normalX = (figure2.getX() - figure1.getX()) / D;
        float normalY = (figure2.getY() - figure1.getY()) / D;
        float tangentX = -normalY;
        float tangentY = normalX;
        // Dot product tangent
        float dpTangent1 = figure1.getDx() * tangentX + figure1.getDy() * tangentY;
        float dpTangent2 = figure2.getDx() * tangentX + figure2.getDy() * tangentY;
        // Dot product normal
        float dpNormal1 = figure1.getDx() * normalX + figure1.getDy() * normalY;
        float dpNormal2 = figure2.getDx() * normalX + figure2.getDy() * normalY;
        // Momentum
        float momentum1 = (dpNormal1 * (figure1.getM() - figure2.getM()) + 2.0f * figure2.getM() * dpNormal2) / (figure1.getM() + figure2.getM());
        float momentum2 = (dpNormal2 * (figure2.getM() - figure1.getM()) + 2.0f * figure1.getM() * dpNormal1) / (figure1.getM() + figure2.getM());

        figure1.setDx(dpTangent1 * tangentX + momentum1 * normalX);
        figure1.setDy(dpTangent1 * tangentY + momentum1 * normalY);
        figure2.setDx(dpTangent2 * tangentX + momentum2 * normalX);
        figure2.setDy(dpTangent2 * tangentY + momentum2 * normalY);
    }

    private boolean inContact(Figure figure1, Figure figure2) {
        return getDistance(figure1, figure2) <= figure1.getR() + figure2.getR();
    }

    private float getDistance(Figure figure1, Figure figure2) {
        return (float) Math.sqrt((figure1.getX() - figure2.getX()) * (figure1.getX() - figure2.getX()) + (figure1.getY() - figure2.getY()) * (figure1.getY() - figure2.getY()));
    }

    private void endMovement() {
        detector.endMovement();
    }

    private void checkForGoal() {
        Figure ball = data.getBall();
        // Team 1's goal
        RectF goal1Top = data.getGoals().get(0);
        RectF goal1Bottom = data.getGoals().get(1);
        if (goal1Top.left + ball.getR() < ball.getX() && ball.getX() < goal1Top.right -  ball.getR() / 2 &&
                 goal1Top.bottom + ball.getR() < ball.getY() && ball.getY() < goal1Bottom.top - ball.getR()) {
            stop();
            Toast.makeText(getContext(), "Goal for Team 2!", Toast.LENGTH_SHORT).show();
            setTeam1OnMove();
            start();

        }
        // Team 2's goal
        RectF goal2Top = data.getGoals().get(2);
        RectF goal2Bottom = data.getGoals().get(3);
        if (ball.getX() - ball.getR() / 2 > goal2Top.left && ball.getX() + ball.getR() < goal2Top.right &&
                ball.getY() - ball.getR() > goal2Top.bottom && ball.getY() + ball.getR() < goal2Bottom.top) {
            stop();
            Toast.makeText(getContext(), "Goal for Team 1!", Toast.LENGTH_SHORT).show();
            setTeam2OnMove();
            start();
        }
    }

    private void drawGame(Canvas canvas) {
        drawCrests(canvas);
        drawTeam(canvas, data.getTeam1(), team1);
        drawTeam(canvas, data.getTeam2(), team2);
        drawBall(canvas, ball);
        drawGoals(canvas, goals);
    }

    private void drawCrests(Canvas canvas) {
        float width = 0.3f * getWidth();
        float height = width;
        float startX = (0.5f * getWidth() - width) / 2;
        float startY = (getHeight() - height) / 2;
        team1.setBounds(
                (int) (startX),
                (int) (startY),
                (int) (startX + width),
                (int) (startY + height)
        );
        team1.setAlpha(50 + ((teamOnMove == 1) ? 100 : 0));
        team1.draw(canvas);
        team1.setAlpha(255);
        startX += 0.5f * getWidth();
        team2.setBounds(
                (int) (startX),
                (int) (startY),
                (int) (startX + width),
                (int) (startY + height)
        );
        team2.setAlpha(50 + ((teamOnMove == 2) ? 100 : 0));
        team2.draw(canvas);
        team2.setAlpha(255);
    }

    private void drawTeam(Canvas canvas, List<Figure> team, Drawable drawable) {
        for (int i = 0; i < GameData.N; ++i) {
            drawPlayer(canvas, team.get(i), drawable);
        }
    }

    private void drawPlayer(Canvas canvas, Figure player, Drawable drawable) {
        drawable.setBounds(
                (int) (player.getX() - Player.R),
                (int) (player.getY() - Player.R),
                (int) (player.getX() + Player.R),
                (int) (player.getY() + Player.R)
        );
        drawable.draw(canvas);
    }

    private void drawBall(Canvas canvas, Drawable drawable) {
        Figure ball = data.getBall();
        drawable.setBounds(
                (int) (ball.getX() - Ball.R),
                (int) (ball.getY() - Ball.R),
                (int) (ball.getX() + Ball.R),
                (int) (ball.getY() + Ball.R)
        );
        drawable.draw(canvas);
    }

    private void drawGoals(Canvas canvas, Drawable drawable) {
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
        /*Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(data.getGoals().get(0), paint);
        canvas.drawRect(data.getGoals().get(1), paint);
        canvas.drawRect(data.getGoals().get(2), paint);
        canvas.drawRect(data.getGoals().get(3), paint);*/
    }
}
