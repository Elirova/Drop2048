package drop2048;

import screenControl.AbstractScreen;
import Entities.Block;
import Entities.Block.Type;
import Entities.ScoreBlock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class Scroll extends ScrollPane {
    private MyRawList rawList;
    private boolean multiline;
    private String emptyLabel;
    public static int itemSize = (int) (Gdx.graphics.getWidth()*0.25f);
           
    /**
     * Constructor.
     */
    public Scroll(boolean multiline, ScrollItem[] items) {            
        super(null);    
        this.multiline = multiline;
        rawList = new MyRawList(items, this);
        setWidget(rawList);
        setWidth(rawList.getPrefWidth());
        setHeight(rawList.getPrefHeight());
        setForceScroll(false, true);
        emptyLabel = "There are no scores yet";
    }
           
    public void setItems(ScrollItem[] items){
        rawList.setItems(items);
        setHeight(rawList.getPrefHeight());
    }
   
    public String getEmptyLabel() {
        return emptyLabel;
    }

    public void setEmptyLabel(String emptyLabel) {
        this.emptyLabel = emptyLabel;
    }
                                                   
    /******************** RAW LIST ************************/
    public class MyRawList extends Widget {
           
        private Scroll Scroll;
        private ScrollItem[] items;
        private float prefHeight;
        private float textOffsetX;
       
        public MyRawList (ScrollItem[] items, Scroll Scroll) {
            setItems(items);
            setWidth(getPrefWidth());
            setHeight(getPrefHeight());
            this.Scroll=Scroll;
        }  
                       
        @Override
        public void draw (Batch batch, float parentAlpha) {
            BitmapFont font = AbstractScreen.getFont(), fontWhite = AbstractScreen.getFontWhite();

            float x = getX();
            float y = getY();
            float itemY = getHeight();
           
            if(items == null){
            		font.drawWrapped(batch, emptyLabel, getX(), Scroll.getHeight()/2,
            				getWidth(), BitmapFont.HAlignment.CENTER);
                    return;
            }
            
            for (int i = 0; i < items.length; i++) {
                boolean isDrawn = true;
                
                if(getHeight()-itemY < Scroll.getScrollY() - itemSize) {
                    isDrawn=false;
                }                              
                if(getHeight()-itemY > Scroll.getScrollY() + Scroll.getHeight()) {
                    isDrawn = false;
                }
               
                if(isDrawn) {
                	ScrollItem.bg.draw(batch, x, y + itemY - itemSize, getWidth(), itemSize*0.8f);
                	items[i].getBlock().setBounds(x + itemSize*0.3f, y - itemSize*0.85f + itemY, itemSize*0.5f, itemSize*0.5f);
                	items[i].getBlock().draw(batch, parentAlpha);

                	// Puntuación
                	float scale = fontWhite.getScaleY();
                	if(multiline) {
                		float height = fontWhite.getWrappedBounds(items[i].getLabel(), getWidth()*0.7f).height;
                		fontWhite.setScale((height > itemSize*0.6f)? (itemSize*0.6f*scale)/fontWhite.getWrappedBounds(items[i].getLabel(), getWidth()*0.7f).height
                				: itemSize/(getWidth()*0.8f));
                		fontWhite.drawWrapped(batch, items[i].getLabel(),
                                 x + textOffsetX + getWidth()*0.3f, y + itemY - itemSize*0.3f, getWidth()*0.7f);
                	} else {
                		fontWhite.setScale(itemSize/(getWidth()*0.7f));
                    	fontWhite.drawWrapped(batch, items[i].getLabel(),
                                 x + textOffsetX + getWidth()*0.3f, y + itemY - itemSize/2, getWidth()*0.7f);
                	}
                	
                	fontWhite.setScale(scale);
                }     
                itemY -= itemSize;
            }
        }
        
        public void setItems (ScrollItem[] objects) {
            if(objects == null){
                items = null;
                return;
            }                      
            if (!(objects instanceof ScrollItem[])) {                     
                throw new IllegalArgumentException("Items must be instance of ScrollItem");
            }
                   
            items = objects;   
            prefHeight = items.length * itemSize; 
        }
                                                                           
        public ScrollItem[] getItems () {
            return items;
        }     
        
        public float getPrefHeight () {
            return prefHeight;
        }
    }      
   
    /************************ LIST ITEM ************************/
    public static class ScrollItem implements Comparable<ScrollItem>{  
		private int points;
        private String label;
        private ScoreBlock block;
        public static NinePatch bg;
        
        // Constructores
        /**
         * Constructor para mostrar los dos colores, sin número y con puntuación
         * @param values
         */
        public ScrollItem(Vector3 values){
            this.points = (int) values.x;
            this.label = String.valueOf(points);
            block = new ScoreBlock(Type.NUMBER, 1, (int)values.y, (int)values.z, (int)(itemSize*0.8f), false);
			if(bg == null) bg = AbstractScreen.getSkin().getPatch("bg-score-transparent");
        }
        
        /**
         * Constructor para mostrar los dos colores, el número y una label con información
         * @param values
         * @param label
         */
        public ScrollItem(Vector3 values, String label){
            points = (int) values.x;
            this.label = label;
            block = new ScoreBlock(Type.NUMBER, points, (int)values.y, (int)values.z, (int)(itemSize*0.8f), true);
			if(bg == null) bg = AbstractScreen.getSkin().getPatch("bg-score-transparent");
        }
        
        /**
         * Constructor para mostrar un bloque especial e información
         * @param values
         * @param label
         */
        public ScrollItem(Vector2 values, String label){
            this.points = 0;
            this.label = label;
            Type type = Type.NUMBER;
            switch((int)values.x) {
            	case 11:
            		type = Type.RANDOM;
            		break;
            	case 12:
            		type = Type.VELDEC;
            		break;
            	case 13:
            		type = Type.RESET;
            		break;
            	case 14:
            		type = Type.VELINC;
            		break;
            }
            block = new ScoreBlock(type, 1, (int)values.x, (int)values.y, (int)(itemSize*0.8f), false);
			if(bg == null) bg = AbstractScreen.getSkin().getPatch("bg-score-transparent");
        }
        
        public ScrollItem(int bgColor, String label){
            this.points = 0;
            this.label = label;
            Type type = Type.NUMBER;
            switch(bgColor) {
            	case 11:
            		type = Type.RANDOM;
            		break;
            	case 12:
            		type = Type.VELDEC;
            		break;
            	case 13:
            		type = Type.RESET;
            		break;
            	case 14:
            		type = Type.VELINC;
            		break;
            }
            block = new ScoreBlock(type, (int)(itemSize*0.8f), false);
			if(bg == null) bg = AbstractScreen.getSkin().getPatch("bg-score-transparent");
        }
        
        public static ScrollItem[] formItems(Vector2[] values, String[] label) {                         
            ScrollItem[] listItems = new ScrollItem[values.length];                       
            for (int i=0; i<values.length; i++)         
              listItems[i] = new ScrollItem(values[i], label[i]);                             
            return listItems;
        }
        
        public static ScrollItem[] formItems(int[] bgColors, String[] label) {                         
            ScrollItem[] listItems = new ScrollItem[bgColors.length];                       
            for (int i=0; i < bgColors.length; i++)         
              listItems[i] = new ScrollItem(bgColors[i], label[i]);                             
            return listItems;
        }
        
        public static ScrollItem[] formItems(Vector3[] points) {                         
            ScrollItem[] listItems = new ScrollItem[points.length];                       
            for (int i=0; i<points.length; i++)         
              listItems[i] = new ScrollItem(points[i]);                             
            return listItems;
        }

		public int getPoints() {
			return points;
		}

		public void setPoints(int pointsint) {
			this.points = pointsint;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String points) {
			this.label = points;
		}
		
		public Block getBlock() {
			return block;
		}

		@Override
		public int compareTo(ScrollItem o) {
			return (o.getPoints() >= points)? 1 : -1;
		}
    }   
}