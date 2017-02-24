package game.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cairns.david.engine.*;
import game.actors.Player;
import game.collision.CollisionEngine;

import javax.imageio.ImageIO;
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

public class Core extends GameCore
{
	// Useful game constants
	static int SCREEN_WIDTH = 512;
    static int SCREEN_HEIGHT = 384;

    float 	lift = 0.0f;
    float	gravity = 0.0f;

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
    
    private Player player = null;
    private ArrayList<Sprite> clouds = new ArrayList<Sprite>();

    private TileMap tmap = new TileMap();	// Our tile map, note that we load it in init()
    
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
        gct.run(false, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {

        Sprite s;	// Temporary reference to a sprite

        // Load the tile map and print it out so we can check it is valid
        tmap.loadMap("maps", "map.txt");

        // Create a set of background sprites that we can 
        // rearrange to give the illusion of motion
        
        robot_idle_anim = new Animation();
        robot_idle_anim.loadAnimationFromSheet("images/robot_idle_anim.PNG", 11, 1, 60);

        robot_left_anim = new Animation();
        robot_left_anim.loadAnimationFromSheet("images/robot_left_anim.PNG", 6, 1, 60);

        robot_right_anim = new Animation();
        robot_right_anim.loadAnimationFromSheet("images/robot_right_anim.PNG", 6, 1, 60);
        
        // Initialise the player with an animation
        player = new Player(robot_idle_anim, 100, 0.08f);
        
        // Load a single cloud animation
        Animation ca = new Animation();
        ca.addFrame(loadImage("images/cloud.png"), 1000);
        
        // Create 3 clouds at random positions off the screen
        // to the right
        for (int c=0; c<3; c++)
        {
        	s = new Sprite(ca);
        	s.setX(SCREEN_WIDTH + (int)(Math.random()*200.0f));
        	s.setY(30 + (int)(Math.random()*150.0f));
        	s.setVelocityX(-0.02f);
        	s.show();
        	clouds.add(s);
        }

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
    	total = 0;
    	      
        player.setX(64);
        player.setY(132);
        player.setVelocityX(0.0f);
        player.setVelocityY(0.0f);
        player.show();
    }
    
    /**
     * Draw the current state of the game
     */
    @Override
    public void draw(Graphics2D g)
    {
        Image background = new ImageIcon("images/galaxy.png").getImage();

        // Be careful about the order in which you draw objects - you
    	// should draw the background first, then work your way 'forward'

    	// First work out how much we need to shift the view 
    	// in order to see where the player is.
        int xo = 0;
        int yo = 0;

        // If relative, adjust the offset so that
        // it is relative to the player

        // ...?
        g.drawImage(background, 0, 0, null);
        
        //g.setColor(Color.white);
        //g.fillRect(0, 0, getWidth(), getHeight());
        
        // Apply offsets to sprites then draw them
        for (Sprite s: clouds)
        {
        	s.setOffsets(xo,yo);
        	s.draw(g);
        }


                
        // Apply offsets to tile map and draw  it
        tmap.draw(g,xo,yo);

        // Apply offsets to player and draw
        player.setOffsets(xo, yo);
        player.draw(g);
        
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
        player.setVelocityY(player.getVelocityY()+(gravity*elapsed));
    	 */

       	player.setAnimationSpeed(1.0f);
       	/*
       	// DELTAX DELTAY SYSTEM ALERT DEFCON ALPHA OPTIMUS
        float temp_dx = 0;
        float temp_dy = 0;
        xydiffcalc.setVelocity(0.0, 0.0);

       	if (player_up) {
            xydiffcalc.setVelocity(0.08f, 270);
            temp_dx += (float)xydiffcalc.getdx();
            temp_dy += (float)xydiffcalc.getdy();

       	}

       	if (player_right) {
            xydiffcalc.setVelocity(0.08f, 0);
            temp_dx += (float)xydiffcalc.getdx();
            temp_dy += (float)xydiffcalc.getdy();
        }

        if (player_left) {
            xydiffcalc.setVelocity(0.08f, 180);
            temp_dx += (float)xydiffcalc.getdx();
            temp_dy += (float)xydiffcalc.getdy();
        }

        if (player_down) {
            xydiffcalc.setVelocity(0.08f, 90);
            temp_dx += (float)xydiffcalc.getdx();
            temp_dy += (float)xydiffcalc.getdy();
        }
        // ROUND DX and DY for the sake of my sanity to 5 digits after decimal

    //        if((temp_dx > 0 && temp_dx < 0.0001)|| (temp_dx < 0 && temp_dx > -0.0001)) temp_dx = 0f;
      //  if((temp_dy > 0 && temp_dy < 0.0001)|| (temp_dy < 0 && temp_dy > -0.0001)) temp_dy = 0f;

        int collision_type = CollisionEngine.checkSimpleCollision(player, tmap, temp_dx, temp_dy, elapsed);

        // Set sprite velocity according to delta values calculated and collision type
        if (collision_type == CollisionEngine.COLLISION_NONE) {
            player.setVelocityX(temp_dx);
            player.setVelocityY(temp_dy);
        } else {
            if(collision_type == CollisionEngine.COLLISION_X_AXIS) {
                player.setVelocityY(temp_dy);
                player.setVelocityX(0f);

            }
            if(collision_type == CollisionEngine.COLLISION_Y_AXIS) {
                player.setVelocityX(temp_dx);
                player.setVelocityY(0f);
            }
            if(collision_type == CollisionEngine.COLLISION_BOTH_AXES) {
                player.stop();
            }
        }
*/
       	player.buildMovement(tmap, elapsed, player_left, player_right, player_up, player_down);

       	// Log angle of movement
        // float angle_deg = xydiffcalc.getAngleFromDxDy(temp_dx, temp_dy);
        // System.out.println("angle(deg): " + angle_deg + " dx: " + temp_dx + " dy: " + temp_dy);

        // CHANGE SPRITE ANIMATIONS

        /* If control-right or control-left true, but not control-right and left simultaneously,
        as that should result in no movement on the x axis -> */
        if ((player_right || player_left) && !(player_right && player_left)) {
            // if control-right is pressed
            if(player_right) {
                player.setAnimation(robot_right_anim);
            }
            // else if control-left is presssed
            else if(player_left) {
                player.setAnimation(robot_left_anim);
            }

        }
        /* if neither control-right or control-left are pressed, then no movement on the
        x axis should occur. -> change the robot's animation to the bobbing one. */
        else {
            player.setAnimation(robot_idle_anim);
        }

                
       	for (Sprite s: clouds)
       		s.update(elapsed);
       	
        // Now update the sprites animation and position
        player.update(elapsed);
       
        // Then check for any collisions that may have occurred
        handleTileMapCollisions(player,elapsed);
         	
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
    	// now it just checks if the player has gone off the bottom
    	// of the tile map.
    	
        if (player.getY() + player.getHeight() > tmap.getPixelHeight())
        {
        	// Put the player back on the map
        	player.setY(tmap.getPixelHeight() - player.getHeight());
        	
        	// and make them bounce
        	player.setVelocityY(-player.getVelocityY() * (0.03f * elapsed));
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
    		Sound s = new Sound("sounds/caw.wav");
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
}
