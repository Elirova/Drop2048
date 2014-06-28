package PopUps;

import screenControl.AbstractScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import drop2048.Background;
import drop2048.Drop2048;

public abstract class PopUp {
	protected static Drop2048 game;
	protected Stage popUpStage;
	protected Table table;
	protected Background bg;
	protected boolean visible;
	protected int w, h;
	
	public PopUp(Batch batch) {
		visible = false;
		popUpStage = new Stage(new ScreenViewport(), batch);
		h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
		bg = new Background(w*0.2f, h*0.2f, w*0.6f, h*0.6f, "button-round-gray"); 
		popUpStage.addActor(bg);
		initializeTable();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		if(visible) Gdx.input.setInputProcessor(popUpStage);
	}
	
	public void show() {
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
		popUpStage.dispose();
	}
}
