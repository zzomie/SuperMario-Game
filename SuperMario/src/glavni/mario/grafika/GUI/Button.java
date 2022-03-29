package glavni.mario.grafika.GUI;

import java.awt.*;
import glavni.mario.*;

public class Button {
	
	public int x, y;
	public int width, height;
	public String label;

	public Button(int x, int y, int width, int height, String label) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic",Font.BOLD,50));
		// linija koda ispod mi je sluzila pri centriranju samog teksta, stvara se okvir oko buttona i tacno vidimo gde je
		//g.drawRect(getX(), getY(), getWidth(), getHeight());
		
		//centriranje na sredinu ekrana, getuje sadasnje vrednosti i u slucaju da je velicina ekrana promenjena, menja i samu poziciju
		//automatski
        FontMetrics fm = g.getFontMetrics();
	    int stringX = (getWidth() - fm.stringWidth(getLabel())) / 2;
	    int stringY = (fm.getAscent() + (getHeight() - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(getLabel(), getX() + stringX, getY() + stringY);
	}
	
	public void triggerEvent() {
		if(getLabel().toLowerCase().contains("start")) Igrica.playing = true;
		else if(getLabel().toLowerCase().contains("exit")) System.exit(0);
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
