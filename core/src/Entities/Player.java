package Entities;

import com.badlogic.gdx.math.Rectangle;


public class Player extends Block {
	private int multScore;
	
	public Player(int number) {
		super(number, 1, 0);
		multScore = 1;
	}
	
	public void updateNumber() {
		game.addScore(pow*multScore*2);
		pow++;
		if(pow > 11) {
			pow = pow%11;
			multScore++;
			color = (color+1)%11;
		}
		number = (int)Math.pow(base, pow);
		bgBlock = bgBlocks[pow-1];
		bgColor = bgColors[color];
		
    	scaleFont = calculateScaleFont(font, number, sizeSmall);
	}
	
	public void move(float x) {
		bounds.x += x-size/2;
		if(bounds.x < 0) bounds.x = 0;
		else if(bounds.x > w - size) bounds.x = w - size;
		setX(bounds.x);
	}
	
	
	
	@Override
	public void setBounds(float x, float y, float width, float height) {
		bounds = new Rectangle(x, y, width, height);
		super.setBounds(x, y, width, height);
	}
	
	@Override
    public void act(float delta){ }
	
}