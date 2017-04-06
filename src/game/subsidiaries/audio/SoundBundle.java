package game.subsidiaries.audio;

import cairns.david.engine.Sound;

import java.util.HashMap;

/**
 * Created by lazarus on 06/04/17.
 */
public class SoundBundle {

    public static final int SOUND_SOUNDTRACK = 21001;
    public static final int SOUND_PLAYER_JUMP = 21002;
    public static final int SOUND_PLAYER_SHOOT = 21003;
    public static final int SOUND_PLAYER_DIE = 21004;
    public static final int SOUND_ENEMY_SHOOT = 21005;
    public static final int SOUND_ENEMY_DIE = 21006;

    HashMap<Integer, Sound> sounds_hashmap;

    public SoundBundle(Sound soundtrack, Sound player_jump, Sound player_shoot, Sound player_die, Sound enemy_shoot, Sound enemy_die) {
        sounds_hashmap = new HashMap<>();
        sounds_hashmap.put(SOUND_SOUNDTRACK, soundtrack);
        sounds_hashmap.put(SOUND_PLAYER_JUMP, player_jump);
        sounds_hashmap.put(SOUND_PLAYER_SHOOT, player_shoot);
        sounds_hashmap.put(SOUND_PLAYER_DIE, player_die);
        sounds_hashmap.put(SOUND_ENEMY_SHOOT, enemy_shoot);
        sounds_hashmap.put(SOUND_ENEMY_DIE, enemy_die);

    }

    public Sound getSound(int key) {
        if(sounds_hashmap.containsKey(key)) {
            return sounds_hashmap.get(key);
        } else {
            return null;
        }
    }
}
