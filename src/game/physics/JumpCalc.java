package game.physics;

import cairns.david.engine.Velocity;
import game.actors.Player;

/**
 * Created by lazarus on 28/03/17.
 */
public class JumpCalc {

    /*
    introduce a private Velocity instance for the jumcalc that cannot be changed
    throughout the execution, TO EXCLUSIVELY have memory of jump velocity and
    handle jump physics
     */
    private static final Velocity VELOCITY;
    private static float GRAVITY_PULL;
    private static int JUMP_HEIGHT;
    private static float TRAVEL;

    static {
        VELOCITY = new Velocity();
        GRAVITY_PULL = 0f;
        JUMP_HEIGHT = 0;
    }

    public static void reInitJump(float gravity_pull, Player player) {
        VELOCITY.setVelocity(0, 0);
        GRAVITY_PULL = gravity_pull;
        JUMP_HEIGHT = player.getJumpHeight();
        TRAVEL = 0f;
    }

    public static float calculateResidualJump(Long elapsed) {

        float diff_y = 0;

        if((int)TRAVEL < JUMP_HEIGHT) {

        }


        return diff_y;
    }

    private float counterGravity(float gravity_pull) {

        float diff_y_counter = 0f;

        return diff_y_counter;
    }
}
