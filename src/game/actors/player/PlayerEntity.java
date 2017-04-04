package game.actors.player;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import cairns.david.engine.TileMap;
import cairns.david.engine.Velocity;
import game.actors.mechanics.Ambulatory;
import game.actors.projectiles.Projectile;
import game.physics.Collidable;
import game.physics.CollisionEngine;

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
    public static final int DEFAULT_JUMP_HEIGHT_PX = 32;
    public static final int DEFAULT_JUMP_DURATION_MS = 500;

    public static final int PLAYER_MAX_HEALTH = 100;



    private int health_points;

    private float base_movement_speed;

    private float speed_multiplier;

    private boolean is_jumping = false;

    private int base_damage;

    private float damage_multiplier;

    private Velocity movement_velocity;
    private Velocity jumping_velocity;

    /***
     * Creates a new Sprite object with the specified Animation,
     * health points, and base movement speed.
     *
     * @param anim The animation to use for the sprite.
     * @param base_movement_speed PlayerEntity's base movement speed
     */
    public PlayerEntity(Animation anim, float base_movement_speed) {
        // call the super constructor
        super(anim);
        // initialize all properties
        this.health_points = 50;
        this.base_movement_speed = base_movement_speed;
        this.speed_multiplier = PlayerEntity.DEFAULT_SPEED_MULTIPLIER;
        this.base_damage = PlayerEntity.DEFAULT_BASE_DAMAGE;
        // initialize movement_velocity class to use
        movement_velocity = new Velocity();
        // create the collision bounds of the sprite
        jumping_velocity = new Velocity();


    }

    /***
     * Builds the movement of the sprite according to the directional key input
     * and combines those. Checks for collision with the contextual TileMap before
     * it applies the movement to itself.
     *
     * @param context the TileMap the sprite is rendered on at the moment;
     * @param time_elapsed time elapsed since last frame in ms
     * @param left boolean value that indicates if the left direction is activated
     * @param right boolean value that indicates if the right direction is activated
     * @param up boolean value that indicates if the up direction is activated
     * @param down boolean value that indicates if the down direction is activated
     */
    @SuppressWarnings("Duplicates")
    public void buildMovement(TileMap context, Long time_elapsed, float gravity, boolean left, boolean right, boolean up, boolean down) {

        float temp_dx = 0;
        float temp_dy = 0;
        float curr_gravity_pull;

        if(this.getVelocityY() > 0) {
            is_jumping = true;
        }

        movement_velocity.setVelocity(0.0, 0.0);

        /*if (up) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 270);
            temp_dx += (float)movement_velocity.getdx();
            temp_dy += (float)movement_velocity.getdy();

        }*/

        if (right) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 0);
            temp_dx += (float) movement_velocity.getdx();
            temp_dy += (float) movement_velocity.getdy();
        }

        if (left) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 180);
            temp_dx += (float) movement_velocity.getdx();
            temp_dy += (float) movement_velocity.getdy();
        }

        if (down) {
            movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 90);
            temp_dx += (float) movement_velocity.getdx();
            temp_dy += (float) movement_velocity.getdy();
        }

        if (up) {
            if(!is_jumping) {
                is_jumping = true;
                movement_velocity.setVelocity(0.2 * getSpeed_multiplier(), 270);
                temp_dy += (float) movement_velocity.getdy();
            }
        }

        /*if (is_jumping) {
            temp_dy += jumping_velocity.getdy();
            jumping_velocity.setVelocity(jumping_velocity.getSpeed() /1.04, -90);
        }*/

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

    }

    /***
     * expells a Projectile
     * @param projectile a projectile class, created anew
     * @param mouse_x the x position of the mouse, respective to the g raphics2D context
     * @param mouse_y the y position of the mouse, respective to the graphics2D context
     */
    public void expelProjectile(Projectile projectile, float mouse_x, float mouse_y) {
        // TODO expel projectile according to the mouse position with origin x, y the sprite centre
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

    public int getJumpHeight() {

        return DEFAULT_JUMP_HEIGHT_PX;
    }

    public int getJumpTime() {

        return DEFAULT_JUMP_DURATION_MS;
    }

    public float getSpeed_multiplier() {

        return speed_multiplier;
    }

    public void setSpeed_multiplier(float speed_multiplier) {
        this.speed_multiplier = speed_multiplier;
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


}
