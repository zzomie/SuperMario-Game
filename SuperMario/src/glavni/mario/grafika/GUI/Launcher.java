package glavni.mario.grafika.GUI;

import java.awt.*;
import glavni.mario.*;

public class Launcher {
	public Button[] buttons;
	
	public Launcher() {
		buttons = new Button[2];
		//kreiranje button-a
		buttons[0] = new Button(Igrica.getFrameWidth()/2-150,Igrica.getFrameHeight()/2-100,300,100,"Start Game");
		buttons[1] = new Button(Igrica.getFrameWidth()/2-150,Igrica.getFrameHeight()/2+100,300,100,"Exit Game");
	}
	
	public void render(Graphics g) {
		//kreiranje pozadine menija
		Color pozadina = new Color(51, 204, 51);
		g.setColor(pozadina);
		g.fillRect(0, 0, Igrica.getFrameWidth(), Igrica.getFrameHeight());
		
		for(int i=0;i<buttons.length;i++) {
			buttons[i].render(g);
		}
	}
}
