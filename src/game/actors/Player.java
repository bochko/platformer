package game.actors;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import cairns.david.engine.TileMap;
import cairns.david.engine.Velocity;
import game.collision.Collidable;
import game.collision.CollisionEngine;

import java.awt.*;

/***
 * Player class extends the Sprite class;
 * adds a layer of functionality useful for the player
 * entity and player movement, and collision.
 *
 * @ author Boyan Atanasov
 * @ see cairns.david.engine.Sprite
 */

public class Player extends Sprite implements Collidable {

    public static final int DEFAULT_BASE_DAMAGE = 25;
    public static final int DEFAULT_SPEED_MULTIPLIER = 1;

    private float health_points;

    private float base_movement_speed;

    private float speed_multiplier;

    private int base_damage;

    private float damage_multiplier;

    private Velocity velocity;

    private Rectangle.Float collision_bounds;

    /***
     * Creates a new Sprite object with the specified Animation,
     * health points, and base movement speed.
     *
     * @param anim The animation to use for the sprite.
     * @param health_points The initial and max health points of the player
     * @param base_movement_speed Player's base movement speed
     */
    public Player(Animation anim, int health_points, float base_movement_speed) {
        // call the super constructor
        super(anim);
        // initialize all properties
        this.health_points = health_points;
        this.base_movement_speed = base_movement_speed;
        this.speed_multiplier = Player.DEFAULT_SPEED_MULTIPLIER;
        this.base_damage = Player.DEFAULT_BASE_DAMAGE;
        // initialize velocity class to use
        velocity = new Velocity();
        // create the collision bounds of the sprite
        collision_bounds = new Rectangle.Float(getX(), getY(), getWidth(), getHeight());

    }

    /***s
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
    public void buildMovement(TileMap context, Long time_elapsed, boolean left, boolean right, boolean up, boolean down) {

        float temp_dx = 0;
        float temp_dy = 0;

        velocity.setVelocity(0.0, 0.0);

        if (up) {
            velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 270);
            temp_dx += (float)velocity.getdx();
            temp_dy += (float)velocity.getdy();

        }

        if (right) {
            velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 0);
            temp_dx += (float)velocity.getdx();
            temp_dy += (float)velocity.getdy();
        }

        if (left) {
            velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 180);
            temp_dx += (float)velocity.getdx();
            temp_dy += (float)velocity.getdy();
        }

        if (down) {
            velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 90);
            temp_dx += (float)velocity.getdx();
            temp_dy += (float)velocity.getdy();
        }

        int collision_type = CollisionEngine.checkBoundsReturnType(this, context, temp_dx, temp_dy, time_elapsed);

        // Set sprite velocity according to delta values calculated and collision type
        if (collision_type == CollisionEngine.COLLISION_NONE) {
            super.setVelocityX(temp_dx);
            super.setVelocityY(temp_dy);
        } else {
            if(collision_type == CollisionEngine.COLLISION_X_AXIS) {
                super.setVelocityY(temp_dy);
                super.setVelocityX(0f);

            }
            if(collision_type == CollisionEngine.COLLISION_Y_AXIS) {
                super.setVelocityX(temp_dx);
                super.setVelocityY(0f);
            }
            if(collision_type == CollisionEngine.COLLISION_BOTH_AXES) {
                super.stop();
            }
        }

    }

    /***
     * expells a Projectile
     * @param projectile
     * @param mouse_x
     * @param mouse_y
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
        return collision_bounds;
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


}
