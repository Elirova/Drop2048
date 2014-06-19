package screenControl;

import Entities.Player;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import drop2048.Drop2048;
import drop2048.MenuBg;

public class TutorialScreen extends AbstractScreen {
	private TextButton nextButton;
	private MenuBg menu;
	private Player player;
	private Image arrowLeft, arrowRight;

    public TutorialScreen() {       
            super();
            setBackground("background/gamebg.png");
            create();
            createTable();
    }
    
    private void create() {
    	// Initialize buttons
    	nextButton = new TextButton("Next", getSkin(), "green");
        
        // Add listeners
    	nextButton.addListener(new InputListener() {
		    @Override
			public boolean touchDown (InputEvent  event, float x, float y, int pointer, int button) {
		    	game.setScreen(new InfoScreen());
		        return false;
		    } } ); 
    	
    	// Initialize menu's background
    	menu = new MenuBg(false); 
        bg.setZIndex(0);
        menu.setZIndex(1);
    	stage.addActor(menu);
    	
    	// Initialize player
//    	player = new ScoreBlock(Type.NUMBER, 2, 2, 3, (int)(w*0.2f), true);
    	player = new Player(2);
    	player.setZIndex(3);
    	stage.addActor(player);   
    	
    	player.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y,
                    int pointer) {
            	player.move(x);
                super.touchDragged(event, x, 0, pointer);
            }
        });
    	
    	// Create arrows
    	arrowLeft = new Image(AbstractScreen.getSkin().getDrawable("arrow-left"));
    	arrowRight = new Image(AbstractScreen.getSkin().getDrawable("arrow-right"));
    }
    
    private void createTable(){
    	table = super.getTable();
    	table.setZIndex(2);
    	Label text = new Label("Move your block to the left or right to dodge other blocks, catch blocks with "
    			+ "the same number as yours or take special blocks to gain points.", getSkin());
    	text.setWrap(true);
    	text.setAlignment(0);
    	text.setFontScale((h*0.35f*font.getScaleY())/font.getWrappedBounds(text.getText(), w*0.8f).height);
		table.add(text).width(w*0.8f).colspan(2).expandY(); 
		table.row();
		table.add(arrowLeft).size(w*0.2f, h*0.1f).spaceRight(h*0.2f);
		table.add(arrowRight).size(w*0.2f, h*0.1f);
		table.row();
		table.add(nextButton).size(w, h*0.1f).spaceTop(h*0.05f).colspan(2);
		table.row();
		table.add().size(0, Drop2048.myRequestHandler.getHeightAd()+h*0.02f);
		table.center().bottom();
    }
	

}
