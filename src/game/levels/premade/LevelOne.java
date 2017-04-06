package game.levels.premade;

import cairns.david.engine.Animation;
import cairns.david.engine.TileMap;
import game.actors.enemy.EnemyEntity;
import game.actors.player.PlayerEntity;
import game.core.PIDController;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelPuppeteer;
import game.subsidiaries.animations.EntityAnimationBundle;
import game.subsidiaries.audio.SoundBundle;
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
    private Animation enemy_idle_anim;
    private Animation enemy_left_anim;
    private Animation enemy_right_anim;
    private EntityAnimationBundle player_animations;
    private EntityAnimationBundle enemy_animations;

    private HeadsUpDisplay hud;

    private PlayerEntity playerEntity = null;
    private EnemyEntity enemy = null;
    private EnemyEntity enemy2 = null;
    private EnemyEntity enemy3 = null;
    private EnemyEntity enemy4 = null;
    private EnemyEntity enemy5 = null;
    private EnemyEntity enemy6 = null;
    private EnemyEntity enemy7 = null;
    private EnemyEntity enemy8 = null;
    private EnemyEntity enemy9 = null;
    private EnemyEntity enemy10 = null;
    private EnemyEntity enemy11 = null;

    private Image background;
    private TileMap tmap = new TileMap();	// Our tile map, note that we load it in init()
    private SpriteMap backdrop_tmap = new SpriteMap();
    private SpriteMap foredrop_tmap = new SpriteMap();


    private int context_width;
    private int context_height;

    LevelPuppeteer levelPuppeteer;

    public LevelOne(int context_width, int context_height, float scale) {
        this.context_height = context_height;
        this.context_width = context_width;
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
        player_animations = new EntityAnimationBundle(robot_idle_anim, robot_right_anim, robot_left_anim, robot_idle_anim, robot_idle_anim);

        // Load enemy animations
        enemy_idle_anim = new Animation();
        enemy_idle_anim.loadAnimationFromSheet("images/sprites/enemy_idle.png", 1, 1, 1000);
        enemy_left_anim = new Animation();
        enemy_left_anim.loadAnimationFromSheet("images/sprites/enemy_walk_anim_left.png", 4, 1, 400);
        enemy_right_anim = new Animation();
        enemy_right_anim.loadAnimationFromSheet("images/sprites/enemy_walk_anim_right.png", 4, 1, 400);
        enemy_animations = new EntityAnimationBundle(enemy_idle_anim, enemy_right_anim, enemy_left_anim, enemy_idle_anim, enemy_idle_anim);

        // Initialise the playerEntity with an animation and base stats
        playerEntity = new PlayerEntity(player_animations, 0.06f);
        playerEntity.setAnimationSpeed(1.0f);
        playerEntity.setX(64);
        playerEntity.setY(132);
        playerEntity.setVelocityX(0.0f);
        playerEntity.setVelocityY(0.0f);
        playerEntity.show();

        // Initialize all enemies with an animation
        enemy = new EnemyEntity(enemy_animations, 0.04f);
        enemy.setAnimationSpeed(1.0f);
        enemy.setX(64);
        enemy.setY(132);
        enemy.setVelocityX(0.0f);
        enemy.setVelocityY(0.0f);
        enemy.show();

        enemy2 = new EnemyEntity(enemy_animations, 0.03f);
        enemy2.setAnimationSpeed(0.8f);
        enemy2.setX(550);
        enemy2.setY(180);
        enemy2.setVelocityX(0.0f);
        enemy2.setVelocityY(0.0f);
        enemy2.show();

        enemy3 = new EnemyEntity(enemy_animations, 0.05f);
        enemy3.setAnimationSpeed(1.2f);
        enemy3.setX(600);
        enemy3.setY(180);
        enemy3.setVelocityX(0.0f);
        enemy3.setVelocityY(0.0f);
        enemy3.show();

        enemy4 = new EnemyEntity(enemy_animations, 0.02f);
        enemy4.setAnimationSpeed(0.6f);
        enemy4.setX(672);
        enemy4.setY(47);
        enemy4.setVelocityX(0.0f);
        enemy4.setVelocityY(0.0f);
        enemy4.show();

        enemy5 = new EnemyEntity(enemy_animations, 0.03f);
        enemy5.setAnimationSpeed(0.8f);
        enemy5.setX(900);
        enemy5.setY(47);
        enemy5.setVelocityX(0.0f);
        enemy5.setVelocityY(0.0f);
        enemy5.show();

        enemy6 = new EnemyEntity(enemy_animations, 0.06f);
        enemy6.setAnimationSpeed(1.4f);
        enemy6.setX(940);
        enemy6.setY(47);
        enemy6.setVelocityX(0.0f);
        enemy6.setVelocityY(0.0f);
        enemy6.show();

        enemy7 = new EnemyEntity(enemy_animations, 0.05f);
        enemy7.setAnimationSpeed(1.2f);
        enemy7.setX(1000);
        enemy7.setY(143);
        enemy7.setVelocityX(0.0f);
        enemy7.setVelocityY(0.0f);
        enemy7.show();

        enemy8 = new EnemyEntity(enemy_animations, 0.05f);
        enemy8.setAnimationSpeed(1.2f);
        enemy8.setX(1040);
        enemy8.setY(143);
        enemy8.setVelocityX(0.0f);
        enemy8.setVelocityY(0.0f);
        enemy8.show();

        enemy3 = new EnemyEntity(enemy_animations, 0.05f);
        enemy3.setAnimationSpeed(1.2f);
        enemy3.setX(600);
        enemy3.setY(180);
        enemy3.setVelocityX(0.0f);
        enemy3.setVelocityY(0.0f);
        enemy3.show();

        enemy3 = new EnemyEntity(enemy_animations, 0.05f);
        enemy3.setAnimationSpeed(1.2f);
        enemy3.setX(600);
        enemy3.setY(180);
        enemy3.setVelocityX(0.0f);
        enemy3.setVelocityY(0.0f);
        enemy3.show();

        enemy3 = new EnemyEntity(enemy_animations, 0.05f);
        enemy3.setAnimationSpeed(1.2f);
        enemy3.setX(600);
        enemy3.setY(180);
        enemy3.setVelocityX(0.0f);
        enemy3.setVelocityY(0.0f);
        enemy3.show();

        // Add the hud to the renderable items
        hud = new HeadsUpDisplay(context_width, context_height, "images/hud/health_bar.png", "images/hud/score_pane.png", playerEntity);

        // initialize puppeteer and surrender sprites to it
        levelPuppeteer = new LevelPuppeteer(tmap, foredrop_tmap, backdrop_tmap, background, playerEntity, hud, scale);
        initializePuppeteer();
    }

    private void initializePuppeteer() {
        playerEntity.setMaster(levelPuppeteer);
        enemy.setMaster(levelPuppeteer);
        enemy2.setMaster(levelPuppeteer);
        enemy3.setMaster(levelPuppeteer);
        enemy4.setMaster(levelPuppeteer);
        enemy5.setMaster(levelPuppeteer);
        enemy6.setMaster(levelPuppeteer);
        enemy7.setMaster(levelPuppeteer);
        enemy8.setMaster(levelPuppeteer);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy2);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy3);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy4);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy5);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy6);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy7);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy8);
    }

    public void update(Long time_elapsed, PIDController pidController) {


        levelPuppeteer.enforceMovement(time_elapsed, GRAVITY, pidController);
        levelPuppeteer.updateAll(time_elapsed);
    }

    public void draw(Graphics2D g) {
        int x_offset = context_width/2 - Math.round(levelPuppeteer.snoopOnPlayerPosition().x);
        x_offset = Math.min(x_offset, 0);
        x_offset = Math.max(context_width - tmap.getPixelWidth(), x_offset);

        int y_offset = 0;

        levelPuppeteer.informPuppeteerOffsets(x_offset, y_offset);

        levelPuppeteer.enforceBackgroundRender(g);
        levelPuppeteer.enforceBackdropRender(g);
        levelPuppeteer.enforceTileMapRender(g);
        levelPuppeteer.enforcePlayerRender(g);
        levelPuppeteer.enforceSpriteRender(g);
        levelPuppeteer.enforceForedropRender(g);
        levelPuppeteer.enforceHUDRender(g);
    }


}
