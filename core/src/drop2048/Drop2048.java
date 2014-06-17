package drop2048;

import screenControl.AbstractScreen;
import screenControl.MenuScreen;
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
	public static IActivityRequestHandler myRequestHandler;

    public Drop2048(IActivityRequestHandler handler) {
        myRequestHandler = handler;
    }
	@Override
	public void create() {		
		Gdx.app.log( Drop2048.LOG, "Creating game" ); 
		load();
		PopUp.setGame(this);
		AbstractScreen.setGame(this);
		Block.initialize();
		setScreen( new MenuScreen());
//		setScreen( new GameScreen(Status.EASY));
		myRequestHandler.showAds(true);
	}

	public static void save() {
		if(profile == null) profile = new Profile();
		ProfileSerializer.write(profile);
	}
	
	public static void load() {
		profile = ProfileSerializer.read();
	}
	
	public static void clearProfile() {
		
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
		//isGooglePlayServicesAvailable();
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
