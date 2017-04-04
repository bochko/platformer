package game.actors.projectiles;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import game.physics.Collidable;

import java.awt.*;

/**
 * Created by boyan on 23/02/17.
 */
public class Projectile extends Sprite implements Collidable {
    /**
     * Creates a new Sprite object with the specified Animation.
     *
     * @param anim The animation to use for the sprite.
     */
    public Projectile(Animation anim) {
        super(anim);
    }

    @Override
    public Rectangle.Float getCollisionBounds() {
        Rectangle.Float collision_bounds = new Rectangle.Float(getX(), getY(), getWidth(), getHeight());
        return collision_bounds;
    }

}
