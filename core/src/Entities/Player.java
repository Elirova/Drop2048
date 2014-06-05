package Entities;

import com.badlogic.gdx.Gdx;


public class Player extends Block {
	
	public Player(int number) {
		super(number, 1);
		this.setBounds(Gdx.graphics.getWidth()*0.5f-bounds.getWidth()/2, Gdx.graphics.getHeight()*0.22f, size, size);
	}
	
	public void updateNumber() {
		game.addScore(pow);
		pow++;
		number = (int)Math.pow(base, pow);
		bg = bgBlocks[(pow-1)%11];
	}
	
	public void move(float x) {
		if(x > 15) x = 15;
		else if(x < -15) x = -15;
		bounds.x += x;
		if(bounds.x < 0) bounds.x = 0;
		else if(bounds.x > w - size) bounds.x = w - size;
		setX(bounds.x);
	}

	@Override
    public void act(float delta){
    	//super.act(delta);
    }
	
}
