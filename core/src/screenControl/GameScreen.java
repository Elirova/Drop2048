package screenControl;

import java.util.Random;

import screenControl.GameSelectScreen.Status;
import Entities.Block;
import Entities.Block.Type;
import Entities.Player;
import PopUps.GamePausePopUp;
import PopUps.WinLosePopUp;
import ProfileSettings.Profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import drop2048.Drop2048;
import drop2048.MenuBg;

public class GameScreen extends AbstractScreen {
	private Status status;
	private GamePausePopUp pauseMenu;
	private WinLosePopUp winLosePopUp;
	private Player player;
	protected MenuBg menu;
	private int h, w, cont;
	private float velocity, prevel;
	private float time, timeSlow;
	Random random;
	private Label score, maxScore;
	private int scoreint, maxScoreint;
	private boolean slow;
	
	private static TextButton menuButton;

    public GameScreen(Status status) {     
            super();
            setBackground("background/gamebg.png");
            this.status = status;
            h = Gdx.graphics.getHeight();
        	w = Gdx.graphics.getWidth();
        	time = cont = 0;
        	timeSlow = prevel = 0;
        	slow = false;
        	random = new Random();
        	
            // Inicializaciones de clase
            GamePausePopUp.setScreen(this);
            Block.setGameScreen(this);
            Block.resetPositions();
            
            // Inicialización de la zona de menú y puntuaciones
            menu = new MenuBg(true); 
            bg.setZIndex(0);
            menu.setZIndex(4);
        	stage.addActor(menu);
        	
        	scoreint = 0;
        	maxScoreint = Profile.getMaxScore(status);
        	score = new Label(String.valueOf(0), getSkin());// new Score(AbstractScreen.font);
        	maxScore = new Label(String.valueOf(maxScoreint), getSkin());//new Score(Profile.getMaxScore(status), AbstractScreen.font);
        	
        	// Inicialización de entidades   
            createPlayer();
            velocity = status.getVelocity();
        	initializeTable();
            
            // Inicialización de popUps y camaras
            pauseMenu = new GamePausePopUp(stage.getBatch());
            winLosePopUp = new WinLosePopUp(stage.getBatch());
            batch.setProjectionMatrix(stage.getCamera().combined);
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
			case EXTREME:
				int n = random.nextInt(3) + 2;
				return (n == 4)? 5 : n; // 2, 3 o 5
			default:
				return 2;
    	}
    }
    
    public Block getNewBlock() {
    	int rand = random.nextInt(100);
    	
    	if(rand < 3) return new Block(Type.VELDEC);
    	else if(rand < 7) return new Block(Type.VELINC);
    	else if(rand < 8) return new Block(Type.RANDOM);
    	else if(rand < 9) return new Block(Type.RESET);
    	else if(rand < 10) return new Block(Type.SLOW);
		
    	Block block = getNewNumberBlock();
		if(block == player) cont = 0;
		return block;
    }
    
    public Block getNewNumberBlock() {
    	int interval = status.getInterval();
    	int mult = random.nextInt(interval) - interval/2 + player.getCont();
    	int color = player.getNumberColor();
    	if(mult <= 0) {
    		if(color == 0) mult = 1;
    		else {
    			color--;
    			mult = 11+mult;
    		}
    	} else if(mult > 11) {
    		mult = mult%11;
    		color += 1;
    	}

    	return new Block(Type.NUMBER, getRandom(), mult, color);
    }
    
    private Block getSameBlock() {
    	return new Block(Type.NUMBER, player.getBase(), player.getPow(), player.getNumberColor());
    }
    
    public void winGame() {
    	winLosePopUp.show(true);
    	setPause(true);
    	Profile.setScore(status, scoreint, player.getPow(), player.getNumberColor());
    	Drop2048.save();
    }
    
    public void loseGame() {
    	time=0;
    	winLosePopUp.show(false);
    	setPause(true);
    	Profile.setScore(status, scoreint, player.getPow(), player.getNumberColor());
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
    	block.setZIndex(1);
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
    	if(slow) {
    		timeSlow -= delta;
    		if(timeSlow <= 0) {
    			slow = false;
    			velocity = prevel;
    		}
    	}
    }
    
    public void setSlow(int time) {
    	slow = true;
    	prevel = velocity;
    	timeSlow = time;
    	velocity = status.getVelocity();
    }
    
    private void checkForBlock() {
    	float change = 5 - velocity*0.01f;
    	if(time >= change) {
			time -= change;
			if(Block.getFree() > status.getMinFree()) {
				cont++;
				if(cont == Block.NUMBLOCKS) {
					addBlock(getSameBlock());
					cont = 0;
				}
				else addBlock(getNewBlock()); // Queda más de un hueco libre
			}
		}
    }
    
    private void initializeTable() {
    	// Inicialize buttons
		menuButton = new TextButton("Menu", skin);
    	
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
        table.center().bottom();
        table.add().width(w*0.3f);
        table.add().width(w*0.3f);
        table.add().width(w*0.3f);
        table.row();
        table.setZIndex(2);
        table.add("Score", "white");
        table.add();
        table.add("Best", "white");
        table.row();
        table.add(score);
        table.add(menuButton).size(w*0.2f, h*0.05f);
        table.add(maxScore);
        table.row();
        table.add().height(heightAd+h*0.01f);
    }
    
    public Player getPlayer(){
    	return player;
    }

    public int getScore() {
    	return scoreint;
    }
    
    public void setScore(int score) {
    	scoreint = score;
    	this.score.setText(String.valueOf(score));
    }
    
    public void addScore(int score) {
    	scoreint += score;
    	this.score.setText(String.valueOf(scoreint));
    	if(scoreint > maxScoreint) {
    		maxScoreint = scoreint;
    		maxScore.setText(String.valueOf(scoreint));
    	}
    }
    
    public Status getStatus() {
    	return status;
    }
    
    public float getVelocity() {
		return velocity;
	}
    
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	public void changeVelocity(float change) {
		if(!slow) {
			this.velocity += change;
			if(velocity < status.getVelocity()) velocity = status.getVelocity();
		}
	}
	
	public void updateVelocity() {
		changeVelocity(status.getIncreaseVelocity());
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
    	winLosePopUp.resize(width, height);
	}
}
