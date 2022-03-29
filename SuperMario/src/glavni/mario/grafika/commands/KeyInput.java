package glavni.mario.grafika.commands;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import glavni.mario.*;
import entiteti.*;

public class KeyInput implements KeyListener {
	// dodat je interfejs keylistener kako bi java prepoznala da mi zelimo da kontrolisemo nesto putem tastature
	
	public void keyPressed(KeyEvent e) {
		// implementirana metoda koja govori sta se desi kada mi pritisnemo odredjeno dugme na tastaturi
		int key = e.getKeyCode();
		for(int i=0;i<Igrica.handler.entity.size();i++) {
			Entitet en = Igrica.handler.entity.get(i);
			if(en.getId()==Id.player) {
				switch(key) {
				case KeyEvent.VK_W:
								if (!en.jumping) {
									en.jumping = true;
									en.gravity = 10.0;
									Igrica.jump.play();
							} 
					break;
					//sluzilo je za laksu proveru sta se desi ako igrac izgubi zivot
				//case KeyEvent.VK_E:
					//en.die();
				case KeyEvent.VK_A:
					en.setVelX(-5);
					en.facing = 0;
					break;
				case KeyEvent.VK_D:
					en.setVelX(5);
					en.facing = 1;
					break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		// implementirana metoda koja govori sta se desi kada mi pustimo dugme koje smo koristili
		int key = e.getKeyCode();
		for(int i=0;i<Igrica.handler.entity.size();i++) {
			Entitet en = Igrica.handler.entity.get(i);
			if(en.getId()==Id.player) {
				switch(key) {
				case KeyEvent.VK_W:
					en.setVelY(0);
					break;
				case KeyEvent.VK_A:
					en.setVelX(0);
					break;
				case KeyEvent.VK_D:
					en.setVelX(0);
					break;
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// ovu metodu necemo koristiti, sluzi da nas unos pretvara u tekst, dozvoljeni su Shift i CapsLock
	}
	
}
