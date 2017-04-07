package game.actors.mechanics;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import game.physics.Collidable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by lazarus on 07/04/2017.
 */
public class SignalSprite extends Sprite implements Collidable {

    private int signal;

    public SignalSprite(Animation anim, int signal) {
        super(anim);
        this.signal = signal;
    }

    @Override
    public Rectangle.Float getCollisionBounds() {
        return new Rectangle2D.Float(getX(), getY(), getWidth(), getHeight());
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }
}
