package game.actors.enemy;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import cairns.david.engine.TileMap;
import cairns.david.engine.Velocity;
import game.actors.mechanics.Ambulatory;
import game.actors.mechanics.Mortal;
import game.actors.player.PlayerEntity;
import game.actors.projectiles.Projectile;
import game.physics.Collidable;
import game.physics.CollisionEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by boyan on 23/02/17.
 */
public class EnemyEntity extends Sprite implements Collidable, Ambulatory, Mortal{

    private final int MOVEMENT_RADIUS = 1000;
    private final int DETECTION_RADIUS = 600;
    private float ORIGIN_X;
    private float ORIGIN_Y;

    private int state;

    private int last_movement = EnemyMove.ENEMY_NO_MOVEMENT;

    private float health_points;

    private float base_movement_speed;

    private float speed_multiplier;

    private boolean is_jumping = false;

    private int base_damage;

    private float damage_multiplier;

    private Velocity movement_velocity;
    private Velocity jumping_velocity;

    /***
     * Creates a new EnemyEntity object with the specified Animation,
     * health points, and base movement speed.
     *
     * @param anim The animation to use for the sprite.
     * @param health_points The initial and max health points of the player
     * @param base_movement_speed PlayerEntity's base movement speed
     */
    public EnemyEntity(Animation anim, int health_points, float base_movement_speed) {
        // call the super constructor
        super(anim);
        // initialize all properties
        this.health_points = health_points;
        this.base_movement_speed = base_movement_speed;
        this.speed_multiplier = PlayerEntity.DEFAULT_SPEED_MULTIPLIER;
        this.base_damage = PlayerEntity.DEFAULT_BASE_DAMAGE;
        // initialize movement_velocity class to use
        movement_velocity = new Velocity();
        // create the collision bounds of the sprite
        jumping_velocity = new Velocity();
        state = Mortal.STATE_ALIVE;

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
    public void buildMovement(TileMap context, Long time_elapsed, float gravity, boolean left, boolean right, boolean up, boolean down) {
        // if sprite is alive compute further movement
        if(getState() == Mortal.STATE_ALIVE) {
            float temp_dx = 0;
            float temp_dy = 0;

            EnemyMove new_move = simulateInput(context, time_elapsed);

            temp_dx += new_move.getDx();
            temp_dy += new_move.getDy();
            last_movement = new_move.getMove();

            movement_velocity.setVelocity(gravity * time_elapsed, 90);
            temp_dy += (float) movement_velocity.getdy() + getVelocityY();

            int collision_type = CollisionEngine.simpleSpriteToMapCollision(this, context, temp_dx, temp_dy, time_elapsed);
            // Set sprite movement_velocity according to delta values calculated and collision type
            if (collision_type == CollisionEngine.COLLISION_NONE) {
                super.setVelocityX(temp_dx);
                super.setVelocityY(temp_dy);
            } else {
                if(collision_type == CollisionEngine.COLLISION_X_AXIS) {
                    super.setVelocityY(temp_dy);
                    super.setVelocityX(0f);

                }
                if(collision_type == CollisionEngine.COLLISION_Y_AXIS) {
                    if(temp_dy > 0) {
                        is_jumping = false;
                    }
                    super.setVelocityX(temp_dx);
                    super.setVelocityY(0f);


                }
                if(collision_type == CollisionEngine.COLLISION_BOTH_AXES) {

                    super.stop();
                    if(temp_dy > 0) {
                        is_jumping = false;
                    }
                }
            }
        } else if (getState() == Mortal.STATE_DYING) {
            super.setVelocityX(0.0f);
            super.setVelocityY(0.0f);
        }


    }

    private EnemyMove simulateInput(TileMap context, Long time_elapsed) {
        ArrayList<EnemyMove> possibleMoves = establishPossibleMoves(context, time_elapsed);
        if(!possibleMoves.isEmpty()) {
            if(last_movement == EnemyMove.ENEMY_NO_MOVEMENT) {
                Random random = new Random();
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            } else {
                for (EnemyMove move : possibleMoves) {
                    if(move.getMove() == last_movement) {
                        return move;
                    }
                }
                Random random = new Random();
                return possibleMoves.get(random.nextInt(possibleMoves.size()));
            }
        }
        else {
            return new EnemyMove(0.0f, 0.0f, EnemyMove.ENEMY_NO_MOVEMENT);
        }
    }

    private ArrayList<EnemyMove> establishPossibleMoves(TileMap context, Long time_elapsed) {
        ArrayList<EnemyMove> possible_moves = new ArrayList<>();
        movement_velocity.setVelocity(0.0, 0.0);
        float temp_dx;
        float temp_dy;

        // check if left movement causes collision
        movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 180);
        temp_dx = (float) movement_velocity.getdx();
        temp_dy = (float) movement_velocity.getdy();

        if(CollisionEngine.simpleSpriteToMapCollision(this, context, temp_dx, temp_dy, time_elapsed)
                == CollisionEngine.COLLISION_NONE) {
            possible_moves.add(new EnemyMove(temp_dx, temp_dy, EnemyMove.ENEMY_MOVEMENT_LEFT));
        }

        // check if right movement causes collision
        movement_velocity.setVelocity(getBase_movement_speed() * getSpeed_multiplier(), 0);
        temp_dx = (float) movement_velocity.getdx();
        temp_dy = (float) movement_velocity.getdy();

        if(CollisionEngine.simpleSpriteToMapCollision(this, context, temp_dx, temp_dy, time_elapsed)
                == CollisionEngine.COLLISION_NONE) {
            possible_moves.add(new EnemyMove(temp_dx, temp_dy, EnemyMove.ENEMY_MOVEMENT_RIGHT));
        }

        return possible_moves;
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
        Rectangle.Float collision_bounds = new Rectangle.Float(getX(), getY(), getWidth(), getHeight());
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

    @Override
    public void setX(float x) {
        super.setX(x);
        ORIGIN_X = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        ORIGIN_Y = y;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
}
