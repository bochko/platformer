package game.levels.mechanics;

import game.core.PIDController;

import java.awt.*;



public interface Level {

    int LEVEL_SIGNAL_TERMINATE = 99999;
    int LEVEL_SIGNAL_GAMEOVER = 77777;
    int LEVEL_SIGNAL_PAUSE = 66666;
    int LEVEL_SIGNAL_RESUME = 55555;

    void reinitialize(int width, int height, float scale);
    void update(Long time_elapsed, PIDController pidController);
    void draw(Graphics2D g);
    void setSignal(int signalContinue);
    int readSignal();
}
