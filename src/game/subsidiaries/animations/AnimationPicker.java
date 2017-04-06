package game.subsidiaries.animations;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;

/**
 * Created by lazarus on 06/04/17.
 */
public class AnimationPicker {

    public AnimationPicker() {

    }

    public static Animation pickAnimation (EntityAnimationBundle bundle, Sprite entity) {
        float dx = entity.getVelocityX();
        float dy = entity.getVelocityY();
        Animation returnAnimation = entity.getAnimation();

        if(Math.abs(dx) > Math.abs(dy)) {
            if(dx > 0) {
                if(!entity.getAnimation().equals(bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_RIGHT)))
                returnAnimation = bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_RIGHT);
            } else if(dx < 0) {
                if(!entity.getAnimation().equals(bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_LEFT)))
                returnAnimation = bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_LEFT);
            }
        } else if (Math.abs(dy) > Math.abs(dx)) {
            if(dy > 0) {
                if(!entity.getAnimation().equals(bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_FALL)))
                returnAnimation = bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_FALL);
            } else if(dy < 0) {
                if(!entity.getAnimation().equals(bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_JUMP)))
                returnAnimation = bundle.getPlayerAnimation(EntityAnimationBundle.PLAYER_ANIM_JUMP);
            }
        }
        return returnAnimation;
    }
}
