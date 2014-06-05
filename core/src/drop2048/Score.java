package drop2048;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Score extends Actor {
	private int score;
	private BitmapFont font;
	
	public Score(BitmapFont font) {
		this(0, font);
	}
	
	public Score(int score, BitmapFont font) {
		this.score = score;
		this.font = font;
		setY(getY()+Gdx.graphics.getHeight()*0.05f+getHeight()/2);
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		font.draw(batch, String.valueOf(score), getX(), getY()+getHeight()/2+font.getCapHeight()/2);
    }
}
