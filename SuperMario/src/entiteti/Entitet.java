package entiteti;

import java.awt.Graphics;
import java.awt.Rectangle;
import glavni.mario.*;

//ova klasa sadrzace sve entitete igrice, odnosno objekte koji ce u ovom slucaju biti igraci

public abstract class Entitet {
	public int x, y;
	public int width, height;
	public int velX, velY;
	public int frame = 0, frameDelay = 0;
	public int facing;
	public int type;
	
	public Id id;
	
	public Handler handler;
	
	public boolean jumping = false, falling = false;
	public boolean invisible = false;
	
	public double gravity = 0.0;

	public Entitet(int x, int y, int width, int height, Id id, Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.handler = handler;
	}
	
	//necemo kreirati buffer za svaki objekat jer igrica moze da ima i preko 100 objekata, pa bi to bilo prezahtevno za procesor
	//ove metode su apstraktne kako bi se automatski kreirale svaki put kada kreiramo entitet
	public abstract void render(Graphics g);
	
	public abstract void tick();
	
	public void die() {
		//brisanje entiteta
		handler.removeEntity(this);
		//ako je u pitanju igrac, pokretanjem metode se gubi po jedan zivot, prikazuje se ekran sa brojem zivota i pusta se zvuk za to
		if(getId()==Id.player) {
			Igrica.lives--;
			Igrica.bsod = true;
			Igrica.death.play();
			//sve dok igrac ima vise od 0 zivota, igra ce trajati
			if(Igrica.lives<=0) Igrica.gameOver = true;
		}
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

	public Id getId() {
		return id;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
	
	public int getType() {
		return type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
//naredne metode sluze za tzv "collision detection", odnosno proveravanje da li se dva objekta dodiruju, getujemo velicinu samih objekata
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), width, height);
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle(getX(), getY()+10, 5, height-20);
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle(getX()+width-5, getY()+10, 5, height-20);
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle(getX()+10, getY(), width-20, 5);
	}
	
	public Rectangle getBoundsBottom() {
		return new Rectangle(getX()+10, getY()+height-5, width-20, 5);
	}
}
