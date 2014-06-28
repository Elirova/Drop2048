package screenControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import drop2048.Drop2048;

/**
 * @author sagoc dev
 * Clase base para la mayoría de las pantallas
 */
public abstract class AbstractScreen implements Screen {
	protected static Drop2048 game;     
	protected static BitmapFont font, fontWhite;
    protected Stage stage;
    protected Batch batch;
    protected static Skin skin;
    protected Table table;
    protected Texture tbg;
	protected Image bg;
	protected boolean pause;
	protected static int h, w, heightAd;
	
	/**
     * Constructor
     */
	public AbstractScreen() {
        stage = new Stage(new ScreenViewport(), batch);
    	Gdx.input.setInputProcessor(stage);
        batch = stage.getBatch();
        pause = false;
	}
	
	public static void setGame(Drop2048 game) {
		AbstractScreen.game = game;
		AbstractScreen.heightAd = Drop2048.myRequestHandler.getHeightAd();
		AbstractScreen.load();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void setBackground(String path) {
		tbg = new Texture(Gdx.files.internal(path));
		tbg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        bg = new Image(new TextureRegionDrawable(new TextureRegion(tbg,512,512)), Scaling.stretch);
        bg.setFillParent(true);
        stage.addActor(bg);
	}
	
	/**
	 * Return the font
	 * @return
	 */
    public static BitmapFont getFont() { 
        return font; 
    }

    public static BitmapFont getFontWhite() { 
        return fontWhite; 
    }
 
    /**
     * Return the skin
     * @return
     */
    public static Skin getSkin() { 
        return skin; 
    }
 
    /**
     * Return the table
     * @return
     */
    protected Table getTable() { 
        if( table == null ) { 
            table = new Table( getSkin() ); 
            table.setFillParent( true ); 
            stage.addActor( table ); 
        } 
        return table; 
    }
    
    protected String getName() {
		return getClass().getSimpleName();
	}
	   
    @Override
    public void show() { }

	/**
	 * 
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setWorldWidth(width);
		stage.getViewport().setWorldHeight(height);
	}

	/**
	 * 
	 */
	@Override
	public void render(float delta) {
		clear();
		drawStage(delta);
	}
	
	public void clear() {
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f ); 
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
	}
	
	public void drawStage(float delta){
		if(!pause) stage.act(delta);
//		table.debug();
		stage.draw();
//		Table.drawDebug(stage);
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	public boolean isPaused() {
	    	return pause;
	}
	
	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override 
	public void resume() {
	}
	
	public static void load() {
        skin = new Skin(Gdx.files.internal("skin/skin.json")); 
        h = Gdx.graphics.getHeight();
		w = Gdx.graphics.getWidth();
		
        font = skin.getFont("font");
        font.setScale(w/1000f); // Redimensión de la fuente. Mirar si se puede hacer de otra forma
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        fontWhite = skin.getFont("font-white");
        fontWhite.setScale(w/1400f);
        fontWhite.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public static void disposeStatic() {
		if (font != null)
            font.dispose();
		if (fontWhite != null)
			fontWhite.dispose();
		if (skin != null)
			skin.dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		tbg.dispose();
	}
}
