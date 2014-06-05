package PopUps;

import screenControl.AbstractScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import drop2048.Background;
import drop2048.Drop2048;

public abstract class PopUp {
	protected static Drop2048 game;
	protected Texture popupTexture;
	protected Stage popUpStage;
	protected Table table;
	protected Background bg;
	protected boolean visible;
	protected int w, h;
	
	public PopUp(Batch batch) {
		visible = false;
		popupTexture = new Texture(Gdx.files.internal("Images/popup.png"));
		popUpStage = new Stage(new ScreenViewport(), batch);
		h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
		bg = new Background(new NinePatch(new TextureRegion(popupTexture, 0, 0, 32, 32), 14, 14, 14, 14),
				w*0.2f, h*0.2f, w*0.6f, h*0.6f); 
		popUpStage.addActor(bg);
		initializeTable();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		if(visible) Gdx.input.setInputProcessor(popUpStage);
	}
	
	public void show() {
//		Gdx.input.setInputProcessor(popUpStage);
		setVisible(true);
	}
	
	public void close() {
		setVisible(false);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public Stage getStage() {
		return popUpStage;
	}
	
	public static void setGame(Drop2048 game) {
		PopUp.game = game;
	}
	
	public void resize(int width, int height) {
		popUpStage.getViewport().setWorldWidth(width);
		popUpStage.getViewport().setWorldHeight(height);
	}
	
	public void draw (float delta) {
		popUpStage.act(delta);
		popUpStage.draw();
	}
	
	protected void initializeTable() {
		table = new Table( AbstractScreen.getSkin() ); 
        table.setFillParent( true ); 
        popUpStage.addActor( table );
	}
	
	public void dispose() {
		popupTexture.dispose();
		popUpStage.dispose();
	}
}
