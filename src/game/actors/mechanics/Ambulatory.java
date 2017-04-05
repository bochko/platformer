package game.actors.mechanics;

import cairns.david.engine.TileMap;
import game.core.PIDController;

/**
 * Created by lazarus on 01/04/17.
 */
public interface Ambulatory {

   void buildMovement(TileMap context, Long time_elapsed, float gravity, PIDController pidController);

}
