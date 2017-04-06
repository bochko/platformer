package game.actors.projectiles;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import game.actors.mechanics.Mortal;
import game.physics.Collidable;

import java.awt.*;

/**
 * Created by boyan on 23/02/17.
 */
public class Projectile extends Sprite implements Collidable, Mortal{

    public static final int ORIGIN_ENEMY = 8899;
    public static final int ORIGIN_PLAYER = 6677;

    private int entity_origin = 0;

    private Animation dying_animation;
    private int state = Mortal.STATE_ALIVE;
    private int death_timer = 600;

    /**
     * Creates a new Sprite object with the specified Animation.
     *
     * @param anim The animation to use for the sprite.
     */
    public Projectile(Animation anim, Animation dying_animation, int origin) {
        super(anim);
        this.dying_animation = dying_animation;
        entity_origin = origin;
    }

    @Override
    public void update(long time_elapsed) {
        super.update(time_elapsed);
        if (getState() == Mortal.STATE_TRIGGER_DYING) {
            this.setVelocityX(0.0f);
            this.setVelocityY(0.0f);
            setAnimation(dying_animation);
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

    @Override
    public Rectangle.Float getCollisionBounds() {
        Rectangle.Float collision_bounds = new Rectangle.Float(getX() +7, getY() +7, getWidth() -14, getHeight() -14);
        return collision_bounds;
    }

    public int getSignature() {
        return entity_origin;
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
