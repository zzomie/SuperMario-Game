package podloga;

import java.awt.Graphics;
import entiteti.powerup.Mushroom;
import glavni.mario.*;
import glavni.mario.grafika.Sprite;


public class PowerUpBlock extends Tile {
	private Sprite powerUp;
	private boolean poppedUp = false;
	private int spriteY = getY();
	private int type;

	public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp, int type) {
		super(x, y, width, height, solid, id, handler);
		this.type = type;
		this.powerUp = powerUp;
	}

	public void render(Graphics g) {
		//crta razlicite slike u zavisnosti od toga da li powerup block aktiviran ili ne
		if(!poppedUp) g.drawImage(powerUp.getBufferedImage(),x,spriteY,width,height,null);
		if(!activated) g.drawImage(Igrica.powerUp.getBufferedImage(),x,y,width,height,null);
		else g.drawImage(Igrica.usedPowerUp.getBufferedImage(),x,y,width,height,null);
	}

	public void tick() {
		if(activated&&!poppedUp) {
			spriteY--;
			if(spriteY<=y-height) {
				if(powerUp==Igrica.mushroom||powerUp==Igrica.lifeMushroom) 
					//u zavisnosti od tipa izabrane pecurke pna se kreira u blocku
				handler.addEntity(new Mushroom(x,spriteY,width,height,Id.mushroom,handler,type));
				poppedUp = true;
			}
		}
	}
}
