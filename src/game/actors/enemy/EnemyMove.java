package game.actors.enemy;

/**
 * Created by lazarus on 31/03/17.
 */
public class EnemyMove {
    private int move = 0;
    private float dx;
    private float dy;


    public EnemyMove(float dx, float dy, int move) {
            this.dx = dx;
            this.dy = dy;
            this.move = move;
    }


    public int getMove() {
        return move;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
}
