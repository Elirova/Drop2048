package drop2048;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor {
	private NinePatch bg;
	
	public Background(NinePatch bg, float x, float y, float width, float height) {
		this.bg = bg;
		setX(x);
		setY(y);
		setHeight(height);
		setWidth(width);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		bg.draw(batch, getX(), getY(), getWidth(), getHeight());
	}
}
