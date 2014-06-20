package screenControl;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import drop2048.Scroll;
import drop2048.Scroll.ScrollItem;

public class InfoScreen extends AbstractScreen {
	private Scroll scroll;
	private TextButton backButton;
	private static ScrollItem[] items;
	/**
	 * Constructor.
	 */
    public InfoScreen() {       
            super();
            setBackground("background/bg.png");
            scroll = new Scroll(true, initializeInfo());
            createTable();
    }
    
    /**
	 * Crea la tabla con los botones.
	 */
    private void createTable(){
    	 // Botón para volver a la pantalla anterior
    	backButton = new TextButton("Back", getSkin());
    	
    	backButton.addListener(new InputListener() { 
		    @Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { 
		        game.setScreen(new MenuScreen());
		        return false;
		    } 
		} );
    	
    	table = getTable();
    	table.center().bottom();
    	table.add("Blocks Information").colspan(4);
    	table.row();
    	table.add().height(h*0.02f);
    	table.row();
    	table.add(scroll).size(w*0.9f, h*0.65f).colspan(4);
    	table.row();
    	table.add().height(h*0.02f);
    	table.row();
    	table.add(backButton).size(w*0.4f, h*0.07f).colspan(4);
    	table.row();
    	table.add().size(0,h*0.1f);
    }
    
    public ScrollItem[] initializeInfo() {
    	if(items == null) {
    		items = new ScrollItem[6];
    		items[0] = new ScrollItem(new Vector3(2, 6, 4), "Catch the numbers with the same color and number than yours.");
    		items[1] = new ScrollItem(11, "Get a random number.");
    		items[2] = new ScrollItem(13, "Return to initial number and speed.");
    		items[3] = new ScrollItem(12, "Decrease speed.");
    		items[4] = new ScrollItem(14, "Increase speed.");
    		items[5] = new ScrollItem(15, "Return to initial speed for a few seconds.");
    	}

       return items;
    }
}
