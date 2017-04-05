package game.actors.mechanics;

import cairns.david.engine.Sprite;

/**
 * Created by lazarus on 04/04/17.
 */
public interface Mortal {

    int STATE_ALIVE = 71;
    int STATE_TRIGGER_DYING = 72;
    int STATE_DYING = 73;
    int STATE_DEAD = 74;

    int getState();

    void setState(int state);

    void die();
}
