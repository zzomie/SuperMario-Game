package glavni.mario.grafika;

import java.awt.image.BufferedImage;

public class Sprite {
	private BufferedImage image;
	
	//slika se cita sa koordinata
	public Sprite(int x, int y, SpriteSheet sheet) {
		image = sheet.getSprite(x, y);
	}
	
	//metoda koja sluzi da pri renderu java uoci da je sprite zapravo slicica koju treba da renderuje
	public BufferedImage getBufferedImage() {
		return image;
	}

}
