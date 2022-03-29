package glavni.mario.grafika;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private BufferedImage image;
	
	// ovo je konstruktor koji sluzi da iz foldera podaci uzima podatke koji ce predstavljati slicice na osnovu path tj puta
	public SpriteSheet(String path) {
		try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//get metoda koja sluzi da pri renderovanju ne moramo da renderujemo ceo spritesheet zbog jedne slicice odnosno jednog sprite-a
	//32 je zbog velicine slicice koja je 32*32
	public BufferedImage getSprite(int x, int y) {
		return image.getSubimage(x*32-32,y*32-32,32,32);
	}
}
