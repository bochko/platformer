package game.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.Iterator;
import java.util.ListIterator;

import cairns.david.engine.*;
import game.actors.projectiles.Projectile;

import javax.swing.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author David Cairns
 *
 */
@SuppressWarnings("serial")

public class Core extends GameCore implements MouseListener
{
	// Useful game constants
	static int SCREEN_WIDTH = 512;
    static int SCREEN_HEIGHT = 384;

    static int FRAME_WIDTH = 1024;
    static int FRAME_HEIGHT = 768;

    static float scale = 0;

    float lift = 0.0f;
    float gravity = 0.001f;

    /* Velocity instance, added to calculate delta-x and delta-y,
    according to button presses, instead of hardcoding movement */
    private Velocity xydiffcalc = new Velocity();
    
    // Game state flags
    private boolean player_up = false;
    private boolean player_down = false;
    private boolean player_left = false;
    private boolean player_right = false;

    // Game resources

    
    private long total;         			// The score will be the total time elapsed since a crash


    /**
	 * The obligatory main method that creates
     * an instance of our class and starts it running
     * 
     * @param args	The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {

        Core gct = new Core();
        gct.init();
        // Start in windowed mode with the given screen height and width
        gct.run(false, FRAME_WIDTH, FRAME_HEIGHT);
    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {
        scale = FRAME_WIDTH/SCREEN_WIDTH;
        // set cursor to a simple crosshair
        setCursor(Cursor.CROSSHAIR_CURSOR);
        // add itself as a mouse listener
        this.addMouseListener(this);

        initializeLevel();
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initializeLevel()
    {

    }
    
    /**
     * Draw the current state of the game
     */
    @Override
    public void draw(Graphics2D g)
    {
        super.paint(g); // calling super.paint NEEDS TO BE CALLED IN ORDER TO utilize the default rendering settings of JFrame, which include double buffering
        Image background = new ImageIcon("images/backgrounds/background.png").getImage();

        // Be careful about the order in which you draw objects - you
    	// should draw the background first, then work your way 'forward'

    	// First work out how much we need to shift the view 
    	// in order to see where the playerEntity is.
        int xo = 0;
        int yo = 0;

        // If relative, adjust the offset so that
        // it is relative to the playerEntity

        // ...?
        
        // Apply offsets to sprites then draw them
        /*for (Sprite s: clouds)
        {
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }*/

        /////////LOOK UP VOLATILE IMAGE FOR GRAPHICS ACCELLERATION\\\\\\\\\\\

        VolatileImage accelerated_buffer = createVolatileImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        Graphics2D accelerated_graphics = accelerated_buffer.createGraphics();
        accelerated_graphics.setClip(0, 0, getWidth(), getHeight());

        // LEVEL + PUPPETEER WORK HERE

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        g.drawImage(accelerated_buffer, at, null);
        accelerated_graphics.dispose();
    }

    /**
     * Update any sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
    public void update(long elapsed)
    {


        backdrop_tmap.updateDecorativeSprites(elapsed);
        foredrop_tmap.updateDecorativeSprites(elapsed);


       	playerEntity.buildMovement(tmap, elapsed, gravity, player_left, player_right, player_up, player_down);

       	enemy.setAnimationSpeed(1.0f);
       	enemy.buildMovement(tmap, elapsed, gravity, player_left, player_right, player_up, player_down);



       	for (Sprite s: clouds)
       		s.update(elapsed);

        // Now update the sprites animation and position
        playerEntity.update(elapsed);
        enemy.update(elapsed);

        ListIterator<Projectile> iter = projectiles.listIterator();
        synchronized (projectiles) {
            while (iter.hasNext()){
                Projectile temp = iter.next();
                temp.update(elapsed);
            }
        }
       
        // Then check for any collisions that may have occurred
        handleTileMapCollisions(playerEntity,elapsed);

        hud.update(playerEntity);
         	
    }



    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'. Initial functionality is limited...
     * 
     * @param s			The Sprite to check collisions for
     * @param elapsed	How time has gone by
     */
    public void handleTileMapCollisions(Sprite s, long elapsed)
    {
    	// This method should check actual tile map collisions. For
    	// now it just checks if the playerEntity has gone off the bottom
    	// of the tile map.
    	
        if (playerEntity.getY() + playerEntity.getHeight() > tmap.getPixelHeight())
        {
        	// Put the playerEntity back on the map
        	playerEntity.setY(tmap.getPixelHeight() - playerEntity.getHeight());
        	
        	// and make them bounce
        	playerEntity.setVelocityY(-playerEntity.getVelocityY() * (0.03f * elapsed));
        }
    }
    
    
     
    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     * 
     *  @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e)
    { 
    	int key = e.getKeyCode();
    	
    	if (key == KeyEvent.VK_ESCAPE) stop();
    	
    	if (key == KeyEvent.VK_UP) player_up = true;

    	if (key == KeyEvent.VK_RIGHT) player_right = true;

    	if (key == KeyEvent.VK_LEFT) player_left = true;

    	if (key == KeyEvent.VK_DOWN) player_down = true;
    	   	
    	if (key == KeyEvent.VK_S)
    	{
    		// Example of playing a sound as a thread
    		Sound s = new Sound("sounds/soundtrack/mainmenu.wav");
    		s.start();
    	}
    }

    public boolean boundingBoxCollision(Sprite s1, Sprite s2)
    {
    	return false;   	
    }


	public void keyReleased(KeyEvent e) { 

		int key = e.getKeyCode();

		// Switch statement instead of lots of ifs...
		// Need to use break to prevent fall through.
		switch (key)
		{
			case KeyEvent.VK_ESCAPE : stop(); break;
			case KeyEvent.VK_UP     : player_up = false; break;
            case KeyEvent.VK_RIGHT  : player_right = false; break;
            case KeyEvent.VK_LEFT   : player_left = false; break;
            case KeyEvent.VK_DOWN   : player_down = false; break;
			default :  break;
		}
	}

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (int) (e.getX()/scale);
        int y = (int) (e.getY()/scale);
        System.out.println("Cursor pos: " + x + y);
        Animation proj_anim = new Animation();
        proj_anim.loadAnimationFromSheet("images/green_alien_enemy.png",1, 1, 60);
        Projectile proj = new Projectile(proj_anim);
        proj.setX(playerEntity.getX());
        proj.setY(playerEntity.getY());
        double angle = xydiffcalc.getAngle(proj.getX(), proj.getY(), x, y);
        xydiffcalc.setVelocity(0.5f, angle);
        proj.setVelocityX((float)xydiffcalc.getdx());
        proj.setVelocityY((float)xydiffcalc.getdy());
        proj.show();

        synchronized (projectiles) {
            projectiles.add(proj);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
