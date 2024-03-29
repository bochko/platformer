package game.levels.premade;

import game.core.PIDController;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lazarus on 07/04/2017.
 */
public class LevelMenu implements Level {

    Image background;
    int signal;
    PIDController pidController;

    public LevelMenu(PIDController pidController) {
        setController(pidController);
        reinitialize(0, 0, 0);
    }

    @Override
    public void reinitialize(int width, int height, float scale) {
        background = new ImageIcon("images/backgrounds/menu.png").getImage();
        signal = LevelBundle.SIGNAL_CONTINUE;
    }

    @Override
    public void setController(PIDController pidController) {
        this.pidController = pidController;

    }

    @Override
    public void update(Long time_elapsed, PIDController pidController) {
        if (pidController.isMouse_primary()) {
            pidController.releaseMouseInput();
            setSignal(LevelBundle.SIGNAL_NEXT_LEVEL);

        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
    }

    @Override
    public void setSignal(int signal) {
        this.signal = signal;
    }

    @Override
    public int readSignal() {
        return signal;
    }

}
