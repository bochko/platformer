package game.levels.premade;

import cairns.david.engine.Animation;
import cairns.david.engine.TileMap;
import game.actors.enemy.EnemyEntity;
import game.actors.player.PlayerEntity;
import game.core.PIDController;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelPuppeteer;
import game.subsidiaries.animations.EntityAnimationBundle;
import game.subsidiaries.visuals.HeadsUpDisplay;
import game.subsidiaries.visuals.SpriteMap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lazarus on 03/04/17.
 */
public class LevelOne implements Level {

    private final float GRAVITY = 0.001f;

    private Animation robot_idle_anim;
    private Animation robot_right_anim;
    private Animation robot_left_anim;
    private Animation enemy_green_anim;
    private EntityAnimationBundle player_animations;

    private HeadsUpDisplay hud;

    private PlayerEntity playerEntity = null;
    private EnemyEntity enemy = null;

    private Image background;
    private TileMap tmap = new TileMap();	// Our tile map, note that we load it in init()
    private SpriteMap backdrop_tmap = new SpriteMap();
    private SpriteMap foredrop_tmap = new SpriteMap();

    LevelPuppeteer levelPuppeteer;

    public LevelOne(int context_width, int context_height, float scale) {
        // load background image
        background = new ImageIcon("images/backgrounds/background.png").getImage();
        // Load the tile map and print it out so we can check it is valid
        foredrop_tmap.loadMap("maps", "foredrop1_map.txt");
        backdrop_tmap.loadMap("maps", "backdrop1_map.txt");
        tmap.loadMap("maps", "map.txt");

        // Load player animations

        robot_idle_anim = new Animation();
        robot_idle_anim.loadAnimationFromSheet("images/robot_idle_anim.PNG", 11, 1, 60);
        robot_left_anim = new Animation();
        robot_left_anim.loadAnimationFromSheet("images/robot_left_anim.PNG", 6, 1, 60);
        robot_right_anim = new Animation();
        robot_right_anim.loadAnimationFromSheet("images/robot_right_anim.PNG", 6, 1, 60);
        enemy_green_anim = new Animation();
        enemy_green_anim.loadAnimationFromSheet("images/green_alien_enemy.png", 1, 1, 60);
        player_animations = new EntityAnimationBundle(robot_idle_anim, robot_right_anim, robot_left_anim, robot_idle_anim, robot_idle_anim);

        // Initialise the playerEntity with an animation and base stats
        playerEntity = new PlayerEntity(player_animations, 0.06f);
        playerEntity.setAnimationSpeed(1.0f);
        playerEntity.setX(64);
        playerEntity.setY(132);
        playerEntity.setVelocityX(0.0f);
        playerEntity.setVelocityY(0.0f);
        playerEntity.show();

        // Initialize all enemies with an animation
        enemy = new EnemyEntity(enemy_green_anim, robot_idle_anim, 100, 0.04f);
        enemy.setAnimationSpeed(1.0f);
        enemy.setX(64);
        enemy.setY(132);
        enemy.setVelocityX(0.0f);
        enemy.setVelocityY(0.0f);
        enemy.show();

        // Add the hud to the renderable items
        hud = new HeadsUpDisplay(context_width, context_height, "images/hud/health_bar.png", "images/hud/score_pane.png", playerEntity);

        // initialize puppeteer and surrender sprites to it
        levelPuppeteer = new LevelPuppeteer(tmap, foredrop_tmap, backdrop_tmap, background, playerEntity, hud, scale);
        initializePuppeteer();
    }

    private void initializePuppeteer() {
        playerEntity.setMaster(levelPuppeteer);
        enemy.setMaster(levelPuppeteer);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy);
    }

    public void update(Long time_elapsed, PIDController pidController) {
        levelPuppeteer.enforceMovement(time_elapsed, GRAVITY, pidController);
        levelPuppeteer.updateAll(time_elapsed);
    }

    public void draw(Graphics2D g, int x_offset, int y_offset) {
        levelPuppeteer.enforceBackdropRender(g, x_offset, y_offset);
        levelPuppeteer.enforceTileMapRender(g, x_offset, y_offset);
        levelPuppeteer.enforcePlayerRender(g, x_offset, y_offset);
        levelPuppeteer.enforceSpriteRender(g, x_offset, y_offset);
        levelPuppeteer.enforceForedropRender(g, x_offset, y_offset);
        levelPuppeteer.enforceHUDRender(g);
    }


}
