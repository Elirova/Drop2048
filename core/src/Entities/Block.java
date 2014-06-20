package Entities;

import screenControl.AbstractScreen;
import screenControl.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import drop2048.Drop2048;

public class Block extends Actor implements Comparable<Block> {
	public enum Type {NUMBER, VELINC, VELDEC, RESET, RANDOM, SLOW, BOMB}
	
	// Variables estáticas
	public static int NUMBLOCKS = 5, SCORESPECIAL = 20, free;
	protected static Player player;
	protected static Texture blockTexture;
    protected static NinePatch[] bgBlocks, bgColors;
    protected static int size = 1, h, w, sizeSmall;
    protected static boolean pos[]; // Posiciones libres en las que puede aparecen un bloque
    protected static GameScreen game;
    protected static BitmapFont font;
    
    // Variables de objeto
	protected int base, pow, number, color;
	protected Rectangle bounds;
	protected TextureRegion img;
    protected NinePatch bgBlock, bgColor;
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
    	
    	scaleFont = calculateScaleFont(font, number, sizeSmall);
    	createBlock();
    	
    	if(this instanceof Player) {
    		setBounds((w*0.5f)-(size/2f), Drop2048.myRequestHandler.getHeightAd()+h*0.15f, size, size);
    	} else if(!(this instanceof ScoreBlock)) {
    		locateBlock();
    	}
    }
    
    public Block(Type type) {
    	this(type, 2, 1, 0);
    }
    
    public Block(int base, int mult, int color) {
    	this(Type.NUMBER, base, mult, color);
    }
    
    protected float calculateScaleFont(BitmapFont font, int number, int size) {
    	float width = font.getBounds(String.valueOf(number)).width;
    	return (width > size*0.8f)? 
    			(size*0.9f*font.getScaleX())/font.getBounds(String.valueOf(number)).width 
    			: (size*0.5f*font.getScaleY())/font.getCapHeight();
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
    
    protected void createBlock() {
    	switch(type) {
    		case NUMBER:
    			bgColor = bgColors[color];
    			bgBlock = bgBlocks[pow-1];
    			break;
			case VELINC:
				bgColor = bgBlocks[14];
    			img = new TextureRegion(blockTexture, 64, 0, 64, 64);
				break;
			case RESET:
				bgColor = bgBlocks[13];
    			img = new TextureRegion(blockTexture, 64, 64, 64, 64);
				break;
			case VELDEC:
				bgColor = bgBlocks[12];
    			img = new TextureRegion(blockTexture, 128, 0, 64, 64);
				break;
			case RANDOM:
				bgColor = bgBlocks[11];
    			img = new TextureRegion(blockTexture, 128, 64, 64, 64);
				break;
			case SLOW:
				bgColor = bgBlocks[15];
				img = new TextureRegion(blockTexture, 192, 0, 64, 64);
			default:
				break;
    	}
    }
    
    public static NinePatch[] getBgBlocks() {
    	return bgBlocks;
    }
    
    public static NinePatch[] getBgColors() {
    	return bgColors;
    }
    
    public static void setGameScreen(GameScreen game) {
    	Block.game = game;
    	Block.player = game.getPlayer();
    	Block.free = NUMBLOCKS;
    }
    
    public static void initialize() {
    	Block.blockTexture = new Texture(Gdx.files.internal("Images/blocks.png"));
    	Block.blockTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	bgBlocks = new NinePatch[18];
    	bgColors = new NinePatch[18];
    	int cont = 0;
    	for(int j = 0; j < 6; j++) {
    		for(int i = 0; i < 3; i++, cont++) {
    			bgBlocks[cont] = new NinePatch(new TextureRegion(blockTexture, i*9, j*9, 9, 9), 4, 4, 4, 4);
    			bgColors[cont] = new NinePatch(new TextureRegion(blockTexture, i*9+32, j*9, 9, 9), 4, 4, 4, 4);
    		}
    	}
    	h = Gdx.graphics.getHeight();
    	w = Gdx.graphics.getWidth();
    	size = (int) (w*((1-(0.04f*(NUMBLOCKS+1)))/NUMBLOCKS));
    	sizeSmall = (int) (size*0.9f);
    	pos = new boolean[NUMBLOCKS];
    	resetPositions();
    	font = AbstractScreen.getFont();
    }
    
    public static void resetPositions() {
    	for(int i = 0; i < NUMBLOCKS; i++) {
    		pos[i] = true;
    	}
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
			case NUMBER:
				if(number == player.getNumber()) player.updateNumber();
				else game.loseGame();// Pierdes la partida
				break;
			case RESET: // Te vuelve a un número primo de tu dificultad
				player.setNumber(game.getRandom(), 1, 0);
				game.setVelocity(game.getStatus().getVelocity());
				break;
			case RANDOM: // Te da un número aleatorio
				player.setNumber(game.getRandom(), (int)(1 + Math.random()*10), 
						player.getNumberColor()); // Te da un número aleatorio posible
				game.addScore(SCORESPECIAL/2);
				break;
			case VELDEC: // Baja la velocidad
				game.changeVelocity(game.getStatus().getIncreaseVelocity()*(-10));
				break;
			case VELINC: // Sube la velocidad
				game.changeVelocity(game.getStatus().getIncreaseVelocity()*15);
				game.addScore(SCORESPECIAL);
				break;
			case SLOW: // Sube la velocidad
				game.setSlow(10);
				game.addScore(SCORESPECIAL/2);
				break;
			default:
				break;
    	
    	}
    }
    
	public int getNumber() {
		return number;
	}

	public void setNumber(int base, int mult, int color) {
		this.number = (int) Math.pow(base, mult);
		this.base = base;
		this.color = color;
		pow = mult;
		bgColor = bgColors[color];
		bgBlock = bgBlocks[pow-1];
		scaleFont  = calculateScaleFont(font, number, size);
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

	public int getPow() {
		return pow;
	}

	public int getBase() {
		return base;
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
    	} else if(bounds.y < Drop2048.myRequestHandler.getHeightAd()) dead();
    }
    
    public void dead() {
    	game.removeEntity(this);
    	game.updateVelocity();
    	game.addScore(1);
    }
    
    @Override
	public void draw(Batch batch, float parentAlpha){
    	bgColor.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
    	if(type == Type.NUMBER) {
    		bgBlock.draw(batch, bounds.x+size*0.05f, bounds.y+size*0.05f, sizeSmall, sizeSmall);
    		float scale = font.getScaleY();
    		font.setScale(scaleFont);
    		font.drawMultiLine(batch, String.valueOf(number), bounds.x, bounds.y + size*0.55f + font.getCapHeight()/2, size, BitmapFont.HAlignment.CENTER);
    		font.setScale(scale);
    	} else {
    		batch.draw(img, bounds.x, bounds.y, bounds.getWidth()/2, bounds.getHeight()/2, 
            		bounds.getWidth(), bounds.getHeight(), 1, 1, getRotation());
    	}
    }

	@Override
	public int compareTo(Block block) {
		return(block.getBase()==this.getBase() && block.getPow() == this.getPow() && block.getNumberColor() == this.getNumberColor())? 1 : -1;
	}
}
