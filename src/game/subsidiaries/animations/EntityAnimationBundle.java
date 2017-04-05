package game.subsidiaries.animations;

import cairns.david.engine.Animation;

import java.util.HashMap;

/**
 * Created by lazarus on 05/04/17.
 */
public class EntityAnimationBundle {

    public static final int PLAYER_ANIM_IDLE = 11001;
    public static final int PLAYER_ANIM_RIGHT = 11002;
    public static final int PLAYER_ANIM_LEFT = 11003;
    public static final int PLAYER_ANIM_JUMP = 11004;
    public static final int PLAYER_ANIM_FALL = 11005;

    HashMap<Integer, Animation> animations_hashmap;

    public EntityAnimationBundle(Animation idle, Animation right, Animation left, Animation jump, Animation fall) {
        animations_hashmap = new HashMap<>();
        animations_hashmap.put(PLAYER_ANIM_IDLE, idle);
        animations_hashmap.put(PLAYER_ANIM_RIGHT, right);
        animations_hashmap.put(PLAYER_ANIM_LEFT, left);
        animations_hashmap.put(PLAYER_ANIM_JUMP, jump);
        animations_hashmap.put(PLAYER_ANIM_FALL, fall);
    }

    public Animation getPlayerAnimation(int key) {
        if(animations_hashmap.containsKey(key)) {
            return animations_hashmap.get(key);
        } else {
            return animations_hashmap.get(PLAYER_ANIM_IDLE);
        }
    }
}