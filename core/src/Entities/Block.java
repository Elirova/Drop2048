package Entities;

import screenControl.AbstractScreen;
import screenControl.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import drop2048.Drop2048;

public class Block extends Actor {
	public enum Type {NUMBER, VELINC, VELDEC, RESET, RANDOM, EXTRA, BOMB}
	
	// Variables estáticas
	protected static int NUMBLOCKS = 5, SCORESPECIAL = 20, free;
	protected static Player player;
	protected static Texture blockTexture;
    protected static NinePatch[] bgBlocks;
    protected static int size, h, w, sizeSmall;
    protected static boolean pos[]; // Posiciones libres en las que puede aparecen un bloque
    protected static GameScreen game;
    protected static BitmapFont font;
    
    // Variables de objeto
	protected int base, pow, number, color;
	protected Rectangle bounds;
	protected TextureRegion img;
    protected NinePatch bg1, bg2;
	protected Type type;
	protected float scaleFont;
	
	private int position;
    
    public Block(Type type, int base, int pow, int color) {
    	this.type = type;
    	if(pow>11) color++;
    	this.pow = pow%12;
    	this.base = base;
    	this.number = (int) Math.pow(base, this.pow);
    	this.color = color;
    	String num = String.valueOf(number);
    	float width = font.getBounds(num).width;
    	scaleFont  = (sizeSmall < width)? (sizeSmall*0.8f)/(width*num.length()) : (sizeSmall*0.65f)/80f;
    	createBlock();
    	
    	if(this instanceof Player) {
    		setBounds((w*0.5f)-(size/2f), Drop2048.myRequestHandler.getHeightAd()+h*0.15f, size, size);
    	} else {
    		locateBlock();
    	}
    }
    
    public Block(Type type) {
    	this(type, 2, 1, 0);
    }
    
    public Block(int base, int mult, int color) {
    	this(Type.NUMBER, base, mult, color);
    }
    
    private void locateBlock() {
    	free--; // Queda una posición menos libre
    	position = (int)(Math.random()*(NUMBLOCKS*2))%NUMBLOCKS; // Genera un número aleatorio entre 0 y NUMBLOCKS-1;
    	boolean found = false;
    	while(!found) {
	    	if(pos[position]) {
	    		found = true;
	    		pos[position] = false;
	    		bounds = new Rectangle(((w*0.04f*(position+1))+(size*position)), h, size, size);
	    	} else position = (position + 1) % NUMBLOCKS;
    	}
    }
    
    private void createBlock() {
    	switch(type) {
    		case NUMBER:
    			bg1 = bgBlocks[color];
    			bg2 = bgBlocks[pow];
    			break;
			case BOMB:
				bg1 = bgBlocks[16];
				img = new TextureRegion(blockTexture, 28, 0, 32, 32);
				break;
			case EXTRA:
				bg1 = bgBlocks[15];
    			img = new TextureRegion(blockTexture, 28, 32, 32, 32);
				break;
			case RANDOM:
				bg1 = bgBlocks[14];
    			img = new TextureRegion(blockTexture, 60, 0, 32, 32);
				break;
			case RESET:
    			bg1 = bgBlocks[13];
    			img = new TextureRegion(blockTexture, 60, 32, 32, 32);
				break;
			case VELDEC:
    			bg1 = bgBlocks[12];
    			img = new TextureRegion(blockTexture, 92, 0, 32, 32);
				break;
			case VELINC:
    			bg1 = bgBlocks[11];
    			img = new TextureRegion(blockTexture, 92, 32, 32, 32);
				break;
			default:
				break;
    	}
    }
    
    public static NinePatch[] getBgBlocks() {
    	return bgBlocks;
    }
    
    public static void setGameScreen(GameScreen game) {
    	Block.game = game;
    	Block.player = game.getPlayer();
    }
    
    public static void initialize() {
    	Block.free = NUMBLOCKS;
    	Block.blockTexture = new Texture(Gdx.files.internal("Images/blocks.png"));
    	bgBlocks = new NinePatch[18];
    	int cont = 0;
    	for(int j = 0; j < 6; j++) {
    		for(int i = 0; i < 3; i++, cont++) {
    			bgBlocks[cont] = new NinePatch(new TextureRegion(blockTexture, i*9, j*9, 9, 9), 4, 4, 4, 4);
    		}
    	}
    	h = Gdx.graphics.getHeight();
    	w = Gdx.graphics.getWidth();
    	size = (int) (w*((1-(0.04f*(NUMBLOCKS+1)))/NUMBLOCKS));
    	sizeSmall = (int) (size*0.9f);
    	pos = new boolean[NUMBLOCKS];
    	for(int i = 0; i < NUMBLOCKS; i++) {
    		pos[i] = true;
    	}
    	font = AbstractScreen.getFont();
    }
    
    public static void setEnemy(Player block) {
    	Block.player = block;
    }
    
    public static int getFree() {
    	return free;
    }
    
    public boolean collides() {
    	return player.getBounds().overlaps(bounds);
	}
    
    public void apply() {
    	switch(type) {
			case BOMB: // Quitar todos los bloques de la escena
				game.addScore(SCORESPECIAL);
				break;
			case EXTRA: // Añadir un segundo bloque de jugador
				game.addScore(SCORESPECIAL);
				break;
			case NUMBER:
				if(number == player.getNumber()) player.updateNumber();
				else game.loseGame();// Pierdes la partida
				break;
			case RESET: // Te vuelve a un número primo de tu dificultad
				player.setNumber(game.getRandom(), 1, 0);
				game.addScore(SCORESPECIAL);
				break;
			case RANDOM: // Te da un número aleatorio
				player.setNumber(game.getRandom(), 1 + (int)(Math.random()*11), 0); // Te da un número aleatorio posible
				game.addScore(SCORESPECIAL);
				break;
			case VELDEC: // Baja la velocidad
				game.changeVelocity(game.getStatus().getIncreaseVelocity()*(-10));
				game.addScore(SCORESPECIAL);
				break;
			case VELINC: // Sube la velocidad
				game.changeVelocity(game.getStatus().getIncreaseVelocity()*15);
				game.addScore(SCORESPECIAL);
				break;
			default:
				break;
    	
    	}
    }
    
	public int getNumber() {
		return number;
	}

	public void resetNumber(int number) {
		this.number = this.base = number;
		pow = 1;
		color = 0;
		bg1 = bgBlocks[color];
		bg2 = bgBlocks[pow];
	}

	public void setNumber(int base, int mult, int color) {
		this.number = (int) Math.pow(base, mult);
		this.base = base;
		this.color = color;
		pow = mult;
		bg1 = bgBlocks[color];
		bg2 = bgBlocks[pow];
	}
	
	public int getCont() {
		return pow;
	}
	
	public int getNumberColor() {
		return color;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public static void dispose() {
		if(blockTexture != null) blockTexture.dispose();
	}
	
	//Métodos act y draw
    @Override
    public void act(float delta){
    	bounds.y = bounds.y - delta*game.getVelocity();
    	if(position != -1 && bounds.y <= h*0.75f) {
    		free++; // Queda una posición más libre
    		pos[position] = true;
    		position = -1;
    	}
    	if (bounds.y <= Drop2048.myRequestHandler.getHeightAd()+h*0.15f+size && collides()) {
    		apply();
    		game.removeEntity(this);
    	} else if(bounds.y < h*0.1f) dead();
    }
    
    public void dead() {
    	game.removeEntity(this);
    	game.updateVelocity();
    	game.addScore(1);
    }
    
    @Override
	public void draw(Batch batch, float parentAlpha){
    	bg1.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
    	if(type == Type.NUMBER) {
    		bg2.draw(batch, bounds.x+size*0.05f, bounds.y+size*0.05f, sizeSmall, sizeSmall);
    		float scale = font.getScaleY();
    		font.setScale(scaleFont);
    		font.drawMultiLine(batch, String.valueOf(number), bounds.x, bounds.y + size/2 + font.getCapHeight()/2, size, BitmapFont.HAlignment.CENTER);
    		font.setScale(scale);
    	} else {
    		batch.draw(img, bounds.x, bounds.y, bounds.getWidth()/2, bounds.getHeight()/2, 
            		bounds.getWidth(), bounds.getHeight(), 1, 1, getRotation());
    	}
    }
}
