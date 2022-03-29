package glavni.mario;

import javax.sound.sampled.*;

public class Sound {
		
	private Clip clip;
		
	public Sound(String path) {
		try {
			//kao kod citanja slika, odredjenoj vrednosti dodeljujemo naredbu da cita putanju zvuka i nakon toga je prosledjujemo i pustamo
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void play() {
		if(clip==null) return;
		// kada izvrsimo radnju klip ce automatski biti zaustavljen i trajace koliko i traje izvorno u datoteci
		clip.stop();	
		clip.setFramePosition(0);
		clip.start();
	}
		
	public void close() {
		stop();
		clip.close();
	}
		
	public void stop() {
		if(clip.isRunning()) 
			clip.stop();
	}
}
