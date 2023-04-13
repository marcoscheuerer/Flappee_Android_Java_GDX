package mao.linatrix.flappybird;

import com.badlogic.gdx.Game;

public class FlappyBird extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}