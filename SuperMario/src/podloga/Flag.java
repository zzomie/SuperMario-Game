package podloga;

import java.awt.Graphics;
import glavni.mario.*;

//zastavica nakon koje mario prelazi na novi nivo ako je pipne
public class Flag extends Tile{

	public Flag(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
	}

	public void render(Graphics g) {
		g.drawImage(Igrica.flag.getBufferedImage(), getX(), getY(), width, 64, null);
	}

	public void tick() {
		
	}
}
