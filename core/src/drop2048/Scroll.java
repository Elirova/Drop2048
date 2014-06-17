package drop2048;

import screenControl.AbstractScreen;
import Entities.Block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class Scroll extends ScrollPane {
    
    public static final String SEPARATOR = "/*~*/";
   
    private MyRawList rawList;
    private boolean isEnabled;
    private String emptyLabel;   
           
    /**
     * Constructor.        
     */
    public Scroll() {            
        super(null);    
        isEnabled = true;
        rawList = new MyRawList(null, this);
        setWidget(rawList);
        setWidth(rawList.getPrefWidth());
        setHeight(rawList.getPrefHeight());
        setScrollingDisabled(true, false);     
        emptyLabel = "There are no scores yet";
    }
                           
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;            
    }
           
    public void setItems(ScrollItem[] items){
        rawList.setItems(items);
        super.setForceScroll(false, true);
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
        private float prefWidth, prefHeight;
        private float itemSize = (int) (Gdx.graphics.getWidth()*0.25f);
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
                    float centerX = Scroll.getWidth()/2;
                    float centerY = Scroll.getHeight()/2;
                    font.draw(batch, emptyLabel, centerX - font.getBounds(emptyLabel).width/2,
                                    centerY+font.getBounds(emptyLabel).height/2);             
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
                	items[i].getBg1().draw(batch, x + itemSize*0.25f, y + itemY - itemSize*0.9f, itemSize*0.6f, itemSize*0.6f);
                	items[i].getBg2().draw(batch, x + itemSize*0.3f, y - itemSize*0.85f + itemY, itemSize*0.5f, itemSize*0.5f);
                	
                	// Puntuación
                	float scale = fontWhite.getScaleY();
                	fontWhite.setScale(itemSize/(getWidth()*0.7f));
                	fontWhite.draw(batch, items[i].getPoints(),
                             x + textOffsetX + getWidth()*0.35f, y + itemY - itemSize/2);
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
        }
                                                                           
        public ScrollItem[] getItems () {
            return items;
        }

        public float getPrefWidth () {
            return prefWidth;
        }

        public float getPrefHeight () {
            return prefHeight;
        }               
    }      
   
    /************************ LIST ITEM ************************/
    public static class ScrollItem implements Comparable<ScrollItem>{  
		private int pointsint;
        private String points;
        private NinePatch bg1, bg2;
        public static NinePatch bg;
        
        public ScrollItem(Vector3 values){
            this.pointsint = (int) values.x;
            this.points = String.valueOf(pointsint);
            bg1 = Block.getBgBlocks()[(int) values.z];
			bg2 = Block.getBgBlocks()[(int) values.y];
			if(bg == null) bg = AbstractScreen.getSkin().getPatch("bg-score-transparent");
        }

        public static ScrollItem[] formItems(Vector3[] points) {                         
            ScrollItem[] listItems = new ScrollItem[points.length];                       
            for (int i=0; i<points.length; i++)         
              listItems[i] = new ScrollItem(points[i]);                             
            return listItems;
        }

		public int getPointsint() {
			return pointsint;
		}

		public void setPointsint(int pointsint) {
			this.pointsint = pointsint;
		}

		public String getPoints() {
			return points;
		}

		public void setPoints(String points) {
			this.points = points;
		}

		public NinePatch getBg1() {
			return bg1;
		}

		public NinePatch getBg2() {
			return bg2;
		}

		@Override
		public int compareTo(ScrollItem o) {
			return (o.getPointsint() >= pointsint)? 1 : -1;
		}
    }   
}