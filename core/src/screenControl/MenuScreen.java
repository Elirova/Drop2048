package screenControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends AbstractScreen {
	private TextButton startGameButton;
	private TextButton infoButton;
	private TextButton scoresButton;
	private TextButton exitButton;
	private Texture texLogo;
	private Image logo;
	
    public MenuScreen() {       
            super();
            setBackground("background/bg.png");
            createButtons();
            createTable();
            texLogo = new Texture(Gdx.files.internal("Images/logo.png"));
            logo = new Image(texLogo);
            stage.addActor(logo);
            float height = h*0.3f;
            float width = (texLogo.getWidth()*height)/texLogo.getHeight();
            logo.setBounds((w-width)/2, h*0.65f, width, height);
    }
    
    private void createButtons() {
    	// Inicialize buttons
        startGameButton = new TextButton("Play", getSkin(), "green");
        scoresButton = new TextButton("Scores", getSkin());
        infoButton = new TextButton("Instructions", getSkin());
        exitButton = new TextButton("Exit", getSkin()); 
        
        // Add listeners
		startGameButton.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {
		    	game.setScreen(new GameSelectScreen());
		        return false;
		    } } ); 
		
		infoButton.addListener(new InputListener() { 
		    @Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		        game.setScreen(new TutorialScreen()); 
		        return false;
		    } } ); 
		
		scoresButton.addListener(new InputListener() { 
		    @Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		        game.setScreen(new ScoreScreen());
		        return false;
		    } 
		} ); 
		
		exitButton.addListener(new InputListener() { 
		    @Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		        Gdx.app.exit();
		        return false;
		    } 
		} );
    }
    
    private void createTable(){
    	table = super.getTable();
    	table.center().bottom();
		table.add(startGameButton).size(w, h*0.1f).spaceBottom(h*0.03f); 
		table.row();
		table.add(scoresButton).size(w, h*0.1f).spaceBottom(h*0.03f);
		table.row(); 
		table.add(infoButton).size(w, h*0.1f).spaceBottom(h*0.03f);
		table.row();
		table.add(exitButton).size(w, h*0.1f);
		table.row();
		table.add().size(0, h*0.15f);
    }
    
    
    
    @Override
    public void dispose() {
    	super.dispose();
    	texLogo.dispose();
    }
}