package game.levels.readytoplay;

import cairns.david.engine.TileMap;
import game.actors.consumable.ConsumableEntity;
import game.actors.enemy.EnemyEntity;
import game.actors.player.PlayerEntity;
import game.subsidiaries.audio.AudioEmitter;
import game.subsidiaries.visuals.DecorativeTileMap;

/**
 * Created by lazarus on 03/04/17.
 */
public class LevelOne {

    // Player
    PlayerEntity player;

    // Enemies
    EnemyEntity enemy_1;

    // Consumables
    ConsumableEntity consumable_1;

    // Tile Maps
    DecorativeTileMap backdrop_tilemap;
    DecorativeTileMap foredrop_tilemap;
    TileMap tilemap;

    // Audio Emitters
    AudioEmitter soundtrack;

    public LevelOne() {

    }


}
