package game.collision;

import cairns.david.engine.GameCore;
import cairns.david.engine.Sprite;
import cairns.david.engine.Tile;
import cairns.david.engine.TileMap;

import java.awt.*;

/***
 * Contains static methods for calculating colission on both axes
 * @author Boyan Atanasov
 * @version 1
 * @see cairns.david.engine.Sprite
 * @see cairns.david.engine.Tile
 */
public class CollisionEngine {

    public static final int COLLISION_NONE = -1;
    public static final int COLLISION_X_AXIS = 0;
    public static final int COLLISION_Y_AXIS = 1;
    public static final int COLLISION_BOTH_AXES = 2;

    /***
     *
     * Simple collision check, made to rule out collision based on the Rectangle.intersects() mechanism
     * Goes into complex collision checking if it detects any.
     * @param entity to check collision with tilemap for
     * @param tilemap context of entity
     * @param delta_x delta_x proposal of entity
     * @param delta_y delta_y proposal of entity
     * @return COLLISION_NONE = -1 / COLLISION_X_AXIS = 0; / COLLISION_Y_AXIS = 1;
     */

    // TODO -> complex collision checking
    public static int checkSimpleCollision(Collidable entity, TileMap tilemap, float delta_x, float delta_y, long elapsed) {
        // get entity bounds
        Rectangle.Float entity_bounds = entity.getCollisionBounds();

        // get entity x+Δx & y+Δy
        float anticipated_x = (float) entity_bounds.getX() + delta_x * elapsed;
        float anticipated_y = (float) entity_bounds.getY() + delta_y * elapsed;

        // raise no flags for collision
        boolean collision_x_axis = false;
        boolean collision_y_axis = false;

        // check collision on x axis
        Tile[][] tileArray = tilemap.getTileArray();

        // cycle through rows of the tilemap
        for (int i = 0; i < tilemap.getMapHeight(); i++) {
            // cycle through columns (cells) of the tilemap
            for (int k = 0; k < tilemap.getMapWidth(); k++) {
                // assign the ID of the tile to a string variable
                String tile_character = String.valueOf(tileArray[k][i].getCharacter());
                // construct new entity bounds with the anticipated position on the X axis, and original values from bounds of the entity
                Rectangle.Float x_transposed_bounds = new Rectangle.Float(anticipated_x, (float)entity_bounds.getY(), entity_bounds.width, entity_bounds.height);
                // construct a rectangle to serve as the bounds of the tile surveyed, and see if intersects with the x-transposed bounds
                if(
                        x_transposed_bounds.intersects(
                                new Rectangle.Float(tileArray[k][i].getXC(), tileArray[k][i].getYC(), tilemap.getTileWidth(), tilemap.getTileHeight()))
                                &&
                                tile_character.equals(".")
                        ) {
                    // flip collision on x axis
                    collision_x_axis = true;

                }

                // construct new entity bounds with the anticipated position on the Y axis, and original values from bounds of the entity
                Rectangle.Float y_transposed_bounds = new Rectangle.Float((float)entity_bounds.getX(), anticipated_y, entity_bounds.width, entity_bounds.height);
                // construct a rectangle to serve as the bounds of the tile surveyed, and see if intersects with the y-transposed bounds
                if(
                        y_transposed_bounds.intersects(
                                new Rectangle.Float(tileArray[k][i].getXC(), tileArray[k][i].getYC(), tilemap.getTileWidth(), tilemap.getTileHeight()))
                                &&
                                tile_character.equals(".")
                        ) {
                    // flip collision on y axis
                    collision_y_axis = true;

                }
            }
        }
        // if collision flags are different
        if (collision_x_axis != collision_y_axis) {
            // we only need to check either to know which one is true
            if (collision_x_axis) {
                return CollisionEngine.COLLISION_X_AXIS;
            }
            // else it's the other one
            else {
                return CollisionEngine.COLLISION_Y_AXIS;
            }
        // if collision flags are the same
        } else {
            // we only need to check either
            if (collision_x_axis) {
                return CollisionEngine.COLLISION_BOTH_AXES;
            }
            // else there is no collision
            else {
                return  CollisionEngine.COLLISION_NONE;
            }
        }
    }

    private boolean complexCollisionCheck(Collidable entity, TileMap context, float delta_x, float delta_y, long elapsed) {
        // TODO -> implement complex collision
        return true;
    }

}

