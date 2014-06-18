package screenControl;

import java.util.ArrayList;

import screenControl.GameSelectScreen.Status;
import ProfileSettings.Profile;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

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
            scroll = new Scroll(false, initializeScores(status));
//            initializeScores(status);
            
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
        		        scroll.setItems(initializeScores(status));
        		        select(aux);
        		        return false;
        		    } 
        		} );
        	}
        	select(0);
        	
        	table = super.getTable();
        	table.center().bottom();
        	table.add("Scores").colspan(4);
        	table.row();
        	table.add().height(h*0.02f);
        	table.row();
        	table.add(difficultyButton[0]).size(w*0.25f, h*0.05f).uniform();
        	table.add(difficultyButton[1]).size(w*0.25f, h*0.05f).uniform();
        	table.add(difficultyButton[2]).size(w*0.25f, h*0.05f).uniform();
        	table.add(difficultyButton[3]).size(w*0.25f, h*0.05f).uniform();
        	table.row();
        	table.add(scroll).size(w*0.9f, h*0.65f).colspan(4);
        	table.row();
        	table.add().height(h*0.02f);
        	table.row();
        	table.add(backButton).size(w*0.4f, h*0.07f).colspan(4);
        	table.row();
        	table.add().size(0,h*0.1f);
    }
    
    public void select(int aux) {
    	for(int i = 0; i < Status.values().length; i++)
    		difficultyButton[i].setStyle(skin.get((i == aux)? "red"+(aux+1) : "default", TextButtonStyle.class));
    }
    
    public ScrollItem[] initializeScores(Status status) {
        if(Profile.getMaxScore(status) == 0) { // No hay ninguna puntuación máxima
        	return null;
        } else {
        	ArrayList<Vector3> intitems = Profile.getScore(status);
        	ArrayList<ScrollItem> items = new ArrayList<ScrollItem>();
        	for (Vector3 item : intitems) {
        		if(item.x <= 0) break;
        		else items.add(new ScrollItem(item));
        	}
            return items.toArray(new ScrollItem[0]);
        }
    }
}
