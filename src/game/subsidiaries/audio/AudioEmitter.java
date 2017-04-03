package game.subsidiaries.audio;

/**
 * Created by lazarus on 03/04/17.
 */
public class AudioEmitter {

    float x_origin;
    float y_origin;
    int radius;

    public AudioEmitter(float x_origin, float y_origin, int radius) {
        this.x_origin = x_origin;
        this.y_origin = y_origin;
        this.radius = radius;
    }


    public float getX_origin() {
        return x_origin;
    }

    public AudioEmitter setX_origin(float x_origin) {
        this.x_origin = x_origin;
        return this;
    }

    public float getY_origin() {
        return y_origin;
    }

    public AudioEmitter setY_origin(float y_origin) {
        this.y_origin = y_origin;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public AudioEmitter setRadius(int radius) {
        this.radius = radius;
        return this;
    }
}
