package glavni.mario;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import entiteti.Entitet;
import glavni.mario.grafika.*;
import glavni.mario.grafika.GUI.Launcher;
import glavni.mario.grafika.commands.KeyInput;
import glavni.mario.grafika.commands.MouseInput;
import podloga.Tile;

//ovo je glavna klasa, ali naziv je "igrica" da bi imalo logike
public class Igrica extends Canvas implements Runnable{

	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4;
	private static final String TITLE = "Super Mario";
	
	private Thread thread;
	private boolean running = false;
	
	private static BufferedImage[] levels;
	
	private static BufferedImage background;
	
	private static int playerX, playerY;
	public static int deathY;
	private static int level = 0;
	
	public static int coins = 0;
	public static int lives = 5;
	public static int bsodTime = 0;
	
	public static boolean bsod = true;
	public static boolean gameOver = false;
	public static boolean playing = false;
	
	public static Handler handler;
	public static SpriteSheet sheet;
	public static Kamera cam;
	public static Launcher launcher;
	
	public static KeyInput key;
	public static MouseInput mouse;
	
    public static Sprite grass;
    public static Sprite powerUp;
    public static Sprite usedPowerUp;
    public static Sprite mushroom;
    public static Sprite lifeMushroom;
    public static Sprite coin;
    public static Sprite star;
    public static Sprite flag;
    public static Sprite life;
    
    public static Sprite[] player;
    public static Sprite[] goomba;
    
    public static Sound jump;
    public static Sound damage;
    public static Sound death;
    public static Sound powerup;
    public static Sound win;
    
    public Igrica() {
    	//podesavanje dimenzije prozora
        Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
        //uz ovo prozor ce uvek biti iste dimenzije
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }
    
    private void init() {
    	//inicijalizacija
        handler = new Handler();
        sheet = new SpriteSheet("/spritesheet.png");
        cam = new Kamera();
        launcher = new Launcher();
        
        //dajemo mogucnost unosa sa tastature i misa
        key = new KeyInput();
        mouse = new MouseInput();
        
        addKeyListener(key);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        
        //dodajemo spritesheets, x i y osu citamo iz gimpa sa grida koji smo napravili
        grass = new Sprite(2,1,sheet);
        powerUp = new Sprite(8,1,sheet);
        usedPowerUp = new Sprite(7,1,sheet);
        
        mushroom = new Sprite(4,2,sheet);
        lifeMushroom = new Sprite(5,2,sheet);
        coin = new Sprite(6,2,sheet);
        star = new Sprite(3, 2, sheet);
        flag = new Sprite(8,2,sheet);
        life  = new Sprite (9,2,sheet);
        
      //duzina niza je 10 jer imamo 10 slicica u gimpu
        player = new Sprite[10];
        goomba = new Sprite[10];
        levels = new BufferedImage[3];
        
        for(int i=0;i<player.length;i++) {
            player[i] = new Sprite(i+1,16,sheet);
        }
        
        for(int i=0;i<goomba.length;i++) {
            goomba[i] = new Sprite(i+1,14,sheet);
        }
        
        try {
        	levels[0] = ImageIO.read(getClass().getResource("/level.png"));
        	levels[1] = ImageIO.read(getClass().getResource("/level2.png"));
        	levels[2] = ImageIO.read(getClass().getResource("/level3.png"));
        	background = ImageIO.read(getClass().getResource("/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        jump = new Sound("/audio/jump.wav");
        damage = new Sound("/audio/damage.wav");
        death = new Sound("/audio/death.wav");
        powerup = new Sound("/audio/power.wav");
        win = new Sound("/audio/win.wav");
        
    }
    
    //synchronized metode dozvoljavaju thread-u da radi odnosno da funkcionise uporedo sa main metodom
	private synchronized void start() {
		if(running) return;
		running = true;
		thread = new Thread(this,"Thread");
		thread.start();
	}
	
	private synchronized void stop() {
		if(!running) return;
		running = false;
		//zaustavlja thread, ali baca exception ako nesto nije u redu
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//metoda koja zahteva implementaciju zbog interfejsa Runnable
	public void run() {
		init();
		requestFocus();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		//prosecno vreme u nanosekundama koje je potrebno za 60 fps-a iznosi 1000000000/60
		double ns = 1000000000.0/60.0;
		double delta = 0;
		//jedna sekunda je 1 000 000 000 nanosekundi
		int frames = 0;
		int ticks = 0;
		//ovaj deo koda ima veze sa racunanjem frejmova i osvezavanja ekrana u sekundi, popravilu bi trebalo da ima oko 60 fps-a
		//i tako znamo da sa igricom nema problema, ne valja ako je vrednost premala jer ce igrica izbagovati i izaci, a ne valja
		//ni kada je previse fps-a, tipa 500 fps-a, jer bi se igrica tako jako tesko igrala
		while(running) {
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime = now;
			//ako je vreme izmedju sadasnjeg i prethodnog frejma predugo (duze od sekunde), osvezavamo frejm opet
			while(delta>=1) {
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			//jedan frejm je otp 16 milisekundi
			if(System.currentTimeMillis()-timer>1000) {
				timer+=1000;
				System.out.println(frames + " fps " + ticks + " updates per second");
				frames = 0;
				ticks = 0;
			}
		}
		stop();
	}
	
	public void render() {
		//renderovanje
		BufferStrategy bs = getBufferStrategy();
		if(bs==null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.drawImage(Igrica.coin.getBufferedImage(), 20, 20, 75, 75, null);
		g.drawImage(Igrica.life.getBufferedImage(), 20, 100, 75, 75, null);
		// setColor moze primiti i rgb vrednosti, a ne ovako
		//g.setColor(new Color(122,45,8)); //primer rgb
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier", Font.BOLD, 20));
		//biramo poziciju gde zelimo da dodamo string na ekran
		g.drawString("x " + Igrica.coins, 100, 95);
		g.drawString("x" + Igrica.lives, 100, 175);
		
		if(!bsod) {
			g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(),getHeight());
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			if(!gameOver) {
				g.drawImage(life.getBufferedImage(), 500, 310, 100, 100, null);
				g.drawString("x " + lives, 610, 390);
			} else if(gameOver) {
				g.drawString("GAME OVER!", 500, 300);
				g.drawString("Coins collected:" + coins, 420, 430);
				g.drawString("Better luck next time!", 375, 470);
		}
		}
		
		if(playing) 
			g.translate(cam.getX(), cam.getY());
		//ako nije ukljucen završni ekran i ako je igra zapoceta, renderuj igru
		if(!bsod&&playing) 
			handler.render(g);
		// ako igra nije zapoceta, renderuj pocetni ekran
		else if(!playing) 
			launcher.render(g);
		// prosledjujemo vrednosti baferu
		g.dispose();
		// dajemo baferu dozvolu za prikaz
		bs.show();
	}
	
	public void tick() {
		//apdejtovanje slike tj prozora
		//ako je igra zapoceta, renderuj i osvezavaj
		if(playing) 
			handler.tick();
		
		for(int i=0;i<handler.entity.size();i++) {
			Entitet e = handler.entity.get(i);
			if(e.getId()==Id.player) {
				cam.tick(e);
			}
		}
		
		if(bsod&&!gameOver&&playing) bsodTime++;
		if(bsodTime>=180) {
			bsodTime = 0;
			handler.clearLevel();
			handler.createLevel(levels[level]);
			bsod = false;
		}
	}
	
	public static int getFrameWidth() {
		return WIDTH*SCALE;
	}
	
	public static int getFrameHeight() {
		return HEIGHT*SCALE;
	}
	
	public static void switchLevel() {
		Igrica.level++;
		handler.clearLevel();
		handler.createLevel(levels[level]);
	}
	
	//metoda koja vraca pravougaonik koji zapravo predstavlja nas prozor igrice i koji ce pratiti koordinate igraca jer
	//je to ono sto kamera takodje prati
	public static Rectangle getVisibleArea() {
		for(int i=0;i<handler.entity.size();i++) {
			Entitet e = handler.entity.get(i);
			if(e.getId()==Id.player) {
					playerX = e.getX();
					playerY = e.getY();
			}
		}
		
		return new Rectangle(playerX-(getFrameWidth()/2-5),playerY-(getFrameHeight()/2-5),getFrameWidth()+10,getFrameHeight()+10);
	}
	
	//metoda koja nam sluzi da igrac nakon pada sa platforme gubi zivot
	public static int getDeathY() {
		LinkedList<Tile> tempList = handler.tile;
		
		//poredimo sve blokove i dobijamo njihovu visinu na kraju
		Comparator<Tile> tileSorter = new Comparator<Tile>() {

			public int compare(Tile t1, Tile t2) {
				if(t1.getY()>t2.getY()) return -1;
				if(t1.getY()<t2.getY()) return 1;
				return 0;
			}
			
		};
		
		Collections.sort(tempList,tileSorter);
		return tempList.getFirst().getY() + tempList.getFirst().getHeight();
	}
	
	public static void main(String[] args) {
		Igrica game = new Igrica();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();//sklapanje u celinu
		//podesavanje prozora
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
	}
	}
