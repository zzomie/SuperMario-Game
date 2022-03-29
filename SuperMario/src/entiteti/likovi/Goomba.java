package entiteti.likovi;

import java.awt.Graphics;
import java.util.Random;
import entiteti.Entitet;
import glavni.mario.*;
import podloga.Tile;

//Goomba predstavlja neprijatelja sa kojim se Mario u ovoj igrici bori, nasledjuje klasu Entitet jer je entitet igrice

public class Goomba extends Entitet{
	//koristimo random da bi Goomba imao random pozicije na samom levelu, u ovom slucaju koristicemo brojeve do 2
	private Random random = new Random();
	
	public Goomba(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		
		int dir = random.nextInt(2);
		
		//u zavisnosti od toga da li je broj 1 ili 0, Goomba ce se kretati levo ili desno, tako ce i biti okrenut
		switch(dir) {
		case 0:
			setVelX(-2);
			facing = 0;
			break;
		case 1:
			setVelX(2);
			facing = 1;
			break;
		}
	}
	
	public void render(Graphics g) {
		// renderovanje razlicite slicice u zavisnosti od smera u koji gleda Goomba, a koji ce se znati na osnovu koda u konstruktoru
		if(facing==0) {
			g.drawImage(Igrica.goomba[frame+5].getBufferedImage(),x,y,width,height,null);
		} else if(facing==1) {
			g.drawImage(Igrica.goomba[frame].getBufferedImage(),x,y,width,height,null);
		}
	}
	
	public void tick() {
		// povecavamo x i y koordinate
		x+=velX;
		y+=velY;
		
		for(int i=0;i<handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			if(t.isSolid()) {
				if(getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					if(falling) falling = false;
				} else if(!falling) {
					falling = true;
					gravity = 0.8;
				}
				if(getBoundsLeft().intersects(t.getBounds())) {
					setVelX(2);
					facing = 1;
				}
				if(getBoundsRight().intersects(t.getBounds())) {
					setVelX(-2);
					facing = 0;
				}
			}
		}
		
		if(falling) {
			gravity+=0.1;
			setVelY((int)gravity);
		}
		
		//pomoc pri animaciji, broji frejmove koji su prosli i ako je bilo vise od broja slicica goombe, vrti sve to ispocetka
		if(velX!=0) {
			frameDelay++;
			if(frameDelay>=10) {
				frame++;
				if(frame>3) {
					frame = 0;
				}
				frameDelay = 0;
			}
		}
	}
}
