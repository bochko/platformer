package game.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/***
 *
 */
public class PIDController implements MouseListener, KeyListener {
    public static final int KBD_ESCAPE = KeyEvent.VK_ESCAPE;
    public static final int KBD_UP = KeyEvent.VK_UP;
    public static final int KBD_DOWN = KeyEvent.VK_DOWN;
    public static final int KBD_LEFT = KeyEvent.VK_LEFT;
    public static final int KBD_RIGHT = KeyEvent.VK_RIGHT;
    public static final int KBD_UP_ALT = KeyEvent.VK_W;
    public static final int KBD_DOWN_ALT = KeyEvent.VK_S;
    public static final int KBD_RIGHT_ALT = KeyEvent.VK_D;
    public static final int KBD_LEFT_ALT = KeyEvent.VK_A;

    private boolean esc;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean mouse_primary;

    private int mouseX;
    private int mouseY;

    /***
     *
     */
    public PIDController() {
        esc = false;
        up = false;
        down = false;
        left = false;
        right = false;
        releaseMouseInput();
    }

    /***
     *
     */
    public void releaseMouseInput() {
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
        return return_value;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KBD_ESCAPE) esc = true;
        if (key == KBD_DOWN || key == KBD_DOWN_ALT) down = true;
        if (key == KBD_UP || key == KBD_UP_ALT) up = true;
        if (key == KBD_LEFT || key == KBD_LEFT_ALT) left = true;
        if (key == KBD_RIGHT || key == KBD_RIGHT_ALT) right = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KBD_ESCAPE) esc = false;
        if (key == KBD_DOWN || key == KBD_DOWN_ALT) down = false;
        if (key == KBD_UP || key == KBD_UP_ALT) up = false;
        if (key == KBD_LEFT || key == KBD_LEFT_ALT) left = false;
        if (key == KBD_RIGHT || key == KBD_RIGHT_ALT) right = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouse_primary = true;
        mouseX = e.getX();
        mouseY = e.getY();
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
