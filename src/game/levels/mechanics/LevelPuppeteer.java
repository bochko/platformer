package game.levels.mechanics;

import game.actors.mechanics.Ambulatory;
import game.actors.enemy.EnemyEntity;

import cairns.david.engine.*;
import game.actors.mechanics.Mortal;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by lazarus on 29/03/17.
 */
public class LevelPuppeteer {

    private TileMap tilemap;
    private LinkedList<Sprite> sprites_list;
    Image levelbackground;


    public LevelPuppeteer() {
        this.tilemap = tilemap;
        this.sprites_list = new LinkedList<Sprite>();
    }

    /***
     * Method takes in a sprite and decides where to put it in the level bundle.
     * Synchronize-Block-s list containers in order to avoid Concurrent Modification.
     * @param sprite the sprite to add in to the level bundle
     * @return true if ok, false if not an instance of any known non-player sprite type
     */
    public boolean surrenderSpriteToJurisdiction(Sprite sprite) {
        if(sprite instanceof EnemyEntity) {
            synchronized (sprites_list) {
                sprites_list.add(sprite);
                return true;
            }
        }
        return false;
    }


    /***
     * Method takes in an array of sprites and decides where to put them in the
     * level bundle individually. Sprites need to be of a non-player type.
     * @param sprites array of non-player sprites
     * @return false if any of the sprites are of non-player type and terminates
     */
    public boolean surrenderSpriteToJurisdiction(Sprite[] sprites) {
        for(Sprite sprite: sprites) {
            surrenderSpriteToJurisdiction(sprite);
        }
        return true;
    }

    /***
     * Self-renders all sprite in scene in order enemies -> consumables -> projectiles -> player
     * PlayerEntity ends up on top in this hierarchy. Synchronize-Block-s list containers in order to avoid Concurrent Modification
     * @param g graphics context to draw on
     * @param x_offset the x-offset of the scene
     * @param y_offset the y-offset of the scene
     */
    public void enforceRender(Graphics2D g, int x_offset, int y_offset) {
        synchronized (sprites_list) {
            for (Sprite sprite: sprites_list) {
                sprite.setOffsets(x_offset, y_offset);
                sprite.draw(g);
            }
        }
    }

    /***
     * builds movement of all sprites that are an instance of the Ambulatory interface,
     * which anything that applies movement changes to self after being added to the sprite_list implements
     * @param time_elapsed the time elapsed for a frame
     * @param gravity the gravity value
     * @param controller_left controller left pressed?
     * @param controller_right controller right pressed?
     * @param controller_up controller up pressed?
     * @param controller_down controller down pressed?
     */
    public void enforceMovement(Long time_elapsed, float gravity,
                                boolean controller_left, boolean controller_right,
                                boolean controller_up, boolean controller_down) {
        synchronized (sprites_list) {
            for (int i = 0; i<sprites_list.size(); i++) {
                if (sprites_list.get(i) instanceof Ambulatory) {
                    ((Ambulatory) sprites_list.get(i)).buildMovement(tilemap, time_elapsed, gravity, controller_left, controller_right, controller_up, controller_down);
                }
            }
        }
    }

    public void updateEntities(Long time_elapsed) {
        synchronized (sprites_list) {
            for (int i = 0; i < sprites_list.size(); i++) {
                sprites_list.get(i).update(time_elapsed);
            }
        }
    }

    
    public void purgeDeadMortals() {
        synchronized (sprites_list) {
            for (Sprite sprite : sprites_list) {
                if (sprite instanceof Mortal) {
                    Mortal mortal = (Mortal) sprite;
                    if (mortal.getState() == Mortal.STATE_DEAD) {
                        sprites_list.remove(sprite);
                    }
                }
            }
        }
    }
}
