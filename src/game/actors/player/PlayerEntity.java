package game.actors.player;

import cairns.david.engine.*;
import game.actors.mechanics.Ambulatory;
import game.actors.projectiles.Projectile;
import game.core.PIDController;
import game.levels.mechanics.LevelPuppeteer;
import game.physics.Collidable;
import game.physics.CollisionEngine;
import game.subsidiaries.animations.AnimationPicker;
import game.subsidiaries.animations.EntityAnimationBundle;

import java.awt.*;

/***
 * PlayerEntity class extends the Sprite class;
 * adds a layer of functionality useful for the player
 * entity and player movement, and collision.
 *
 * @ author Boyan Atanasov
 * @ see cairns.david.engine.Sprite
 */

public class PlayerEntity extends Sprite implements Collidable, Ambulatory {

    public static final int DEFAULT_BASE_DAMAGE = 25;
    public static final int DEFAULT_SPEED_MULTIPLIER = 2;
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final long PLAYER_DEFAULT_DAMAGE_TIMEGAP = 1250L;
    public static final long PLAYER_DEFAULT_START_DELAY = 1000L;
    public static final long PLAYER_SHOOT_DELAY = 500L;

    private int health_points;

    private float base_movement_speed;

    private float speed_multiplier;

    private boolean is_jumping = false;

    private int base_damage;

    private float damage_multiplier;

    private Velocity movement_velocity;;
    private LevelPuppeteer master;
    private EntityAnimationBundle animations;
    private long damage_counter;
    private long start_delay;
    private long projectile_timer;


    /***
     * Creates a new Sprite object with the specified Animation,
     * health points, and base movement speed.
     *
     * @param base_movement_speed PlayerEntity's base movement speed
     */
    public PlayerEntity(EntityAnimationBundle animations, float base_movement_speed) {
        // call the super constructor, at this point it is not effective to use the Sprite default constructor, but
        // I'll keep this here just for the sake of using the original code.
        super(animations.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_IDLE));
        // initialize all properties
        this.animations = animations;
        this.health_points = PLAYER_MAX_HEALTH;
        this.base_movement_speed = base_movement_speed;
        this.speed_multiplier = PlayerEntity.DEFAULT_SPEED_MULTIPLIER;
        this.base_damage = PlayerEntity.DEFAULT_BASE_DAMAGE;
        // initialize movement_velocity class to use
        movement_velocity = new Velocity();
        damage_counter = PLAYER_DEFAULT_DAMAGE_TIMEGAP;
        start_delay = PLAYER_DEFAULT_START_DELAY;
        projectile_timer = PLAYER_SHOOT_DELAY;


    }

    public void setMaster(LevelPuppeteer master) {
        this.master = master;
    }

    /***
     * Builds the movement of the sprite according to the directional key input
     * and combines those. Checks for collision with the contextual TileMap before
     * it applies the movement to itself.
     *
     * @param context the TileMap the sprite is rendered on at the moment;
     * @param time_elapsed time elapsed since last frame in ms
     */
    @SuppressWarnings("Duplicates")
    public void buildMovement(TileMap context, Long time_elapsed, float gravity, PIDController pidController) {

        if (start_delay > 0) return;

        float temp_dx = 0;
        float temp_dy = 0;

        if(this.getVelocityY() > 0) {
            is_jumping = true;
        }

        movement_velocity.setVelocity(0.0, 0.0);

        if (pidController.isRight()) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 0);
            temp_dx += (float) movement_velocity.getdx();
            temp_dy += (float) movement_velocity.getdy();
        }

        if (pidController.isLeft()) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 180);
            temp_dx += (float) movement_velocity.getdx();
            temp_dy += (float) movement_velocity.getdy();
        }

        if (pidController.isUp()) {
            if(!is_jumping) {
                Sound jump = new Sound("sounds/sfx/player_jump.wav");
                jump.start();
                is_jumping = true;
                movement_velocity.setVelocity(0.2 * getSpeed_multiplier(), 270);
                temp_dy += (float) movement_velocity.getdy();
            }
        }

            movement_velocity.setVelocity(gravity * time_elapsed, 90);
            temp_dy += (float) movement_velocity.getdy();

        int collision_type = CollisionEngine.simpleSpriteToMapCollision(this, context, temp_dx, super.getVelocityY() + temp_dy, time_elapsed);
        // Set sprite movement_velocity according to delta values calculated and collision type
        if (collision_type == CollisionEngine.COLLISION_NONE) {
            super.setVelocityX(temp_dx); // we want linear change in x
            super.setVelocityY(super.getVelocityY() + temp_dy); // we want forces adding up in y
        } else {
            if(collision_type == CollisionEngine.COLLISION_X_AXIS) {
                super.setVelocityY(super.getVelocityY() + temp_dy); // we want forces adding up in y
                super.setVelocityX(0f);

            }
            if(collision_type == CollisionEngine.COLLISION_Y_AXIS) {
                if(temp_dy + getVelocityY()  > 0) {
                    is_jumping = false;
                }
                super.setVelocityX(temp_dx);
                super.setVelocityY(0f);
                // and let gravity do its part

            }
            if(collision_type == CollisionEngine.COLLISION_BOTH_AXES) {

                super.stop();
                if(temp_dy + getVelocityY() > 0) {
                    is_jumping = false;
                }
            }
        }
        if(pidController.isMouse_primary() && projectile_timer <= 0) {
            Sound projectile_sound = new Sound("sounds/sfx/player_shoot.wav");
            projectile_sound.start();
            expelProjectile(pidController.getMouseX(), pidController.getMouseY());
            pidController.releaseMouseInput();
            projectile_timer = PLAYER_SHOOT_DELAY;
        }
        this.setAnimation(AnimationPicker.pickAnimation(animations, this));
    }

    @Override
    public void update(long time_elapsed) {
        super.update(time_elapsed);
        damage_counter -= time_elapsed;
        start_delay -= time_elapsed;
        projectile_timer -= time_elapsed;
    }

    /***
     * expells a Projectile
     * @param mouse_x the x position of the mouse, respective to the g raphics2D context
     * @param mouse_y the y position of the mouse, respective to the graphics2D context
     */
    public void expelProjectile(float mouse_x, float mouse_y) {
        System.out.println("x: " + (int)mouse_x + " y: " + (int)mouse_y);
        float origin_x = getX();
        float origin_y = getY();
        float dest_x = mouse_x/master.getScale_factor() - master.getOffsetX();
        float dest_y = mouse_y/master.getScale_factor() - master.getOffsetY();
        Velocity temp_proj_velocity = new Velocity();
        Animation proj_anim = new Animation();
        proj_anim.loadAnimationFromSheet("images/sprites/player_projectile_static.png",1, 1, 60);
        Animation proj_anim_dying = new Animation();
        proj_anim_dying.loadAnimationFromSheet("images/sprites/anim_player_projectile_dying.png", 10, 1 ,60);
        Projectile proj = new Projectile(proj_anim, proj_anim_dying, Projectile.ORIGIN_PLAYER);
        proj.setX(origin_x);
        proj.setY(origin_y);
        double angle = temp_proj_velocity.getAngle(proj.getX(), proj.getY(), dest_x, dest_y);
        temp_proj_velocity.setVelocity(0.5f, angle);
        proj.setVelocityX((float)temp_proj_velocity.getdx());
        proj.setVelocityY((float)temp_proj_velocity.getdy());
        proj.show();
        master.conjureProjectile(proj);
    }

    /***
     * Returns a Rectangle.Float to serve as the collision bounds
     *
     * @return a Rectangle.Float to serve as collision bounds
     */
    @Override
    public Rectangle.Float getCollisionBounds() {
        Rectangle.Float collision_bounds = new Rectangle.Float(getX() + 10, getY() + 5, getWidth() - 20, getHeight() - 7);
        return collision_bounds;
    }

    public float getSpeed_multiplier() {
        return speed_multiplier;
    }

    public float getBase_movement_speed() {
        return base_movement_speed;
    }

    public int getHealth_points() {
        return health_points;
    }

    public void setHealth_points(int health_points) {
        this.health_points = health_points;
    }

    public void applyHitByEnemyProjectile() {
        if (damage_counter <= 0L) {
            damage_counter = PLAYER_DEFAULT_DAMAGE_TIMEGAP;
            setHealth_points(getHealth_points() - 10);
        }
    }

    public void applyHitByEnemyEntity() {
        if (damage_counter <= 0L) {
            setHealth_points(getHealth_points() - 10);
            damage_counter = PLAYER_DEFAULT_DAMAGE_TIMEGAP;
        }

    }


}
