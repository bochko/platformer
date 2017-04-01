package game.levels;

import game.actors.ConsumableEntity;
import game.actors.enemy.EnemyEntity;
import game.actors.Player;

import cairns.david.engine.*;
import game.actors.Projectile;
import game.core.Core;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by lazarus on 29/03/17.
 */
public class LevelBundle {

    private TileMap tilemap;
    private Player player;
    private LinkedList<EnemyEntity> enemy_list;
    private LinkedList<Projectile> projectile_list;
    private LinkedList<ConsumableEntity> consumable_list;

    public LevelBundle() {
        this.tilemap = tilemap;
        this.player = player;
        this.enemy_list = new LinkedList<>();
        this.projectile_list = new LinkedList<>();
        this.consumable_list = new LinkedList<>();
    }

    /***
     * Method takes in a sprite and decides where to put it in the level bundle.
     * Synchronize-Block-s list containers in order to avoid Concurrent Modification.
     * @param sprite the sprite to add in to the level bundle
     * @return true if ok, false if not an instance of any known non-player sprite type
     */
    public boolean addSpriteToLevel(Sprite sprite) {
        if(sprite instanceof EnemyEntity) {
            synchronized (enemy_list) {
                enemy_list.add((EnemyEntity)sprite);
                return true;
            }
        }
        if(sprite instanceof ConsumableEntity) {
            synchronized (consumable_list) {
                consumable_list.add((ConsumableEntity)sprite);
                return true;
            }
        }
        if(sprite instanceof Projectile) {
            synchronized (projectile_list) {
                projectile_list.add((Projectile)sprite);
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
    public boolean addSpriteToLevel(Sprite[] sprites) {
        boolean non_player;
        for(Sprite sprite: sprites) {
            non_player = addSpriteToLevel(sprite);
            if (!non_player) {
                return false;
            }
        }
        return true;
    }

    /***
     * Self-renders all sprite in scene in order enemies -> consumables -> projectiles -> player
     * Player ends up on top in this hierarchy. Synchronize-Block-s list containers in order to avoid Concurrent Modification
     * @param g the graphical context to self-render on
     */
    public void spritesRenderSelf(Graphics2D g, int x_offset, int y_offset) {
        synchronized (enemy_list) {
            for (Sprite sprite: enemy_list) {
                sprite.setOffsets(x_offset, y_offset);
                sprite.draw(g);
            }
        }

        synchronized (consumable_list) {
            for (Sprite sprite: consumable_list) {
                sprite.setOffsets(x_offset, y_offset);
                sprite.draw(g);
            }
        }

        synchronized (projectile_list) {
            for (Sprite sprite: projectile_list) {
                sprite.setOffsets(x_offset, y_offset);
                sprite.draw(g);
            }
        }
        player.draw(g);
    }

    public void spritesBuildMovement(TileMap context, Long time_elapsed, float gravity,
                                     boolean controller_left, boolean controller_right,
                                     boolean controller_up, boolean controller_down) {
        synchronized (enemy_list) {
            for (EnemyEntity enemy: enemy_list) {
                enemy.buildMovement(context, time_elapsed, gravity);
            }
        }
        player.buildMovement(context, time_elapsed, gravity, controller_left,
                controller_right, controller_up, controller_down);
    }

}
