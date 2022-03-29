package glavni.mario;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import entiteti.*;
import entiteti.likovi.Goomba;
import entiteti.likovi.Igrac;
import entiteti.powerup.PowerStar;
import podloga.*;

//ova klasa sadrzace sve funkcije odnosno mogucnosti koje se mogu izvrsiti nad entitetima/podlogama

public class Handler {
	//lista ce biti od pomoci pri renderu jer mozemo sve renderovati odjednom kao listu umesto da pisemo kod za pojedinacno renderovanje
	public LinkedList<Entitet> entity = new LinkedList<Entitet>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
		
	public void render(Graphics g) {
		//klasicne for petlje koje prolaze kroz sve elemente liste i renderuju ih
		for(int i=0;i<entity.size();i++) {
			Entitet e = entity.get(i);
				if(Igrica.getVisibleArea()!=null&&e.getBounds().intersects(Igrica.getVisibleArea())) 
					e.render(g);
		}
			
		for(int i=0;i<tile.size();i++) {
			Tile t = tile.get(i);
				if(Igrica.getVisibleArea()!=null&&t.getBounds().intersects(Igrica.getVisibleArea())) 
					t.render(g);
		}
		
		//kopiran kod iz dela klase igrica, tu je da bi se broj skupljenih novcica prikazivao normalno,
		//bez ovog koda se renderuje iza blokova i nije lako procitati ga
		g.drawImage(Igrica.coin.getBufferedImage(), Igrica.getVisibleArea().x+20, Igrica.getVisibleArea().y+20, 75, 75, null);
		g.drawImage(Igrica.life.getBufferedImage(), Igrica.getVisibleArea().x+20, Igrica.getVisibleArea().y+100, 75, 75, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier", Font.BOLD, 20));
		g.drawString("x" + Igrica.coins, Igrica.getVisibleArea().x+100, Igrica.getVisibleArea().y+80);
		g.drawString("x" + Igrica.lives, Igrica.getVisibleArea().x+100, Igrica.getVisibleArea().y+155);
		}
		
	public void tick() {
		//klasicne for petlje koje prolaze kroz sve elemente liste i refresh-uju ih
		for(int i=0;i<entity.size();i++) {
			Entitet e = entity.get(i);
			e.tick();
			}
			
		for(int i=0;i<tile.size();i++) {
			Tile t = tile.get(i);
			if(Igrica.getVisibleArea()!=null&&t.getBounds().intersects(Igrica.getVisibleArea())) t.tick();
			}
		}
		
	public void addEntity(Entitet e) {
		//dodavanje entiteta
			entity.add(e);
	}
		
	public void removeEntity(Entitet e) {
		//brisanje entiteta
			entity.remove(e);
	}
		
	public void addTile(Tile t) {
		//dodavanje blokova
			tile.add(t);
	}
		
	public void removeTile(Tile t) {
		//brisanje blokova
			tile.remove(t);
	}
		
	public void createLevel(BufferedImage level) {
		int width = level.getWidth();
		int height = level.getHeight();
			
		for(int y=0;y<height;y++) {
			for(int x = 0;x<width;x++) {
				int pixel = (int) level.getRGB(x, y);
				
				//ovako dobijamo potpunu vrednost svake od ove tri boje u pixel-u
				//ove strelice pokazuju koliko puta pomeramo brojeve u binarnom kodu ovih boja kako bi ih java ocitala
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
					
				//objasnjenje koda ispod
				//sliku crtam u gimpu nalik onome kako bih zelela da leveli izgledaju
				//u mom slucaju svi leveli se prelaze odjednom, s tim sto vremenom postaje sve teze preci level
				//kod ispod ima mogucnost da sa slike ocita piksele odredjenih boja
				//u slucaju da na slici uoci piksel crne boje dodace podlogu, u slucaju plave dodace igraca itd
				if(red==0&&green==0&&blue==0) addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));//citanje podloge
				if(red==0&&green==0&&blue==255) addEntity(new Igrac(x*64,y*64,48,48,Id.player,this));//citanje igraca
				if(red==255&&green==255&&blue==0) addEntity(new Coin(x*64,y*64,64,64,Id.coin,this));//citanje novcica
				if(red==255&green==128&&blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Igrica.lifeMushroom,1));//citanje 1-up
				if(red==0&green==255&&blue==0) addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,this));//citanje zastavice
				if(red==255 && green==119&&blue==0) addEntity(new Goomba(x*64, y*64, 64, 64, Id.goomba,this));//citanje goomba-e
				if(red==255&green==0&&blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Igrica.mushroom,0));//citanje 1-up
				if(red==238&green==247&&blue==96) addEntity(new PowerStar(x*64, y*64, 64, 64, Id.star, this));//citanje zvezdice
				}
			}
			//definisemo kraj levela koji se nalazi tek kad se sve izrenderuje
			Igrica.deathY = Igrica.getDeathY();
		}
		
	public void clearLevel() {
		//ciscenje nivoa
			entity.clear();
			tile.clear();
	}

}
