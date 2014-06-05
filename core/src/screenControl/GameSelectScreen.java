package screenControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameSelectScreen  extends AbstractScreen {
	public enum Status {
		EASY(50,5), NORMAL(65,5), HARD(65,3), HARDCORE(70,3);
		
		int velocity, interval;
		
		private Status(int velocity, int interval) {
			this.velocity = velocity;
			this.interval = interval;
		}
		
		public int getVelocity() {
			return velocity;
		}
		
		public int getInterval() {
			return interval;
		}
	}
	private TextButton bEasy, bNormal, bHard, bHardcore;
	
	public GameSelectScreen() {
		super();
		setBackground("background/bg.png");
        createButtons();
        createTable();
	}

	private void createButtons() {
		bEasy = new TextButton("Easy", getSkin());
		bNormal = new TextButton("Normal", getSkin());
		bHard = new TextButton("Hard", getSkin());
		bHardcore = new TextButton("Hardcore", getSkin());
		
		bEasy.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {                   
		    	game.setScreen(new GameScreen(Status.EASY));
		        return false;
		    } } ); 
		
		bNormal.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {                   
		    	game.setScreen(new GameScreen(Status.NORMAL));
		        return false;
		    } } );
		
		bHard.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {                   
		    	game.setScreen(new GameScreen(Status.HARD));
		        return false;
		    } } );
		
		bHardcore.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {                   
		    	game.setScreen(new GameScreen(Status.HARDCORE));
		        return false;
		    } } );
	}
	
	private void createTable(){
		table = super.getTable();
    	int h = Gdx.graphics.getHeight();
    	
        table.add().spaceBottom(h*0.1f);
        table.row();
        table.add(bEasy);
        table.row();
        table.add(bNormal);
        table.row();
        table.add(bHard);
        table.row();
        table.add(bHardcore);
        table.row();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
