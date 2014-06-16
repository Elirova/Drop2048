package screenControl;

import java.util.ArrayList;

import screenControl.GameSelectScreen.Status;
import ProfileSettings.Profile;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import drop2048.Scroll;
import drop2048.Scroll.ScrollItem;


public class ScoreScreen extends AbstractScreen {
	private Scroll scroll;
	private TextButton backButton;
	private TextButton[] difficultyButton;
	private Status status;
    /**
     * Constructor.
     */
    public ScoreScreen() {       
            super();
            status = Status.EASY;
            setBackground("background/bg.png");
            ScrollItem.initialize();
            scroll = new Scroll();
            initializeScores(status);
            
         // Botón para volver a la pantalla anterior
        	backButton = new TextButton("Back", getSkin());
        	
        	backButton.addListener(new InputListener() { 
    		    @Override
    			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
    		        game.setScreen(new MenuScreen());
    		        return false;
    		    } 
    		} );
        	
        	// Botones de dificultad
            difficultyButton = new TextButton[Status.values().length];
            
        	for (int i = 0; i < Status.values().length; i++) {
        		difficultyButton[i] = new TextButton(Status.values()[i].getName(), AbstractScreen.getSkin());
        		final int aux = i;
        		difficultyButton[i].addListener(new InputListener() { 
        		    @Override
        			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
        		        status = Status.values()[aux];
        		        return false;
        		    } 
        		} );
        	}
        	
        	table = super.getTable();
        	table.bottom();
        	table.add(scroll).size(w*0.95f, h*0.8f);
        	table.row();
        	table.add(backButton).size(w*0.4f, h*0.1f);
        	table.row();
        	table.add().size(0,h*0.05f);
    }
    
    public void initializeScores(Status status) {
        if(Profile.getMaxScore(status) == 0) { // No hay ninguna puntuación máxima
        	scroll.setItems(null);
        } else {
        	ArrayList<Integer> intitems = Profile.getScore(status);
        	ArrayList<ScrollItem> items = new ArrayList<ScrollItem>();
        	for (Integer item : intitems) {
        		if(item <= 0) break;
        		else items.add(new ScrollItem(status, item));
        	}
        	System.out.println(items);
//        	System.out.println((ScrollItem[]) items.toArray());
            scroll.setItems(items.toArray(new ScrollItem[0]));
        }
        
        scroll.setEnabled(false);
    }
}
