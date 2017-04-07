package game.levels.mechanics;

import game.actors.mechanics.Ambulatory;
import game.actors.enemy.EnemyEntity;

import cairns.david.engine.*;
import game.actors.mechanics.Mortal;
import game.actors.mechanics.SignalSprite;
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
    private int signal;

    private float scale_factor;
    private int x_offset;
    private int y_offset;

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
        this.signal = LevelBundle.SIGNAL_CONTINUE;
        x_offset = 0;
        y_offset = 0;
    }

    public void informPuppeteerOffsets(int x, int y) {
        x_offset = x;
        y_offset = y;
    }

    public int getOffsetX() {
        return x_offset;
    }

    public int getOffsetY() {
        return y_offset;
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

     */
    public void enforceSpriteRender(Graphics2D g) {
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
     */
    public void enforceTileMapRender(Graphics2D g) {

        tilemap.draw(g, x_offset, y_offset);
    }

    /***
     *
     * @param g

     */
    public void enforceForedropRender(Graphics2D g ){
        foredrop.draw(g, x_offset, y_offset);
    }

    public void enforceHUDRender(Graphics2D g) {

        hud.draw(g);
    }

    public void enforceBackgroundRender(Graphics2D g) {
        g.drawImage(levelbackground, 0, 0, null);
    }

    /***
     *
     * @param g
     */
    public void enforceBackdropRender(Graphics2D g) {
        backdrop.draw(g, x_offset, y_offset);
    }

    public void enforcePlayerRender(Graphics2D g) {
        playerEntity.setOffsets(x_offset, y_offset);
        playerEntity.draw(g);
    }
    /***
     *
     * @param time_elapsed
     */
    public void updateAll(long time_elapsed) {
            playerEntity.update(time_elapsed);
            updateEntities(time_elapsed);
            foredrop.updateDecorativeSprites(time_elapsed);
            backdrop.updateDecorativeSprites(time_elapsed);
            purgeDeadMortals();
            crosscheckSprites();
            hud.update(playerEntity);
            if(playerEntity.getHealth_points() <= 0) {
                this.setSignal(LevelBundle.SIGNAL_RESTART_LEVEL);
            }
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
                Sprite sprite = sprites_list.get(i);
                if (sprite instanceof Collidable) {
                    // check if player is colliding with enemy projectiles
                    if(CollisionEngine.spriteToSpriteCollision(playerEntity, ((Collidable) sprite))) {
                        if (sprite instanceof Projectile) {
                            if (((Projectile) sprite).getSignature() == Projectile.ORIGIN_ENEMY) {
                                playerEntity.applyHitByEnemyProjectile();
                                ((Projectile) sprites_list.get(i)).die();
                            }
                        }
                    }
                    // check if player is colliding with an enemy
                    if (sprite instanceof EnemyEntity) {
                        if(CollisionEngine.spriteToSpriteCollision(playerEntity, ((Collidable) sprite))) {
                            if(((Mortal) sprite).getState() == Mortal.STATE_ALIVE)
                            playerEntity.applyHitByEnemyEntity();
                        }
                    }
                    // check if player is colliding with any SIGNAL SPRITES
                    if (sprite instanceof SignalSprite) {
                        if(CollisionEngine.spriteToSpriteCollision(playerEntity, ((Collidable) sprite))) {
                            int new_signal = ((SignalSprite) sprite).getSignal();
                            this.setSignal(new_signal);
                            System.out.println("signal changed");
                        }
                    }
                    // check if an enemy is being hit by a player projectile
                    if (sprite instanceof EnemyEntity && sprite instanceof Mortal) {
                        for (int p = 0; p < sprites_list.size(); p++) {
                            if(CollisionEngine.spriteToSpriteCollision(((Collidable) sprites_list.get(i)), ((Collidable) sprites_list.get(p)))) {
                                if(sprites_list.get(p) instanceof Projectile) {
                                    if (((Projectile) sprites_list.get(p)).getSignature() == Projectile.ORIGIN_PLAYER && ((EnemyEntity) sprite).getState() == Mortal.STATE_ALIVE) {
                                        ((Mortal) sprites_list.get(i)).die();
                                        ((Projectile) sprites_list.get(p)).die();
                                        Sound die = new Sound("sounds/sfx/enemy_die.wav");
                                        die.start();
                                    }
                                }
                            }
                        }
                    }
                    // check if projectile is hitting the tilemap
                    if (sprite instanceof Projectile && sprite instanceof Mortal && sprite instanceof Collidable) {
                        if(CollisionEngine.simpleSpriteToMapCollision((Collidable) sprite, tilemap, 0f, 0f, 0L) != CollisionEngine.COLLISION_NONE) {
                            ((Mortal) sprites_list.get(i)).die();
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

    public void playSound(int key, boolean looping) {

    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int readSignal() {
        return signal;
    }
}
