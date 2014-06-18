package PopUps;

import screenControl.AbstractScreen;
import screenControl.GameScreen;
import screenControl.MenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import drop2048.Drop2048;

public class GamePausePopUp extends PopUp {
	private TextButton resumeButton, resetButton, exitButton;
	protected static GameScreen gameScreen;
	
	public GamePausePopUp(Batch batch) {
		super(batch);
	}

	public static void setScreen(GameScreen gameScreen) {
		GamePausePopUp.gameScreen = gameScreen;
	}
	
	@Override
	protected void initializeTable() {
		super.initializeTable();
		resumeButton = new TextButton("Resume", AbstractScreen.getSkin());
		resumeButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		    	gameScreen.setGamePause(false);
		        return false;
		    } 
		} );
		
		resetButton = new TextButton("Reset", AbstractScreen.getSkin());
		resetButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
				game.setScreen(new GameScreen(gameScreen.getStatus()));
		        return false;
		    } 
		} );
		
		exitButton = new TextButton("Exit", AbstractScreen.getSkin());
		exitButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		    	Drop2048.save();
		    	game.setScreen(new MenuScreen());
		        return false;
		    } 
		} );
		table.add("Game paused");
		table.row();
		table.add().height(h*0.03f);
		table.row();
		table.add(resumeButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.1f);
		table.row();
		table.add().height(h*0.03f);
		table.row();
		table.add(resetButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.1f);
		table.row();
		table.add().height(h*0.03f);
		table.row();
		table.add(exitButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.1f);
	}
}
