package screenControl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameSelectScreen  extends AbstractScreen {
	public enum Status {
		EASY(h*0.15f, h*0.0012f, 5, "Easy", 3), 
		NORMAL(h*0.2f, h*0.0013f, 5, "Normal", 2), 
		HARD(h*0.24f, h*0.0015f, 3, "Hard", 1),
		EXTREME(h*0.28f, h*0.0017f, 3, "Extreme", 1);
		
		int velocity, interval, minFree;
		float incVel;
		String name;
		
		private Status(float velocity, float incVel, int interval, String name, int minFree) {
			this.velocity = (int) velocity;
			this.incVel = incVel;
			this.interval = interval;
			this.name = name;
			this.minFree = minFree;
		}
		
		/**
		 * Devuelve el número de columnas que debe de haber siempre libres.
		 * @return
		 */
		public int getMinFree() {
			return minFree;
		}
		
		/**
		 * Devuelve el nombre de la dificultad.
		 * @return
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Devuelve la velocidad inicial de la dificultad.
		 * @return
		 */
		public int getVelocity() {
			return velocity;
		}
		
		/**
		 * Devuelve el incremento de velocidad por bloque.
		 * @return
		 */
		public float getIncreaseVelocity() {
			return incVel;
		}
		
		/**
		 * Devuelve el intervalo de números que aparecerán con respecto al número del jugador.
		 * @return
		 */
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
		bEasy = new TextButton(Status.EASY.getName(), getSkin(), "red1");
		bNormal = new TextButton(Status.NORMAL.getName(), getSkin(), "red2");
		bHard = new TextButton(Status.HARD.getName(), getSkin(), "red3");
		bHardcore = new TextButton(Status.EXTREME.getName(), getSkin(), "red4");
		
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
		    	game.setScreen(new GameScreen(Status.EXTREME));
		        return false;
		    } } );
	}
	
	private void createTable(){
		table = super.getTable();

        table.add(bEasy).size(w, h*0.1f).spaceBottom(h*0.03f);
        table.row();
        table.add(bNormal).size(w, h*0.1f).spaceBottom(h*0.03f);
        table.row();
        table.add(bHard).size(w, h*0.1f).spaceBottom(h*0.03f);
        table.row();
        table.add(bHardcore).size(w, h*0.1f);
        table.row();
		table.add().height(h*0.15f);
		table.bottom();
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
