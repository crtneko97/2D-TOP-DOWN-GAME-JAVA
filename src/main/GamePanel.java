package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	//Screen settings
	
	final int originalTileSize = 16;
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeigth = tileSize * maxScreenRow; // 576 pixels

	// FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	Player player = new Player(this,keyH);
	
	
	// Set player's default position
	//int playerX = 100;
	//int playerY = 100;
	//int playerSpeed = 4;
	//We don't need these since we took care of the movement inside player class
	

	// Create constructor
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeigth));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true); //With this, this GamePanel can be "focused" to receive key input.
	}
	
	public void startGamethread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}


	@Override
	public void run() {

		//Delta method
		double drawInterval = 1000000000/FPS; // 0.01666 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	public void update() {

		player.update();

	}
	
	public void paintComponent(Graphics g) {
		
		
		
		//Graphics A class that has many functions to draw objects on the screen.
		super.paintComponent(g); //It's just a format whenever u use this paincompenent u need to type this.
		
		
		/* Graphics2D 
		 * Graphics2D class extends the Graphics class to provide more
		 * sophisticated control over geometry, coordinate transformations,
		 * color management, and text layout. 
		 */
		
		Graphics2D g2 = (Graphics2D)g; 
		
		
		tileM.draw(g2);
		
		player.draw(g2);

		/*dispose()
		 * Dispose of this graphics context and 
		 * release any system resources that it is using.
		 */
		
		g2.dispose();
		
	}

}
