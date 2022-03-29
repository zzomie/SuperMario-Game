package entiteti;

import java.awt.Graphics;
import glavni.mario.*;

public class Coin extends Entitet{

	public Coin(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
	}

	public void render(Graphics g) {
		g.drawImage(Igrica.coin.getBufferedImage(),x,y,width,height,null);
	}

	public void tick() {
		
	}
}
