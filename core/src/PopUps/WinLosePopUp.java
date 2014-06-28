package PopUps;

import screenControl.AbstractScreen;
import screenControl.GameScreen;
import screenControl.MenuScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WinLosePopUp extends PopUp {
	private TextButton backButton;
	
	public WinLosePopUp(Batch batch) {
		super(batch);
	}
	
	public void show(boolean win) {
		if(win) initializeWinTable();
		else	initializeLoseTable();
		initializeCommonTable();
		super.show();
	}
	
	@Override
	public void close() {
		super.close();
	}
	
	protected void initializeWinTable() {
		table.add("YOU WIN!");
	}
	
	protected void initializeLoseTable() {
		table.add("YOU LOSE!");
		
	}
	
	private void initializeCommonTable() {
		backButton = new TextButton("Back to MainMenu", AbstractScreen.getSkin(), "round-gray-white");
		backButton.addListener(new InputListener() { 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
				game.setScreen(new MenuScreen());
		        return false;
		    } 
		} );
		table.add().height(h*0.15f);
		table.row();
		table.add("Score:");
		table.row();
		table.add(String.valueOf(((GameScreen)game.getScreen()).getScore()));
		table.row();
		if(((GameScreen)game.getScreen()).getScore() >= ((GameScreen)game.getScreen()).getMaxScore()) {
			table.add("New record!!", "white").spaceTop(h*0.03f).spaceBottom(h*0.03f);
			table.row();
		} else table.add().height(h*0.1f);
		table.row();
		table.add(backButton);
	}
	
}
