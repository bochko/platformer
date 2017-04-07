package game.core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import cairns.david.engine.*;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelBundle;
import game.levels.premade.LevelOne;
import game.levels.premade.LevelTwo;
import game.subsidiaries.audio.LoopingSound;


@SuppressWarnings("serial")
public class Core extends GameCore
{

	// Useful game constants
	private static final int SCREEN_WIDTH = 512;
    private static final int SCREEN_HEIGHT = 384;
    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;
    private float scale = 0;

    // PID Controller
    private static final PIDController pidController = new PIDController();

    // Game soundtrack
    private LoopingSound soundtrack;

    // Levels
    private LevelBundle levels;
    private Level level1;
    private Level level2;

    // Current level
    private Level level;
    private static int current_level_key = LevelBundle.MENU_INDEX;

    // Scalable buffer
    private BufferedImage non_accelerated_buffer;
    // Graphics buffer
    private Graphics2D non_accelerated_graphics;
    // Transform to be configured from scale
    AffineTransform at;

    /**
	 * The obligatory main method that creates
     * an instance of our class and starts it running
     * 
     * @param args	The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {

        Core gct = new Core();
        gct.init();
        gct.run(false, FRAME_WIDTH, FRAME_HEIGHT);

    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init()
    {
        scale = FRAME_WIDTH/SCREEN_WIDTH;
        // add levels to the bundle
        levels = new LevelBundle();
        level2 = new LevelOne(SCREEN_WIDTH, SCREEN_HEIGHT, scale);
        level1 = new LevelTwo(SCREEN_WIDTH, SCREEN_HEIGHT, scale);
        levels.addLevel(LevelBundle.MENU_INDEX, level1);
        levels.addLevel(LevelBundle.MENU_INDEX + 1, level2);
        setCursor(Cursor.CROSSHAIR_CURSOR);
        this.addMouseListener(pidController);
        this.addKeyListener(pidController);
        non_accelerated_buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        soundtrack = new LoopingSound("sounds/soundtrack/mainmenu.wav");
        soundtrack.start();
        initializeLevel();
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initializeLevel() {
            level = levels.getLevel(current_level_key);
            level.setSignal(LevelBundle.SIGNAL_CONTINUE);
    }

    public void restartLevel() {
            level.reinitialize(SCREEN_WIDTH, SCREEN_HEIGHT, scale);
    }
    
    /**
     * Draw the current state of the game
     */
    @Override
    public void draw(Graphics2D g)
    {
        super.paint(g); // calling super.paint NEEDS TO BE CALLED IN ORDER TO utilize the default rendering settings of JFrame, which include double buffering
        non_accelerated_graphics = non_accelerated_buffer.createGraphics();
        non_accelerated_graphics.setClip(0, 0, getWidth(), getHeight());
        at = new AffineTransform();
        non_accelerated_graphics.clearRect(0, 0, getWidth(), getHeight());
        try {
            level.draw(non_accelerated_graphics);
        } catch (Exception e) {
            System.out.println("Level couldn't load in time for draw");
        }
        at.scale(scale, scale);
        g.drawImage(non_accelerated_buffer, at, null);

        //read level1 signal, delivered by the signal sprite
        if(level.readSignal() != LevelBundle.SIGNAL_CONTINUE) {
            if(level.readSignal() == LevelBundle.SIGNAL_MAIN_MENU) {
                current_level_key = LevelBundle.MENU_INDEX;
                initializeLevel();
            } else if (level.readSignal() == LevelBundle.SIGNAL_NEXT_LEVEL) {
                current_level_key += 1;
                initializeLevel();
            } else if (level.readSignal() == LevelBundle.SIGNAL_RESTART_LEVEL) {
                Sound restart = new Sound("sounds/sfx/level_restart.wav");
                restart.start();
                restartLevel();
            }
        }
    }

    /**
     * Update any sprites and check for collisions
     * 
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */    
    public void update(long elapsed)
    {
        level.update(elapsed, pidController);
    }
}
