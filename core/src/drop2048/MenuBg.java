package drop2048;

import screenControl.AbstractScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuBg extends Actor {
	private NinePatch bg, bgScore;
	private boolean haveBgScore;
	private static float endMenu, w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
	
	public MenuBg(boolean haveBgScore) {
		this.haveBgScore = haveBgScore;
		bg = AbstractScreen.getSkin().getPatch("bg-menu");
		bgScore = (haveBgScore)? AbstractScreen.getSkin().getPatch("bg-score") : null;
		setBounds(0, Drop2048.myRequestHandler.getHeightAd(), w, h*0.14f);
		MenuBg.endMenu = getHeight() + Drop2048.myRequestHandler.getHeightAd();
	}
	
	public static float getEndMenu() {
		return endMenu;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		bg.draw(batch, getX(), getY(), getWidth(), getHeight());
		if(haveBgScore) {
			bgScore.draw(batch, getX()+w*0.05f, getY()+h*0.01f, w*0.3f, h*0.12f);
			bgScore.draw(batch, getX()+w*0.65f, getY()+h*0.01f, w*0.3f, h*0.12f);
		}
	}
}
