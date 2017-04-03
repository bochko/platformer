package game.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import cairns.david.engine.*;
import game.actors.player.PlayerEntity;
import game.actors.projectiles.Projectile;
import game.actors.enemy.EnemyEntity;
import game.subsidiaries.visuals.DecorativeTileMap;
import sun.java2d.loops.FillRect;

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
    private Animation robot_idle_anim;
    private Animation robot_right_anim;
    private Animation robot_left_anim;
    private Animation enemy_green_anim;
    
    private PlayerEntity playerEntity = null;
    private EnemyEntity enemy = null;
    private ArrayList<Sprite> clouds = new ArrayList<>();

    private final LinkedList<Projectile> projectiles = new LinkedList<>();

    private TileMap tmap = new TileMap();	// Our tile map, note that we load it in init()
    private DecorativeTileMap backdrop_tmap = new DecorativeTileMap();
    private DecorativeTileMap foredrop_tmap = new DecorativeTileMap();
    
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

        Sprite s;	// Temporary reference to a sprite

        // Load the tile map and print it out so we can check it is valid
        foredrop_tmap.loadMap("maps", "foredrop1_map.txt");
        backdrop_tmap.loadMap("maps", "backdrop1_map.txt");
        tmap.loadMap("maps", "map.txt");


        // Create a set of background sprites that we can 
        // rearrange to give the illusion of motion
        
        robot_idle_anim = new Animation();
        robot_idle_anim.loadAnimationFromSheet("images/robot_idle_anim.PNG", 11, 1, 60);

        robot_left_anim = new Animation();
        robot_left_anim.loadAnimationFromSheet("images/robot_left_anim.PNG", 6, 1, 60);

        robot_right_anim = new Animation();
        robot_right_anim.loadAnimationFromSheet("images/robot_right_anim.PNG", 6, 1, 60);

        enemy_green_anim = new Animation();
        enemy_green_anim.loadAnimationFromSheet("images/green_alien_enemy.png", 1, 1, 60);

        
        // Initialise the playerEntity with an animation
        playerEntity = new PlayerEntity(robot_idle_anim, 100, 0.06f);

        enemy = new EnemyEntity(enemy_green_anim, 100, 0.04f);
        
        // Load a single cloud animation
        Animation ca = new Animation();
        ca.addFrame(loadImage("images/cloud.png"), 1000);

        initialiseGame();
      		
        System.out.println(tmap);
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initialiseGame()
    {
        playerEntity.setX(64);
        playerEntity.setY(132);
        playerEntity.setVelocityX(0.0f);
        playerEntity.setVelocityY(0.0f);
        playerEntity.show();
        enemy.setX(64);
        enemy.setY(132);
        enemy.setVelocityX(0.0f);
        enemy.setVelocityY(0.0f);
        enemy.show();

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

        
        //g.setColor(Color.black);
       // g.fillRect(0, 0, getWidth(), getHeight());
        
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
        accelerated_graphics.setColor(Color.black);
        accelerated_graphics.fillRect(0, 0, getWidth(), getHeight());

        accelerated_graphics.drawImage(background, 0, 0, null);

        // draw backdrop animations
        backdrop_tmap.draw(accelerated_graphics, xo, yo);
                
        // Apply offsets to tile map and draw  it
        tmap.draw(accelerated_graphics,xo,yo);

        // Apply offsets to playerEntity and draw
        playerEntity.setOffsets(xo, yo);
        playerEntity.draw(accelerated_graphics);

        enemy.setOffsets(xo, yo);
        enemy.draw(accelerated_graphics);

        Iterator<Projectile> iter = projectiles.iterator();
        synchronized (projectiles) {
            while (iter.hasNext()) {
                Projectile temp = iter.next();
                temp.setOffsets(xo, yo);
                temp.draw(accelerated_graphics);
            }
        }

        foredrop_tmap.draw(accelerated_graphics, xo, yo);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        g.drawImage(accelerated_buffer, at, null);
        accelerated_graphics.dispose();



        // Show score and status information
        /*String msg = String.format("Score: %d", total/100);
        g.setColor(Color.darkGray);
        g.drawString(msg, getWidth() - 80, 50);*/
    }

    /**
     * Update any sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
    public void update(long elapsed)
    {
    	
        // Make adjustments to the speed of the sprite due to gravity
        /*
        playerEntity.setVelocityY(playerEntity.getVelocityY()+(gravity*elapsed));
    	 */

        backdrop_tmap.updateDecorativeSprites(elapsed);
        foredrop_tmap.updateDecorativeSprites(elapsed);

       	playerEntity.setAnimationSpeed(1.0f);
       	playerEntity.buildMovement(tmap, elapsed, gravity, player_left, player_right, player_up, player_down);

       	enemy.setAnimationSpeed(1.0f);
       	enemy.buildMovement(tmap, elapsed, gravity, player_left, player_right, player_up, player_down);

       	// Log angle of movement
        // float angle_deg = xydiffcalc.getAngleFromDxDy(temp_dx, temp_dy);
        // System.out.println("angle(deg): " + angle_deg + " dx: " + temp_dx + " dy: " + temp_dy);

        // CHANGE SPRITE ANIMATIONS

        /* If control-right or control-left true, but not control-right and left simultaneously,
        as that should result in no movement on the x axis -> */
        if ((player_right || player_left) && !(player_right && player_left)) {
            // if control-right is pressed
            if(player_right) {
                playerEntity.setAnimation(robot_right_anim);
            }
            // else if control-left is presssed
            else if(player_left) {
                playerEntity.setAnimation(robot_left_anim);
            }

        }
        /* if neither control-right or control-left are pressed, then no movement on the
        x axis should occur. -> change the robot's animation to the bobbing one. */
        else {
            playerEntity.setAnimation(robot_idle_anim);
        }

                
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
