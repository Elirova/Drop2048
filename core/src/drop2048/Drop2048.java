package drop2048;

import screenControl.AbstractScreen;
import screenControl.GameScreen;
import screenControl.GameSelectScreen.Status;
import Entities.Block;
import PopUps.PopUp;
import ProfileSettings.Profile;
import ProfileSettings.ProfileSerializer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Drop2048 extends Game implements ApplicationListener  {	
	public static final String LOG = Drop2048.class.getSimpleName();
	public static Profile profile;
	
	@Override
	public void create() {		
		Gdx.app.log( Drop2048.LOG, "Creating game" ); 
		load();
		PopUp.setGame(this);
		AbstractScreen.setGame(this);
    	setScreen( new GameScreen(Status.EASY));
	}

	public static void save() {
		ProfileSerializer.write(profile);
	}
	public static void load() {
		profile = ProfileSerializer.read();
	}
	
	@Override
	public void dispose() {
		save();
		AbstractScreen.disposeStatic();
		Block.dispose();
		super.dispose();
        Gdx.app.log( Drop2048.LOG, "Disposing game" );
	}

	@Override
	public void render() {	
		super.render();
		getScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		super.resize( width, height );
        Gdx.app.log( Drop2048.LOG, "Resizing game to: " + width + " x " + height ); 
	}

	@Override
	public void pause() {
		super.pause();
		//save();
        Gdx.app.log( Drop2048.LOG, "Pausing game" ); 
	}

	@Override
	public void resume() {
		super.resume();
		AbstractScreen.load();
		//load();
        Gdx.app.log( Drop2048.LOG, "Resuming game" );
	}
	
	@Override 
	public void setScreen( Screen screen ) { 
        super.setScreen( screen ); 
        Gdx.app.log( Drop2048.LOG, "Setting screen: " + screen.getClass().getSimpleName() ); 
    }
}