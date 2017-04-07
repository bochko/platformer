package game.actors.mechanics;

import cairns.david.engine.Animation;
import cairns.david.engine.Sprite;
import game.levels.mechanics.LevelBundle;
import game.levels.mechanics.LevelPuppeteer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lazarus on 07/04/2017.
 */
public class ButtonSprite extends Sprite implements MouseListener {

    private LevelPuppeteer master;

    public ButtonSprite(Animation animation) {
        super(animation);
    }

    public void setMaster(LevelPuppeteer master) {
        this.master = master;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        master.setSignal(LevelBundle.SIGNAL_NEXT_LEVEL);
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
