package game.actors.mechanics;

import cairns.david.engine.TileMap;

/**
 * Created by lazarus on 01/04/17.
 */
public interface Ambulatory {

    /***
     *
     * @param context
     * @param time_elapsed
     * @param gravity
     * @param left
     * @param right
     * @param up
     * @param down
     */
   void buildMovement(TileMap context, Long time_elapsed, float gravity, boolean left, boolean right, boolean up, boolean down);

}
