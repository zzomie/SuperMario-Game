package podloga;

import java.awt.*;
import java.awt.image.BufferedImage;
import glavni.mario.*;

public class Trail extends Tile{
	
	private BufferedImage image;
	//alpha predstavlja vidljivost slika koje ce biti iza maria u animaciji
	private float alpha = 0.8f;

	public Trail(int x, int y, int width, int height, boolean solid, Id id, Handler handler, BufferedImage image) {
		super(x, y, width, height, solid, id, handler);
		this.image = image;
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
	}

	public void tick() {
		//smanjujemo vidjivost tih slicica pri svakom frejmu
		alpha-=0.05f;
		//ako vidljivost bude premala, brisemo trail jer nam nije potreban vise, nevidljiv je
		if(alpha<0.08) die();
	}

}
