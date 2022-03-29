package entiteti.likovi;

import java.awt.Graphics;
import glavni.mario.*;
import podloga.*;
import entiteti.Entitet;

//klasa igrac predstavlja klasu rezervisanu za glavnog lika igrice - Maria, predstavlja nasledjenu klasu klase Entitet

public class Igrac extends Entitet{
	private int invisibilityTime = 0;
	
	public Igrac(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
     }
	
	public void render(Graphics g) {
		//definisemo izgled igraca u zavisnosti od toga kako je okrenut
			if(facing==0) {
				g.drawImage(Igrica.player[frame+5].getBufferedImage(),x,y,width,height,null);
			} else if(facing==1) {
				g.drawImage(Igrica.player[frame].getBufferedImage(),x,y,width,height,null);
			}
		
			//kod koji je pod komentarima sam koristila cesto kako bih videla da li igrac lepo reaguje na prepreku
			/* g.setColor(Color.BLACK);
			 g.fillRect(desniOkvir().x, desniOkvir().y, desniOkvir().width, desniOkvir().height);
			 g.fillRect(leviOkvir().x, leviOkvir().y, leviOkvir().width, leviOkvir().height);
			 g.fillRect(gornjiOkvir().x, gornjiOkvir().y, gornjiOkvir().width, gornjiOkvir().height);
			 g.fillRect(donjiOkvir().x, donjiOkvir().y, donjiOkvir().width, donjiOkvir().height);*/
	}
	
	public void tick() {
		x+=velX;
		y+=velY;
		
		//ako je igrac otisao ispod levela, gubi zivot
		if(getY()>Igrica.deathY) die();
		
		if(invisible) {
			//crta razlicite slike "repa" u zavisnosti od strane gledanja
			if(facing==0) 
				handler.addTile(new Trail(getX(), getY(), getWidth(), getHeight(), false, Id.trail, handler, Igrica.player[frame+5].getBufferedImage()));
			else if(facing==1) 
				handler.addTile(new Trail(getX(), getY(), getWidth(), getHeight(), false, Id.trail, handler, Igrica.player[frame].getBufferedImage()));

			invisibilityTime++;
			//600 jer ima 60 fps, znaci 10 sekundi bi bilo 600
			if(invisibilityTime>=600) {
				invisible = false;
				invisibilityTime = 0;
			}
		}
		
		for(int i=0;i<handler.tile.size();i++) {
			Tile t = handler.tile.get(i);
			// ako objekat nije "cvrst" odnosno prepoznatljiv prekida se petlja
			if(t.isSolid()) {
				// intersects predstavlja presecanje stoga sledeci if-ovi kazu sta ce se dogoditi u slucaju da se ovi okviri medjusobno seku
				if(getBounds().intersects(t.getBounds())) {
					if(t.getId()==Id.flag) {
						// ako se igrac susretne sa zastavicom, prelazi na sledeci nivo
						Igrica.switchLevel();
						// pustanje zvuka pobede
						Igrica.win.play();
					}
				}
				
				if(getBoundsTop().intersects(t.getBounds())) {
					setVelY(0);
					if(jumping) {
						jumping = false;
						gravity = 0.8;
						falling = true;
					}
					if(t.getId()==Id.powerUp) {
						// ako se igrac susretne sa powerup blokom sa gornje strane, powerup blok se aktivira
						if(getBoundsTop().intersects(t.getBounds())) t.activated = true;
					}
				}
				if(getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					if(falling) falling = false;
					 // mogucnost pada sa platforme
				} else if(!falling&&!jumping) {
					falling = true;
					gravity = 0.8;
				}
				if(getBoundsLeft().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX()+t.getWidth();
				}
				if(getBoundsRight().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX()-width;
				}
			}
		}
		
		for(int i=0;i<handler.entity.size();i++) {
			Entitet e = handler.entity.get(i);
			//u slucaju da entitet koji se kreira bude pecurkica i ima presecanje sa likom
			if(e.getId()==Id.mushroom) {
				switch(e.getType()) {
				case 0:
					// u slucaju da se sretne sa prvom pecurkom (crvenom), njegova velicina ce se povecati
					if(getBounds().intersects(e.getBounds())) {
						int tpX = getX();
						int tpY = getY();
						width+=(width/3);
						height+=(height/3);
						setX(tpX-width);
						setY(tpY-height);
						// pokrenuce se zvuk dodeljen za koriscenje powerup-a
						Igrica.powerup.play();
						// pecurkica ce nestati u trenutku kada je igrac pokupi
						e.die();
					}
					break;
				case 1:
					// u slucaju da se sretne sa drugom pecurkom (zelenom), broj zivota ce se povecati
					if(getBounds().intersects(e.getBounds())) {
						Igrica.lives++;
						// pecurkica ce nestati u trenutku kada je igrac pokupi
						e.die();
						// pokrenuce se zvuk dodeljen za koriscenje powerup-a
						Igrica.powerup.play();
					}
				}
				
			} else if(e.getId()==Id.goomba) {
				// u slucaju da se igrac sretne sa Goombom
				if(invisible&&getBounds().intersects(e.getBounds())) {
					// ako je igrac pokupio zvezdicu i postao neunistiv, Goomba nestaje/umire
					e.die();
					// pokrenuce se zvuk dodeljen za ovaj slucaj
					Igrica.damage.play();
				}
				else {	
					if(getBoundsBottom().intersects(e.getBoundsTop())) {
						// ako je igrac udario Goombu sa gornje strane (skocio mu na glavu), Goomba nestaje/umire
						if(e.getId()==Id.goomba) {
							e.die();
							// pokrenuce se zvuk dodeljen za ovaj slucaj
							Igrica.damage.play();
						}
					}
					// naredna dva ifa opisuju sta ce se desiti ako se igrac sa leve ili desne strane sretne sa Goombom
					if(getBoundsLeft().intersects(e.getBounds())) {
							if(e.getId()==Id.goomba) {
								// igrac gubi zivot
								die();
								// pokrenuce se zvuk dodeljen za ovaj slucaj
								Igrica.damage.play();
							}
					}
							if(getBoundsRight().intersects(e.getBounds())) {
								if(e.getId()==Id.goomba) {
									// igrac gubi zivot
									die();
									// pokrenuce se zvuk dodeljen za ovaj slucaj
									Igrica.damage.play();
								}
						
					} 
				}
				// ako igrac naidje na novcic
			} else if(e.getId()==Id.coin) {
				if(getBounds().intersects(e.getBounds())&&e.getId()==Id.coin) {
					// broj novcica se povecava
					Igrica.coins++;
					//Igrica.coinSound.play();
					// novcic se unistava u momentu kada ga igrac pokupi
					e.die();
				}
				// ako igrac naidje na novcic
			}  else if(e.getId()==Id.star) {
				if(getBounds().intersects(e.getBounds())) {
					// igrac postaje nevidljiv, odnosno postaje neunistiv
					invisible = true;
					// pokrenuce se zvuk dodeljen za ovaj slucaj
					Igrica.powerup.play();
					// zvezdica se unistava u momentu kada je igrac pokupi
					e.die();
				}
			} 
		}
		
		// ukoliko igrac skace
		if(jumping) {
			// int je u zagradi jer je gravitacija po tipu double, a VelY int
			// ukoliko igrac skace gravitacija ce se smanjivati
			gravity-=0.17;
			setVelY((int)-gravity);
			if(gravity<=0.5) {
				// u slucaju da je gravitacija manja od ove vrednosti igrac pocinje da pada
				jumping = false;
				falling = true;
			}
		}	
		// ukoliko igrac pada
		if(falling) {
			// ukoliko igrac pada gravitacija ce se povecavati 
			gravity+=0.17;
			// int je u zagradi jer je gravitacija po tipu double, a VelY int
			setVelY((int)gravity);
		}
		
		//pomoc pri animaciji, broji frejmove koji su prosli i ako je bilo vise od broja slicica igraca, vrti sve to ispocetka
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
