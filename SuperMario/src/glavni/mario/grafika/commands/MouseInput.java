package glavni.mario.grafika.commands;

import java.awt.event.*;
import glavni.mario.*;
import glavni.mario.grafika.GUI.Button;

public class MouseInput implements MouseListener, MouseMotionListener{
	public int x, y;

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
		// nije koriscena jer clicked i pressed nije isto, pressed uzima vrednosti i tokom pomeranja misa, a clicked ne
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		for(int i=0;i<Igrica.launcher.buttons.length;i++) {
			Button button = Igrica.launcher.buttons[i];
			
			//pozivanje metode triggerEvent ako je mis u okviru button-a
			if(x>=button.getX()&&y>=button.getY()&&x<=button.getX()+button.getWidth()&&y<=button.getY()+button.getHeight()) 
				button.triggerEvent();
		}
	}

	public void mouseReleased(MouseEvent e) {
		
	}

}
