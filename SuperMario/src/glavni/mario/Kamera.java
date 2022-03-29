package glavni.mario;

import entiteti.Entitet;

public class Kamera {
	
	public int x, y;

	public void tick(Entitet player) {
		//prati pomeranje igraca u svakom momentu i pomera se u skladu sa tim
		setX(-player.getX() + Igrica.WIDTH*2);
		setY(-player.getY() + Igrica.HEIGHT*2);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
