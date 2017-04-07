package game.levels.premade;

import cairns.david.engine.Animation;
import cairns.david.engine.TileMap;
import game.actors.enemy.EnemyEntity;
import game.actors.mechanics.SignalSprite;
import game.actors.player.PlayerEntity;
import game.core.PIDController;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelBundle;
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

    private SignalSprite end_level_sprite;
    private Animation anim_end_level_sprite;

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
        reinitialize(context_width, context_height, scale);
    }

    public void reinitialize(int context_width, int context_height, float scale) {
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
        enemy.setX(117);
        enemy.setY(250);
        enemy.setVelocityX(0.0f);
        enemy.setVelocityY(0.0f);
        enemy.show();

        enemy2 = new EnemyEntity(enemy_animations, 0.03f);
        enemy2.setAnimationSpeed(0.8f);
        enemy2.setX(550);
        enemy2.setY(172);
        enemy2.setVelocityX(0.0f);
        enemy2.setVelocityY(0.0f);
        enemy2.show();

        enemy3 = new EnemyEntity(enemy_animations, 0.05f);
        enemy3.setAnimationSpeed(1.2f);
        enemy3.setX(600);
        enemy3.setY(172);
        enemy3.setVelocityX(0.0f);
        enemy3.setVelocityY(0.0f);
        enemy3.show();

        enemy4 = new EnemyEntity(enemy_animations, 0.02f);
        enemy4.setAnimationSpeed(0.6f);
        enemy4.setX(672);
        enemy4.setY(42);
        enemy4.setVelocityX(0.0f);
        enemy4.setVelocityY(0.0f);
        enemy4.show();

        enemy5 = new EnemyEntity(enemy_animations, 0.03f);
        enemy5.setAnimationSpeed(0.8f);
        enemy5.setX(900);
        enemy5.setY(42);
        enemy5.setVelocityX(0.0f);
        enemy5.setVelocityY(0.0f);
        enemy5.show();

        enemy6 = new EnemyEntity(enemy_animations, 0.06f);
        enemy6.setAnimationSpeed(1.4f);
        enemy6.setX(940);
        enemy6.setY(42);
        enemy6.setVelocityX(0.0f);
        enemy6.setVelocityY(0.0f);
        enemy6.show();

        enemy7 = new EnemyEntity(enemy_animations, 0.05f);
        enemy7.setAnimationSpeed(1.2f);
        enemy7.setX(1000);
        enemy7.setY(140);
        enemy7.setVelocityX(0.0f);
        enemy7.setVelocityY(0.0f);
        enemy7.show();

        enemy8 = new EnemyEntity(enemy_animations, 0.05f);
        enemy8.setAnimationSpeed(1.2f);
        enemy8.setX(1040);
        enemy8.setY(140);
        enemy8.setVelocityX(0.0f);
        enemy8.setVelocityY(0.0f);
        enemy8.show();

        enemy9 = new EnemyEntity(enemy_animations, 0.05f);
        enemy9.setAnimationSpeed(1.2f);
        enemy9.setX(1600);
        enemy9.setY(240);
        enemy9.setVelocityX(0.0f);
        enemy9.setVelocityY(0.0f);
        enemy9.show();

        enemy10 = new EnemyEntity(enemy_animations, 0.02f);
        enemy10.setAnimationSpeed(1.2f);
        enemy10.setX(1600);
        enemy10.setY(140);
        enemy10.setVelocityX(0.0f);
        enemy10.setVelocityY(0.0f);
        enemy10.show();

        enemy11 = new EnemyEntity(enemy_animations, 0.07f);
        enemy11.setAnimationSpeed(1.2f);
        enemy11.setX(1600);
        enemy11.setY(140);
        enemy11.setVelocityX(0.0f);
        enemy11.setVelocityY(0.0f);
        enemy11.show();

        // Create end-level sprite
        anim_end_level_sprite = new Animation();
        anim_end_level_sprite.loadAnimationFromSheet("images/sprites/consumable_speed.png", 10, 1, 120);
        end_level_sprite = new SignalSprite(anim_end_level_sprite, LevelBundle.SIGNAL_NEXT_LEVEL);
        end_level_sprite.setX(1980);
        end_level_sprite.setY(152);
        end_level_sprite.setVelocityX(0.0f);
        end_level_sprite.setVelocityY(0.0f);
        end_level_sprite.show();

        // Add the hud to the renderable items
        hud = new HeadsUpDisplay(context_width, context_height, "images/hud/health_bar.png", playerEntity);

        // initialize puppeteer and surrender sprites to it
        levelPuppeteer = new LevelPuppeteer(tmap, foredrop_tmap, backdrop_tmap, background, playerEntity, hud, scale);
        initializePuppeteer();
    }

    @Override
    public void setController(PIDController controller) {
        return;
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
        enemy9.setMaster(levelPuppeteer);
        enemy10.setMaster(levelPuppeteer);
        enemy11.setMaster(levelPuppeteer);

        levelPuppeteer.surrenderSpriteToJurisdiction(enemy);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy2);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy3);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy4);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy5);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy6);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy7);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy8);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy9);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy10);
        levelPuppeteer.surrenderSpriteToJurisdiction(enemy11);
        levelPuppeteer.surrenderSpriteToJurisdiction(end_level_sprite);
    }


    public void update(Long time_elapsed, PIDController pidController) {


        levelPuppeteer.enforceMovement(time_elapsed, GRAVITY, pidController);
        levelPuppeteer.updateAll(time_elapsed);
    }

    public void draw(Graphics2D g) {
        int x_offset = context_width/2 - Math.round(levelPuppeteer.snoopOnPlayerPosition().x);
        x_offset = Math.min(x_offset, 0);
        x_offset = Math.max(context_width - tmap.getPixelWidth(), x_offset);

        int y_offset = context_height/2 - Math.round(levelPuppeteer.snoopOnPlayerPosition().y);
        y_offset = Math.min(y_offset, 0);
        y_offset = Math.max(context_height - tmap.getPixelHeight(), y_offset);

        levelPuppeteer.informPuppeteerOffsets(x_offset, y_offset);

        levelPuppeteer.enforceBackgroundRender(g);
        levelPuppeteer.enforceBackdropRender(g);
        levelPuppeteer.enforceTileMapRender(g);
        levelPuppeteer.enforcePlayerRender(g);
        levelPuppeteer.enforceSpriteRender(g);
        levelPuppeteer.enforceForedropRender(g);
        levelPuppeteer.enforceHUDRender(g);
    }

    public void setSignal(int signal) {
        levelPuppeteer.setSignal(signal);
    }

    public int readSignal() {
        return levelPuppeteer.readSignal();
    }
}
