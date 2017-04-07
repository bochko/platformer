package game.actors.enemy;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import cairns.david.engine.TileMap;
import cairns.david.engine.Velocity;
import game.actors.mechanics.Ambulatory;
import game.actors.mechanics.Mortal;
import game.actors.player.PlayerEntity;
import game.actors.projectiles.Projectile;
import game.core.PIDController;
import game.levels.mechanics.LevelPuppeteer;
import game.physics.Collidable;
import game.physics.CollisionEngine;
import game.subsidiaries.animations.AnimationPicker;
import game.subsidiaries.animations.EntityAnimationBundle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by boyan on 23/02/17.
 */
public class EnemyEntity extends Sprite implements Collidable, Ambulatory, Mortal{

    public static final int DETECTION_RADIUS = 600;
    public static final long PROJECTILE_TIMER_DEFAULT = 1500L;
    public static final long DEATH_TIMER_DEFAULT = 2000L;
    public static final float BASE_MOVEMENT_SPEED_DEFAULT = 0.4f;
    public static final long ENEMY_DEFAULT_START_DELAY = 1000L;
    private int state;
    private int last_movement = EnemyMove.ENEMY_NO_MOVEMENT;
    private float base_movement_speed;
    private float speed_multiplier;
    private long projectile_timer;
    private long death_timer;

    private Animation anim_dying;

    private Velocity movement_velocity;
    private Velocity jumping_velocity;
    private LevelPuppeteer master;
    private EntityAnimationBundle animations;
    private long start_delay;

    /***
     * Creates a new EnemyEntity object with the specified Animation,
     * health points, and base movement speed.
     *
     * @param base_movement_speed PlayerEntity's base movement speed
     */
    public EnemyEntity(EntityAnimationBundle animations, float base_movement_speed) {
        // call the super constructor
        super(animations.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_IDLE));
        this.animations = animations;
        this.anim_dying = animations.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_IDLE);
        // initialize all properties
        this.base_movement_speed = base_movement_speed;
        this.speed_multiplier = PlayerEntity.DEFAULT_SPEED_MULTIPLIER;
        // initialize movement_velocity class to use
        movement_velocity = new Velocity();
        // create the collision bounds of the sprite
        jumping_velocity = new Velocity();
        state = Mortal.STATE_ALIVE;
        projectile_timer =
        death_timer = EnemyEntity.DEATH_TIMER_DEFAULT;
        start_delay = ENEMY_DEFAULT_START_DELAY;

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
        if(start_delay > 0) return;
        // if sprite is dying
        if (getState() == Mortal.STATE_TRIGGER_DYING || getState() == Mortal.STATE_DYING) {
            setVelocityY(0.0f);
            setVelocityX(0.0f);
        }
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
                    super.setVelocityX(temp_dx);
                    super.setVelocityY(0f);


                }
                if(collision_type == CollisionEngine.COLLISION_BOTH_AXES) {

                    super.stop();
                }
            }
        } else if (getState() == Mortal.STATE_TRIGGER_DYING) {
            super.setVelocityX(0.0f);
            super.setVelocityY(0.0f);
        }

        this.setAnimation(AnimationPicker.pickAnimation(animations, this));

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

    @Override
    public void update(long time_elapsed) {
        super.update(time_elapsed);
        start_delay -= time_elapsed;
        if (getState() == Mortal.STATE_ALIVE) {
            projectile_timer -= time_elapsed;
            expelTimedProjectile();
        }
        if (getState() == Mortal.STATE_TRIGGER_DYING) {
            setAnimation(anim_dying);
            setAnimationSpeed(1.0f);
            setState(Mortal.STATE_DYING);
        }
        if (getState() == Mortal.STATE_DYING) {
            if(death_timer <= 0) {
                setState(Mortal.STATE_DEAD);
            }
            death_timer -= time_elapsed;
        }
    }

    public void expelTimedProjectile() {
        if(projectile_timer <=0) {
            projectile_timer = 1400; // reset projectile timer

                float origin_x = getX();
                float origin_y = getY();
                float dest_x = master.snoopOnPlayerPosition().x;
                float dest_y = master.snoopOnPlayerPosition().y;
                Velocity temp_proj_velocity = new Velocity();
                Animation proj_anim = new Animation();
                proj_anim.loadAnimationFromSheet("images/sprites/enemy_projectile_static.png",1, 1, 60);
                Animation proj_dying_anim = new Animation();
                proj_dying_anim.loadAnimationFromSheet("images/sprites/anim_enemy_projectile_dying.png", 10, 1, 60);
                Projectile proj = new Projectile(proj_anim, proj_dying_anim, Projectile.ORIGIN_ENEMY);
                proj.setX(origin_x);
                proj.setY(origin_y);
                double angle = temp_proj_velocity.getAngle(proj.getX(), proj.getY(), dest_x, dest_y);
                temp_proj_velocity.setVelocity(0.2f, angle);
                proj.setVelocityX((float)temp_proj_velocity.getdx());
                proj.setVelocityY((float)temp_proj_velocity.getdy());
                proj.show();
                master.conjureProjectile(proj);
        }
    }

    /***
     * Returns a Rectangle.Float to serve as the collision bounds
     *
     * @return a Rectangle.Float to serve as collision bounds
     */
    @Override
    public Rectangle.Float getCollisionBounds() {
        Rectangle.Float collision_bounds = new Rectangle.Float(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 5);
        return collision_bounds;
    }

    public float getSpeed_multiplier() {
        return speed_multiplier;
    }

    public float getBase_movement_speed() {
        return base_movement_speed;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void die() {
        setState(Mortal.STATE_TRIGGER_DYING);
    }
}
