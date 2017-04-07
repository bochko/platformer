package game.levels.premade;

import game.core.PIDController;
import game.levels.mechanics.Level;
import game.levels.mechanics.LevelBundle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lazarus on 07/04/2017.
 */
public class LevelEnd implements Level {

    Image background;
    int signal;

    public LevelEnd() {
        reinitialize(0, 0, 0);
    }

    @Override
    public void reinitialize(int width, int height, float scale) {
        background = new ImageIcon("images/backgrounds/end.png").getImage();
        signal = LevelBundle.SIGNAL_CONTINUE;
    }

    @Override
    public void setController(PIDController pidController) {
        return;
    }

    @Override
    public void update(Long time_elapsed, PIDController pidController) {

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
