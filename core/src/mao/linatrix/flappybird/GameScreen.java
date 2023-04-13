package mao.linatrix.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {

    private final float BIRD_SIZE_PIXEL = 128;
    private static final float MOVE_TIME = 0.1F;
    private SpriteBatch batch;
    private Texture background;
    private Texture[][] birds;
    private int birdFlapState = 1;

    private float timer = MOVE_TIME;
    private int invNum = 1;

    private int birdColor = 2;

    private float birdPosY = 0;

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
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {
            Gdx.app.log("Touched", "Yep");
        }

        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;

            if (birdFlapState >= 2 || birdFlapState <= 0) {
                invNum *= -1;
            }
            birdFlapState += invNum;
        }

        clearScreen();

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(birds[birdColor][birdFlapState], (Gdx.graphics.getWidth() - BIRD_SIZE_PIXEL) / 2F, (Gdx.graphics.getHeight() - BIRD_SIZE_PIXEL) / 2F, BIRD_SIZE_PIXEL, BIRD_SIZE_PIXEL);
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
