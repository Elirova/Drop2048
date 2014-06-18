package Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ScoreBlock extends Block {
	private int sizeBlock;
	private boolean showNumber;

	public ScoreBlock(Type type, int base, int pow, int color, int size, boolean showNumber) {
		super(type, base, pow, color);
		this.sizeBlock = size;
		this.showNumber = showNumber;
	}
	
	public ScoreBlock(Type type, int size, boolean showNumber) {
		super(type, 2, 1, 1);
    	createBlock();
		this.sizeBlock = size;
		this.showNumber = showNumber;
	}
	
	@Override
	public void act(float delta){ }
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		bgBlock.draw(batch, getX() - sizeBlock*0.1f, getY() - sizeBlock*0.1f, sizeBlock*0.8f, sizeBlock*0.8f);
    	if(type == Type.NUMBER) {
    		bgColor.draw(batch, getX(), getY(), sizeBlock*0.6f, sizeBlock*0.6f);
    		if(showNumber) {
	    		float scale = font.getScaleY();
	    		font.setScale(scaleFont);
	    		font.drawMultiLine(batch, String.valueOf(number), getX(), getY() + sizeBlock*0.55f, sizeBlock*0.6f, BitmapFont.HAlignment.CENTER);
	    		font.setScale(scale);
    		}
    	} else {
    		batch.draw(img, getX(), getY(), getWidth()/2, getHeight()/2, 
    				getWidth(), getHeight(), 1, 1, getRotation());
    	}
	}
}
