package game.levels.mechanics;

import game.actors.mechanics.Ambulatory;
import game.actors.enemy.EnemyEntity;

import cairns.david.engine.*;
import game.actors.mechanics.Mortal;
import game.actors.player.PlayerEntity;
import game.actors.projectiles.Projectile;
import game.core.PIDController;
import game.physics.Collidable;
import game.physics.CollisionEngine;
import game.subsidiaries.visuals.HeadsUpDisplay;
import game.subsidiaries.visuals.SpriteMap;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by lazarus on 29/03/17.
 */
public class LevelPuppeteer {

    private TileMap tilemap;
    private SpriteMap foredrop;
    private SpriteMap backdrop;
    private Image levelbackground;
    private PlayerEntity playerEntity;
    private HeadsUpDisplay hud;
    private LinkedList<Sprite> sprites_list;

    private float scale_factor;

    /***
     *
     * @param tilemap
     * @param foredrop
     * @param backdrop
     * @param levelbackground
     * @param playerEntity
     * @param hud
     */
    public LevelPuppeteer(TileMap tilemap, SpriteMap foredrop, SpriteMap backdrop,
                          Image levelbackground, PlayerEntity playerEntity, HeadsUpDisplay hud, float scale_factor) {
        this.tilemap = tilemap;
        this.foredrop = foredrop;
        this.backdrop = backdrop;
        this.levelbackground = levelbackground;
        this.playerEntity = playerEntity;
        this.hud = hud;
        this.sprites_list = new LinkedList<>();
        this.scale_factor = scale_factor;
    }

    /***
     * Method takes in a sprite and decides where to put it in the level bundle.
     * Synchronize-Block-s list containers in order to avoid Concurrent Modification.
     *
     * @param sprite the sprite to add in to the level bundle
     */
    public void surrenderSpriteToJurisdiction(Sprite sprite) {
        synchronized (sprites_list) {
            sprites_list.add(sprite);
        }
    }

    /***
     * Self-renders all sprite in scene in order enemies -> consumables -> projectiles -> player
     * PlayerEntity ends up on top in this hierarchy. Synchronize-Block-s list containers in order to avoid Concurrent Modification
     * @param g graphics context to draw on
     * @param x_offset the x-offset of the scene
     * @param y_offset the y-offset of the scene
     */
    public void enforceSpriteRender(Graphics2D g, int x_offset, int y_offset) {
        synchronized (sprites_list) {

            for (int i = 0; i < sprites_list.size(); i++) {
                sprites_list.get(i).setOffsets(x_offset, y_offset);
                sprites_list.get(i).draw(g);
            }
        }
    }

    /***
     *
     * @param g
     * @param x_offset
     * @param y_offset
     */
    public void enforceTileMapRender(Graphics2D g, int x_offset, int y_offset) {
        tilemap.draw(g, x_offset, y_offset);
    }

    /***
     *
     * @param g
     * @param x_offset
     * @param y_offset
     */
    public void enforceForedropRender(Graphics2D g, int x_offset, int y_offset) {
        foredrop.draw(g, x_offset, y_offset);
    }

    public void enforceHUDRender(Graphics2D g) {
        hud.draw(g);
    }

    /***
     *
     * @param g
     * @param x_offset
     * @param y_offset
     */
    public void enforceBackdropRender(Graphics2D g, int x_offset, int y_offset) {
        backdrop.draw(g, x_offset, y_offset);
    }

    public void enforcePlayerRender(Graphics2D g, int x_offset, int y_offset) {
        playerEntity.setOffsets(x_offset, y_offset);
        playerEntity.draw(g);
    }
    /***
     *
     * @param time_elapsed
     */
    public void updateAll(long time_elapsed) {
            purgeDeadMortals();
            crosscheckSprites();
            playerEntity.update(time_elapsed);
            foredrop.updateDecorativeSprites(time_elapsed);
            backdrop.updateDecorativeSprites(time_elapsed);
            updateEntities(time_elapsed);
            hud.update(playerEntity);

    }

    /***
     * builds movement of all sprites that are an instance of the Ambulatory interface,
     * which anything that applies movement changes to self after being added to the sprite_list implements
     * @param time_elapsed the time elapsed for a frame
     * @param gravity the gravity value
     */
    public void enforceMovement(Long time_elapsed, float gravity, PIDController pidController) {
        synchronized (sprites_list) {
            for (int i = 0; i < sprites_list.size(); i++) {
                if (sprites_list.get(i) instanceof Ambulatory) {
                    ((Ambulatory) sprites_list.get(i)).buildMovement(tilemap, time_elapsed, gravity, pidController);
                }
            }
        }
        playerEntity.buildMovement(tilemap, time_elapsed, gravity, pidController);
    }

    /***
     *
     * @param time_elapsed
     */
    private void updateEntities(Long time_elapsed) { // !!!! All iterations are done with for loops instead of foreach loops,
        // because iteration while modifying is forbidden and a for loop is not an iteration over the list technically
        synchronized (sprites_list) {
            for (int i = 0; i < sprites_list.size(); i++) {
                sprites_list.get(i).update(time_elapsed);
            }
        }
    }

    /***
     *
     */
    private void purgeDeadMortals() {
        synchronized (sprites_list) {
            for (int i = 0; i < sprites_list.size(); i++) {
                if (sprites_list.get(i) instanceof Mortal) {
                    if (((Mortal) sprites_list.get(i)).getState() == Mortal.STATE_DEAD) {
                        sprites_list.remove(i);
                    }
                }
            }
        }
    }

    /***
     *
     * @param projectile
     */
    public void conjureProjectile(Projectile projectile) {
        synchronized (sprites_list) {
            sprites_list.add(projectile);
        }
    }

    /***
     *
     */
    private void crosscheckSprites() {
        synchronized (sprites_list) {
            for(int i = 0; i < sprites_list.size(); i++) {
                if (sprites_list.get(i) instanceof Collidable) {
                    // check if player is colliding with enemy projectiles
                    if(CollisionEngine.spriteToSpriteCollision(playerEntity, ((Collidable) sprites_list.get(i)))) {
                        if (sprites_list.get(i) instanceof Projectile) {
                            if (((Projectile) sprites_list.get(i)).getSignature() == Projectile.ORIGIN_ENEMY) {
                                playerEntity.applyHitByEnemyProjectile();
                            }
                        }
                    }
                    // check if player is colliding with an enemy
                    if(CollisionEngine.spriteToSpriteCollision(playerEntity, ((Collidable) sprites_list.get(i)))) {
                        if (sprites_list.get(i) instanceof EnemyEntity) {
                            playerEntity.applyHitByEnemyEntity();
                        }
                    }
                    // check if an enemy is being hit by a player projectile
                    if (sprites_list.get(i) instanceof EnemyEntity && sprites_list.get(i) instanceof Mortal) {
                        for (int p = 0; p < sprites_list.size(); p++) {
                            if(CollisionEngine.spriteToSpriteCollision(((Collidable) sprites_list.get(i)), ((Collidable) sprites_list.get(p)))) {
                                if(sprites_list.get(p) instanceof Projectile) {
                                    if (((Projectile) sprites_list.get(p)).getSignature() == Projectile.ORIGIN_PLAYER) {
                                        ((Mortal) sprites_list.get(i)).die();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     *
     * @return
     */
    public float getScale_factor() {
        return scale_factor;
    }

    public Point.Float snoopOnPlayerPosition() {
        return new Point.Float(playerEntity.getX(), playerEntity.getY());
    }
}
