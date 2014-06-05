package screenControl;

import java.util.Random;

import Entities.Block;
import Entities.Block.Type;
import Entities.Player;
import PopUps.GamePausePopUp;
import PopUps.WinLosePopUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import drop2048.Background;
import drop2048.Drop2048;
import drop2048.Score;

public class GameScreen extends AbstractScreen {
	private GameSelectScreen.Status status;
	private GamePausePopUp pauseMenu;
	private WinLosePopUp winLosePopUp;
	private Player player;
	private Score score, maxScore;
	protected Texture bgTexture;
	protected Background bg;
	private int h, w;
	private float velocity;
	private float time;
	Random random;
	
	private static TextButton menuButton;

    public GameScreen(GameSelectScreen.Status status) {     
            super();
            setBackground("background/gamebg.png");
            this.status = status;
            h = Gdx.graphics.getHeight();
        	w = Gdx.graphics.getWidth();
        	time = 0;
        	random = new Random();
        	
            // Inicializaciones de clase
            GamePausePopUp.setScreen(this);
            Block.initialize(this);
            
            // Inicialización de la zona de menú y puntuaciones
        	bgTexture = new Texture(Gdx.files.internal("Images/popup.png"));
        	bg = new Background(new NinePatch(new TextureRegion(bgTexture, 0, 0, 32, 32), 14, 14, 14, 14), 
        			0, h*0.1f, w, h*0.1f); 
        	stage.addActor(bg);
        	score = new Score(AbstractScreen.font);
        	maxScore = new Score(Drop2048.profile.getMaxScore(status), AbstractScreen.font);
        	
        	// Inicialización de entidades   
            createPlayer();
            velocity = status.getVelocity();
        	initializeTable();
            
            // Inicialización de popUps y camaras
            pauseMenu = new GamePausePopUp(stage.getBatch());
            winLosePopUp = new WinLosePopUp(stage.getBatch());
            batch.setProjectionMatrix(stage.getCamera().combined);
            
            
            addEntity(new Block(Type.BOMB));

    }
    
    private void createPlayer() {
    	player = new Player(getRandom());
    	
    	stage.addActor(player);   
    	Block.setEnemy(player);
    	
    	player.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y,
                    int pointer) {
            	player.move(x);
                super.touchDragged(event, x, 0, pointer);
            }
        });
    }
    
    public int getRandom() {
    	switch(status) {
			case HARD:
				return random.nextInt(2) + 2; // 2 o 3
			case HARDCORE:
				int n = random.nextInt(3) + 2;
				return (n == 4)? 5 : n; // 2, 3 o 5
			default:
				return 2;
    	}
    }
    
    public Block getNewBlock() {
    	int rand = 1 + (int)(Math.random()*50);
    	switch(rand) {
	    	case 1:
	    	case 2:
	    		return new Block(Type.VELDEC);
	    	case 3:
	    	case 4:
	    		return new Block(Type.VELINC);
	    	case 5:
	    		return new Block(Type.RANDOM);
	    	case 6:
	    		return new Block(Type.RESET);
	    	default:
	    		return getNewNumberBlock();
    	}
    }
    
    public Block getNewNumberBlock() {
    	int interval = status.getInterval();
    	int mult = random.nextInt(interval) - interval/2 + player.getCont();
    	if(mult <= 0) mult = 1;

    	return new Block(Type.NUMBER, getRandom(), mult);
    }
    
    public void winGame() {
    	winLosePopUp.show(true);
    	setPause(true);
    	Drop2048.profile.setScore(score.getScore(), status);
    	Drop2048.save();
    }
    
    public void loseGame() {
    	winLosePopUp.show(false);
    	setPause(true);
    	Drop2048.profile.setScore(score.getScore(), status);
    	Drop2048.save();
    }
    
    public void setGamePause(boolean pause) {
    	setPause(pause);
    	if(pause) pauseMenu.show();
    	else pauseMenu.close();
    }
    
    @Override
    public void setPause(boolean pause) {
    	super.setPause(pause);
    	if(!pause) Gdx.input.setInputProcessor(stage);
    }
    
    public void addBlock(Block block) {
    	stage.addActor(block);
    }
    
    @Override
    public void render(float delta) {
    	if(!pause) gameRender(delta);
    	else if(pauseMenu.isVisible()) pauseMenu.draw(delta);
    	else if(winLosePopUp.isVisible()) winLosePopUp.draw(delta);
    }
    
    private void gameRender(float delta) {
    	super.render(delta);
    	time += delta;
    	checkForBlock();
//    	stage.getBatch().begin();
//        	getFont().draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 30);
//    	stage.getBatch().end();
    }
    
    private void checkForBlock() {
    	float change = 5 - velocity*0.03f;
    	if(time >= change) {
			time -= change;
			addBlock(getNewBlock());
		}
    }
    
    private void initializeTable() {
    	// Inicialize buttons
		menuButton = new TextButton("...", skin);
    	
        // Add listeners to buttons
        menuButton.addListener(new InputListener() { 
		    @Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		    	setGamePause(true);
		        return false;
		    } 
		} );
        
        //Add buttons to the table
        getTable();
        table.bottom();
//        table.add().height(h*0.8f);
//        table.row();
        table.add(score).width(w*0.3f).height(h*0.1f).uniform().spaceLeft(w*0.1f);
    	table.add(menuButton).size(w*0.1f, w*0.1f).spaceRight(w*0.1f).spaceLeft(w*0.1f).center();
        table.add(maxScore).width(w*0.3f).height(h*0.1f).spaceRight(w*0.1f);
        table.row();
        table.add().height(h*0.1f);
    }
    
    public Player getPlayer(){
    	return player;
    }
    public float getVelocity() {
		return velocity;
	}

    public int getScore() {
    	return score.getScore();
    }
    
    public void setScore(int score) {
    	this.score.setScore(score);
    }
    
    public void addScore(int score) {
    	this. score.addScore(score);
    }
    
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	public void changeVelocity(float change) {
		this.velocity+=change;
		if(velocity<10) velocity = 20;
	}

	public void addEntity(Block block) {
		stage.getRoot().addActorAt(0, block);
	}
	
	public void removeEntity(Block block) {
    	getStage().getRoot().removeActor(block);
    }
	
   
    @Override
	public void resize(int width, int height) {
    	super.resize(width, height);
    	pauseMenu.resize(width, height);
	}
    
    @Override
	public void dispose() {

	}
}
