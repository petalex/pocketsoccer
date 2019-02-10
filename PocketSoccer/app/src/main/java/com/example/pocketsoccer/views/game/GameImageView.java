package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.utils.GameInfoManager;
import com.example.pocketsoccer.utils.ImageManager;
import com.example.pocketsoccer.utils.SoundManager;
import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Figure;
import com.example.pocketsoccer.views.game.figures.Player;
import com.example.pocketsoccer.views.game.timers.MatchTimer;
import com.example.pocketsoccer.views.game.timers.MoveTimer;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class GameImageView extends ImageView {
    private GameData data;

    private MoveDetector detector;

    private GameActivity listener;

    private Drawable team1, team2, ball, goals, selectedPlayer;

    private boolean init = true, enabled = false, reset = false, computer = false, timeFinished = false;

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
        MoveTimer.init(this);
        MatchTimer.init(this);
    }

    public void release() {
        MoveTimer.release();
        MatchTimer.release();
    }

    public void initDrawables() {
        team1 = ImageManager.getTeam(GameInfoManager.getTeam1());
        team2 = ImageManager.getTeam(GameInfoManager.getTeam2());
        ball = ImageManager.getBall();
        goals = ImageManager.getGoals();
        selectedPlayer = ImageManager.getSelectedPlayer();
    }

    public void start(boolean newGame) {
        MatchTimer.setTimer();

        setTeam1OnMove();

        enabled = true;
        MoveTimer.setTimer();
        MatchTimer.unpauseTimer();

        reset = newGame; // Only reset data when new game
        invalidate();
    }

    public void reset() {
        enabled = true;
        MoveTimer.setTimer();
        MatchTimer.unpauseTimer();


        reset = true;
        invalidate();
    }

    public void unfreeze() {
        enabled = true;
        MoveTimer.unpauseTimer();
        MatchTimer.unpauseTimer();
    }

    public void freeze() {
        enabled = false;
        MoveTimer.pauseTimer();
        MatchTimer.pauseTimer();
    }

    public void swapTeamOnMove() {
        switch (GameInfoManager.getTeamOnMove()) {
            case 1:
                setTeam2OnMove();
                break;
            case 2:
                setTeam1OnMove();
                break;
        }
    }

    public void timeFinished() {
        timeFinished = true;
        invalidate();
    }

    private void gameOver() {
        Runnable finish = new Runnable() {
            @Override
            public void run() {
                SoundManager.whistleSound();
            }
        };
        SoundManager.whistleSound();
        Handler handler1 = new Handler();
        handler1.postDelayed(finish, 1000);
        Handler handler2 = new Handler();
        handler2.postDelayed(finish, 1000);

        listener.gameOver();
    }

    public GameData getData() {
        return data;
    }

    public MoveDetector getDetector() {
        return detector;
    }

    public void setOnGameOverListener(GameActivity listener) {
        this.listener = listener;
    }

    public void setTeam1OnMove() {
        GameInfoManager.setTeamOnMove(1);
        if (GameInfoManager.isComputer1()) {
            data.setTeamOnMove(null);
            computer = true;
            invalidate();
        } else {
            data.setTeamOnMove(data.getTeam1());
        }
    }

    public void setTeam2OnMove() {
        GameInfoManager.setTeamOnMove(2);
        data.setTeamOnMove(data.getTeam2());
        if (GameInfoManager.isComputer2()) {
            data.setTeamOnMove(null);
            computer = true;
            invalidate();
        } else {
            data.setTeamOnMove(data.getTeam2());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (reset) {
            data.reset(getWidth(), getHeight());
            reset = false;
        }
        if (init) {
            // Only true for a first time
            data.loadStaticData(getWidth(), getHeight());
            init = false;
        }
        if (computer) {
            // Execute computer's move on main thread
            computerMove();
            computer = false;
        }
        if (timeFinished) {
            // Execute game over on main thread
            gameOver();
        }
        if (enabled) {
            if (move()) {
                resolveCollisions();
            } else {
                endMovement();
            }
            checkForGoal();
        }
        drawGame(canvas);
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
        boolean ballHit = false;
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
                        if (figure1 instanceof Ball || figure2 instanceof Ball) {
                            ballHit = true;
                        }
                    }
                }
            }
        }
        if (ballHit) {
            SoundManager.bounceSound();
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
        if (figure.getX() - figure.getR() <= 0) {
            figure.setX(figure.getX() + Math.abs(figure.getDx()));
            figure.setDx(Figure.L * Math.abs(figure.getDx()));
            if (figure instanceof Ball) {
                SoundManager.bounceSound();
            }
        }

        if (figure.getX() + figure.getR() >= getWidth()) {
            figure.setX(figure.getX() - Math.abs(figure.getDx()));
            figure.setDx(-Figure.L * Math.abs(figure.getDx()));
            if (figure instanceof Ball) {
                SoundManager.bounceSound();
            }
        }

        if (figure.getY() - figure.getR() <= 0){
            figure.setY(figure.getY() + Math.abs(figure.getDy()));
            figure.setDy(Figure.L * Math.abs(figure.getDy()));
            if (figure instanceof Ball) {
                SoundManager.bounceSound();
            }
        }

        if (figure.getY() + figure.getR() >= getHeight()) {
            figure.setY(figure.getY() - Math.abs(figure.getDy()));
            figure.setDy(-Figure.L * Math.abs(figure.getDy()));
            if (figure instanceof Ball) {
                SoundManager.bounceSound();
            }
        }
    }

    private void goalsResolution(Figure figure) {
        leftGoalResolution(figure, data.getGoals().get(0));
        leftGoalResolution(figure, data.getGoals().get(1));
        rightGoalResolution(figure, data.getGoals().get(2));
        rightGoalResolution(figure, data.getGoals().get(3));
    }

    private void leftGoalResolution(Figure figure, RectF goal) {
        float dx = figure.getDx(), dy = figure.getDy();
        float delta = (float) Math.sqrt(dx * dx + dy * dy);
        float distanceTopRight = getDistance(figure, new Ball(new PointF(goal.right, goal.top)));
        float distanceBottomRight = getDistance(figure, new Ball(new PointF(goal.right, goal.bottom)));
        if (goal.right <= figure.getX()) {
            // Top right corner
            if (figure.getY() <= goal.top) {
                if (figure.getR() - delta <= distanceTopRight && distanceTopRight <= figure.getR()) {
                    figure.setX(figure.getX() + Math.abs(figure.getDx()));
                    figure.setY(figure.getY() - Math.abs(figure.getDy()));
                    figure.setDx(Figure.L * Math.abs(figure.getDx()));
                    figure.setDy(-Figure.L * Math.abs(figure.getDy()));
                    return;
                }
            }
            // Bottom right corner
            if (goal.bottom <= figure.getY()) {
                if (figure.getR() - delta <= distanceBottomRight && distanceBottomRight <= figure.getR()) {
                    figure.setX(figure.getX() + Math.abs(figure.getDx()));
                    figure.setY(figure.getY() + Math.abs(figure.getDy()));
                    figure.setDx(Figure.L * Math.abs(figure.getDx()));
                    figure.setDy(Figure.L * Math.abs(figure.getDy()));
                    return;
                }
            }
        }
        if (goal.right + figure.getR() - Math.abs(dx) <= figure.getX() && figure.getX() <= goal.right + figure.getR() && dx < 0) {
            // Right edge
            if (goal.top <= figure.getY() && figure.getY() < goal.bottom) {
                figure.setX(figure.getX() + Math.abs(figure.getDx()));
                figure.setDx(Math.abs(figure.getDx()));
                return;
            }
        }
        if (goal.left + figure.getR() <= figure.getX() && figure.getX() < goal.right) {
            // Top edge
            if (goal.top - figure.getR() <= figure.getY() && figure.getY() <= goal.top - figure.getR() + Math.abs(dy) && dy > 0) {
                figure.setY(figure.getY() - Math.abs(figure.getDy()));
                figure.setDy(-Figure.L * Math.abs(figure.getDy()));
                return;
            }
            // Bottom edge
            if (goal.bottom + figure.getR() - Math.abs(dy) <= figure.getY() && figure.getY() <= goal.bottom + figure.getR() && dy < 0) {
                figure.setY(figure.getY() + Math.abs(dy));
                figure.setDy(Figure.L * Math.abs(dy));
                return;
            }
        }
    }

    private void rightGoalResolution(Figure figure, RectF goal) {
        float dx = figure.getDx(), dy = figure.getDy();
        float delta = (float) Math.sqrt(dx * dx + dy * dy);
        float distanceTopLeft = getDistance(figure, new Ball(new PointF(goal.left, goal.top)));
        float distanceBottomLeft = getDistance(figure, new Ball(new PointF(goal.left, goal.bottom)));
        if (figure.getX() <= goal.left) {
            // Top left corner
            if (figure.getY() <= goal.top) {
                if (figure.getR() - delta <= distanceTopLeft && distanceTopLeft <= figure.getR()) {
                    figure.setX(figure.getX() - Math.abs(figure.getDx()));
                    figure.setY(figure.getY() - Math.abs(figure.getDy()));
                    figure.setDx(-Figure.L * Math.abs(figure.getDx()));
                    figure.setDy(-Figure.L * Math.abs(figure.getDy()));
                    return;
                }
            }
            // Bottom left corner
            if (goal.bottom <= figure.getY()) {
                if (figure.getR() - delta <= distanceBottomLeft && distanceBottomLeft <= figure.getR()) {
                    figure.setX(figure.getX() - Math.abs(figure.getDx()));
                    figure.setY(figure.getY() + Math.abs(figure.getDy()));
                    figure.setDx(-Figure.L * Math.abs(figure.getDx()));
                    figure.setDy(Figure.L * Math.abs(figure.getDy()));
                    return;
                }
            }
        }
        if (goal.left + figure.getR() <= figure.getX() && figure.getX() <= goal.left + figure.getR() + Math.abs(dx) && dx > 0) {
            // Left edge
            if (goal.top <= figure.getY() && figure.getY() < goal.bottom) {
                figure.setX(figure.getX() - Math.abs(figure.getDx()));
                figure.setDx(-Math.abs(figure.getDx()));
                return;
            }
        }
        if (goal.left <= figure.getX() && figure.getX() < goal.right - figure.getR()) {
            // Top edge
            if (goal.top - figure.getR() <= figure.getY() && figure.getY() <= goal.top - figure.getR() + Math.abs(dy) && dy > 0) {
                figure.setY(figure.getY() - Math.abs(figure.getDy()));
                figure.setDy(-Figure.L * Math.abs(figure.getDy()));
                return;
            }
            // Bottom edge
            if (goal.bottom + figure.getR() - Math.abs(dy) <= figure.getY() && figure.getY() <= goal.bottom + figure.getR() && dy < 0) {
                figure.setY(figure.getY() + Math.abs(dy));
                figure.setDy(Figure.L * Math.abs(dy));
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
        if (goal1Top.left + ball.getR() < ball.getX() && ball.getX() < goal1Top.right - ball.getR() &&
                goal1Top.bottom + ball.getR() < ball.getY() && ball.getY() < goal1Bottom.top - ball.getR()) {
            SoundManager.crowdSound();
            freeze();
            Runnable restart = new Runnable() {
                @Override
                public void run() {
                    setTeam1OnMove();
                    GameInfoManager.addToScore2();
                    if (GameInfoManager.getMatchType().equals("goals") &&
                            GameInfoManager.getScore2() == GameInfoManager.getMatch()) {
                        gameOver();
                    } else {
                        SoundManager.whistleSound();
                        reset();
                    }
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(restart, 1000);
        }
        // Team 2's goal
        RectF goal2Top = data.getGoals().get(2);
        RectF goal2Bottom = data.getGoals().get(3);
        if (ball.getX() - ball.getR() > goal2Top.left && ball.getX() + ball.getR() < goal2Top.right &&
                ball.getY() - ball.getR() > goal2Top.bottom && ball.getY() + ball.getR() < goal2Bottom.top) {
            SoundManager.crowdSound();
            freeze();
            Runnable restart = new Runnable() {
                @Override
                public void run() {
                    SoundManager.whistleSound();
                    setTeam2OnMove();
                    GameInfoManager.addToScore1();
                    if (GameInfoManager.getMatchType().equals("goals") &&
                            GameInfoManager.getScore1() == GameInfoManager.getMatch()) {
                        gameOver();
                    } else {
                        reset();
                    }
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(restart, 1000);
        }
    }

    private void drawGame(Canvas canvas) {
        drawCrests(canvas);
        drawMoveTime(canvas);
        drawTeam(canvas, data.getTeam1(), team1);
        drawTeam(canvas, data.getTeam2(), team2);
        drawBall(canvas);
        drawGoals(canvas);
        drawResult(canvas);
    }

    private void drawCrests(Canvas canvas) {
        float width = 0.3f * getWidth();
        float height = width;
        float startX = (0.5f * getWidth() - width) / 2;
        float startY = (getHeight() - height) / 2;

        switch (GameInfoManager.getTeamOnMove()) {
            case 1:
                team1.setAlpha(150);
                team2.setAlpha(50);
                break;
            case 2:
                team1.setAlpha(50);
                team2.setAlpha(150);
                break;
        }

        team1.setBounds(
                (int) (startX),
                (int) (startY),
                (int) (startX + width),
                (int) (startY + height)
        );
        team1.draw(canvas);

        startX += 0.5f * getWidth();
        team2.setBounds(
                (int) (startX),
                (int) (startY),
                (int) (startX + width),
                (int) (startY + height)
        );
        team2.draw(canvas);

        team1.setAlpha(255);
        team2.setAlpha(255);
    }

    private void drawTeam(Canvas canvas, List<Figure> team, Drawable teamDrawable) {
        for (int i = 0; i < GameData.N; ++i) {
            drawPlayer(canvas, team.get(i), teamDrawable);
        }
    }

    private void drawPlayer(Canvas canvas, Figure player, Drawable teamDrawable) {
        if (player == data.getSelectedPlayer()) {
            selectedPlayer.setBounds(
                    (int) (player.getX() - Player.R * 1.75f),
                    (int) (player.getY() - Player.R * 1.75f),
                    (int) (player.getX() + Player.R * 1.75f),
                    (int) (player.getY() + Player.R * 1.75f)
            );
            selectedPlayer.draw(canvas);
        }
        teamDrawable.setBounds(
                (int) (player.getX() - Player.R),
                (int) (player.getY() - Player.R),
                (int) (player.getX() + Player.R),
                (int) (player.getY() + Player.R)
        );
        teamDrawable.draw(canvas);
    }

    private void drawBall(Canvas canvas) {
        Figure ballFigure = data.getBall();
        ball.setBounds(
                (int) (ballFigure.getX() - Ball.R),
                (int) (ballFigure.getY() - Ball.R),
                (int) (ballFigure.getX() + Ball.R),
                (int) (ballFigure.getY() + Ball.R)
        );
        ball.draw(canvas);
    }

    private void drawGoals(Canvas canvas) {
        goals.setBounds(0, 0, getWidth(), getHeight());
        goals.draw(canvas);
    }

    private void drawResult(Canvas canvas) {
        Paint paint = new Paint();

        float a = 0.4f * getWidth();
        float b = 0.1f * getHeight();
        float left = (getWidth() - a) / 2;
        float top = 0.05f * getHeight();
        RectF scoreboard = new RectF(left, top, left + a, top + b);

        // Main rect
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);
        canvas.drawRoundRect(scoreboard, b / 4, b / 2, paint);

        // Border rect rect
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.025f * b);
        canvas.drawRoundRect(scoreboard, b / 4, b / 2, paint);

        if (GameInfoManager.isComputer1()) {
            // Computer icon for player 1
            Drawable computer = getResources().getDrawable(R.drawable.ic_computer_white_24dp);
            computer.mutate();
            computer.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            computer.setAlpha(200);
            computer.setBounds(
                    (int) (left + 0.05f * a),
                    (int) (top + 0.3f * b),
                    (int) (left + 0.05f * a + 0.4f * b),
                    (int) (top + 0.7f * b)
            );
            computer.draw(canvas);
        }

        // Team 1 crest
        team1.setBounds(
                (int) (left + 0.15f * a),
                (int) (top + 0.1f * b),
                (int) (left + 0.15f * a + 0.8f * b),
                (int) (top + 0.9f * b)
        );
        team1.draw(canvas);

        // Player 1 score
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(0.7f * b);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("" + GameInfoManager.getScore1(), left + 0.45f * a, top + 0.75f * b, paint);

        // Middle line
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.005f * a);
        canvas.drawLine(left + 0.5f * a, top + 0.2f * b, left + 0.5f * a, top + 0.8f * b, paint);

        // Player 2 score
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(0.7f * b);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("" + GameInfoManager.getScore2(), left + 0.55f * a, top + 0.75f * b, paint);

        // Team 2 crest
        team2.setBounds(
                (int) (left + 0.85f * a - 0.8f * b),
                (int) (top + 0.1f * b),
                (int) (left + 0.85f * a),
                (int) (top + 0.9f * b)
        );
        team2.draw(canvas);

        if (GameInfoManager.isComputer2()) {
            // Computer icon for player 2
            Drawable computer = getResources().getDrawable(R.drawable.ic_computer_white_24dp);
            computer.mutate();
            computer.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            computer.setAlpha(200);
            computer.setBounds(
                    (int) (left + 0.95f * a - 0.4f * b),
                    (int) (top + 0.3f * b),
                    (int) (left + 0.95f * a),
                    (int) (top + 0.7f * b)
            );
            computer.draw(canvas);
        }

        // Match time
        a = a / 4;
        b = 2 * b / 3;
        left = (getWidth() - a) / 2;
        top = top + 3 * b / 2;
        RectF timeboard = new RectF(left, top, left + a, top + b);

        // Main rect
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(200);
        canvas.drawRoundRect(timeboard, b / 4, b / 2, paint);

        // Border rect rect
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.025f * b);
        canvas.drawRoundRect(timeboard, b / 4, b / 2, paint);

        paint.setColor(Color.WHITE);
        // Time minutes
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(0.7f * b);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(timeToString(GameInfoManager.getTime() / 60), left + 0.45f * a, top + 0.75f * b, paint);

        // Semicolon
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(0.7f * b);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(":", left + 0.5f * a, top + 0.75f * b, paint);

        // Time seconds
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(0.7f * b);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(timeToString(GameInfoManager.getTime() % 60), left + 0.55f * a, top + 0.75f * b, paint);
    }

    private String timeToString(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return "" + time;
    }

    private void drawMoveTime(Canvas canvas) {
        Paint paint = new Paint();

        float b = 0.3f * getHeight();
        float a = b;
        float left = (getWidth() - a) / 2;
        float top = (getHeight() - b) / 2;

        // Time to move
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);
        paint.setTextSize(b);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("" + MoveTimer.getMoveTime(), left + 0.2f * b, top + 0.85f * b, paint);
    }

    private void computerMove() {
        // Randomly pick player from a team
        final int player = (int) (GameData.N * Math.random());

        Runnable afterPicking = new Runnable() {
            @Override
            public void run() {
                // Player picked
                Figure figure = null;
                switch (GameInfoManager.getTeamOnMove()) {
                    case 1:
                        figure = data.getTeam1().get(player);
                        break;
                    case 2:
                        figure = data.getTeam2().get(player);
                        break;
                }
                data.setSelectedPlayer(figure);
                invalidate();
            }
        };

        Runnable afterWaiting = new Runnable() {
            @Override
            public void run() {
                // Player picked
                Figure figure = null;
                switch (GameInfoManager.getTeamOnMove()) {
                    case 1:
                        figure = data.getTeam1().get(player);
                        break;
                    case 2:
                        figure = data.getTeam2().get(player);
                        break;
                }
                // Move picked player towards the ball after waiting
                Figure ball = data.getBall();
                //float D = getDistance(figure, ball);
                float dx = (float) ((0.5f + Math.random()) * (ball.getX() - figure.getX()));
                float dy = (float) ((0.5f + Math.random()) * (ball.getY() - figure.getY()));

                figure.initMove(dx, dy);
                detector.startMovement();
                data.setSelectedPlayer(null);
                swapTeamOnMove();
                MoveTimer.setTimer();
            }
        };

        // Wait between 10 - 40 % of move to pick a player
        float timeToPickPlayer = (float) (0.1f * MoveTimer.MOVE_TIME + 0.3f * MoveTimer.MOVE_TIME * Math.random());
        Handler pickingHandler = new Handler();
        pickingHandler.postDelayed(afterPicking, (long) timeToPickPlayer * 1000);

        // Wait between 10 - 40 % of move to init player's move after picking the player
        float timeToMovePlayer = (float) (0.1f * MoveTimer.MOVE_TIME + 0.3f * MoveTimer.MOVE_TIME * Math.random());
        Handler moveHandler = new Handler();
        moveHandler.postDelayed(afterWaiting, (long) (timeToPickPlayer + timeToMovePlayer) * 1000);
    }
}