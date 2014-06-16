package PopUps;

import screenControl.AbstractScreen;
import screenControl.GameScreen;
import screenControl.MenuScreen;
import ProfileSettings.Profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import drop2048.Drop2048;

public class GamePausePopUp extends PopUp {
	private TextButton resumeButton, statsButton, exitButton;
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
		    	Gdx.app.log( Drop2048.LOG, "Pulsado botón menu" );
		    	gameScreen.setGamePause(false);
		        return false;
		    } 
		} );
		
		statsButton = new TextButton("Stats", AbstractScreen.getSkin());
		statsButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		    	Gdx.app.log( Drop2048.LOG, "Pulsado botón menu" );
		    	System.out.println("Pulsado boton para cambiar los stats");
		        return false;
		    } 
		} );
		
		exitButton = new TextButton("Exit", AbstractScreen.getSkin());
		exitButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
//				Profile.setScore(score.getScore(), status);
		    	Drop2048.save();
		    	game.setScreen(new MenuScreen());
		        return false;
		    } 
		} );
		
		table.add(resumeButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.15f);
		table.row();
		table.add(statsButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.15f);
		table.row();
		table.add(exitButton).size(Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.15f);
	}
}
