package mao.linatrix.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class GameScreen extends ScreenAdapter {

    private final float GAME_WIDTH = Gdx.graphics.getWidth();
    private final float GAME_HEIGHT = Gdx.graphics.getHeight();
    private final float SCALE_FACTOR = 5.F;
    private static final float MOVE_TIME = 0.1F;
    private final float EARTH_GRAVITY = 2.F;
    private final float GAME_SPEED = 6.F;

    // gap => times of bird height
    private float gapBirdHeightFactor = 5.F;
    private float gap;

    private SpriteBatch batch;
    private Texture background;

    // pipes [pipeState: 0=bottom|1=top] [0=green|1=red]
    private int numberOfPipes = 4;
    private Texture[][] pipes;
    private int pipeState = 0;
    private int pipeColor = 0;
    private float[] pipeBottomPosX = new float[numberOfPipes];
    private float pipeBottomPosY = 0.F;
    private float[] pipeTopPosX = new float[numberOfPipes];
    private float pipeTopPosY = 0.F;
    private float pipeWidth;
    private float pipeHeight;
    private float pipeVelocity = GAME_SPEED;
    private float[] pipeOffset = new float[numberOfPipes];
    private float maxPipeOffset;
    private float distanceBetweenPipes = GAME_WIDTH * 3 / 4;

    // birds [birdFlapState: 0=downflap|1=midflap|2=upflap] [birdColor: 0=redbird|1=bluebird|2=yellowbird]
    private Texture[][] birds;
    private int birdFlapState = 1;
    private int birdColor = 0;
    private float birdPosY = 0.F;
    private float birdPosX = 0.F;
    private float birdFreeFallVelocity = 0.F;
    private float birdWidth;
    private float birdHeight;

    private float timer = MOVE_TIME;
    private int invNum = 1;

    private boolean gameState = false;

    private Random randomGenerator;

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("background-day.png");

        birds = new Texture[3][3];
        birds[0][0] = new Texture("redbird-downflap.png");
        birds[0][1] = new Texture("redbird-midflap.png");
        birds[0][2] = new Texture("redbird-upflap.png");
        birds[1][0] = new Texture("bluebird-downflap.png");
        birds[1][1] = new Texture("bluebird-midflap.png");
        birds[1][2] = new Texture("bluebird-upflap.png");
        birds[2][0] = new Texture("yellowbird-downflap.png");
        birds[2][1] = new Texture("yellowbird-midflap.png");
        birds[2][2] = new Texture("yellowbird-upflap.png");

        pipes = new Texture[2][2];
        pipes[0][0] = new Texture("pipe-bottom-green.png");
        pipes[0][1] = new Texture("pipe-bottom-red.png");
        pipes[1][0] = new Texture("pipe-top-green.png");
        pipes[1][1] = new Texture("pipe-top-red.png");

        birdWidth = birds[birdFlapState][birdColor].getWidth() * SCALE_FACTOR;
        birdHeight = birds[birdFlapState][birdColor].getHeight() * SCALE_FACTOR;

        birdPosY = (Gdx.graphics.getHeight() - birdHeight) / 2.F;
        birdPosX = (Gdx.graphics.getWidth() - birdWidth) / 2.F;

        pipeWidth = pipes[pipeState][pipeColor].getWidth() * SCALE_FACTOR;
        pipeHeight = pipes[pipeState][pipeColor].getHeight() * SCALE_FACTOR;

        gap = birds[birdColor][birdFlapState].getHeight() * gapBirdHeightFactor * SCALE_FACTOR;

        pipeBottomPosY = (((GAME_HEIGHT - gap) / 2.F) - pipeHeight);
        pipeTopPosY = (GAME_HEIGHT + Math.abs(pipeBottomPosY) - pipeHeight);
        maxPipeOffset = GAME_HEIGHT / 2.F - gap / 2.F -100;

        randomGenerator = new Random();

        for (int i = 0; i < numberOfPipes; i++) {
            pipeOffset[i] = (randomGenerator.nextFloat() - 0.5F) * ((float) GAME_HEIGHT - gap - 200.F);
            pipeTopPosX[i] = pipeBottomPosX[i] = (float) ((GAME_WIDTH - pipeWidth) / 2.F + i * distanceBetweenPipes);
        }
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {
            gameState = true;
        }

        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;

            if (birdFlapState >= 2 || birdFlapState <= 0) {
                invNum *= -1;
            }

            birdFlapState += invNum;
        }

        if (gameState != false) {
            if (Gdx.input.isTouched()) {
                birdFreeFallVelocity = -20;


            }

            if (birdPosY > 0.F  || birdFreeFallVelocity < 0.F) {
                birdFreeFallVelocity += EARTH_GRAVITY;
                birdPosY -= birdFreeFallVelocity;
            }

        } else {
            if (Gdx.input.justTouched()) {
                gameState = true;
            }
        }

        clearScreen();
        batch.begin();
        batch.draw(background, 0, 0, GAME_WIDTH, GAME_HEIGHT);

        for (int i = 0; i < numberOfPipes; i++) {

            if (pipeBottomPosX[i] < -pipeWidth) {
                pipeBottomPosX[i] = pipeTopPosX[i] += (numberOfPipes * distanceBetweenPipes);
            } else {
                pipeBottomPosX[i] = pipeTopPosX[i] -= pipeVelocity;
            }
            batch.draw(pipes[0][pipeColor], pipeBottomPosX[i], pipeBottomPosY + pipeOffset[i], pipeWidth, pipeHeight);
            batch.draw(pipes[1][pipeColor], pipeTopPosX[i], pipeTopPosY + pipeOffset[i], pipeWidth, pipeHeight);
        }

        batch.draw(birds[birdColor][birdFlapState], birdPosX, birdPosY, birdWidth, birdHeight);
        batch.end();

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
