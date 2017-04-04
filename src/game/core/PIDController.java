package game.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lazarus on 04/04/17.
 */
public class PIDController implements MouseListener, KeyListener {
    public static final int KBD_ESCAPE = KeyEvent.VK_ESCAPE;
    public static final int KBD_UP = KeyEvent.VK_UP;
    public static final int KBD_DOWN = KeyEvent.VK_DOWN;
    public static final int KBD_LEFT = KeyEvent.VK_LEFT;
    public static final int KBD_RIGHT = KeyEvent.VK_RIGHT;

    private boolean esc;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean mouse_primary;

    public PIDController() {
        esc = false;
        up = false;
        down = false;
        left = false;
        right = false;
        releaseMouseInput();
    }

    private void releaseMouseInput() {
        mouse_primary = false;
    }


    /***
     *
     * @return
     */
    public boolean isEsc() {
        return esc;
    }

    /***
     *
     * @return
     */
    public boolean isUp() {
        return up;
    }

    /***
     *
     * @return
     */
    public boolean isDown() {
        return down;
    }

    /***
     *
     * @return
     */
    public boolean isLeft() {
        return left;
    }

    /***
     *
     * @return
     */
    public boolean isRight() {
        return right;
    }

    /***
     *
     * @return
     */
    public boolean isMouse_primary() {
        boolean return_value =  mouse_primary;
        releaseMouseInput();
        return return_value;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KBD_ESCAPE) esc = true;
        if (key == KBD_DOWN) down = true;
        if (key == KBD_UP) up = true;
        if (key == KBD_LEFT) left = true;
        if (key == KBD_RIGHT) right = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KBD_ESCAPE) esc = false;
        if (key == KBD_DOWN) down = false;
        if (key == KBD_UP) up = false;
        if (key == KBD_LEFT) left = false;
        if (key == KBD_RIGHT) right = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouse_primary = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
