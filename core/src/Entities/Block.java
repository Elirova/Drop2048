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

public class Block extends Actor {
	public enum Type {NUMBER, VELINC, VELDEC, RESET, RANDOM, EXTRA, BOMB}
	
	// Variables estáticas
	protected static Player player;
	protected static Texture blockTexture;
    protected static NinePatch[] bgBlocks;
    protected static int size, h, w;
    protected static boolean pos[]; // Posiciones libres en las que puede aparecen un bloque
    protected static GameScreen game;
    protected BitmapFont font;
    
    // Variables de objeto
    
	protected int base, pow, number;
	protected Rectangle bounds;
	protected TextureRegion img;
    protected NinePatch bg;
	protected Type type;
	
	private int position;
    
    public Block(Type type, int base, int mult) {
    	this.type = type;
    	this.number = (int) Math.pow(base, mult);
    	pow = mult;
    	this.base = base;
    	createBlock();
    	font = AbstractScreen.getFont();
    	float width = font.getBounds(String.valueOf(number)).width;
		font.setScale(width < size ? size/60f : size/width);
    	if(this instanceof Player) {
    		bounds = new Rectangle(w*0.5f-size/2, h*0.22f, size, size);
    	} else {
    		locateBlock();
    	}
    }
    
    public Block(Type type) {
    	this(type, 2, 1);
    }
    
    public Block(int base, int mult) {
    	this(Type.NUMBER, base, mult);
    }
    
    private void locateBlock() {
    	int random = (int)(Math.random()*4); // Genera un número aleatorio entre 0 y 4;
    	boolean found = false;
    	while(!found) {
	    	if(pos[random]) {
	    		found = true;
	    		pos[random] = false;
	    		position = random;
//	    		System.out.println("Bloque en posición " + position);
		    	switch(random) {
		    		case 0:
		    			bounds = new Rectangle(w*0.04f, h, size, size);
		    			break;
		    		case 1:
		    			bounds = new Rectangle(w*0.23f, h, size, size);
		    			break;
		    		case 2:
		    			bounds = new Rectangle(w*0.42f, h, size, size);
		    			break;
		    		case 3:
		    			bounds = new Rectangle(w*0.61f, h, size, size);
		    			break;
		    		default:
		    			bounds = new Rectangle(w*0.8f , h, size, size);
		    			break;
		    	}
	    	} else {
	    		random = (random + 1) % 5;
	    	}
    	}
    }
    
    private void createBlock() {
    	switch(type) {
    		case NUMBER:
    			bg = bgBlocks[(pow-1)%11];
    			break;
			case BOMB:
				bg = bgBlocks[16];
				img = new TextureRegion(blockTexture, 28, 0, 32, 32);
				break;
			case EXTRA:
    			bg = bgBlocks[15];
    			img = new TextureRegion(blockTexture, 28, 32, 32, 32);
				break;
			case RANDOM:
    			bg = bgBlocks[14];
    			img = new TextureRegion(blockTexture, 60, 0, 32, 32);
				break;
			case RESET:
    			bg = bgBlocks[13];
    			img = new TextureRegion(blockTexture, 60, 32, 32, 32);
				break;
			case VELDEC:
    			bg = bgBlocks[12];
    			img = new TextureRegion(blockTexture, 92, 0, 32, 32);
				break;
			case VELINC:
    			bg = bgBlocks[11];
    			img = new TextureRegion(blockTexture, 92, 32, 32, 32);
				break;
			default:
				break;
    	}
    }
    
    public static void initialize(GameScreen game) {
    	Block.game = game;
//    	Block.font = AbstractScreen.getFont();
    	Block.player = game.getPlayer();
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
    	size = (int) (w*0.15f);
    	pos = new boolean[5];
    	for(int i = 0; i < 5; i++) {
    		pos[i] = true;
    	}
    }
    
    public static void setEnemy(Player block) {
    	Block.player = block;
    }
    
    public boolean collides() {
    	return player.getBounds().overlaps(bounds);
	}
    
    public void apply() {
    	switch(type) {
			case BOMB: // Quitar todos los bloques de la escena
				game.addScore(50);
				break;
			case EXTRA: // Añadir un segundo bloque de jugador
				game.addScore(50);
				break;
			case NUMBER:
				if(number == player.getNumber()) {
					player.updateNumber();
				} else {
					game.loseGame();// Pierdes la partida
				}
				break;
			case RESET: // Te vuelve a un número primo de tu dificultad
				player.setNumber(game.getRandom(), 1);
				game.addScore(50);
				break;
			case RANDOM: // Te da un número aleatorio
				player.setNumber(game.getRandom(), 1 + (int)(Math.random()*6)); // Te da un número aleatorio posible
				game.addScore(50);
				break;
			case VELDEC: // Baja la velocidad
				game.changeVelocity(-5);
				game.addScore(50);
				break;
			case VELINC: // Sube la velocidad
				game.changeVelocity(10);
				game.addScore(50);
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
		bg = bgBlocks[(pow-1)%11];
	}

	public void setNumber(int base, int mult) {
		this.number = (int) Math.pow(base, mult);
		this.base = base;
		pow = mult;
		bg = bgBlocks[(pow-1)%11];
	}
	
	public int getCont() {
		return pow;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public static void dispose() {
		blockTexture.dispose();
	}
	
	//Métodos act y draw
    @Override
    public void act(float delta){
    	bounds.y = bounds.y - delta*game.getVelocity();
    	if(bounds.y <= h*0.85f && position != -1) {
    		pos[position] = true;
//    		System.out.println("Bloque en posición " + position + " ha dejado libre su sitio");
    		position = -1;
    	}
    	if (bounds.y <= h*0.35f && collides()) {
//    		System.out.println("Ha chocado");
    		apply();
    		game.removeEntity(this);
    	} else if(bounds.y < h*0.1f) {
    		dead();
    	}
    }
    
    public void dead() {
    	game.removeEntity(this);
    	game.changeVelocity(0.5f);
    	game.addScore(1);
    }
    
    @Override
	public void draw(Batch batch, float parentAlpha){
    	bg.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
    	if(type == Type.NUMBER) {
    		font.drawMultiLine(batch, String.valueOf(number), bounds.x, bounds.y + size/2 + font.getCapHeight()/2, size, BitmapFont.HAlignment.CENTER);
    	} else {
    		batch.draw(img, bounds.x, bounds.y, bounds.getWidth()/2, bounds.getHeight()/2, 
            		bounds.getWidth(), bounds.getHeight(), 1, 1, getRotation());
    	}
    }
}
