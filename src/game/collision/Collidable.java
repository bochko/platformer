package game.collision;

import java.awt.*;

/***
 * @author Boyan Atanasov
 */
public interface Collidable {

    /***
     * Returns a Rectangle.Float to serve as the collision bounds
     *
     * @return a Rectangle.Float to serve as collision bounds
     */
    Rectangle.Float getCollisionBounds();

    float getX();

    float getY();
}
